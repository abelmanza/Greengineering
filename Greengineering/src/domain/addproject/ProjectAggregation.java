package domain.addproject;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import persistence.DataManagement;


public class ProjectAggregation {

	final static String legacy = "Legacy System";
	public static void addDir(String pathdir,  String nameProject) throws IllegalArgumentException, IOException, CoreException{
		if(pathdir==null){
			//throw new IncorrectPathException();
		}
		if(nameProject==null){
			//throw new IncorrectNameProjectException();
		}
		
		DataManagement.getInstance().loadFolder(pathdir, DataManagement.getInstance().getPathFolder(legacy, nameProject));
		/*Refresh*/
		DataManagement.getInstance().refreshProject(nameProject);
	}
	
}
