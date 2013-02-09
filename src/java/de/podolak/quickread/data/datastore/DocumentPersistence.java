package de.podolak.quickread.data.datastore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;
import de.podolak.quickread.Utilities;
import de.podolak.quickread.data.Book;
import de.podolak.quickread.data.Project;
import de.podolak.quickread.data.Song;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dude
 */
public class DocumentPersistence {
    
    private static final Logger LOGGER = Logger.getLogger(DocumentPersistence.class.getName());
    
    public static Project getFirstProject() {
        //TODO: this is kind of clumsy ...
        ArrayList<Project> projectList = loadProjectList___new();
        Project firstProject = projectList.get(0);
        return firstProject;
    }
    
    //TODO: use the current user here, return empty document when no user is logged on, read last document ID from cookie
    //TODO: add metadata
    public static Document getLastDocument() {
        return loadDocument(2L);
    }
    
    public static Document loadDocument(Integer documentId) {
        return loadDocument(Long.valueOf(documentId));
    }
    
    public static Document loadDocument(Long id) {
        Key key = KeyFactory.createKey("Document", id);
            
        try {
            Entity datastoreDocument = DatastoreServiceFactory.getDatastoreService().get(key);
            Document document = loadDocumentFromString(((Text)datastoreDocument.getProperty("content")).getValue());
            return document;
        } catch (EntityNotFoundException ex) {
            LOGGER.log(Level.INFO, "no Document with id {0} found, creating a new one", id);
            
            // create new Document, store immediately and hope the id is still not used ;-)
            //TODO: read the current (= max) version data from metadata and use it here, use 1 for now
            Document document = new Document(id, 1, new Node(Utilities.getI18NText("document.new")), new Date(), new Date(), DocumentType.COMMON);
            storeDocument(document);
            return document;
        }
    }
    
    public static Document loadDocumentFromString(String content) {
        return DocumentHandler.deserialize(content);
    }
    
//    public static Document storeDocument(Document document) {
//        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//        Entity datastoreDocument;
//        
//        if (document.getId() != null) {
//            Key key = KeyFactory.createKey("Document", document.getId());
//            
//            try {
//                datastoreDocument = datastore.get(key);
//            } catch (EntityNotFoundException ex) {
//                datastoreDocument = new Entity(key);
//            }
//        } else {
//            datastoreDocument = new Entity("Document");
//            document.setId(datastoreDocument.getKey().getId());
//        }
//        
//        datastoreDocument.setProperty("content", new Text(DocumentHandler.serialize(document)));
//        datastore.put(datastoreDocument);
//        
//        return document;
//    }
    
    public static <T extends Document> T storeDocument(T document) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity datastoreDocument;
        
        if (document.getId() != null) {
            Key key = KeyFactory.createKey("Document", document.getId());
            
            try {
                datastoreDocument = datastore.get(key);
            } catch (EntityNotFoundException ex) {
                datastoreDocument = new Entity(key);
            }
        } else {
            datastoreDocument = new Entity("Document");
            document.setId(datastoreDocument.getKey().getId());
        }
        
        datastoreDocument.setProperty("content", new Text(DocumentHandler.serialize(document)));
        datastore.put(datastoreDocument);
        
        LOGGER.log(Level.INFO, ((Text)datastoreDocument.getProperty("content")).getValue());
        
