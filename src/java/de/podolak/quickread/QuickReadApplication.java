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
import de.podolak.quickread.ui.NodeForm;
import de.podolak.quickread.ui.NodeView;
import de.podolak.quickread.ui.ProjectManagementWindow;
import de.podolak.quickread.ui.SearchWindow;
import de.podolak.quickread.ui.Toolbar;
import de.podolak.quickread.ui.wizard.NewDocumentWindow;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.vaadin.dialogs.ConfirmDialog;

@SuppressWarnings("serial")
public class QuickReadApplication extends Application implements
        ValueChangeListener, ItemClickListener,
        Container.ItemSetChangeListener, Container.PropertySetChangeListener {

    private static final int ROOT_INDEX = 0;
    private Window window;
    private static HashMap<ApplicationContext, TransactionListener> requestListeners = new HashMap<ApplicationContext, TransactionListener>();
    private boolean isInitialized = false;
    
    // ui
    private NavigationTree navigationTree;
    private Toolbar toolbar;
    private HorizontalSplitPanel horizontalSplit = new HorizontalSplitPanel();
    
    // Lazyly created ui references
    private NodeView nodeView = null;
    private HelpWindow helpWindow = null;
    private NodeForm nodeForm = null;
    private SearchWindow searchWindow = null;

    private NewDocumentWindow newDocumentWindow = null;
    
    private ProjectManagementWindow projectManagementWindow = null;
    
    // data handling
    private HierarchicalContainer dataContainer;
    private Project project;

    // <editor-fold defaultstate="collapsed" desc=" application ">
    @Override
    public void init() {
        initData(DocumentPersistence.getFirstProject());
        
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
    
    public void projectManagement() {
        getMainWindow().addWindow(getProjectManagementWindow());
        getProjectManagementWindow().setSelectedProject(project);
    }
    
    public void cancelProjectManagement() {
        getMainWindow().removeWindow(projectManagementWindow);
    }
    
    public void closeProjectManagement(Project selectedProject) {
        getMainWindow().removeWindow(projectManagementWindow);
        initData(selectedProject);
        navigationTree.setContainerDataSource(getDataContainer());
    }
    
    public void closeNewDocumentWindow(Document newDocument) {
        getMainWindow().removeWindow(newDocumentWindow);
        
        if (newDocument != null) {
            project.addDocument(newDocument);
            initData(project);
            navigationTree.setContainerDataSource(getDataContainer());
        }
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
        }
        return helpWindow;
    }

    private NodeView getNodeView() {
        if (nodeView == null) {
            nodeForm = new NodeForm(this);
            nodeView = new NodeView(nodeForm);
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
            
//            if (dataContainer != null) {
//                dataContainer.addListener(newDocumentWindow);
//            }
        }
        return newDocumentWindow;
    }

    public void showHelpWindow() {
        getMainWindow().addWindow(getHelpWindow());
    }
    
    public void showSearchWindow() {
        getMainWindow().addWindow(getSearchWindow());
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
        System.out.println("2: " + event);
        System.out.println(dataContainer.size());
    }

    @Override
    public void containerPropertySetChange(PropertySetChangeEvent event) {
        System.out.println("4: " + event);
    }
    // </editor-fold>

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" data ">
    @Deprecated
    public void resetData() {
//        initData(DocumentPersistence.loadDocument(2L));
//        navigationTree.setContainerDataSource(getDataContainer());
//        navigationTree.requestRepaint();
    }
    
    private void initData(Project project) {
        dataContainer = new HierarchicalContainer();
        createDataContainer(project);
    }
    
    public void save() {
//        Document newDocument = DocumentPersistence.storeDocument(new Document(
//                project.getId(),
//                DocumentPersistence.getDefaultVersion(),
//                getNode(ROOT_INDEX),
//                project.getCreateDate(),
//                new Date(),
//                project.getDocumentType()));
//
//        if (newDocument == null) {
//            showMessage(
//                    Utilities.getI18NText("action.save.error.caption"),
//                    Utilities.getI18NText("action.save.error.description"),
//                    Notification.TYPE_WARNING_MESSAGE);
//        } else {
//            showMessage(
//                    Utilities.getI18NText("action.save.success.caption"),
//                    Utilities.getI18NText("action.save.success.description"));
//            createDataContainer(newDocument);
//            navigationTree.requestRepaint();
//        }
        

//        // always set root node first !!!
//        Project newProject = new Project();
//        newProject.setRoot(getNode(ROOT_INDEX));
//        newProject.setDocumentType(project.getDocumentType());
//        newProject.setId(project.getId());
//        newProject.setSerializationVersion(DocumentPersistence.getDefaultVersion());
//        newProject.setCreateDate(project.getCreateDate());
//        newProject.setLastModifyDate(new Date());
//        
//        project = DocumentPersistence.storeDocument(newProject);
//
//        if (project == null) {
//            showMessage(
//                    Utilities.getI18NText("action.save.error.caption"),
//                    Utilities.getI18NText("action.save.error.description"),
//                    Notification.TYPE_WARNING_MESSAGE);
//        } else {
//            showMessage(
//                    Utilities.getI18NText("action.save.success.caption"),
//                    Utilities.getI18NText("action.save.success.description"));
//            createDataContainer(project);
//            navigationTree.setContainerDataSource(getDataContainer());
//        }
        
        
        
        project = DocumentPersistence.storeDocument(project);

        if (project == null) {
            showMessage(
                    Utilities.getI18NText("action.save.error.caption"),
                    Utilities.getI18NText("action.save.error.description"),
                    Notification.TYPE_WARNING_MESSAGE);
        } else {
            showMessage(
                    Utilities.getI18NText("action.save.success.caption"),
                    Utilities.getI18NText("action.save.success.description"));
            createDataContainer(project);
            navigationTree.requestRepaint();
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
            if (selectedItemIdObject instanceof Integer) {
                Integer selectedItemId = (Integer) selectedItemIdObject;
                Item selectedItem = dataContainer.getItem(selectedItemId);
                DocumentType selectedDocumentType = (DocumentType) selectedItem.getItemProperty("documenttype").getValue();
                Object lastItemIdObject = dataContainer.lastItemId();

                if (lastItemIdObject instanceof Integer) {
                    Integer lastItemId = (Integer) dataContainer.lastItemId();
                    Node node = null;
                    
                    switch (selectedDocumentType) {
                        case PROJECT:
                            showNewDocumentWindow();
                            break;
                        
                        // for now all the other ones just get new nodes
                        case BOOK:
                        case SONG:
                        case COMMON:
                        default:
                            node = new Node(Utilities.getI18NText("data.newNode.title"), Utilities.getI18NText("data.newNode.text"));
                            ((Node)selectedItem.getItemProperty("node").getValue()).addChild(node);
                            break;
                    }
                    
                    if (node != null) {
                        addItem(node, lastItemId + 1, selectedItemId, true);
                        save();
                    }
                } else {
                    Logger.getLogger(QuickReadApplication.class.getName()).log(Level.SEVERE, "last item id in dataContainer is not of type Integer");
                }
            } else {
                Logger.getLogger(QuickReadApplication.class.getName()).log(Level.SEVERE, "selected item id is not of type Integer");
            }
        } else {
            Logger.getLogger(QuickReadApplication.class.getName()).log(Level.SEVERE, "selected item id is null");
        }
        
        navigationTree.requestRepaint();
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

        if (selectedItemIdObject != null) {
            ConfirmDialog.show(
                    getMainWindow(),
                    Utilities.getI18NText("action.removeNode.confirmDialog.caption"),
                    Utilities.getI18NText("action.removeNode.confirmDialog.text"),
                    Utilities.getI18NText("action.removeNode.confirmDialog.yes"),
                    Utilities.getI18NText("action.removeNode.confirmDialog.no"),
                    new ConfirmDialog.Listener() {
                        @Override
                        public void onClose(ConfirmDialog dialog) {
                            if (dialog.isConfirmed()) {
                                Object parentItemIdObject = dataContainer.getParent(selectedItemIdObject);
                                Item selectedItem = dataContainer.getItem(selectedItemIdObject);
                                Node selectedNode = (Node)selectedItem.getItemProperty("node").getValue();
                                Item parentItem = dataContainer.getItem(parentItemIdObject);
                                Node parentNode = (Node)parentItem.getItemProperty("node").getValue();
                                parentNode.removeChild(selectedNode);
                                
                                //TODO: is there a better way to get the children in order?
                                List<Object> children = Arrays.asList(dataContainer.getChildren(parentItemIdObject).toArray());

                                if (children.size() == 1) {
                                    dataContainer.setChildrenAllowed(parentItemIdObject, false);
                                    navigationTree.setValue(parentItemIdObject);
                                    nodeForm.setItemDataSource(dataContainer.getItem(parentItemIdObject));
                                    dataContainer.removeItemRecursively(selectedItemIdObject);
                                } else {
                                    int index = children.indexOf(selectedItemIdObject);

                                    if (index > 0) {
                                        index--;
                                    }

                                    navigationTree.setValue(children.get(index));
                                    nodeForm.setItemDataSource(dataContainer.getItem(children.get(index)));
                                    dataContainer.removeItem(selectedItemIdObject);
                                }

                                save();
                            }
                        }
                    });
        } else {
            Logger.getLogger(QuickReadApplication.class.getName()).log(Level.SEVERE, "selected item id is null");
        }
    }

    private void createDataContainer(Project project) {
        this.project = project;

        // create new container
        dataContainer.removeAllItems();
        dataContainer.addContainerProperty("title", String.class, Utilities.getI18NText("data.newNode.title"));
        dataContainer.addContainerProperty("text", String.class, Utilities.getI18NText("data.newNode.text"));
        dataContainer.addContainerProperty("icon", ThemeResource.class, new ThemeResource("../runo/icons/16/document.png"));
        dataContainer.addContainerProperty("documenttype", DocumentType.class, null);
        dataContainer.addContainerProperty("node", Node.class, null);

        // add project as root node
        int projectId = addProject(project, ROOT_INDEX);
        int newId = projectId + 1;
        
        if (project.getDocumentIdList().size() > 0) {
            for (Document document : project.getDocumentList()) {
                newId = addDocument(document, newId, projectId);
            }
        }
    }
    
    private int addProject(Project project, int itemId) {
        Item item = dataContainer.addItem(itemId);
        item.getItemProperty("title").setValue(project.getCaption());
        item.getItemProperty("text").setValue("");
        item.getItemProperty("icon").setValue(new ThemeResource("../runo/icons/16/folder.png"));
        item.getItemProperty("documenttype").setValue(DocumentType.PROJECT);
        item.getItemProperty("node").setValue(project);
        dataContainer.setChildrenAllowed(itemId, project.getDocumentIdList().size() > 0);
        return itemId;
    }
    
    private int addDocument(Document document, int itemId, int parentId) {
        Item item = dataContainer.addItem(itemId);
        item.getItemProperty("title").setValue(document.getCaption());
        item.getItemProperty("text").setValue("");
        item.getItemProperty("icon").setValue(new ThemeResource("../runo/icons/16/folder.png"));
        item.getItemProperty("documenttype").setValue(DocumentType.COMMON);
        item.getItemProperty("node").setValue(document);
        dataContainer.setChildrenAllowed(itemId, document.getDataNode().numberOfChildren() > 0);
        dataContainer.setParent(itemId, parentId);
        
        itemId = addItem(document.getDataNode(), itemId + 1, itemId, false);
        
        return itemId;
    }
    
    private int addItem(Node node, int itemId, int parentId, boolean rootVisible) {
        int newId = itemId;
        
        if (rootVisible) {
            // add new item
            Item item;
            item = dataContainer.addItem(itemId);
            item.getItemProperty("title").setValue(node.getKey());
            item.getItemProperty("text").setValue(node.getValue());
            item.getItemProperty("icon").setValue(new ThemeResource("../runo/icons/16/folder.png"));
            item.getItemProperty("node").setValue(node);
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

    private Node getNode(int itemId) {
        Item item = dataContainer.getItem(itemId);
        Node node = new Node(
                item.getItemProperty("title").getValue().toString(),
                item.getItemProperty("text").getValue().toString());

        Collection<?> children = dataContainer.getChildren(itemId);
        if (children != null && children.size() > 0) {
            for (Object childIdObject : children) {
                node.addChild(getNode((Integer) childIdObject));
            }
        }

        return node;
    }
    // </editor-fold>

    // </editor-fold>

}
