package de.podolak.quickread.data.datastore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dude
 */
public class Node implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String key;
    private String value;
//    private Nodes children;
    private ArrayList<Node> children;

    public Node() {
        this("", "", null);
    }
    
    public Node(String key) {
        this(key, "", null);
    }

    public Node(String key, String value) {
        this(key, value, null);
    }
    
//    public Node(String key, String value, Nodes children) {
    public Node(String key, String value, ArrayList<Node> children) {
        this.key = key;
        this.value = value;
        this.children = children;
        
        if (this.children == null) {
            //this.children = new Nodes();
            this.children = new ArrayList<Node>();
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

    //public Nodes getChildren() {
    public ArrayList<Node> getChildren() {
        return children;
    }
    
    public boolean addChild(Node child) {
        return this.children.add(child);
    }
    
    public boolean removeChild(Node child) {
        return this.children.remove(child);
    }
    
    public Node getFirstChildByKey(String key) {
        Node child = null;
        
        if (key != null && !key.isEmpty()) {
            for (Node node : children) {
                if (key.equals(node.getKey())) {
                    child = node;
                    break;
                }
            }
        }
        
        return child;
    }
    
    public int numberOfChildren() {
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
        
        if (children.size() > 0) {
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
                if (children.size() > 0) {
                    for (Node child : children) {
                        valueList.addAll(child.getValueListByKeyPath(parts[1]));
                    }
                }
            }
        }
        
        return valueList;
    }
    
    public List<Node> getNodeListByKeyPath(String keyPath) {
        if (keyPath == null || keyPath.isEmpty()) {
            return null;
        }
        
        ArrayList<Node> nodeList = new ArrayList<Node>();
        String[] parts = keyPath.split("\\.", 2);
        
        if (parts[0].equals(key)) {
            if (parts.length == 1) {
                nodeList.add(this);
            } else {
                if (children.size() > 0) {
                    for (Node child : children) {
                        nodeList.addAll(child.getNodeListByKeyPath(parts[1]));
                    }
                }
            }
        }
        
        return nodeList;
    }
    
    public void setValueByKeyPath(String keyPath, Object value) {
        if (keyPath == null || keyPath.isEmpty()) {
            return;
        }
        
        List<Node> nodeList = getNodeListByKeyPath(keyPath);
        
        if (nodeList == null && nodeList.isEmpty()) {
            createNodeByKeyPath(keyPath).setValue(value.toString());
        } else if (nodeList.size() > 1) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, "found multiple nodes for key path '{0}'", keyPath);
        } else {
            nodeList.get(0).setValue(value.toString());
        }
    }
    
    /**
     * Returns a new node with the given key path.
     * A new node is always created even when such a node already exists.
     * If any parents in the key path are missing they are also created.
     * 
     * @param keyPath
     * @return 
     */
    public Node createNodeByKeyPath(String keyPath) {
        if (keyPath == null || keyPath.isEmpty()) {
            return null;
        }

        Node newNode;
        String[] parts = keyPath.split("\\.", 2);
        Node nextChild = null;

        if (parts.length == 1){
            newNode = new Node(parts[0]);
            addChild(newNode);
        } else {
            for (Node child : children) {
                if (parts[0].equals(child.getKey())) {
                    nextChild = child;
                    break;
                }
            }

            if (nextChild == null) {
                nextChild = new Node(parts[0]);
                addChild(nextChild);
            }

            newNode = nextChild.createNodeByKeyPath(parts[1]);
        }
        
        return newNode;
    }
    
    /**
     * Returns a new node with the given key path and sets it's value.
     * A new node is always created even when such a node already exists.
     * If any parents in the key path are missing they are also created.
     * 
     * @param keyPath
     * @return 
     */
    public Node createNodeByKeyPathWithValue(String keyPath, Object value) {
        Node newNode = createNodeByKeyPath(keyPath);
        
        if (newNode != null && value != null) {
            if (value instanceof String) {
                newNode.setValue(value.toString());
            } else if (value instanceof Integer) {
                newNode.setValue(value.toString());
            } else if (value instanceof Long) {
                newNode.setValue(value.toString());
            } else if (value instanceof Boolean) {
                newNode.setValue(value.toString());
            } else if (value instanceof Date) {
                newNode.setValue(Long.toString(((Date)value).getTime()));
            } else if (value instanceof DocumentType) {
                newNode.setValue(((DocumentType)value).name());
            } else {
                Logger.getLogger(Node.class.getName()).log(Level.INFO, "value '{0}' with unknown class '{1}' omitted",
                        new Object[]{value, value.getClass()});
            }
        }
        
        return newNode;
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

    public String toJson() {
        StringBuilder childJsonString = new StringBuilder();
        if (!children.isEmpty()) {
            for (Node child : children) {
                childJsonString
                        .append("{")
                        .append(child.toJson())
                        .append("}")
                        .append(",");
            }
            childJsonString.deleteCharAt(childJsonString.length() - 1);
        }
        
        return new StringBuilder()
                .append("\"Node\": {")
                .append("\"key\": \"").append(key).append("\",")
                .append("\"value\": \"").append(value).append("\",")
                .append("\"children\": [").append(childJsonString).append("]")
                .append("}")
                .toString();
    }

}
