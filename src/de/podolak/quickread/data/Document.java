package de.podolak.quickread.data;

import java.io.Serializable;

/**
 *
 * @author Dude
 */
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private Node root;
    
    // TODO: add more metadata, author, url, format, source, ...

    public Document() {
    }

    public Document(Integer id, Node root) {
        this.id = id;
        this.root = root;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    @Override
    public String toString() {
        return root.toString();
    }

}
