package de.podolak.quickread.data;

import java.io.Serializable;
import java.util.Date;

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
    
    // TODO: add more metadata, author, url, format, source, ...

    public Document() {
    }

    public Document(Long id, Integer serializationVersion, Node root, Date createDate, Date lastModifyDate, DocumentType documentType) {
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

    @Override
    public String toString() {
        return createDate.getTime() + ";" + lastModifyDate.getTime() + ";" + root.toString();
    }
    
}
