package de.podolak.quickread;

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
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import de.podolak.quickread.data.Document;
import de.podolak.quickread.data.Node;
import de.podolak.quickread.data.persistence.DocumentPersistence;
import de.podolak.quickread.ui.HelpWindow;
import de.podolak.quickread.ui.NavigationTree;
import de.podolak.quickread.ui.NodeForm;
import de.podolak.quickread.ui.NodeView;
import de.podolak.quickread.ui.Toolbar;
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
    
    // data handling
    private HierarchicalContainer dataContainer;
    private Document document;

    // <editor-fold defaultstate="collapsed" desc=" application ">
    @Override
    public void init() {
        initData();
        
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

    public HelpWindow getHelpWindow() {
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

    public void showHelpWindow() {
        getMainWindow().addWindow(getHelpWindow());
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
    private void initData() {
        dataContainer = new HierarchicalContainer();
        createDataContainer(DocumentPersistence.getLastDocument());
    }
    
    public void save() {
        Document newDocument = DocumentPersistence.storeDocument(new Document(
                document.getId(),
                getNode(ROOT_INDEX),
                document.getCreateDate(),
                new Date()));

        if (newDocument == null) {
            window.showNotification(
                    Utilities.getI18NText("action.save.error.caption"),
                    Utilities.getI18NText("action.save.error.description"),
                    Notification.TYPE_WARNING_MESSAGE);
        } else {
            window.showNotification(
                    Utilities.getI18NText("action.save.success.caption"),
                    Utilities.getI18NText("action.save.success.description"));
            createDataContainer(newDocument);
            navigationTree.requestRepaint();
        }
    }

    // <editor-fold defaultstate="collapsed" desc=" data container handling ">
    public Container getDataContainer() {
        return dataContainer;
    }

    public void addNode() {
        Object selectedItemIdObject = navigationTree.getValue();

        if (selectedItemIdObject != null) {
            if (selectedItemIdObject instanceof Integer) {
                Integer selectedItemId = (Integer) selectedItemIdObject;
                Object lastItemIdObject = dataContainer.lastItemId();

                if (lastItemIdObject instanceof Integer) {
                    Integer lastItemId = (Integer) dataContainer.lastItemId();
                    Node node = new Node(Utilities.getI18NText("data.newNode.title"), Utilities.getI18NText("data.newNode.text"));
                    addItem(node, lastItemId + 1, selectedItemId);
                    save();
                } else {
                    Logger.getLogger(QuickReadApplication.class.getName()).log(Level.SEVERE, "last item id in dataContainer is not of type Integer");
                }
            } else {
                Logger.getLogger(QuickReadApplication.class.getName()).log(Level.SEVERE, "selected item id is not of type Integer");
            }
        } else {
            Logger.getLogger(QuickReadApplication.class.getName()).log(Level.SEVERE, "selected item id is null");
        }
    }

    public void removeNode() {
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

    private void createDataContainer(Document document) {
        this.document = document;

        // create new container
        dataContainer.removeAllItems();
        dataContainer.addContainerProperty("title", String.class, Utilities.getI18NText("data.newNode.title"));
        dataContainer.addContainerProperty("text", String.class, Utilities.getI18NText("data.newNode.text"));
        dataContainer.addContainerProperty("icon", ThemeResource.class, new ThemeResource("../runo/icons/16/document.png"));

        // add data tree
        addItem(document.getRoot(), ROOT_INDEX, -1);
    }

    private int addItem(Node node, int itemId, int parentId) {
        // add new item
        Item item;
        item = dataContainer.addItem(itemId);
        item.getItemProperty("title").setValue(node.getTitle());
        item.getItemProperty("text").setValue(node.getText());
        item.getItemProperty("icon").setValue(new ThemeResource("../runo/icons/16/folder.png"));
        dataContainer.setChildrenAllowed(itemId, node.getNumberOfChildren() > 0);

        // add parent
        if (parentId >= 0) {
            dataContainer.setChildrenAllowed(parentId, true);
            dataContainer.setParent(itemId, parentId);
        }

        // add children
        int newId = itemId + 1;
        for (Node child : node.getChildren()) {
            newId = addItem(child, newId, itemId);
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
