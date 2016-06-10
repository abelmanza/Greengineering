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
			extensionFamily.setAttribute(new Attribute("name","Extensions"));
			extensionFamily.addNamespaceDeclaration(xmi);
			//extensionFamily.addNamespaceDeclaration(kdm);
			extensionFamily.setName("extension");
			
			Element stereotype = stereotypeClass(xmi);
			Element stereotype1 = stereotypeMethod(xmi);
			Element stereotype2 = stereotypeAtt(xmi);
			
			Element stereotype3 = new Element("stereotype");
			stereotype3.addNamespaceDeclaration(xmi);
			stereotype3.setAttribute(new Attribute("name","Instantiated element"));
			stereotype3.setAttribute("id", "id.10", xmi);
			
			extensionFamily.addContent(stereotype);
			extensionFamily.addContent(stereotype1);
			extensionFamily.addContent(stereotype2);
			extensionFamily.addContent(stereotype3);
			
			List list = rootNode.getChildren();
			for(int i = 0; i<list.size(); i++){
				Element element = (Element) list.get(i);
				if(element.getChildren().size()>0){
					if(element.getName().equals("Segment")){
						element.addContent(0,extensionFamily);
					}
				}
			}

			
			XMLOutputter xmlOutput = new XMLOutputter();
	        // display ml
			Format format = Format.getPrettyFormat();
			format.setEncoding("ASCII");
	        // display ml
	        xmlOutput.setFormat(format);
	        xmlOutput.output(document, new FileWriter(ResourcesPlugin.getWorkspace().getRoot().getLocation()+"\\"+nameProject+"\\Source KDM\\LS_KDMModel.xmi")); 
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private static Element stereotypeClass(Namespace xmi) {
		//Stereotype 1
		Element stereotype = new Element("stereotype");
		stereotype.addNamespaceDeclaration(xmi);
		//extensionFamily.addNamespaceDeclaration(kdm);
		stereotype.setAttribute(new Attribute("name","Checked Class"));
		stereotype.setAttribute("id", "id.1", xmi);
		
		Element tag = new Element("tag");
		tag.addNamespaceDeclaration(xmi);
		tag.setAttribute("id", "id.2", xmi);
		tag.setAttribute(new Attribute("type","Method"));
		stereotype.addContent(tag);
		
		Element tag1 = new Element("tag");
		tag1.addNamespaceDeclaration(xmi);
		tag1.setAttribute("id", "id.3", xmi);
		tag1.setAttribute(new Attribute("type","Attribute"));
		stereotype.addContent(tag1);
		return stereotype;
	}
	
	private static Element stereotypeMethod(Namespace xmi) {
		//Stereotype 1
		Element stereotype = new Element("stereotype");
		stereotype.addNamespaceDeclaration(xmi);
		//extensionFamily.addNamespaceDeclaration(kdm);
		stereotype.setAttribute(new Attribute("name","Checked Method"));
		stereotype.setAttribute("id", "id.4", xmi);
		
		Element tag = new Element("tag");
		tag.addNamespaceDeclaration(xmi);
		tag.setAttribute("id", "id.5", xmi);
		tag.setAttribute(new Attribute("type","Class"));
		stereotype.addContent(tag);
		
		Element tag1 = new Element("tag");
		tag1.addNamespaceDeclaration(xmi);
		tag1.setAttribute("id", "id.6", xmi);
		tag1.setAttribute(new Attribute("type","Attribute"));
		stereotype.addContent(tag1);
		return stereotype;
	}
	
	private static Element stereotypeAtt(Namespace xmi) {
		//Stereotype 1
		Element stereotype = new Element("stereotype");
		stereotype.addNamespaceDeclaration(xmi);
		//extensionFamily.addNamespaceDeclaration(kdm);
		stereotype.setAttribute(new Attribute("name","Checked Attribute"));
		stereotype.setAttribute("id", "id.7", xmi);
		
		Element tag = new Element("tag");
		tag.addNamespaceDeclaration(xmi);
		tag.setAttribute("id", "id.8", xmi);
		tag.setAttribute(new Attribute("type","Class"));
		stereotype.addContent(tag);
		
		Element tag1 = new Element("tag");
		tag1.addNamespaceDeclaration(xmi);
		tag1.setAttribute("id", "id.9", xmi);
		tag1.setAttribute(new Attribute("type","Method"));
		stereotype.addContent(tag1);
		return stereotype;
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
