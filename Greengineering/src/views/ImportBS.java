package views;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import persistence.DataManagement;

public class ImportBS {

	protected Shell shlImportBadsmellPlugin;
	private Text text;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ImportBS window = new ImportBS();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlImportBadsmellPlugin.open();
		shlImportBadsmellPlugin.layout();
		while (!shlImportBadsmellPlugin.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlImportBadsmellPlugin = new Shell();
		shlImportBadsmellPlugin.setSize(396, 194);
		shlImportBadsmellPlugin.setText("Import Bad-Smell Plug-in");
		
		text = new Text(shlImportBadsmellPlugin, SWT.BORDER);
		text.setBounds(94, 37, 277, 21);
		
		Button btnImportBadsmellPlugin = new Button(shlImportBadsmellPlugin, SWT.NONE);
		btnImportBadsmellPlugin.addSelectionListener(new SelectionAdapter() {
			
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				DirectoryDialog dd = new DirectoryDialog(shlImportBadsmellPlugin);
				dd.setText("Select Legacy System");
				String plugdir = dd.open();
				text.setText(plugdir);
			}
		});
		btnImportBadsmellPlugin.setBounds(10, 35, 78, 25);
		btnImportBadsmellPlugin.setText("Search");
		
		Button btnAcept = new Button(shlImportBadsmellPlugin, SWT.NONE);
		btnAcept.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String plugdir = text.getText();
				//Move to the plugin eclipse folder
			}
		});
		btnAcept.setBounds(212, 127, 75, 25);
		btnAcept.setText("Acept");
		
		Button btnCancel = new Button(shlImportBadsmellPlugin, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlImportBadsmellPlugin.dispose();
			}
		});
		btnCancel.setBounds(296, 127, 75, 25);
		btnCancel.setText("Cancel");

	}
}
