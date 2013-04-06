package de.podolak.quickread.data;

import de.podolak.quickread.Utilities;
import de.podolak.quickread.data.datastore.Document;
import de.podolak.quickread.data.datastore.DocumentPersistence;
import de.podolak.quickread.data.datastore.DocumentType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dude
 */
public class Project extends Document {

    public static final String KEY_PROJECT_NAME = "name";
    public static final String KEY_DOCUMENT_ID  = "documentID";
    
    private List<Long> documentIdList;
    private List<Document> documentList;

    public Project() {
        setDocumentType(DocumentType.PROJECT);
    }
    
    public String getName() {
        String name = getFirstData(KEY_PROJECT_NAME);
        
        if (name == null) {
            name = Utilities.getI18NText("data.newProject.name");
        }
        
        return name;
    }
    
    public void setName(String name) {
        setData(KEY_PROJECT_NAME, name);
    }
    
    public List<Long> getDocumentIdList() {
        // lazily extract the list of document IDs from node structure
        // do not yet load documents, this is done in getDocumentList()
        if (documentIdList == null) {
            documentIdList = new ArrayList<Long>();
            List<String> documentIdStringList = getValueListByKey(KEY_DOCUMENT_ID);
            for (String documentIdString : documentIdStringList) {
                try {
                    documentIdList.add(Long.parseLong(documentIdString));
                } catch (NumberFormatException e) {
                    // There should not be a key "documentID" with a non integer
                    // content. If there is one, silenty omit it.
                    Logger.getLogger(Project.class.getName()).log(Level.INFO, "{0} with non integer value '{1}' omitted",
                            new Object[] { KEY_DOCUMENT_ID, documentIdString });
                }
            }
        }
        
        return documentIdList;
    }

    public List<Document> getDocumentList() {
        // lazily fetch the list of documents from datastore
        if (documentList == null) {
            documentList = new ArrayList<Document>();
            for (Long documentId : getDocumentIdList()) {
                documentList.add(DocumentPersistence.loadDocument(documentId));
            }
        }
        
        return documentList;
    }

    public void addDocument(Document document) {
        getDocumentList().add(document);
        getDocumentIdList().add(document.getId());
    }
    
    public void removeDocument(Document document) {
        getDocumentList().remove(document);
        getDocumentIdList().remove(document.getId());
    }

    @Override
    public String getCaption() {
        return getName();
    }
    
    @Override
    public List<String> getAttributeStringList() {
        List<String> attributeStringList = super.getAttributeStringList();
        
        attributeStringList.add("name");
        
        return attributeStringList;
    }
}
