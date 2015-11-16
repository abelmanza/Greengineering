package domain.menuHandlers;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

import persistence.DataManagement;
import domain.addproject.ProjectAggregation;


/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class LegacyHandler extends AbstractHandler {
	private Shell shell=null;
	private String nameProject = "";
	private IStructuredSelection selection=null;
	/**
	 * The constructor.
	 */
	public LegacyHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		ISelection sel = HandlerUtil.getCurrentSelection(event);
		shell=HandlerUtil.getActiveShell(event);
		DirectoryDialog dd = new DirectoryDialog(shell,SWT.MULTI);
		dd.setText("Select Legacy System");
		String pathdir = "";
		selection = (IStructuredSelection) sel;
		nameProject=DataManagement.getNameProject(selection.getFirstElement());
		if(dd!=null){
			pathdir = dd.open();
			if (pathdir != null && pathdir.length() > 0) {
				System.out.println(pathdir); 
				try {
					ProjectAggregation.addDir(pathdir, nameProject);
				} catch (IllegalArgumentException | IOException | CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
