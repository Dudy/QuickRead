package de.podolak.quickread.data.datastore;

import static de.podolak.quickread.data.datastore.Document.Metadata.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dude
 */
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public static final String ROOT_KEY_PREFIX     = "root";
    public static final String DATA_KEY_PREFIX     = "data";
    public static final String METADATA_KEY_PREFIX = "";
    
    private static final List<String> attributeStringList;
    
    private Node root;
    
    static {
        attributeStringList = new ArrayList<String>();
        attributeStringList.add("documentType");
    }

    public Document() {
        this(DocumentType.COMMON);
    }

    public Document(Node root) {
        this.root = root;
    }

    public Document(Document other) {
        this(other.getId(), other.getSerializationVersion(), other.getRoot(), other.getCreateDate(), other.getLastModifyDate(),
                other.getDocumentType());
    }

    public Document(DocumentType documentType) {
        this(null, DocumentPersistence.getDefaultVersion(), new Node(ROOT_KEY_PREFIX), new Date(), new Date(), documentType);
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
        // setRoot must be the first as all data is stored therein
        setRoot(root);
        setId(id);
        setSerializationVersion(serializationVersion);
        setCreateDate(createDate);
        setLastModifyDate(lastModifyDate);
        setDocumentType(documentType);
    }
    
    public Node getRoot() {
        if (root == null) {
            root = new Node(ROOT_KEY_PREFIX);
        }

        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
    
    public void setRootPreserveMetadata(Node root) {
        String[] metadataValues = new String[Metadata.values().length];
        
        for (int i = 0; i < Metadata.values().length; i++) {
            metadataValues[i] = getMetadata(Metadata.values()[i]);
        }
        
        this.root = root;
        
        for (int i = 0; i < Metadata.values().length; i++) {
            setMetadata(Metadata.values()[i], metadataValues[i]);
        }
    }
    
    public Long getId() {
        return getMetadata(ID);
    }

    public void setId(Long id) {
        setMetadata(ID, id);
    }

    public Integer getSerializationVersion() {
        return getMetadata(SERIALIZATION_VERSION);
    }

    public void setSerializationVersion(Integer serializationVersion) {
        setMetadata(SERIALIZATION_VERSION, serializationVersion);
    }

    public Date getCreateDate() {
        return getMetadata(CREATE_DATE);
    }

    public void setCreateDate(Date createDate) {
        setMetadata(CREATE_DATE, createDate);
    }

    public Date getLastModifyDate() {
        return getMetadata(LAST_MODIFY_DATE);
    }

    public void setLastModifyDate(Date lastModifyDate) {
        setMetadata(LAST_MODIFY_DATE, lastModifyDate);
    }

    public DocumentType getDocumentType() {
        DocumentType documentType = getMetadata(DOCUMENT_TYPE);
        
        if (documentType == null) {
            documentType = DocumentType.COMMON;
        }
        
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        setMetadata(DOCUMENT_TYPE, documentType);
    }

    /**
     * Returns a list of all values defined by the given key. There are no
     * contrains on keys, you may use one several times and therefore there
     * might be a list of values.
     *
     * @param key key to search the value for
     * @return a list of values of the given key
     */
    public List<String> getValueListByKey(String key) {
        return getRoot().getValueListByKey(key);
    }

    /**
     * Returns the first value defined by the given key. There are no contrains
     * on keys, you may use one several times and therefore there might be a
     * list of values. This method returns the first one while preorder-
     * traversing the data tree. This may return
     * <code>null</code> if there is no such key.
     *
     * @param key key to search the value for
     * @return first value of the given key or null
     */
    public String getFirstValueByKey(String key) {
        List<String> valueList = getValueListByKey(key);

        if (valueList == null || valueList.isEmpty()) {
            return null;
        } else {
            return valueList.get(0);
        }
    }

    public List<String> getValueListByKeyPath(String keyPath) {
        return getRoot().getValueListByKeyPath(keyPath);
    }

    public String getFirstValueByKeyPath(String keyPath) {
        List<String> valueList = getValueListByKeyPath(keyPath);

        if (valueList == null || valueList.isEmpty()) {
            return null;
        } else {
            return valueList.get(0);
        }
    }
    
    public Node getDataNode() {
        Node node = getRoot().getFirstChildByKey(DATA_KEY_PREFIX);

        if (node == null) {
            node = new Node(DATA_KEY_PREFIX);
            getRoot().addChild(node);
        }
        
        return node;
    }
    
    public String getFirstData(String keyPath) {
        String value = null;
        Node dataNode = getDataNode();
        List<Node> nodeListByKeyPath = dataNode.getChildNodeListByKeyPath(keyPath);
        
        if (nodeListByKeyPath != null && !nodeListByKeyPath.isEmpty()) {
            value = nodeListByKeyPath.get(0).getValue();
        }
        
        return value;
    }
    
    /**
     * Stores the value at the key path. The data is by convention always stored
     * at the node "root.data". The prefix "root.data" should not be part of the
     * key path parameter. A new node will always be created no matter if it
     * already exists.
     *
     * @param keyPath
     * @param value
     */
    public void addData(String keyPath, Object value) {
        getDataNode().createNodeByKeyPathWithValue(keyPath, value);
    }

    /**
     * Stores the value at the key path. The data is by convention always stored
     * at the node "root.data". The prefix "root.data" should not be part of the
     * key path parameter. If there is already a single node at the specified
     * location it's value is changed. If there is no such node or there are
     * several nodes at that key path a new one will be created.
     *
     * @param keyPath
     * @param value
     */
    public void setData(String keyPath, Object value) {
        Node dataNode = getDataNode();
        List<Node> nodeList = dataNode.getChildNodeListByKeyPath(keyPath);

        if (nodeList == null || nodeList.size() != 1) {
            dataNode = dataNode.createNodeByKeyPath(keyPath);
        } else {
            dataNode = nodeList.get(0);
        }

        dataNode.setValue(value.toString());
    }

    public <T> T getMetadata(Metadata metadata) {
        return metadata.getFromDocument(this);
    }

    public void setMetadata(Metadata metadata, Object value) {
        metadata.setInDocument(this, value);
    }

    /**
     * Returns a text representation of this Document. By convention this is the
     * value of the root node.
     *
     * @return text representation of this Document
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        for (String attr : attributeStringList) {
            builder.append(getFirstData(attr));
        }
        
        return builder.toString();
        
        //return getRoot().getValue();
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
        if (this.root != other.root && (this.root == null || !this.root.equals(other.root))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (this.root != null ? this.root.hashCode() : 0);
        return hash;
    }
    
    public String toJson() {
        StringBuilder jsonString = new StringBuilder();

        jsonString
                .append("\"Document\": {")
                .append(root.toJson())
                .append("}");

        return jsonString.toString();
    }
    
    public String getCaption() {
        return "document with ID " + getId();
    }
    
    public List<String> getAttributeStringList() {
        return new ArrayList<String>(Arrays.asList(new String[] { "documentType" }));
    }
    
    public enum Metadata {

        ID("id", Long.class),
        SERIALIZATION_VERSION("serializationVersion", Integer.class),
        CREATE_DATE("createDate", Date.class),
        LAST_MODIFY_DATE("lastModifyDate", Date.class),
        DOCUMENT_TYPE("documentType", DocumentType.class);
        
        private String identifier;
        private Class type;

        private Metadata(String identifier, Class type) {
            this.identifier = identifier;
            this.type = type;
        }

        public String getIdentifier() {
            return identifier;
        }

        /**
         * Returns the object representing this metadata enum from the given
         * document. This returns an object of the correct type, i.e. dates are
         * of type java.util.type, version numbers are of type java.lang.Integer
         * and so on. Returns
         * <code>null</code> if the document does not contain such an
         * information.
         *
         * @param document the <code>Document</code> to extract the metadata
         * from
         * @return
         * @see Document
         */
        public <T> T getFromDocument(Document document) {
            Object object = null;
            String objectString = document.getFirstValueByKeyPath(METADATA_KEY_PREFIX + identifier);

            if (objectString != null) {
                try {
                    if (type == String.class) {
                        object = objectString;
                    } else if (type == Integer.class) {
                        object = Integer.parseInt(objectString);
                    } else if (type == Long.class) {
                        object = Long.parseLong(objectString);
                    } else if (type == Boolean.class) {
                        object = Boolean.parseBoolean(objectString);
                    } else if (type == Date.class) {
                        object = new Date(Long.parseLong(objectString));
                    } else if (type == DocumentType.class) {
                        object = DocumentType.valueOf(objectString);
                    } else {
                        Logger.getLogger(Metadata.class.getName()).log(Level.INFO, "{0} with unknown class '{1}' of value '{2}' omitted",
                                new Object[]{identifier, type, objectString});
                    }
                } catch (NumberFormatException e) {
                    Logger.getLogger(Metadata.class.getName()).log(Level.INFO, "{0} with non {1} value '{2}' omitted",
                            new Object[]{identifier, type, objectString});
                } catch (IllegalArgumentException e) {
                    Logger.getLogger(Metadata.class.getName()).log(Level.INFO, "{0} with non {1} value '{2}' omitted",
                            new Object[]{identifier, type, objectString});
                }
            } else {
                Logger.getLogger(Metadata.class.getName()).log(Level.INFO, "{0} with null value omitted", identifier);
            }

            return (T) object;
        }

        public void setInDocument(Document document, Object value) {
            Node metadataNode = document.getRoot().getFirstChildByKey(identifier);

            if (metadataNode == null) {
                metadataNode = new Node(identifier);
                document.getRoot().addChild(metadataNode);
            }
            
            if (value == null) {
                metadataNode.setValue(null);
            } else {
                if (type == String.class) {
                    metadataNode.setValue(value.toString());
                } else if (type == Integer.class) {
                    metadataNode.setValue(value.toString());
                } else if (type == Long.class) {
                    metadataNode.setValue(value.toString());
                } else if (type == Boolean.class) {
                    metadataNode.setValue(value.toString());
                } else if (type == Date.class) {
                    metadataNode.setValue(Long.toString(((Date)value).getTime()));
                } else if (type == DocumentType.class) {
                    metadataNode.setValue(((DocumentType)value).name());
                } else {
                    Logger.getLogger(Metadata.class.getName()).log(Level.INFO, "{0} with unknown class '{1}' of value '{2}' omitted",
                            new Object[]{identifier, type, value});
                }
            }
        }
    }
}
