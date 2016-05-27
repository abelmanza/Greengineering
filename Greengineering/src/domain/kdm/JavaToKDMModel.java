package domain.kdm;

import java.io.File;
import java.io.FileOutputStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.modisco.infra.discovery.core.exception.DiscoveryException;
import org.eclipse.modisco.java.discoverer.DiscoverKDMModelFromJavaProject;



public class JavaToKDMModel{

	public static void javatoKDMM (IProject project, String savePath) throws DiscoveryException{
		try{
			DiscoverKDMModelFromJavaProject disco = new DiscoverKDMModelFromJavaProject();
			IJavaProject javaProject = JavaCore.create(project);
			disco.discoverElement(javaProject, new NullProgressMonitor());
			Resource KDMResource = disco.getTargetModel();
			FileOutputStream fout = new FileOutputStream(new File(savePath));
			KDMResource.save(fout,null);
			fout.close();			
		} catch (Exception e){
			System.err.println("Error: "+ e.getMessage());
		}
		
	}
		/*public static void javatoKDMM (IProject project, String savePath) throws DiscoveryException{
		try{
			DiscoverKDMModelFromProject disco = new DiscoverKDMModelFromProject();
			disco.discoverElement(project, new NullProgressMonitor());
			Resource KDMResource = disco.getTargetModel();
			FileOutputStream fout = new FileOutputStream(new File(savePath));
			KDMResource.save(fout,null);
			fout.close();			
		} catch (Exception e){
			System.err.println("Error: "+ e.getMessage());
		}
		
	}*/


}
