package domain.menuHandlers;


import org.eclipse.core.commands.AbstractHandler;
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
import views.BSManagement;
import views.ImportBS;

public class BSListHandler extends AbstractHandler{
	private IStructuredSelection selection=null;
	private String nameProject = "";
	
	public BSListHandler(){
	}
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		BSManagement bsm = new BSManagement();
		bsm.open();
		
		return null;
	}



}
