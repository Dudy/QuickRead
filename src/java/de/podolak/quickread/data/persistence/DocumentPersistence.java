package de.podolak.quickread.data.persistence;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import de.podolak.quickread.data.Document;
import de.podolak.quickread.data.Node;
import java.io.StringReader;
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
    
    //TODO: use the current user here, return empty document when no user is logged on
    public static Document getLastDocument() {
        return loadDocument(1L);
    }
    
    public static Document loadDocument(Long id) {
        Key key = KeyFactory.createKey("Document", id);
            
        try {
            Entity datastoreDocument = DatastoreServiceFactory.getDatastoreService().get(key);
            Document document = loadDocumentFromString(((Text)datastoreDocument.getProperty("content")).getValue());
            document.setCreateDate(new Date((Long)datastoreDocument.getProperty("createDate")));
            document.setLastModifyDate(new Date((Long)datastoreDocument.getProperty("lastModifyDate")));
            return document;
        } catch (EntityNotFoundException ex) {
            Logger.getLogger(DocumentPersistence.class.getName()).log(Level.INFO, "no Document with id {0} found, creating a new one", id);
            
            // create new Document, store immediately and hope the id is still not used ;-)
            Document document = new Document(id, new Node("no title"), new Date(), new Date());
            storeDocument(document);
            return document;
        }
    }
    
    public static Document loadDocumentFromString(String content) {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            return DocumentHandler.deserializeDocument(builder.parse(new InputSource(new StringReader(content))));
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

        Long id = document.getId();
        Entity datastoreDocument;
        
        if (id != null) {
            Key key = KeyFactory.createKey("Document", id);
            
            try {
                datastoreDocument = datastore.get(key);
                datastoreDocument.setProperty("createDate", document.getCreateDate().getTime());
            } catch (EntityNotFoundException ex) {
                datastoreDocument = new Entity("Document", id);
                datastoreDocument.setProperty("createDate", new Date().getTime());
            }
        } else {
            datastoreDocument = new Entity("Document");
            document.setId(datastoreDocument.getKey().getId());
            datastoreDocument.setProperty("createDate", new Date().getTime());
        }
        
        datastoreDocument.setProperty("content", new Text(DocumentHandler.serialize(document)));
        datastoreDocument.setProperty("lastModifyDate", new Date().getTime());
        datastore.put(datastoreDocument);
        
        return document;
    }

}
