package domain.transformationm2m;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class AddExtensionFamily {


	public static void crearExtensionFamily(File xmlFile, String nameProject) {
		SAXBuilder builder = new SAXBuilder();
		Document document;
		try {
			document = (Document) builder.build( xmlFile );
			Element rootNode = document.getRootElement();
			
			//Extension Family
			Element extensionFamily = new Element("extension");
			Namespace xmi = Namespace.getNamespace("xmi", "http://www.omg.org/XMI");
			//Namespace kdm = Namespace.getNamespace("kdm", "http://www.eclipse.org/MoDisco/kdm/kdm");
			extensionFamily.setAttribute(new Attribute("name","Example extensions"));
			extensionFamily.addNamespaceDeclaration(xmi);
			//extensionFamily.addNamespaceDeclaration(kdm);
			extensionFamily.setName("extension");
			
			//Stereotype 1
			Element stereotype = new Element("stereotype");
			stereotype.addNamespaceDeclaration(xmi);
			//extensionFamily.addNamespaceDeclaration(kdm);
			stereotype.setAttribute(new Attribute("name","Checked Class"));
			stereotype.setAttribute("id", "id.1", xmi);
			extensionFamily.addContent(stereotype);
			
			
			rootNode.addContent(1,extensionFamily);

			
			XMLOutputter xmlOutput = new XMLOutputter();
	        // display ml
	        xmlOutput.setFormat(Format.getPrettyFormat());
	        xmlOutput.output(document, new FileWriter(ResourcesPlugin.getWorkspace().getRoot().getLocation()+"\\"+nameProject+"\\Grey KDM\\LS_KDMModel.xmi")); 
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
