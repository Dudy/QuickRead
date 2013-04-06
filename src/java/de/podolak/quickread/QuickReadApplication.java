package de.podolak.quickread;

import com.google.appengine.api.users.UserServiceFactory;
import com.vaadin.Application;
import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.PropertySetChangeEvent;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.service.ApplicationContext;
import com.vaadin.service.ApplicationContext.TransactionListener;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import de.podolak.quickread.data.Project;
import de.podolak.quickread.data.datastore.Document;
import de.podolak.quickread.data.datastore.DocumentPersistence;
import de.podolak.quickread.data.datastore.DocumentType;
import de.podolak.quickread.data.datastore.Node;
import de.podolak.quickread.ui.HelpWindow;
import de.podolak.quickread.ui.NavigationTree;
import de.podolak.quickread.ui.NodeView;
import de.podolak.quickread.ui.OldNodeView;
import de.podolak.quickread.ui.ProjectManagementWindow;
import de.podolak.quickread.ui.SearchWindow;
import de.podolak.quickread.ui.Toolbar;
import de.podolak.quickread.ui.OldNodeForm;
import de.podolak.quickread.ui.wizard.NewDocumentWindow;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.vaadin.dialogs.ConfirmDialog;

// TODO use ApplicationContext for synchronization between parts of the application
//      (e.g. this Application class, Handlers, Factories, Windows aso.)

