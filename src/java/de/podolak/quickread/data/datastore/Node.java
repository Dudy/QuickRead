package de.podolak.quickread.data.datastore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    
    public List<String> getValueListByKey(String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }
        
        ArrayList<String> valueList = new ArrayList<String>();
        
        if (key.equals(this.key)) {
            valueList.add(value);
        }
        
        if (children != null && children.size() > 0) {
            for (Node child : children) {
                valueList.addAll(child.getValueListByKey(key));
            }
        }
        
        return valueList;
    }
    
    public List<String> getValueListByKeyPath(String keyPath) {
        if (keyPath == null || keyPath.isEmpty()) {
            return null;
        }
        
        ArrayList<String> valueList = new ArrayList<String>();
        String[] parts = keyPath.split("\\.", 2);
        
        if (parts[0].equals(key)) {
            if (parts.length == 1) {
                valueList.add(value);
            } else {
                if (children != null && children.size() > 0) {
                    for (Node child : children) {
                        valueList.addAll(child.getValueListByKeyPath(parts[1]));
                    }
                }
            }
        }
        
        return valueList;
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
