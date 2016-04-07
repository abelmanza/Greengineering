package views;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import persistence.DataManagement;

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
	
	public void fillList(List list){
		ArrayList array = new ArrayList();
		String path = Platform.getInstallLocation().getURL().toString()+"dropins/plugins/";
		path = path.substring(6);
		System.out.println(path);
		array = DataManagement.getInstance().getAllFiles(path, "jar");
		
		for(int i = 0; i<array.size();i++){
			File f = new File(array.get(i).toString());
			list.add(f.getName());
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlBadsmellManagement = new Shell();
		shlBadsmellManagement.setSize(445, 311);
		shlBadsmellManagement.setText("Bad-Smell Management");
		ArrayList array = new ArrayList();
		
		List list = new List(shlBadsmellManagement, SWT.BORDER);
		list.setBounds(10, 10, 328, 224);
		fillList(list);
		
		Button btnAdd = new Button(shlBadsmellManagement, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ImportBS imp = new ImportBS();
				imp.open();
			}
		});
		btnAdd.setBounds(344, 10, 75, 25);
		btnAdd.setText("Add");
		
		Button btnDelete = new Button(shlBadsmellManagement, SWT.NONE);
		btnDelete.setBounds(344, 41, 75, 25);
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
		btnCancel.setBounds(344, 239, 75, 25);
		btnCancel.setText("Cancel");

	}
}
