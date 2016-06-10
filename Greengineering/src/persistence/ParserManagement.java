package persistence;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import domain.parser.JavaParser;
import domain.transformationm2m.ModelClean;

public class ParserManagement {
	public static void collectProjectData(String Path){
		ArrayList<File> files = new ArrayList <File>();
		files = DataManagement.getInstance().getAllFiles(Path,".java");
		
		if(files.size()!=0){
			for(File f: files){
				if(f.isFile()){
					getFileInfo(f.getPath());
				}
				if(f.isDirectory()){
					collectProjectData(f.getPath());
				}
			}
		}
	}
	
	public static void getFileInfo(String path){
		JavaParser.getInstance().analyzeFile(path);
		JavaParser.getInstance().cleanArrayFile();
	}
	
	public static void fileToXML(String filePath){
		Element project = new Element("Project");
		Document doc = new Document(project);
		
		try {
			FileInputStream f = new FileInputStream(filePath);
			DataInputStream in = new DataInputStream(f);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			Element cl = new Element("Class");
			br.readLine();
			while((strLine = br.readLine()) != null){
				String [] tokens = strLine.split(" ");
	
				if(tokens[0].equalsIgnoreCase("code:ClassUnit")){
					cl = new Element("code:ClassUnit");
					cl.setAttribute(new Attribute("name", tokens[1]));
					cl.setAttribute(new Attribute("beginLine", tokens[2]));
					doc.getRootElement().addContent(cl);
					
				}else if (tokens[0].equalsIgnoreCase("code:MethodUnit")){
					Element method = new Element("code:MethodUnit");
					method.setAttribute(new Attribute("name", tokens[1]));
					method.setAttribute(new Attribute("beginLine", tokens[2]));
					cl.addContent(method);
				}else if (tokens[0].equalsIgnoreCase("code:StorableUnit")){
					Element att = new Element("code:StorableUnit");
					att.setAttribute(new Attribute("name", tokens[1]));
					att.setAttribute(new Attribute("beginLine", tokens[2]));
					cl.addContent(att);
				}else{
					System.out.println("Something is wrong, there is not a class, a method or an attribute!");
				}
				
			}
			
			XMLOutputter xmlOutput = new XMLOutputter();
			
			Format format = Format.getPrettyFormat();
			format.setEncoding("iso-8859-1");
			
	        // display ml
	        xmlOutput.setFormat(format);
	        xmlOutput.output(doc, new FileWriter("D://Usuarios//Abel//Descargas//ejemplos//XML.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public static void main (String [] args){
		collectProjectData("D://Usuarios//Abel//Descargas//ejemplos");
		//ModelClean.addNumberLine(xmlFile, "D://Usuarios//Abel//Descargas//ejemplos//output.txt", "D://Usuarios//Abel//Descargas//ejemplos//conlinea.txt");
		//fileToXML("D://Usuarios//Abel//Descargas//ejemplos//output.txt");
		
	}

}
