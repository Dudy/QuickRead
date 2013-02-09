package de.podolak.quickread.ui;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.PropertySetChangeEvent;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import de.podolak.quickread.QuickReadApplication;
import de.podolak.quickread.Utilities;
import de.podolak.quickread.data.Project;
import de.podolak.quickread.data.datastore.Document;
import de.podolak.quickread.data.datastore.DocumentPersistence;
import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("serial")
public class ProjectManagementWindow extends Window implements Property.ValueChangeListener, ItemClickEvent.ItemClickListener,
        Container.ItemSetChangeListener, Container.PropertySetChangeListener {

    private QuickReadApplication app;
    private HierarchicalContainer dataContainer;
    private Tree projectTree;

    public ProjectManagementWindow(final QuickReadApplication app) {
        this.app = app;

        setCaption(Utilities.getI18NText("projectManagement.caption"));

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        //initProjectTree();
        initProjectTree___new();
        addComponent(projectTree);



        Button newProject = new Button(Utilities.getI18NText("action.newProject"));
        newProject.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Project newProject = new Project();
                newProject.setSerializationVersion(DocumentPersistence.getDefaultVersion());
                newProject.setCreateDate(new Date());
                newProject.setLastModifyDate(new Date());
                DocumentPersistence.storeDocument(newProject);
                dataContainer.addItem(newProject);
                projectTree.select(newProject);
            }
        });
        addComponent(newProject);

        Button show = new Button(Utilities.getI18NText("action.show"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                app.closeProjectManagement(getSelectedProject());
            }
        });
        addComponent(show);

        Button cancel = new Button(Utilities.getI18NText("action.cancel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                app.cancelProjectManagement();
            }
        });
        addComponent(cancel);
    }

//    private class TreeActionHandler implements Property.ValueChangeListener, ItemClickEvent.ItemClickListener,
//            Container.ItemSetChangeListener, Container.PropertySetChangeListener {
//
//        @Override
//        public void valueChange(ValueChangeEvent event) {
////            throw new UnsupportedOperationException("Not supported yet.");
//            System.out.println("valueChange");
//        }
//
//        @Override
//        public void itemClick(ItemClickEvent event) {
////            throw new UnsupportedOperationException("Not supported yet.");
//            System.out.println("itemClick");
//        }
//
//        @Override
//        public void containerItemSetChange(ItemSetChangeEvent event) {
////            throw new UnsupportedOperationException("Not supported yet.");
//            System.out.println("containerItemSetChange");
//        }
//
//        @Override
//        public void containerPropertySetChange(PropertySetChangeEvent event) {
//            System.out.println("containerPropertySetChange");
//        }
//    }
    public void setSelectedProject(Project project) {
        projectTree.select(project);
    }

    public Project getSelectedProject() {
        return (Project) dataContainer.getItem(projectTree.getValue()).getItemProperty("project").getValue();
    }

    private void initProjectTree() {
        // fetch data
        ArrayList<Document> projectList = DocumentPersistence.loadProjectList();

        // create new container
        dataContainer = new HierarchicalContainer();
        dataContainer.addContainerProperty("title", String.class, Utilities.getI18NText("data.newNode.title"));

        int itemId = 0;
        Item item;

        // TODO: use the documents as subnodes
        // TODO: add context menu to make a document the "main document" of a project, that is the document
        //       that is shown in the main view
        for (Document project : projectList) {
            item = dataContainer.addItem(itemId);
            item.getItemProperty("title").setValue(project.getValueListByKey("name").get(0));
            dataContainer.setChildrenAllowed(itemId, false);
            itemId++;
        }
        projectTree = new Tree("projects", dataContainer);

        projectTree.setItemCaptionPropertyId("title");
        projectTree.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);

        projectTree.setImmediate(true);
        projectTree.setSelectable(true);
        projectTree.setMultiSelect(false);
        projectTree.setNullSelectionAllowed(false);

//        TreeActionHandler treeActionHandler = new TreeActionHandler();
//
//        projectTree.addListener((ItemClickEvent.ItemClickListener) treeActionHandler);
//        projectTree.addListener((Property.ValueChangeListener) treeActionHandler);
//        projectTree.addListener((Container.ItemSetChangeListener) treeActionHandler);
//        projectTree.addListener((Container.PropertySetChangeListener) treeActionHandler);
        
        projectTree.addListener((ItemClickEvent.ItemClickListener) this);
        projectTree.addListener((Property.ValueChangeListener) this);
        projectTree.addListener((Container.ItemSetChangeListener) this);
        projectTree.addListener((Container.PropertySetChangeListener) this);
    }

    private void initProjectTree___new() {
        // fetch data
        ArrayList<Project> projectList = DocumentPersistence.loadProjectList___new();

        // create new container
        dataContainer = new HierarchicalContainer();
        dataContainer.addContainerProperty("title", String.class, Utilities.getI18NText("data.newNode.title"));
        dataContainer.addContainerProperty("project", Project.class, null);

        int itemId = 0;
        Item item;

        // TODO: use the documents as subnodes
        // TODO: add context menu to make a document the "main document" of a project, that is the document
        //       that is shown in the main view (update: not necessary when displaying all documents)
        for (Project project : projectList) {
            item = dataContainer.addItem(itemId);
            item.getItemProperty("title").setValue(project.getName());
            item.getItemProperty("project").setValue(project);
            dataContainer.setChildrenAllowed(itemId, false);
            itemId++;
        }
        projectTree = new Tree("projects", dataContainer);

        projectTree.setItemCaptionPropertyId("title");
        projectTree.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);

        projectTree.setImmediate(true);
        projectTree.setSelectable(true);
        projectTree.setMultiSelect(false);
        projectTree.setNullSelectionAllowed(false);

        projectTree.addListener((ItemClickEvent.ItemClickListener) this);
        projectTree.addListener((Property.ValueChangeListener) this);
        projectTree.addListener((Container.ItemSetChangeListener) this);
        projectTree.addListener((Container.PropertySetChangeListener) this);
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
//            throw new UnsupportedOperationException("Not supported yet.");
        System.out.println("valueChange");
    }

    @Override
    public void itemClick(ItemClickEvent event) {
//            throw new UnsupportedOperationException("Not supported yet.");
        System.out.println("itemClick");
    }

    @Override
    public void containerItemSetChange(ItemSetChangeEvent event) {
//            throw new UnsupportedOperationException("Not supported yet.");
        System.out.println("containerItemSetChange");
    }

    @Override
    public void containerPropertySetChange(PropertySetChangeEvent event) {
        System.out.println("containerPropertySetChange");
    }
}
