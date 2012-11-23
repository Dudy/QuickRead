package de.podolak.quickread.data.persistence;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import de.podolak.quickread.QuickReadApplication;
import de.podolak.quickread.data.Document;
import de.podolak.quickread.data.persistence.ejb.DocumentJpaController;
import de.podolak.quickread.data.persistence.ejb.exceptions.RollbackFailureException;
import java.io.StringReader;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;

/**
 *
 * @author Dude
 */
public class DocumentPersistence {
    
    private static DocumentPersistenceType DEFAULT_PERSISTENCE_TYPE = DocumentPersistenceType.GAE;
    private static QuickReadApplication app;

    public static Document loadDocument(Long id) {
        return loadDocumentByType(id, DEFAULT_PERSISTENCE_TYPE);
    }
    
    public static Document loadDocumentByType(Long id, DocumentPersistenceType persistenceType) {
        switch (persistenceType) {
            case JPA:
                return loadDocumentByTypeJPA(id);
            case GAE:
                return loadDocumentByTypeGAE(id);
            default:
                throw new AssertionError();
        }
    }
    
    public static Document loadDocumentByTypeJPA(Long id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(app.getPersistenceUnitName());
        DocumentJpaController controller = new DocumentJpaController(emf);
        return loadDocumentFromString(controller.findDocumentEntity(id).getContent());
    }
    
    public static Document loadDocumentByTypeGAE(Long id) {
        Key key = KeyFactory.createKey("Document", id);
            
        try {
            Entity datastoreDocument = DatastoreServiceFactory.getDatastoreService().get(key);
            Document document = loadDocumentFromString(((Text)datastoreDocument.getProperty("content")).getValue());
            document.setCreateDate(new Date((Long)datastoreDocument.getProperty("createDate")));
            document.setLastModifyDate(new Date((Long)datastoreDocument.getProperty("lastModifyDate")));
            return document;
        } catch (EntityNotFoundException ex) {
            Logger.getLogger(DocumentPersistence.class.getName()).log(Level.SEVERE, null, ex);
            
            Entity dd = new Entity("Document", id);
            dd.setProperty("content", new Text(DocumentJpaController.document.getContent()));
            dd.setProperty("createDate", new Date().getTime());
            dd.setProperty("lastModifyDate", new Date().getTime());
            DatastoreServiceFactory.getDatastoreService().put(dd);
            return loadDocumentFromString(DocumentJpaController.document.getContent());
        }
    }
    
    public static Document loadDocumentFromString(String content) {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            DocumentScanner scanner = new DocumentScanner(builder.parse(new InputSource(new StringReader(content))));
            scanner.visitDocument();
            return scanner.getDocument();
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
        return storeDocumentByType(document, DEFAULT_PERSISTENCE_TYPE);
    }
    
    public static Document storeDocumentByType(Document document, DocumentPersistenceType persistenceType) {
        switch (persistenceType) {
            case JPA:
                return storeDocumentByTypeJPA(document);
            case GAE:
                return storeDocumentByTypeGAE(document);
            default:
                throw new AssertionError();
        }
    }
    
    public static Document storeDocumentByTypeJPA(Document document) {
        de.podolak.quickread.data.persistence.ejb.Document documentToStore = SimpleDocumentWriter.write(document);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(app.getPersistenceUnitName());
        DocumentJpaController controller = new DocumentJpaController(emf);
        
        try {
            if (documentToStore.getId() == null) {
                controller.create(documentToStore);
                document.setId(documentToStore.getId());
            } else {
                controller.edit(documentToStore);
            }
            return document;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(DocumentPersistence.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DocumentPersistence.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    private static Document storeDocumentByTypeGAE(Document document) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Long id = document.getId();
        Entity datastoreDocument = null;
        
        if (id != null) {
            Key key = KeyFactory.createKey("Document", id);
            
            try {
                datastoreDocument = datastore.get(key);
                datastoreDocument.setProperty("createDate", document.getCreateDate().getTime());
            } catch (EntityNotFoundException ex) {
                Logger.getLogger(DocumentPersistence.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            datastoreDocument = new Entity("Document");
            document.setId(datastoreDocument.getKey().getId());
            datastoreDocument.setProperty("createDate", new Date().getTime());
        }
        
        if (datastoreDocument != null) {
            datastoreDocument.setProperty("content", new Text(SimpleDocumentWriter.write(document).getContent()));
            datastoreDocument.setProperty("lastModifyDate", new Date().getTime());
            datastore.put(datastoreDocument);
        }
        
        return document;
    }

    public static void registerApp(QuickReadApplication app) {
        DocumentPersistence.app = app;
    }
    
}
