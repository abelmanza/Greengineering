package domain.transformationm2m;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.m2m.atl.core.launch.ILauncher;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IExtractor;
import org.eclipse.m2m.atl.core.IInjector;
import org.eclipse.m2m.atl.core.IModel;
import org.eclipse.m2m.atl.core.IReferenceModel;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFExtractor;
import org.eclipse.m2m.atl.core.emf.EMFInjector;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFReferenceModel;
import org.eclipse.m2m.atl.engine.emfvm.launch.EMFVMLauncher;

public class m2mgetset {
	public static void doTransformation(){	
		try{
			
			/*
			 * Initializations
			 */
			ILauncher transformationLauncher = new EMFVMLauncher();
			ModelFactory modelFactory = new EMFModelFactory();
			IInjector injector = new EMFInjector();
			IExtractor extractor = new EMFExtractor();
			
			/*
			 * Load metamodels
			 */
		
			
			IReferenceModel KDMMetamodel = modelFactory.newReferenceModel();
			injector.inject(KDMMetamodel, "file:///"+ResourcesPlugin.getWorkspace().getRoot().getLocation()+"/EjemploTrasgit/KDMmetamodelecore.ecore");
			
			/*
			 * Insert original model
			 */
			IModel KDMModel = modelFactory.newModel(KDMMetamodel);
			injector.inject(KDMModel,"file:///"+ResourcesPlugin.getWorkspace().getRoot().getLocation()+"/EjemploTrasgit/PruebaATL_kdm.xmi");

			/*
			 * Create transformated model
			 */
			IModel companyModel_Total = modelFactory.newModel(KDMMetamodel);
			
			/*
			 * Run transformation
			 */
			transformationLauncher.initialize(new HashMap<String,Object>());
			transformationLauncher.addInModel(KDMModel, "IN", "MM");
			transformationLauncher.addOutModel(companyModel_Total, "OUT", "MM1");
			transformationLauncher.launch(ILauncher.RUN_MODE, new NullProgressMonitor(), new HashMap<String,Object>(),
				new FileInputStream(ResourcesPlugin.getWorkspace().getRoot().getLocation()+"/EjemploTrasgit/GenerarGetSet/GenerarGetSet.asm"));
			
			extractor.extract(companyModel_Total, ResourcesPlugin.getWorkspace().getRoot().getLocation()+"/EjemploTrasgit/pruebaresultado.xmi");
			
			/*
			 * Unload all models and metamodels (EMF-specific)
			 */
			EMFModelFactory emfModelFactory = (EMFModelFactory) modelFactory;
			emfModelFactory.unload((EMFModel) companyModel_Total);
			emfModelFactory.unload((EMFReferenceModel) KDMMetamodel);
		} catch(ATLCoreException e){
			e.printStackTrace();
		} catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
private static String lazyMetamodelRegistration(String metamodelPath){
		
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
   	
	    ResourceSet rs = new ResourceSetImpl();
	    // Enables extended meta-data, weird we have to do this but well...
	    final ExtendedMetaData extendedMetaData = new BasicExtendedMetaData(EPackage.Registry.INSTANCE);
	    rs.getLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);
	
	    Resource r = rs.getResource(URI.createFileURI(metamodelPath), true);
	    EObject eObject = r.getContents().get(0);
	    // A meta-model might have multiple packages we assume the main package is the first one listed
	    if (eObject instanceof EPackage) {
	        EPackage p = (EPackage)eObject;
	        System.out.println(p.getNsURI());
	        EPackage.Registry.INSTANCE.put(p.getNsURI(), p);
	        return p.getNsURI();
	    }
	    return null;
	}
	
}
