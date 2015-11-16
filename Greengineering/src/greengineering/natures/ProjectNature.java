package greengineering.natures;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class ProjectNature implements IProjectNature {
	private IProject iProject;
	public static final String NATURE_ID = "greengineering.ProjectNature";
	@Override
	public void configure() throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deconfigure() throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public IProject getProject() {
		// TODO Auto-generated method stub
		return iProject;
	}

	@Override
	public void setProject(IProject project) {
		// TODO Auto-generated method stub
		this.iProject = project;
	}

}
