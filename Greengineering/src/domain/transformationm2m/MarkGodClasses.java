package domain.transformationm2m;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class MarkGodClasses {

	public static void main (String[]args){
		
		
	}

	public static void crearExtensionFamily(File xmlFile, String nameProject) {
		SAXBuilder builder = new SAXBuilder();
		Document document;
		try {
			document = (Document) builder.build( xmlFile );
			Element rootNode = document.getRootElement();
			
			List list = rootNode.getChildren("code:ClassUnit");
			for(int i = 0; i<list.size(); i++){
				System.out.println(list.get(i));
			}
			
			XMLOutputter xmlOutput = new XMLOutputter();
	        // display ml
	        xmlOutput.setFormat(Format.getPrettyFormat());
	        xmlOutput.output(document, new FileWriter(ResourcesPlugin.getWorkspace().getRoot().getLocation()+"\\"+nameProject+"\\Grey KDM\\LS_ModelMarked.xmi")); 
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

	public static void cargarXML(File xmlFile) {
		
		SAXBuilder builder = new SAXBuilder();
		
		try{
			Document document = (Document) builder.build( xmlFile );
			Element rootNode = document.getRootElement();
			System.out.println(rootNode.getName());
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
}
