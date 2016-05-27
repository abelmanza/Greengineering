package domain.menuHandlers;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.modisco.infra.discovery.core.exception.DiscoveryException;
import org.eclipse.ui.handlers.HandlerUtil;

import domain.kdm.*;
import domain.transformationm2m.*;
import persistence.DataManagement;

public class KDMHandler extends AbstractHandler {
	private IStructuredSelection selection=null;
	private String nameProject = "";
	
	public KDMHandler(){
		
	}
	
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ISelection sel = HandlerUtil.getCurrentSelection(event);
		selection = (IStructuredSelection) sel;
		nameProject=DataManagement.getNameProject(selection.getFirstElement());
		IProject newProject = ResourcesPlugin.getWorkspace().getRoot().getProject(nameProject);
		IProject [] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		IProject insideProject = null;
		for (int i = 0; i < projects.length; i++) {
			if(newProject.getFolder("Legacy System").getFolder(projects[i].getName()).getName().equalsIgnoreCase(projects[i].getName())){
				insideProject = projects[i];
			}
		}
		if (newProject.exists() && !newProject.isOpen())
			try {
				newProject.open(null);
			} catch (CoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		//IProject insideProject = newProject.getFolder("Legacy System").getFolder(name)
		
		try {
			JavaToKDMModel.javatoKDMM(insideProject,ResourcesPlugin.getWorkspace().getRoot().getLocation()+"\\"+nameProject+"\\Grey KDM\\LS_KDMModel.xmi");
			File xmlFile = new File(ResourcesPlugin.getWorkspace().getRoot().getLocation()+"\\"+nameProject+"\\Grey KDM\\LS_KDMModel.xmi");
			AddExtensionFamily.crearExtensionFamily(xmlFile, nameProject);
			DataManagement.getInstance().refreshProject(nameProject);
		} catch (DiscoveryException | CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	

}
