package de.podolak.quickread.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import de.podolak.quickread.QuickReadApplication;
import de.podolak.quickread.data.Project;
import de.podolak.quickread.data.datastore.Document;
import de.podolak.quickread.data.datastore.DocumentPersistence;
import de.podolak.quickread.data.datastore.DocumentType;
import de.podolak.quickread.data.datastore.Node;
import java.util.Date;

/**
 *
 * @author Dude
 */
@SuppressWarnings("serial")
public class SearchWindow extends Window {

    private QuickReadApplication app;
    private Button goButton = new Button("go");
    
    public SearchWindow(QuickReadApplication app) {
        this.app = app;
        
        setCaption("DUMMY - not yet i18nized");
        addComponent(goButton);
        goButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                goAction();
            }
        });
        
    }
    
    private void goAction() {
        //resetDatastore();
        app.resetData();
    }
    
    private void createTwoInitialProjects() {
        Node root = new Node(Project.KEY_PROJECT_NAME, "Project 1");
        Document project = new Document(1L, 1, root, new Date(), new Date(), DocumentType.PROJECT);
        Node node = new Node(Project.KEY_DOCUMENT_ID, "2");
        root.addChild(node);
        node = new Node(Project.KEY_DOCUMENT_ID, "3");
        root.addChild(node);
        DocumentPersistence.storeDocument(project);
        System.out.println("document 1 stored - PROJECT");
        DocumentPersistence.storeDocument(new Document(2L, 1, new Node("unbenanntes erstes Buch"), new Date(), new Date(), DocumentType.BOOK));
        System.out.println("document 2 stored - BOOK");
        DocumentPersistence.storeDocument(new Document(3L, 1, new Node("unbenanntes zweites Buch"), new Date(), new Date(), DocumentType.BOOK));
        System.out.println("document 3 stored - BOOK");
        
        root = new Node(Project.KEY_PROJECT_NAME, "Project 2");
        project = new Document(4L, 1, root, new Date(), new Date(), DocumentType.PROJECT);
        node = new Node(Project.KEY_DOCUMENT_ID, "6");
        root.addChild(node);
        node = new Node(Project.KEY_DOCUMENT_ID, "3");
        root.addChild(node);
        node = new Node(Project.KEY_DOCUMENT_ID, "5");
        root.addChild(node);
        DocumentPersistence.storeDocument(project);
        System.out.println("document 4 stored - PROJECT");
        DocumentPersistence.storeDocument(new Document(5L, 1, new Node("unbenanntes Musikstück"), new Date(), new Date(), DocumentType.SONG));
        System.out.println("document 5 stored - SONG");
        DocumentPersistence.storeDocument(new Document(6L, 1, new Node("unbenanntes drittes Buch"), new Date(), new Date(), DocumentType.BOOK));
        System.out.println("document 6 stored - BOOK");
    }
    
    private void createTwoInitialProjects___new() {
        Project project1 = new Project();
        project1.setId(1L);
        project1.setSerializationVersion(DocumentPersistence.getDefaultVersion());
        project1.setCreateDate(new Date());
        project1.setLastModifyDate(new Date());
        
        
        Node root = new Node(Project.KEY_PROJECT_NAME, "Project 1");
        Document project = new Document(1L, 1, root, new Date(), new Date(), DocumentType.PROJECT);
        Node node = new Node(Project.KEY_DOCUMENT_ID, "2");
        root.addChild(node);
        node = new Node(Project.KEY_DOCUMENT_ID, "3");
        root.addChild(node);
        DocumentPersistence.storeDocument(project);
        System.out.println("document 1 stored - PROJECT");
        DocumentPersistence.storeDocument(new Document(2L, 1, new Node("unbenanntes erstes Buch"), new Date(), new Date(), DocumentType.BOOK));
        System.out.println("document 2 stored - BOOK");
        DocumentPersistence.storeDocument(new Document(3L, 1, new Node("unbenanntes zweites Buch"), new Date(), new Date(), DocumentType.BOOK));
        System.out.println("document 3 stored - BOOK");
        
        root = new Node(Project.KEY_PROJECT_NAME, "Project 2");
        project = new Document(4L, 1, root, new Date(), new Date(), DocumentType.PROJECT);
        node = new Node(Project.KEY_DOCUMENT_ID, "6");
        root.addChild(node);
        node = new Node(Project.KEY_DOCUMENT_ID, "3");
        root.addChild(node);
        node = new Node(Project.KEY_DOCUMENT_ID, "5");
        root.addChild(node);
        DocumentPersistence.storeDocument(project);
        System.out.println("document 4 stored - PROJECT");
        DocumentPersistence.storeDocument(new Document(5L, 1, new Node("unbenanntes Musikstück"), new Date(), new Date(), DocumentType.SONG));
        System.out.println("document 5 stored - SONG");
        DocumentPersistence.storeDocument(new Document(6L, 1, new Node("unbenanntes drittes Buch"), new Date(), new Date(), DocumentType.BOOK));
        System.out.println("document 6 stored - BOOK");
    }

    private void emptyDatastore() {
        DocumentPersistence.emptyDatastore();
        System.out.println("datastore erased");
    }
    
    private void resetDatastore() {
        emptyDatastore();
        createTwoInitialProjects();
    }
}
