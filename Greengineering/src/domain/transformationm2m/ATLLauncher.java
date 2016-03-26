package domain.transformationm2m;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.core.resources.ResourcesPlugin;
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
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.gmt.modisco.omg.kdm.code.CodePackage;
import org.eclipse.m2m.atl.emftvm.EmftvmFactory;
import org.eclipse.m2m.atl.emftvm.ExecEnv;
import org.eclipse.m2m.atl.emftvm.Metamodel;
import org.eclipse.m2m.atl.emftvm.Model;
import org.eclipse.m2m.atl.emftvm.impl.resource.EMFTVMResourceFactoryImpl;
import org.eclipse.m2m.atl.emftvm.util.DefaultModuleResolver;
import org.eclipse.m2m.atl.emftvm.util.ModuleResolver;
import org.eclipse.m2m.atl.emftvm.util.TimingData;


public class ATLLauncher {
	
	// Some constants for quick initialization and testing.
	
	
	// The input and output metamodel nsURIs are resolved using lazy registration of metamodels, see below.
	private String inputMetamodelNsURI;
	private String outputMetamodelNsURI;
	
	//Main transformation launch method
	public void launch(String nameProject){
		String IN_METAMODEL_NAME = "MM";
		
		String IN_MODEL ="file:///"+ResourcesPlugin.getWorkspace().getRoot().getLocation()+"/"+nameProject+"/Grey KDM/PruebaATL_kdm.xmi";
		String OUT_MODEL = "file:///"+ResourcesPlugin.getWorkspace().getRoot().getLocation()+"/"+nameProject+"/Green KDM/TransEjemplo.xmi";
		
		String TRANSFORMATION_DIR = "file:///"+ResourcesPlugin.getWorkspace().getRoot().getLocation()+"/Resources/transformations/";
		String TRANSFORMATION_MODULE= "pruebas";
		
		/* 
		 * Creates the execution environment where the transformation is going to be executed,
		 * you could use an execution pool if you want to run multiple transformations in parallel,
		 * but for the purpose of the example let's keep it simple.
		 */
		ExecEnv env = EmftvmFactory.eINSTANCE.createExecEnv();
		ResourceSet rs = new ResourceSetImpl();
		System.out.println(IN_MODEL);
		System.out.println(TRANSFORMATION_DIR);

		/*
		 * Load meta-models in the resource set we just created, the idea here is to make the meta-models
		 * available in the context of the execution environment, the ResourceSet is later passed to the
		 * ModuleResolver that is the actual class that will run the transformation.
		 * Notice that we use the nsUris to locate the metamodels in the package registry, we initialize them 
		 * from Ecore files that we registered lazily as shown below in e.g. registerInputMetamodel(...) 
		 */
		Metamodel metamodel = EmftvmFactory.eINSTANCE.createMetamodel();
		metamodel.setResource(CodePackage.eINSTANCE.eResource());
		env.registerMetaModel(IN_METAMODEL_NAME, metamodel);
		
		/*
		 * Create and register resource factories to read/parse .xmi and .emftvm files,
		 * we need an .xmi parser because our in/output models are .xmi and our transformations are
		 * compiled using the ATL-EMFTV compiler that generates .emftvm files
		 */
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xml", new XMLResourceFactoryImpl());
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("emftvm", new EMFTVMResourceFactoryImpl());
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new EcoreResourceFactoryImpl());
		
		// Load models
		Model inOutModel = EmftvmFactory.eINSTANCE.createModel();
		inOutModel.setResource(rs.getResource(URI.createURI(IN_MODEL, true), true));
		env.registerInOutModel("IN", inOutModel);
		
		/*
		 *  Load and run the transformation module
		 *  Point at the directory your transformations are stored, the ModuleResolver will 
		 *  look for the .emftvm file corresponding to the module you want to load and run
		 */
		
		ModuleResolver mr = new DefaultModuleResolver(TRANSFORMATION_DIR, rs);

		TimingData td = new TimingData();
		env.loadModule(mr, TRANSFORMATION_MODULE);
		td.finishLoading();
		env.run(td);
		td.finish();
			
		// Save models
		try {
			inOutModel.getResource().setURI(URI.createURI(OUT_MODEL, true));
			inOutModel.getResource().save(Collections.emptyMap());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * I seriously hate relying on the eclipse facilities, and if you're not building an eclipse plugin
	 * you can't rely on eclipse's registry (let's say you're building a stand-alone tool that needs to run ATL
	 * transformation, you need to 'manually' register your metamodels) 
	 * This method does two things, it initializes an Ecore parser and then programmatically looks for
	 * the package definition on it, obtains the NsUri and registers it.
	 */
	private String lazyMetamodelRegistration(String metamodelPath){
		
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
	
	/*
	 * As shown above we need the inputMetamodelNsURI and the outputMetamodelNsURI to create the context of
	 * the transformation, so we simply use the return value of lazyMetamodelRegistration to store them.
	 * -- Notice that the lazyMetamodelRegistration(..) implementation may return null in case it doesn't 
	 * find a package in the given metamodel, so watch out for malformed metamodels.
	 * 
	 */
	public void registerInputMetamodel(String inputMetamodelPath){	
		inputMetamodelNsURI = lazyMetamodelRegistration(inputMetamodelPath);
		System.out.println("Meu Input " + inputMetamodelNsURI);
	}

	public void registerOutputMetamodel(String outputMetamodelPath){
		outputMetamodelNsURI = lazyMetamodelRegistration(outputMetamodelPath);
		System.out.println("Meu output " + outputMetamodelNsURI);
	}
	
	/*
	 *  A test main method, I'm using constants so I can quickly change the case study by simply
	 *  modifying the header of the class.
	 */	
	public static void doTransformation(String nameProject){
		ATLLauncher l = new ATLLauncher();
		l.launch(nameProject);
	}
}
