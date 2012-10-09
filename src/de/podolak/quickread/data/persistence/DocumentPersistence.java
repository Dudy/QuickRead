package de.podolak.quickread.data.persistence;

import de.podolak.quickread.data.Document;
import de.podolak.quickread.data.persistence.ejb.DocumentJpaController;
import de.podolak.quickread.data.persistence.ejb.exceptions.RollbackFailureException;
import java.io.StringReader;
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
    
    private static DocumentPersistenceType persistenceType = DocumentPersistenceType.JPA;

    public static de.podolak.quickread.data.Document loadDocument(int id) {
        return loadDocumentByType(id, persistenceType);
    }
    
    public static Document loadDocumentByType(int id, DocumentPersistenceType persistenceType) {
        switch (persistenceType) {
            case JPA:
                return loadDocumentByTypeJPA(id);
            case GAE:
                return loadDocumentByTypeGAE(id);
            default:
                throw new AssertionError();
        }
    }
    
    public static Document loadDocumentByTypeJPA(int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("QuickRead_PU");
        DocumentJpaController controller = new DocumentJpaController(emf);
        return loadDocumentFromString(controller.findDocumentEntity(id).getContent());
    }
    
    public static Document loadDocumentByTypeGAE(int id) {
        //TODO: load from Google App Engine db.Model
        return null;
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
        de.podolak.quickread.data.persistence.ejb.Document documentToStore = SimpleDocumentWriter.write(document);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("QuickRead_PU");
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
    
}
