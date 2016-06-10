package views;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;























import javax.xml.parsers.DocumentBuilderFactory;























import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Tree;

/**
 * This class demonstrates TreeViewer. It shows the drives, directories, and
 * files on the system.
 */
public class FileTree extends ApplicationWindow {
  /**
   * FileTree constructor
 * @param resultFile 
   */
	private File resultFile;
  public FileTree(File resultFile) {
    super(null);
    this.resultFile = resultFile;
  }

  /**
   * Runs the application
   */
  public void run() {
    // Don't return from open() until window closes
    setBlockOnOpen(true);

    // Open the main window
    open();

    // Dispose the display
    //Display.getCurrent().dispose();
  }

  /**
   * Configures the shell
   * 
   * @param shell
   *            the shell
   */
  protected void configureShell(Shell shell) {
  	shell.addMouseListener(new MouseAdapter() {
  		@Override
  		public void mouseDoubleClick(MouseEvent e) {
  			
  		}
  	});
    super.configureShell(shell);

    // Set the title bar text and the size
    shell.setText("File Tree");
    shell.setSize(400, 400);
  }

  /**
   * Creates the main window's contents
   * 
   * @param parent
   *            the main window
   * @return Control
   */
  protected Control createContents(Composite parent) {
    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new GridLayout(1, false));

    // Create the tree viewer to display the file tree
    final TreeViewer tv = new TreeViewer(composite);
    Tree tree = tv.getTree();
    tree.addMouseListener(new MouseAdapter() {
    	@Override
    	
    	public void mouseDoubleClick(MouseEvent e) {
    		System.out.println("Mouse Double click.");
    		TreeItem [] t = tv.getTree().getSelection();
    		String [] st= t[0].getText().split(" ");
    		String name = st[st.length-1];
    		String type = st[1];
    		String pName = "";
    		String hName = ""; 
    		
    		IProject [] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
    		for (int i = 0; i < projects.length; i++) {
    			if(projects[i].getFolder("Legacy System").exists()){
    				pName = projects[i].getName();
    				
    			}
    		}
    		IProject newProject = ResourcesPlugin.getWorkspace().getRoot().getProject(pName);
    		for (int i = 0; i < projects.length; i++) {
    			if(newProject.getFolder("Legacy System").getFolder(projects[i].getName()).getName().equalsIgnoreCase(projects[i].getName())){
    				
    				hName = newProject.getFolder("Legacy System").getFolder(projects[i].getName()).getName();
    				char[] caracteres = hName.toCharArray();
    				caracteres[0] = Character.toUpperCase(caracteres[0]);
    				for (int j = 0; j < hName.length()- 2; j++) 
    				    if (caracteres[j] == ' ' || caracteres[j] == '.' || caracteres[j] == ',' || caracteres[j] == '_')
    				      caracteres[j + 1] = Character.toUpperCase(caracteres[j + 1]);
    				
    				hName = new String(caracteres);
    			}
    		}
    		
    		int nLine = returnNumberLine(type, name);
    		System.out.println(nLine);
    		
    		
    		
    		String filePath =ResourcesPlugin.getWorkspace().getRoot().getLocation()+"\\"+pName+"\\Legacy System\\"+hName+"\\src\\"+name+".java";
    		final IFile inputFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(Path.fromOSString(filePath));
    		if (inputFile != null) {
    		    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    		    try {
					IEditorPart openEditor = IDE.openEditor(page, inputFile);
					navigateToLine(inputFile, nLine, page);
					
				} catch (PartInitException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    		}


    	}
    });
    tv.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
    tv.setContentProvider(new FileTreeContentProvider());
    tv.setLabelProvider(new FileTreeLabelProvider());
    tv
    .setInput(createLazyTreeModelFrom(resultFile.toString()));
    tv.expandAll();

    return composite;
  }

  
  public static int returnNumberLine(String type, String name){
	  int nLine = 0;
	  String nType = "";
	  FileInputStream data;
	  if(type.equals("Class")){
		  nType = "code:ClassUnit";
	  } else if(type.equals("Method")){
		  nType = "code:MethodUnit";
	  }else if(type.equals("Attribute")){
		  nType = "code:StorableUnit";
	  }
	try {
		data = new FileInputStream(ResourcesPlugin.getWorkspace().getRoot().getLocation()+"\\Resources\\output.txt");
		DataInputStream in = new DataInputStream(data);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		br.readLine();
		while((strLine = br.readLine()) != null){
			String [] tokens = strLine.split(" ");
			if(tokens[0].equalsIgnoreCase(nType) && tokens[1].equalsIgnoreCase(name)){
				nLine = Integer.parseInt(tokens[2]);
			}
		}
		br.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	  return nLine;
  }
  public static void navigateToLine(IFile file, Integer line, IWorkbenchPage page)
  {
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put(IMarker.LINE_NUMBER, line);
      IMarker marker = null;
      try {
          marker = file.createMarker(IMarker.TEXT);
          marker.setAttributes(map);
          try {
              IDE.openEditor(page, marker);
          } catch ( PartInitException e ) {
              //complain
          }
      } catch ( CoreException e1 ) {
          //complain
      } finally {
          try {
              if (marker != null)
                  marker.delete();
          } catch ( CoreException e ) {
              //whatever
          }
      }
  }
  private Object createLazyTreeModelFrom(String xmlFilePath) {
      Node documentNode = null;
      try {
          documentNode = DocumentBuilderFactory.newInstance()
                  .newDocumentBuilder().parse(new File(xmlFilePath));
      } catch (Exception e) {
          e.printStackTrace();
      }
      return documentNode;
  }
  /**
   * The application entry point
   * 
   * @param args
   *            the command line arguments
   */
  /*public static void main(String[] args) {
    new FileTree().run();

  }*/
}

/**
 * This class provides the content for the tree in FileTree
 */

class FileTreeContentProvider implements ITreeContentProvider {
  /**
   * Gets the children of the specified object
   * 
   * @param arg0
   *            the parent object
   * @return Object[]
   */
	 public Object[] getChildren(Object parentElement) {
         NodeList nodeList = ((Node) parentElement).getChildNodes();
         int nodesCount = nodeList.getLength();
         List<Node> nodes = new ArrayList<Node>();
         for (int i = 0; i < nodesCount; i++) {
             Node currentNode = nodeList.item(i);
             if (null != currentNode
                     && Node.TEXT_NODE == currentNode.getNodeType()
                     && "".equals(currentNode.getNodeValue().trim())) {
                 
                 continue;
             }
             nodes.add(currentNode);

         }
         return nodes.toArray();
     }

