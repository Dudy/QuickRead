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
import de.podolak.quickread.data.Project;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;

/**
 *
 * @author Dude
 */
public class DocumentPersistence {
    
    //TODO: use the current user here, return empty document when no user is logged on, read last document ID from cookie
    //TODO: add metadata
    public static Document getLastDocument() {
        return loadDocument(2L);
    }
    
    public static Document loadDocument(Long id) {
        Key key = KeyFactory.createKey("Document", id);
            
        try {
            Entity datastoreDocument = DatastoreServiceFactory.getDatastoreService().get(key);
            Document document = loadDocumentFromString(((Text)datastoreDocument.getProperty("content")).getValue());
            return document;
        } catch (EntityNotFoundException ex) {
            Logger.getLogger(DocumentPersistence.class.getName()).log(Level.INFO, "no Document with id {0} found, creating a new one", id);
            
            // create new Document, store immediately and hope the id is still not used ;-)
            //TODO: read the current (= max) version data from metadata and use it here, use 1 for now
            Document document = new Document(id, 1, new Node(Utilities.getI18NText("document.new")), new Date(), new Date(), DocumentType.COMMON);
            storeDocument(document);
            return document;
        }
    }
    
    public static Document loadDocumentFromString(String content) {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            return DocumentHandler.deserialize(builder.parse(new InputSource(new StringReader(content))));
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            Logger.getLogger(DocumentPersistence.class.getName()).log(Level.SEVERE, "Fehler: kann keinen Parser erzeugen", e);
        } catch (org.xml.sax.SAXException e) {
            Logger.getLogger(DocumentPersistence.class.getName()).log(Level.SEVERE, "Fehler: kann das document nicht parsen", e);
        } catch (java.io.IOException e) {
            Logger.getLogger(DocumentPersistence.class.getName()).log(Level.SEVERE, "Fehler: kann das document nicht öffnen", e);
        }
        
        return null;
    }
    
    public static Document storeDocument(Document document) {
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
        return 2;
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
        
        return projectList;
    }
    
}
