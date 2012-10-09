package de.podolak.quickread.data;

import com.vaadin.data.util.BeanItemContainer;
import de.podolak.quickread.data.persistence.DocumentPersistence;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dude
 */
public class DocumentContainer extends BeanItemContainer<Document> implements Serializable {
    
    public static final Object[] NATURAL_COL_ORDER = new Object[] { "title", "text" };

    private static DocumentContainer instance;
    
    public DocumentContainer() throws InstantiationException, IllegalAccessException {
        super(Document.class);
        
        // just for now ...
        addItem(DocumentPersistence.loadDocument(1));
    }
    
    public static DocumentContainer getInstance() {
        if (instance == null) {
            try {
                instance = new DocumentContainer();
            } catch (InstantiationException ex) {
                Logger.getLogger(DocumentContainer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(DocumentContainer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return instance;
    }
    
}
