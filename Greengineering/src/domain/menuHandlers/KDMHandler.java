package domain.menuHandlers;

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
import persistence.DataManagement;

public class KDMHandler implements IHandler {
	private IStructuredSelection selection=null;
	private String nameProject = "";
	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ISelection sel = HandlerUtil.getCurrentSelection(event);
		selection = (IStructuredSelection) sel;
		nameProject=DataManagement.getNameProject(selection.getFirstElement());
		IProject newProject = ResourcesPlugin.getWorkspace().getRoot().getProject(nameProject);
				
		try {
			JavaToKDMModel.javatoKDMM(newProject,ResourcesPlugin.getWorkspace().getRoot().getLocation()+"\\"+nameProject+"\\KDM Model\\LS_KDMModel.xmi");
			DataManagement.getInstance().refreshProject(nameProject);
		} catch (DiscoveryException | CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}
