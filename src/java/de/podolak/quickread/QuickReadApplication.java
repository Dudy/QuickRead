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
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.dialogs.ConfirmDialog;

@SuppressWarnings("serial")
public class QuickReadApplication extends Application implements
        ClickListener, ValueChangeListener, ItemClickListener,
        Container.ItemSetChangeListener, Container.PropertySetChangeListener {

    private static final int ROOT_INDEX = 0;

    private Window window;
    private NavigationTree navigationTree;
    private Button addNode = new Button(Utilities.getI18NText("action.addNode.button.caption"));
    private Button removeNode = new Button(Utilities.getI18NText("action.removeNode.button.caption"));
    private Button search = new Button(Utilities.getI18NText("action.search.button.caption"));
    private Button help = new Button(Utilities.getI18NText("action.help.button.caption"));
    private HorizontalSplitPanel horizontalSplit = new HorizontalSplitPanel();

    // Lazyly created ui references
    private NodeView nodeView = null;
    private HelpWindow helpWindow = null;
    private NodeForm nodeForm = null;

    // data handling
    private HierarchicalContainer dataContainer;
    private Long documentId = null;
    private Date createDate;

    @Override
    public void init() {
        documentId = 1L; // vorerst mal nur auf dem einen Datensatz operieren
        dataContainer = new HierarchicalContainer();
        createDataContainer(DocumentPersistence.loadDocument(documentId));
        navigationTree = new NavigationTree(this);

        buildMainLayout();
        setMainComponent(getNodeView());
    }

    private void buildMainLayout() {
        window = new Window(Utilities.getI18NText("window.title"));
        System.out.println(window);
        setMainWindow(window);

        setTheme("quickread");

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        layout.addComponent(createToolbar());
        layout.addComponent(horizontalSplit);
        layout.setExpandRatio(horizontalSplit, 1);

        horizontalSplit.setSplitPosition(400, Sizeable.UNITS_PIXELS);
        horizontalSplit.setFirstComponent(navigationTree);

        getMainWindow().setContent(layout);
    }

    private HorizontalLayout createToolbar() {
        HorizontalLayout lo = new HorizontalLayout();
        lo.addComponent(addNode);
        lo.addComponent(removeNode);

        lo.addComponent(search);
        lo.addComponent(help);

        addNode.addListener((ClickListener) this);
        removeNode.addListener((ClickListener) this);

        search.addListener((ClickListener) this);
        help.addListener((ClickListener) this);

        addNode.setIcon(new ThemeResource("icons/32x32/actions/edit_add.png"));
        removeNode.setIcon(new ThemeResource("icons/32x32/actions/editdelete.png"));

        addNode.addStyleName("multiline");
        removeNode.addStyleName("multiline");

        search.setIcon(new ThemeResource("icons/32x32/actions/find.png"));
        help.setIcon(new ThemeResource("icons/32x32/actions/contents.png"));

        lo.setMargin(true);
        lo.setSpacing(true);

        lo.setStyleName("toolbar");

        lo.setWidth("100%");

        Embedded em = new Embedded("", new ThemeResource("images/Quickread06_height64.png"));
        lo.addComponent(em);
        lo.setComponentAlignment(em, Alignment.TOP_RIGHT);
        lo.setExpandRatio(em, 1);

        return lo;
    }

    private void setMainComponent(Component c) {
        horizontalSplit.setSecondComponent(c);
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

    private void showHelpWindow() {
        getMainWindow().addWindow(getHelpWindow());
    }

    public Container getDataContainer() {
        return dataContainer;
    }

    private void addNode() {
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

    private void removeNode() {
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

    public void save() {
        Document document = DocumentPersistence.storeDocument(getDocument());

        if (document == null) {
            window.showNotification(
                    Utilities.getI18NText("action.save.error.caption"),
                    Utilities.getI18NText("action.save.error.description"),
                    Notification.TYPE_WARNING_MESSAGE);
        } else {
            window.showNotification(
                    Utilities.getI18NText("action.save.success.caption"),
                    Utilities.getI18NText("action.save.success.description"));
            createDataContainer(document);
            navigationTree.requestRepaint();
        }
    }

    // <editor-fold defaultstate="collapsed" desc=" event handlers ">
    @Override
    public void buttonClick(ClickEvent event) {
        final Button source = event.getButton();

        if (source == help) {
            showHelpWindow();
        } else if (source == addNode) {
            addNode();
        } else if (source == removeNode) {
            removeNode();
        }
    }
    
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

    // <editor-fold defaultstate="collapsed" desc=" data container handling ">
    private void createDataContainer(Document document) {
        createDate = document.getCreateDate();
        
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

    private Document getDocument() {
        //TODO: WTF? Ich erzeuge ein neues Document jedes Mal? Wasn Quatsch!
        // Mach das weg! Oder doch net? Shit! Persistenz neu überdenken!!! DB Modell erstellen! Dann machen!
        
        return new Document(documentId, getNode(ROOT_INDEX), createDate, new Date());
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
    
}