     public Object getParent(Object element) {
         return ((Node) element).getParentNode();
     }

     public boolean hasChildren(Object element) {
         return ((Node) element).hasChildNodes();
     }

     public Object[] getElements(Object inputElement) {
         return getChildren(inputElement);
     }

     public void dispose() {
         // noop
     }

     public void inputChanged(Viewer viewer, Object oldInput,
             Object newInput) {
         // noop
     }
}

/**
 * This class provides the labels for the file tree
 */

class FileTreeLabelProvider implements ILabelProvider {
  // The listeners
	
	private List listeners;

	  private Image file;
	  private Image pack;
	  private Image dir;
	  private Image marked;
	  private Image method;
	  private Image storable;

	  public FileTreeLabelProvider() {
	    listeners = new ArrayList();

	      dir= AbstractUIPlugin.imageDescriptorFromPlugin("Greengineering", "/icons/packagefolder_obj.gif").createImage();
	      file= AbstractUIPlugin.imageDescriptorFromPlugin("Greengineering", "/icons/jcu_obj.gif").createImage();
	      pack= AbstractUIPlugin.imageDescriptorFromPlugin("Greengineering", "/icons/package_obj.gif").createImage();
	      marked = AbstractUIPlugin.imageDescriptorFromPlugin("Greengineering", "/icons/signed_no.gif").createImage();
	      method = AbstractUIPlugin.imageDescriptorFromPlugin("Greengineering", "/icons/methpub_obj.gif").createImage();
	      storable = AbstractUIPlugin.imageDescriptorFromPlugin("Greengineering", "/icons/javaassist_co.gif").createImage();
	  }

	public void addListener(ILabelProviderListener listener) {

		listeners.add(listener);
    }

    public void dispose() {

    }

    public boolean isLabelProperty(Object element, String property) {

        return false;
    }

    public void removeListener(ILabelProviderListener listener) {

    }

    public Image getImage(Object element) {
    	NamedNodeMap attributes = ((Node) element).getAttributes();
    	int attributeCount = attributes.getLength();
    	Image image = null;
    	if(((Node) element).getNodeName().equals("Class")){
    		image = file;
    	}
    	else if(((Node) element).getNodeName().equals("Package")){
			image = pack;
		}else if(((Node) element).getNodeName().equals("Method")){
			image = method;
		}else if(((Node) element).getNodeName().equals("Attribute")){
			image = storable;
		}
		else{
			image = dir;
		}
    	for(int i = 0; i<attributeCount;i++){
    		Node attributeNode = attributes.item(i);
    		
    		if(attributeNode.getNodeName().equals("stereotype")){
    			return marked;
    		}
    	}

    	return image;
    }

    public String getText(Object element) {
        Node node = (Node) element;
        if (null == node) {
            return getText(element);
        } else if (Node.TEXT_NODE == node.getNodeType()) {
            return node.getTextContent().trim();
        } else {
            StringBuilder elementRepresentation = new StringBuilder(" ");
            elementRepresentation.append(node.getNodeName());
            NamedNodeMap attributes = node.getAttributes();

            if (null == attributes) {
                elementRepresentation.append(" ");
                return elementRepresentation.toString();
            }

            int attributeCount = attributes.getLength();
            for (int i = 0; i < attributeCount; i++) {
                Node attributeNode = attributes.item(i);
                if(attributeNode.getNodeName().equals("name") || attributeNode.getNodeName().equals("xsi:type")){
                    elementRepresentation.append(" -> ");
                    elementRepresentation.append(attributeNode
                            .getNodeValue());
                    elementRepresentation.append(" ");
                }
                elementRepresentation.append(" ");

            }
            elementRepresentation.append(" ");
            return elementRepresentation.toString();
        }
    }
}

