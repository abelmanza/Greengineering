package domain.transformationm2m;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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

public class ModelClean {


	public static File clean(File xmlFile, String nameProject) {
		SAXBuilder builder = new SAXBuilder();
		Document document = new Document();
		Document docModel;
		File f = new File(nameProject);
		try {
			docModel = (Document) builder.build(xmlFile);
			
			Element name = new Element(nameProject);
			document.setRootElement(name);
			Element rootNode = docModel.getRootElement();
			Element newRootNode = document.getRootElement();
			Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
				
			List list = rootNode.getChildren();
			for(int i = 0; i<list.size(); i++){
				//SEGMENT
				Element element = (Element) list.get(i);
				if(element.getChildren().size()>0){
					List list1 = element.getChildren();
					
					for(int j = 0; j<list1.size(); j++){
						//MODEL
						Element element1 = (Element) list1.get(j);
						if(element1.getChildren().size()>0){
							List list2 = element1.getChildren();
							
							for(int k = 0; k<list2.size(); k++){
								//PACKAGE
								Element element2 = (Element) list2.get(k);
								String typep = element2.getAttributeValue("type", xsi);
								if(typep!= null && typep.equals("code:Package")){
									Element pack = new Element("Package");
									if(element2.getChildren().size()>0){
										List list3 = element2.getChildren();
										
										for(int l = 0; l<list3.size(); l++){
											//CLASS
											Element elementClass = (Element) list3.get(l);
											if(elementClass.hasAttributes()){
												if(elementClass.getAttributeValue("type",xsi)!= null && elementClass.getAttributeValue("type",xsi).equals("code:ClassUnit")){
													Element cl = new Element("Class");
													if(elementClass.getChildren().size()>0){
														List listMethod = elementClass.getChildren();
														
														if(listMethod.size()>0){
															for(int m = 0; m<listMethod.size();m++){
																//ATTRIBUTES & METHODS
																Element elementMethod = (Element) listMethod.get(m);
																
																String type = elementMethod.getAttributeValue("type", xsi);
																Element su;
																if(type!= null && type.equals("code:StorableUnit")){
																	su = new Element("Attribute");
																	su.setAttribute(new Attribute("name",elementMethod.getAttributeValue("name")));
																	if(elementMethod.getAttribute("stereotype")!=null){
																		su.setAttribute(new Attribute("stereotype",elementMethod.getAttributeValue("stereotype")));
																	}
																	cl.addContent(su);
																}else if(type!= null && type.equals("code:MethodUnit")){
																	su = new Element("Method");
																	su.setAttribute(new Attribute("name",elementMethod.getAttributeValue("name")));
														
																	su.setAttribute(new Attribute("name",elementMethod.getAttributeValue("name")));
																	if(elementMethod.getAttribute("stereotype")!=null){
																		su.setAttribute(new Attribute("stereotype",elementMethod.getAttributeValue("stereotype")));
																	}
																	cl.addContent(su);
																}
															}
															/*
															 * Condition of bad-smell
															 */
															
														}
													}
													
													cl.setAttribute(new Attribute("name",elementClass.getAttributeValue("name")));
													if(elementClass.getAttribute("stereotype")!=null){
														cl.setAttribute(new Attribute("stereotype",elementClass.getAttributeValue("stereotype")));
													}
													pack.addContent(cl);
												}
											}
								
										}
									}
									
									pack.setAttribute(new Attribute("name",element2.getAttributeValue("name")));
									if(element2.getAttribute("stereotype")!=null){
										pack.setAttribute(new Attribute("stereotype",element2.getAttributeValue("stereotype")));
									}
									newRootNode.addContent(pack);
								}
								
								
							}
						}
						
					}
				}
				
			}
			
			
			

			
			XMLOutputter xmlOutput = new XMLOutputter();
	        // display ml
			Format format = Format.getPrettyFormat();
			format.setEncoding("iso-8859-1");
			
	        // display ml
	        xmlOutput.setFormat(format);
	        xmlOutput.output(document, new FileWriter(f));
	        //xmlOutput.output(document, new FileWriter(ResourcesPlugin.getWorkspace().getRoot().getLocation()+"\\"+nameProject+"\\Bad-Smell detected KDM\\CheckedModelClean.xmi")); 
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        return f;
	}
	
	public static void addNumberLine(File xmlFile, String filePath, String exitPath){
		
		SAXBuilder builder = new SAXBuilder();
		Document document;
		try {
			
			FileInputStream f = new FileInputStream(filePath);
			DataInputStream in = new DataInputStream(f);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			br.readLine();
			
			document = (Document) builder.build( xmlFile );
			Element rootNode = document.getRootElement();
			System.out.println(rootNode);
			Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
			
			List list = rootNode.getChildren();
			while((strLine = br.readLine()) != null){
				String [] tokens = strLine.split(" ");
				for(int i = 0; i<list.size(); i++){
					Element element = (Element) list.get(i);
					if(element.getChildren().size()>0){
						List list1 = element.getChildren();
						
						for(int j = 0; j<list1.size(); j++){
							Element element1 = (Element) list1.get(j);
							if(element1.getChildren().size()>0){
								List list2 = element1.getChildren();
								
								for(int k = 0; k<list2.size(); k++){
									Element element2 = (Element) list2.get(k);
									if(element2.getChildren().size()>0){
										List list3 = element2.getChildren();
										
										for(int l = 0; l<list3.size(); l++){
											Element elementClass = (Element) list3.get(l);
											if(elementClass.hasAttributes()){
												
												if(elementClass.getAttributeValue("type",xsi)!= null && elementClass.getAttributeValue("type",xsi).equals("code:ClassUnit")){
													if(elementClass.getAttributeValue("type",xsi).equals(tokens[0]) && elementClass.getAttributeValue("name").equals(tokens[1])){
														elementClass.setAttribute(new Attribute("beginLine", tokens[2]));
													}
													
													if(elementClass.getChildren().size()>0){
														List listMethod = elementClass.getChildren();
														
														if(listMethod.size()>0){
															for(int m = 0; m<listMethod.size();m++){
																Element elementMethod = (Element) listMethod.get(m);
																String type = elementMethod.getAttributeValue("type", xsi);
																if(elementMethod.getAttributeValue("type",xsi)!= null && elementMethod.getAttributeValue("type",xsi).equals(tokens[0]) && elementMethod.getAttributeValue("name").equals(tokens[1])){
																	elementMethod.setAttribute(new Attribute("beginLine", tokens[2]));
																}
															}
															
														}
													}
											
												}
											}
								
										}
									}
									
								}
							}
							
						}
					}
					
				}
			}
			
			
			
			XMLOutputter xmlOutput = new XMLOutputter();
			Format format = Format.getPrettyFormat();
			format.setEncoding("iso-8859-1");
	        // display ml
	        xmlOutput.setFormat(format);
	        xmlOutput.output(document, new FileWriter(exitPath));
			
			
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
