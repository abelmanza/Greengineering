package views;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
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
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
    tv.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
    tv.setContentProvider(new FileTreeContentProvider());
    tv.setLabelProvider(new FileTreeLabelProvider());
    tv
    .setInput(createLazyTreeModelFrom(resultFile.toString()));
    tv.expandAll();

    return composite;
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

