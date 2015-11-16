package greengineering.wizards;

import java.net.URI;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import domain.projects.ProjectStructure;

public class NewProjectWizard extends Wizard implements INewWizard, IExecutableExtension {

	private WizardNewProjectCreationPage _pageOne;
	//private WizardExternalProjectImportPage _pageTwo;
	private IConfigurationElement _configurationElement;
	
	public NewProjectWizard() {
		// TODO Auto-generated constructor stub
		setWindowTitle("Greengineering");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		String name = _pageOne.getProjectName();
	    URI location = null;
	    if (!_pageOne.useDefaults()) {
	        location = _pageOne.getLocationURI();
	    } // else location == null
	 
	    ProjectStructure.createProject(name, location);
	    BasicNewProjectResourceWizard.updatePerspective(_configurationElement);
	 
	    return true;
	}
	
	@Override
	public void addPages() {
	    super.addPages();
	   // _pageTwo = new WizardExternalProjectImportPage();
	    _pageOne = new WizardNewProjectCreationPage("From Scratch Project Wizard");
	    _pageOne.setTitle("New Greengineering Project");
	    _pageOne.setDescription("Create a greengineering folder structure.");
	 
	    addPage(_pageOne);
	}

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		// TODO Auto-generated method stub
		_configurationElement = config;
	}

}
