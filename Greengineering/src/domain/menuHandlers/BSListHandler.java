package domain.menuHandlers;


import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import persistence.DataManagement;
import domain.transformationm2m.*;

public class BSListHandler implements IHandler {
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
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(
				window.getShell(),
				"Greengineering",
				"Manage BS List");
		
		//m2mgetset.doTransformation();
		//pruebaTransformacion.launchTransformation();
		
		ISelection sel = HandlerUtil.getCurrentSelection(event);
		selection = (IStructuredSelection) sel;
		nameProject=DataManagement.getNameProject(selection.getFirstElement());
		
		ATLLauncher.doTransformation(nameProject);
		
		try {
			DataManagement.getInstance().refreshProject(nameProject);
		} catch (CoreException e) {
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
