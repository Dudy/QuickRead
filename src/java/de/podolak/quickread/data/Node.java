package de.podolak.quickread.data;

import java.io.Serializable;

/**
 *
 * @author Dude
 */
public class Node implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String key;
    private String value;
    private Nodes children;

    public Node() {
        this("", "", null);
    }
    
    public Node(String key) {
        this(key, "", null);
    }

    public Node(String key, String value) {
        this(key, value, null);
    }
    
    public Node(String key, String value, Nodes children) {
        this.key = key;
        this.value = value;
        this.children = children;
        
        if (this.children == null) {
            this.children = new Nodes();
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Nodes getChildren() {
        return children;
    }

    public void setChildren(Nodes children) {
        this.children = children;
    }
    
    public boolean addChild(Node child) {
        return this.children.add(child);
    }
    
    public boolean removeChild(Node child) {
        return this.children.remove(child);
    }
    
    public int getNumberOfChildren() {
        return this.children.size();
    }

    @Override
    public String toString() {
        return
                "node=[" +
                "key=" + key + "," +
                "value=" + value + "," +
                children +
                "]";
    }

}