        return document;
    }
    
    public static void emptyDatastore() {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("Document");
        PreparedQuery pq = datastore.prepare(q);
        
        for (Entity entity : pq.asIterable()) {
            datastore.delete(entity.getKey());
        }
    }
    
    public static Integer getDefaultVersion() {
        // TODO: think about storing this in metadata in datastore
        return 3;
    }

    public static ArrayList<Document> loadProjectList() {
        ArrayList<Document> documentList = new ArrayList<Document>();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("Document");
        PreparedQuery pq = datastore.prepare(q);
        
        for (Entity entity : pq.asIterable()) {
            Document document = loadDocumentFromString(((Text)entity.getProperty("content")).getValue());
            
            if (document.getDocumentType() == DocumentType.PROJECT) {
                documentList.add(document);
            }
        }
        
        return documentList;
    }
    
    public static ArrayList<Project> loadProjectList___new() {
        ArrayList<Project> projectList = new ArrayList<Project>();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("Document");
        PreparedQuery pq = datastore.prepare(q);
        
        for (Entity entity : pq.asIterable()) {
            Document document = loadDocumentFromString(((Text)entity.getProperty("content")).getValue());
            
            if (document.getDocumentType() == DocumentType.PROJECT) {
                projectList.add((Project)document);
            }
        }
        
        if (projectList.isEmpty()) {
            projectList.add(createAndGetInitialProject());
        }
        
        return projectList;
    }
    
    private static Project createAndGetInitialProject() {
        Project returnValue = null;
        
        Project project = new Project();
        project.setId(1L);
        project.setSerializationVersion(3);
        project.setCreateDate(new Date());
        project.setLastModifyDate(new Date());
        project.setDocumentType(DocumentType.PROJECT);
        project.setName("project 1");
        project.addData("documentID", 2);
        project.addData("documentID", 3);
        storeDocument(project);
        returnValue = project;
        
        Book book = new Book();
        book.setId(2L);
        book.setSerializationVersion(3);
        book.setCreateDate(new Date());
        book.setLastModifyDate(new Date());
        book.setDocumentType(DocumentType.BOOK);
        book.setAuthor("Dirk Podolak");
        book.setTitle("the joy of programming");
        book.addData("introduction", "Dirk Podolak talks about the joy of programming.");
        book.addData("contents", "Start here to explore why Dirk Podolak thinks it is a joy to write computer programs.");
        book.addData("index", "lookup table of some important terms");
        book.addData("contents.chapter 1", "todo: summary of chapter 1");
        book.addData("contents.chapter 2", "todo: summary of chapter 2");
        book.addData("contents.chapter 3", "todo: summary of chapter 3");
        storeDocument(book);
        
        book = new Book();
        book.setId(3L);
        book.setSerializationVersion(3);
        book.setCreateDate(new Date());
        book.setLastModifyDate(new Date());
        book.setDocumentType(DocumentType.BOOK);
        book.setAuthor("Dirk Podolak");
        book.setTitle("the pain of programming");
        book.addData("introduction", "Dirk Podolak talks about the pain of programming.");
        book.addData("contents", "Start here to explore why Dirk Podolak thinks it is a pain to write computer programs.");
        book.addData("index", "lookup table of some important terms");
        book.addData("contents.chapter 1", "todo: summary of chapter 1");
        book.addData("contents.chapter 2", "todo: summary of chapter 2");
        book.addData("contents.chapter 3", "todo: summary of chapter 3");
        storeDocument(book);
        
        project = new Project();
        project.setId(4L);
        project.setSerializationVersion(3);
        project.setCreateDate(new Date());
        project.setLastModifyDate(new Date());
        project.setDocumentType(DocumentType.PROJECT);
        project.setName("project 2");
        project.addData("documentID", 6);
        project.addData("documentID", 3);
        project.addData("documentID", 5);
        storeDocument(project);
        
        Song song = new Song();
        song.setId(5L);
        song.setSerializationVersion(3);
        song.setCreateDate(new Date());
        song.setLastModifyDate(new Date());
        song.setDocumentType(DocumentType.SONG);
        song.setTitle("Phuture Paradise");
        song.setArtist("Dirk Podolak");
        song.addData("text", "the future ... is paradise (30 times)");
        song.addData("music", "three instruments");
        song.addData("music.percussion", "dumm de dumm ... blizzzzzz (15 times)");
        song.addData("music.triangle", "dschingggg dschingggg (60 times)");
        song.addData("music.piano", "pling pling plong plang pling (22 times)");
        storeDocument(song);
        
        book = new Book();
        book.setId(6L);
        book.setSerializationVersion(3);
        book.setCreateDate(new Date());
        book.setLastModifyDate(new Date());
        book.setDocumentType(DocumentType.BOOK);
        book.setAuthor("Dirk Podolak");
        book.setTitle("Music for the People");
        book.addData("introduction", "Dirk Podolak talks about how music can reach peoples hearts easier than words.");
        book.addData("contents", "Start here to explore how Dirk Podolak thinks music can change the future.");
        book.addData("index", "lookup table of some important terms");
        book.addData("contents.chapter 1", "todo: summary of chapter 1");
        book.addData("contents.chapter 2", "todo: summary of chapter 2");
        book.addData("contents.chapter 3", "todo: summary of chapter 3");
        book.addData("contents.chapter 4", "todo: summary of chapter 4");
        book.addData("contents.chapter 5", "todo: summary of chapter 5");
        storeDocument(book);
        
        return returnValue;
    }
    
}
