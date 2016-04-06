package views;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class BSManagement {

	protected Shell shlBadsmellManagement;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BSManagement window = new BSManagement();
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
		shlBadsmellManagement.open();
		shlBadsmellManagement.layout();
		while (!shlBadsmellManagement.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlBadsmellManagement = new Shell();
		shlBadsmellManagement.setSize(303, 311);
		shlBadsmellManagement.setText("Bad-Smell Management");
		
		List list = new List(shlBadsmellManagement, SWT.BORDER);
		list.setBounds(10, 10, 186, 224);
		
		Button btnAdd = new Button(shlBadsmellManagement, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ImportBS imp = new ImportBS();
				imp.open();
			}
		});
		btnAdd.setBounds(202, 9, 75, 25);
		btnAdd.setText("Add");
		
		Button btnDelete = new Button(shlBadsmellManagement, SWT.NONE);
		btnDelete.setBounds(202, 40, 75, 25);
		btnDelete.setText("Delete");
		
		Button btnApply = new Button(shlBadsmellManagement, SWT.NONE);
		btnApply.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnApply.setBounds(10, 238, 89, 25);
		btnApply.setText("Apply selected");
		
		Button btnCancel = new Button(shlBadsmellManagement, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlBadsmellManagement.dispose();
			}
		});
		btnCancel.setBounds(202, 238, 75, 25);
		btnCancel.setText("Cancel");

	}
}
