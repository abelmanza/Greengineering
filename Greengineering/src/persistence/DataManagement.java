package persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;



public class DataManagement {

	private static DataManagement data=null;
	private DataManagement(){}
	public static DataManagement getInstance(){
		if(data==null)data=new DataManagement();
		return data;
	}

	public void loadFolder(String pathSourceFolder, String pathDestinationFolder) throws IOException, CoreException {
		// TODO Auto-generated method stub
		
		File folder=new File(pathSourceFolder); 
		
		ArrayList<File> files= new ArrayList<File>();
			
		
		files= getAllFiles(pathSourceFolder,".java");
				
		if(files.size()!=0){
			String newPathFolder=pathDestinationFolder+File.separator+folder.getName();
			File newFolder=new File(newPathFolder);
			newFolder.mkdir();
			for(File f:files){
				if(f.isFile()){
					loadFile(f.getPath(),newPathFolder);
				}
				if(f.isDirectory()){
					loadFolder(f.getPath(),newPathFolder);
				}
			}
		}

	}
	
	public void loadFragmentFolder(String pathSourceFolder, String pathDestinationFolder) throws IOException, CoreException {
		// TODO Auto-generated method stub
		
		File folder=new File(pathSourceFolder); 
		
		ArrayList<File> files= new ArrayList<File>();
			
		
		files= getAllFiles(pathSourceFolder,"*");
				
		if(files.size()!=0){
			String newPathFolder=pathDestinationFolder+File.separator+folder.getName();
			File newFolder=new File(newPathFolder);
			newFolder.mkdir();
			for(File f:files){
				if(f.isFile()){
					loadFile(f.getPath(),newPathFolder);
				}
				if(f.isDirectory()){
					loadFolder(f.getPath(),newPathFolder);
				}
			}
		}

	}
	
	public ArrayList<File> getAllFiles(String path, String extension) {
		ArrayList<File> listaArchivos = new ArrayList<File>();
		File file=new File(path);
		
		if (file.isDirectory()){
			File[] files=file.listFiles();
			if(extension!="*"){
				for(File f:files){
					if(f.isDirectory()||f.getAbsolutePath().endsWith(extension))listaArchivos.add(f);
				}
			}
			else{ //All
				for(File f:files){
					listaArchivos.add(f);
				}
			}
		}
		
		return listaArchivos;
	}
	
	public void loadFile(String pathSourceFile, String pathDestinationFolder) throws IOException, CoreException{
		File file=new File(pathSourceFile);
		String pathNewFile=pathDestinationFolder+File.separator+file.getName();
		
		File newFile = new File (pathNewFile);
		newFile.createNewFile();
		
		fileCopy(file.getPath(),newFile.getPath());
	}
	
	private void fileCopy(String path_sourceFile, String path_destinationFile) {
		 
		try {
			File inFile = new File(path_sourceFile);
			File outFile = new File(path_destinationFile);
 
			FileInputStream in = new FileInputStream(inFile);
			FileOutputStream out = new FileOutputStream(outFile);

			
			int c;
			while( (c = in.read() ) != -1){
				out.write(c);
			}
			in.close();
			out.close();
		} catch(IOException e) {
			System.err.println("Hubo un error de entrada/salida");
		}
	}
	
	public void refreshProject(String nameProject) throws CoreException{
		
		IProject newProject = ResourcesPlugin.getWorkspace().getRoot().getProject(nameProject);
		
		newProject.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
	}
	
	public String getPathFolder(String nameFolder, String nameProject)throws IllegalArgumentException{
		IProject newProject = ResourcesPlugin.getWorkspace().getRoot().getProject(nameProject);
		IFolder folder = newProject.getFolder(nameFolder);
		String pathFolder = folder.getLocation().toString();
		return pathFolder;
	}
	
	public static String getNameProject(Object elementsSelected) {
		// TODO Auto-generated method stub
		if (elementsSelected instanceof IResource) {
			IResource res = (IResource) elementsSelected;
			String nameProject=res.getProject().getName();
			return nameProject;
		}
		else ;
		return null;
	}
}
