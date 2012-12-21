package de.podolak.quickread.data.datastore;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Dude
 */
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Integer serializationVersion;
    private Node root;
    private Date createDate;
    private Date lastModifyDate;
    private DocumentType documentType;
    
    // dummy attribute to mimic Java Bean behaviour, see getTextRepresentation()
    private transient String textRepresentation;
    
    // TODO: add more metadata, author, url, format, source, ...

    public Document() {
    }
    
    public Document(Document other) {
        this(other.getId(), other.getSerializationVersion(), other.getRoot(), other.getCreateDate(), other.getLastModifyDate(),
                other.getDocumentType());
    }
    
    public Document(DocumentType documentType) {
        this(null, DocumentPersistence.getDefaultVersion(), getDefaultRootNode(documentType), new Date(), new Date(), documentType);
    }
    
    public Document(Node root, Date createDate, Date lastModifyDate, DocumentType documentType) {
        this(null, DocumentPersistence.getDefaultVersion(), root, createDate, lastModifyDate, documentType);
    }
    
    public Document(Integer serializationVersion, Node root, Date createDate, Date lastModifyDate, DocumentType documentType) {
        this(null, serializationVersion, root, createDate, lastModifyDate, documentType);
    }

    public Document(Long id, Integer serializationVersion, Node root, Date createDate, Date lastModifyDate, DocumentType documentType) {
        init(id, serializationVersion, root, createDate, lastModifyDate, documentType);
    }
    
    public final void init(Document document) {
        init(document.getId(), document.getSerializationVersion(), document.getRoot(), document.getCreateDate(),
                document.getLastModifyDate(), document.getDocumentType());
    }
    
    public final void init(Long id, Integer serializationVersion, Node root, Date createDate, Date lastModifyDate,
            DocumentType documentType) {
        this.id = id;
        this.serializationVersion = serializationVersion;
        this.root = root;
        this.createDate = createDate;
        this.lastModifyDate = lastModifyDate;
        this.documentType = documentType;
    }

    public Integer getSerializationVersion() {
        return serializationVersion;
    }

    public void setSerializationVersion(Integer serializationVersion) {
        this.serializationVersion = serializationVersion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }
    
    /**
     * Returns a list of all values defined by the given key.
     * There are no contrains on keys, you may use one several
     * times and therefore there might be a list of values.
     * 
     * @param key key to search the value for
     * @return a list of values of the given key
     */
    public List<String> getValueListByKey(String key) {
        return root.getValueListByKey(key);
    }
    
    /**
     * Returns the first value defined by the given key.
     * There are no contrains on keys, you may use one several
     * times and therefore there might be a list of values.
     * This method returns the first one while preorder-
     * traversing the data tree.
     * This may return <code>null</code> if there is no
     * such key.
     * 
     * @param key key to search the value for
     * @return first value of the given key or null
     */
    public String getFirstValueByKey(String key) {
        List<String> valueList = getValueListByKey(key);
        
        if (valueList == null) {
            return null;
        } else {
            return valueList.get(0);
        }
    }
    
    public List<String> getValueListByKeyPath(String keyPath) {
        return root.getValueListByKeyPath(keyPath);
    }

    /**
     * This is a dummy operation to mimic Java Bean behaviour for an artificial attribute "textRepresentation".
     * It returns a textual representation of this object.
     * TODO: think about using toString()
     * 
     * @return 
     */
    public String getTextRepresentation() {
        refreshTextRepresentation();
        return textRepresentation;
    }

    public void setTextRepresentation(String textRepresentation) {
        // noop, dummy operation to mimic Java Bean behaviour, see getTextRepresentation()
        refreshTextRepresentation();
    }
    
    private void refreshTextRepresentation() {
        List<String> nameList = getValueListByKey("name");
        
        if (nameList == null || nameList.isEmpty()) {
            textRepresentation = "";
        } else {
            textRepresentation = nameList.get(0);
        }
    }

    @Override
    public String toString() {
        switch (documentType) {
            case BOOK:
                return createDate.getTime() + ";" + lastModifyDate.getTime() + ";" + root.toString();
            case PROJECT:
                return getTextRepresentation();
            case SONG:
                return "toString() for SONG not yet implemented";
            default:
                return "unknown documwent type in toString() method";
        }
    }
    
//    @Override
//    public String toString() {
//        return root.getValue();
//    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Document other = (Document) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    public static Node getDefaultRootNode(DocumentType type) {
        Random random = new Random();
        
        switch (type) {
            case BOOK:
                return new Node("unbenanntes Buch - " + random.nextInt());
            case SONG:
                return new Node("unbenanntes Musikstück - " + random.nextInt());
            case PROJECT:
                return new Node("name", "new project");
            default:
                return new Node("unknown");
        }
    }
    
}