@SuppressWarnings("serial")
public class QuickReadApplication extends Application implements
        ValueChangeListener, ItemClickListener,
        Container.ItemSetChangeListener, Container.PropertySetChangeListener {

    private static final Logger LOGGER = Logger.getLogger(QuickReadApplication.class.getName());
    
    private static final long ROOT_INDEX = 0;
    private Window window;
    private static final HashMap<ApplicationContext, TransactionListener> requestListeners = new HashMap<ApplicationContext, TransactionListener>();
    private boolean isInitialized = false;
    // ui
    private NavigationTree navigationTree;
    private Toolbar toolbar;
    private final HorizontalSplitPanel horizontalSplit = new HorizontalSplitPanel();
    // Lazyly created ui references
    private HelpWindow helpWindow = null;
    
    //private NodeView nodeView = null;
    //private EditForm nodeForm = null;
    private OldNodeView nodeView = null;
    private OldNodeForm nodeForm = null;
    
    private SearchWindow searchWindow = null;
    private NewDocumentWindow newDocumentWindow = null;
    private ProjectManagementWindow projectManagementWindow = null;
    // data handling
    private HierarchicalContainer dataContainer;

    private Project project;

    // <editor-fold defaultstate="collapsed" desc=" application ">
    @Override
    public void init() {
        createDataContainer(DocumentPersistence.getFirstProject());

        // Only add one listener per context as Vaadin calls every listener in
        // the context for every request.
        if (getContext() != null && requestListeners.get(getContext()) == null) {
            TransactionListener listener = new TransactionListener() {
                @Override
                public void transactionStart(Application app, Object req) {
                    if (!isInitialized) {
                        buildUI();
                        isInitialized = true;
                    }

                    toolbar.update((HttpServletRequest) req);
                }

                @Override
                public void transactionEnd(Application app, Object req) {
                    // NOP
                }
            };

            getContext().addTransactionListener(listener);

            requestListeners.put(getContext(), listener);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" UI ">
    private void buildUI() {
        buildMainLayout();
        setMainComponent(getNodeView());
    }

    private void buildMainLayout() {
        window = new Window(Utilities.getI18NText("window.title"));

        setMainWindow(window);
        setTheme("quickread");

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        layout.addComponent(toolbar = new Toolbar(this));
        layout.addComponent(horizontalSplit);
        layout.setExpandRatio(horizontalSplit, 1);

        horizontalSplit.setSplitPosition(400, Sizeable.UNITS_PIXELS);
        horizontalSplit.setFirstComponent(navigationTree = new NavigationTree(this));

        getMainWindow().setContent(layout);
    }

    private void setMainComponent(Component c) {
        horizontalSplit.setSecondComponent(c);
    }

    public void showProjectManagementWindow() {
        showMessage("not yet implemented", "the project management has not yet been implemented");
        
        //getMainWindow().addWindow(getProjectManagementWindow());
        //getProjectManagementWindow().setSelectedProject(project);
    }

    public void cancelProjectManagement() {
        getMainWindow().removeWindow(projectManagementWindow);
    }

    public void closeProjectManagement(Project selectedProject) {
        getMainWindow().removeWindow(projectManagementWindow);
        initDataContainer(selectedProject);
        navigationTree.setContainerDataSource(getDataContainer());
    }

    public void closeNewDocumentWindow(Document newDocument) {
        getMainWindow().removeWindow(newDocumentWindow);

        if (newDocument != null) {
            project.addDocument(newDocument);
            save();
        }
        
        newDocumentWindow = null;
    }

    private ProjectManagementWindow getProjectManagementWindow() {
        if (projectManagementWindow == null) {
            projectManagementWindow = new ProjectManagementWindow(this);
            projectManagementWindow.center();
        }
        return projectManagementWindow;
    }

    private HelpWindow getHelpWindow() {
        if (helpWindow == null) {
            helpWindow = new HelpWindow();
            helpWindow.setWidth("600px");
            helpWindow.center();
        }
        return helpWindow;
    }

//    private NodeView getNodeView() {
//        if (nodeView == null) {
//            //nodeForm = new EditForm(this);
//            //nodeView = new NodeView(nodeForm);
//            nodeForm = new OldNodeForm(this);
//            nodeView = new OldNodeView(nodeForm);
//        }
//        return nodeView;
//    }
    
    private OldNodeView getNodeView() {
        if (nodeView == null) {
            //nodeForm = new EditForm(this);
            //nodeView = new NodeView(nodeForm);
            nodeForm = new OldNodeForm(this);
            nodeView = new OldNodeView(nodeForm);
        }
        return nodeView;
    }

    private SearchWindow getSearchWindow() {
        if (searchWindow == null) {
            searchWindow = new SearchWindow(this);
        }
        return searchWindow;
    }

    private NewDocumentWindow getNewDocumentWindow() {
        if (newDocumentWindow == null) {
            newDocumentWindow = new NewDocumentWindow(this);
            newDocumentWindow.center();
        }
        return newDocumentWindow;
    }

    public void showHelpWindow() {
        getMainWindow().addWindow(getHelpWindow());
    }

    public void showSearchWindow() {
        showMessage("not yet implemented", "the search has not yet been implemented");
        
        //getMainWindow().addWindow(getSearchWindow());
    }

    public void showNewDocumentWindow() {
        getMainWindow().addWindow(getNewDocumentWindow());
    }

    public void showMessage(String caption, String description) {
        window.showNotification(caption, description);
    }

    public void showMessage(String caption, String description, int type) {
        window.showNotification(caption, description, type);
    }

    // <editor-fold defaultstate="collapsed" desc=" event handlers ">
    @Override
    public void valueChange(ValueChangeEvent event) {
        Property property = event.getProperty();

        if (property == navigationTree) {
            Item selectedItem = navigationTree.getSelectedItem();
            if (selectedItem != nodeForm.getItemDataSource()) {
                nodeForm.setItemDataSource(selectedItem);
            }
        }
    }

    @Override
    public void itemClick(ItemClickEvent event) {
        if (event.getSource() == navigationTree) {
            Object itemId = event.getItemId();

            if (itemId != null) {
                // System.out.println("1: " + event);
            }
        }
    }

    @Override
    public void containerItemSetChange(ItemSetChangeEvent event) {
    }

    @Override
    public void containerPropertySetChange(PropertySetChangeEvent event) {
    }
    // </editor-fold>
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" data ">
    private void createDataContainer(Project project) {
        dataContainer = new HierarchicalContainer();
        dataContainer.addContainerProperty("title", String.class, Utilities.getI18NText("data.newNode.title")); // i18n
        dataContainer.addContainerProperty("rawTitle", String.class, "data.newNode.title"); // raw string, no i18n
        dataContainer.addContainerProperty("text", String.class, Utilities.getI18NText("data.newNode.text"));
        dataContainer.addContainerProperty("icon", ThemeResource.class, new ThemeResource("../runo/icons/16/document.png"));
        dataContainer.addContainerProperty("object", Object.class, null);
        dataContainer.addContainerProperty("itemId", Object.class, null);
        dataContainer.addContainerProperty("documenttype", DocumentType.class, null);
        initDataContainer(project);
    }

    public void save() {
        Project newProject = DocumentPersistence.storeDocument(project);

        if (newProject == null) {
            showMessage(
                    Utilities.getI18NText("action.save.error.caption"),
                    Utilities.getI18NText("action.save.error.description"),
                    Notification.TYPE_WARNING_MESSAGE);
        } else {
            Object selectedItemIdObject = navigationTree.getValue();
            
            java.util.ArrayList<Document> newDocumentList = new java.util.ArrayList<Document>();
            // iterate over the OLD project !!
            for (Document document : project.getDocumentList()) {
                LOGGER.log(Level.FINE, document.toJson());
                newDocumentList.add(DocumentPersistence.storeDocument(document));
            }
            newProject.getDocumentList().clear();
            newProject.getDocumentList().addAll(newDocumentList);
            
            showMessage(
                    Utilities.getI18NText("action.save.success.caption"),
                    Utilities.getI18NText("action.save.success.description"));
            
            createDataContainer(newProject);
            navigationTree.setContainerDataSource(getDataContainer());
            navigationTree.requestRepaint();
            
            if (selectedItemIdObject != null) {
                navigationTree.setValue(selectedItemIdObject);
                nodeForm.setItemDataSource(dataContainer.getItem(selectedItemIdObject));
            }
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc=" data container handling ">
    public Container getDataContainer() {
        return dataContainer;
    }

    public void addNode() {
        if (!UserServiceFactory.getUserService().isUserLoggedIn()) {
            showMessage(
                    Utilities.getI18NText("authentication.noUser.caption"),
                    Utilities.getI18NText("authentication.addNode.description"),
                    Window.Notification.TYPE_WARNING_MESSAGE);
            return;
        }

        Object selectedItemIdObject = navigationTree.getValue();

        if (selectedItemIdObject != null) {
            if (selectedItemIdObject instanceof Long) {
                Long selectedItemId = (Long) selectedItemIdObject;
                Item selectedItem = dataContainer.getItem(selectedItemId);
                DocumentType selectedDocumentType = (DocumentType) selectedItem.getItemProperty("documenttype").getValue();
                Object lastItemIdObject = dataContainer.lastItemId();
                
                if (lastItemIdObject instanceof Long) {
                    Node node = null;

                    switch (selectedDocumentType) {
                        case PROJECT:
                            showNewDocumentWindow();
                            // the closeNewDocument() method handles adding a new document
                            break;
                        case BOOK:
                        case SONG:
                            node = new Node(Utilities.getI18NText("data.newNode.title"), Utilities.getI18NText("data.newNode.text"));
                            ((Document) selectedItem.getItemProperty("object").getValue()).getDataNode().addChild(node);
                            LOGGER.log(Level.FINE, "New Node:\n{0}", node.toJson());
                            break;
                        case COMMON:
                            node = new Node(Utilities.getI18NText("data.newNode.title"), Utilities.getI18NText("data.newNode.text"));
                            ((Node) selectedItem.getItemProperty("object").getValue()).addChild(node);
                            LOGGER.log(Level.FINE, "New Node:\n{0}", node.toJson());
                            break;
                        default:
                            LOGGER.log(Level.SEVERE, "unknown selected document type {0}", selectedDocumentType);
                            break;
                    }
                    
                    if (node != null) {
                        save();
                    }
                } else {
                    LOGGER.log(Level.SEVERE, "last item id in dataContainer is not of type Long");
                }
            } else {
                LOGGER.log(Level.SEVERE, "selected item id is not of type Long");
            }
        } else {
            LOGGER.log(Level.SEVERE, "selected item id is null");
        }
    }
    
    public void removeNode() {
        if (!UserServiceFactory.getUserService().isUserLoggedIn()) {
            showMessage(
                    Utilities.getI18NText("authentication.noUser.caption"),
                    Utilities.getI18NText("authentication.removeNode.description"),
                    Window.Notification.TYPE_WARNING_MESSAGE);
            return;
        }
        
        final Object selectedItemIdObject = navigationTree.getValue();

        if (selectedItemIdObject instanceof Long && ((Long)selectedItemIdObject) != ROOT_INDEX) {
            final Object parentItemIdObject = dataContainer.getParent(selectedItemIdObject);
            final Item parentItem = dataContainer.getItem(parentItemIdObject);
            final Item selectedItem = dataContainer.getItem(selectedItemIdObject);
            final Object selectedObject = selectedItem.getItemProperty("object").getValue();

            if (selectedObject instanceof Node) {
                Node node = (Node) selectedObject;
                String key = node.getKey();
                Object parentObject = parentItem.getItemProperty("object").getValue();

                if (parentObject instanceof Document) {
                    for (String attribute : ((Document)parentObject).getAttributeStringList()) {
                        if (attribute.equals(key)) {
                            showMessage(
                                    Utilities.getI18NText("error.general"),
                                    Utilities.getI18NText("document.nodeNotDeletable"),
                                    Window.Notification.TYPE_WARNING_MESSAGE);
                            return;
                        }
                    }
                }
            }
            
            ConfirmDialog.show(
                    getMainWindow(),
                    Utilities.getI18NText("action.removeNode.confirmDialog.caption"),
                    Utilities.getI18NText("action.removeNode.confirmDialog.text"),
                    Utilities.getI18NText("action.removeNode.confirmDialog.yes"),
                    Utilities.getI18NText("action.removeNode.confirmDialog.no"),
                    new ConfirmDialog.Listener() {
                        @Override
                        public void onClose(ConfirmDialog dialog) {
                            // this just removes the node from the underlying data structure, the
                            // data container handling (and therefore the navigation tree's composition)
                            // is done by the save() method
                            
                            if (dialog.isConfirmed()) {
                                Node parentNode = null;
                                DocumentType parentDocumentType = (DocumentType) parentItem.getItemProperty("documenttype").getValue();
                                Object newIndex;
                                
                                switch (parentDocumentType) {
                                    case PROJECT:
                                        // remove document from project
                                        project.removeDocument((Document)selectedObject);
                                        
                                        // no node has to be removed as we already removed the whole document
                                        parentNode = null;
                                        break;
                                    case BOOK:
                                    case SONG:
                                        // adjust parent node such that the node to be deleted will be removed
                                        // from the parent document's data
                                        parentNode = ((Document)parentItem.getItemProperty("object").getValue()).getDataNode();
                                        break;
                                    case COMMON:
                                        parentNode = (Node) parentItem.getItemProperty("object").getValue();
                                        break;
                                    default:
                                        throw new AssertionError();
                                }

                                // calculate new index for the navigation tree
                                //TODO: is there a better way to get the children in order?
                                List<Object> children = Arrays.asList(dataContainer.getChildren(parentItemIdObject).toArray());
                                if (children.size() == 1) {
                                    newIndex = parentItemIdObject;
                                } else {
                                    int index = children.indexOf(selectedItemIdObject);
                                    if (index > 0) {
                                        index--;
                                    }
                                    newIndex = children.get(index);
                                }

                                // remove node and save data structure
                                if (parentNode != null) {
                                    parentNode.removeChild((Node)selectedObject);
                                }
                                save();
                                
                                // reset navigation tree index
                                if (newIndex != null) {
                                    navigationTree.setValue(newIndex);
                                    nodeForm.setItemDataSource(dataContainer.getItem(newIndex));
                                }
                            }
                        }
                    });
        } else {
            LOGGER.log(Level.SEVERE, "selected item id is null");
        }
    }

    private void initDataContainer(Project project) {
        this.project = project;
        this.dataContainer.removeAllItems();

        // add project as root node
        addProject(project);
    }

    /**
     * Adds the project as root node.
     * 
     * @param project 
     */
    private void addProject(Project project) {
        Item item = dataContainer.addItem(ROOT_INDEX);
        item.getItemProperty("title").setValue(project.getCaption());
        item.getItemProperty("rawTitle").setValue(project.getCaption());
        item.getItemProperty("text").setValue("");
        item.getItemProperty("icon").setValue(new ThemeResource("../runo/icons/16/folder.png"));
        item.getItemProperty("documenttype").setValue(DocumentType.PROJECT);
        item.getItemProperty("object").setValue(project);
        item.getItemProperty("itemId").setValue(ROOT_INDEX);
        dataContainer.setChildrenAllowed(ROOT_INDEX, project.getDocumentIdList().size() > 0);
        
        // add documents to root node
        long newId = ROOT_INDEX + 1;
        if (project.getDocumentIdList().size() > 0) {
            for (Document document : project.getDocumentList()) {
                newId = addDocument(document, newId);
            }
        }
    }

    /**
     * Returns the ID of the added document.
     * 
     * @param document
     * @param itemId
     * @param parentId
     * @return 
     */
    private long addDocument(Document document, long itemId) {
        Item item = dataContainer.addItem(itemId);
        item.getItemProperty("title").setValue(document.getCaption());
        item.getItemProperty("rawTitle").setValue(document.getCaption());
        item.getItemProperty("text").setValue("");
        item.getItemProperty("icon").setValue(new ThemeResource("../runo/icons/16/folder.png"));
        item.getItemProperty("documenttype").setValue(document.getDocumentType());
        item.getItemProperty("object").setValue(document);
        item.getItemProperty("itemId").setValue(itemId);
        dataContainer.setChildrenAllowed(itemId, document.getDataNode().numberOfChildren() > 0);
        dataContainer.setParent(itemId, ROOT_INDEX);

        // add data node
        itemId = addItem(document.getDataNode(), itemId + 1, itemId, false);
        
        return itemId;
    }

    /**
     * Returns the next valid ID. Beware! This returns the next valid ID, in contrast to
     * addDocument, which returned the ID of the item just added.
     * 
     * @param parentObject
     * @param node
     * @param itemId
     * @param parentId
     * @param rootVisible
     * @return 
     */
    private long addItem(Node node, long itemId, long parentId, boolean rootVisible) {
        LOGGER.log(Level.FINE, "adding item {0} as child to item {1} with {2} root, node is {3}",
                new String[] {Long.toString(itemId), Long.toString(parentId),
                    rootVisible ? "visible" : "invisible", node.toJson()});
        long newId = itemId;

        if (rootVisible) {
            // add new item
            Item item;
            item = dataContainer.addItem(itemId);
            
            String keyText = node.getKey();
            Item parentItem = dataContainer.getItem(parentId);
            Object parentObject = parentItem.getItemProperty("object").getValue();
            DocumentType parentDocumentType = (DocumentType) parentItem.getItemProperty("documenttype").getValue();
            if (parentObject instanceof Document) {
                for (String attribute : ((Document) parentObject).getAttributeStringList()) {
                    if (attribute.equals(node.getKey())) {
                        keyText = Utilities.getI18NText(parentDocumentType.getPrefix() + keyText);
                        break;
                    }
                }
            }
            
            item.getItemProperty("title").setValue(keyText);
            item.getItemProperty("rawTitle").setValue(node.getKey());
            item.getItemProperty("text").setValue(node.getValue());
            item.getItemProperty("icon").setValue(new ThemeResource("../runo/icons/16/folder.png"));
            item.getItemProperty("object").setValue(node);
            item.getItemProperty("itemId").setValue(itemId);
            item.getItemProperty("documenttype").setValue(DocumentType.COMMON);
            dataContainer.setChildrenAllowed(itemId, node.numberOfChildren() > 0);
            
            // add parent
            if (parentId >= 0) {
                dataContainer.setChildrenAllowed(parentId, true);
                dataContainer.setParent(itemId, parentId);
            }

            newId++;
        } else {
            itemId = parentId;
        }

        // add children
        for (Node child : node.getChildren()) {
            newId = addItem(child, newId, itemId, true);
        }

        // return last valid id
        return newId;
    }
    // </editor-fold>
    // </editor-fold>
}
