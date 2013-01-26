package de.podolak.quickread.data.datastore;

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
public class Node implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String key;
    private String value;
    private List<Node> children;

    public Node() {
        this("", "", null);
    }
    
    public Node(String key) {
        this(key, "", null);
    }

    public Node(String key, String value) {
        this(key, value, null);
    }
    
    public Node(String key, String value, List<Node> children) {
        this.key = key;
        this.value = value;
        this.children = children;
        
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        
        if (value == null) {
            value = "";
        }
        
        if (this.children == null) {
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

    public List<Node> getChildren() {
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
    
//    public List<String> getValueListByKey(String key) {
//        if (key == null || key.isEmpty()) {
//            return null;
//        }
//        
//        ArrayList<String> valueList = new ArrayList<String>();
//        
//        if (key.equals(this.key)) {
//            valueList.add(value);
//        }
//        
//        if (children.size() > 0) {
//            for (Node child : children) {
//                valueList.addAll(child.getValueListByKey(key));
//            }
//        }
//        
//        return valueList;
//    }
    
    /**
     * Returns the first value of the given key or null if no such key exists.
     * 
     * @param key
     * @return 
     */
    public String getFirstValueByKey(String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }
        
        List<String> valueList = getValueListByKey(key);
        
        if (valueList != null && !valueList.isEmpty()) {
            return valueList.get(0);
        }
        
        return null;
    }
    
    /**
     * Returns the first value of the given key path or null if no such key path exists.
     * 
     * @param keyPath
     * @return 
     */
    public String getFirstValueByKeyPath(String keyPath) {
        if (keyPath == null || keyPath.isEmpty()) {
            return null;
        }
        
        List<String> valueList = getValueListByKeyPath(keyPath);
        
        if (valueList != null && !valueList.isEmpty()) {
            return valueList.get(0);
        }
        
        return null;
    }
    
    /**
     * Returns the list of values that are stored for the given key.
     * This works recursively, checks this node, all children, their
     * children and so on.
     * The list may be empty if the key is <code>null</code>, empty
     * or doesn't exist. This will never return <code>null</code>.
     * 
     * @param key key to look up values for
     * @return list of values for key, may be empty but never <code>null</code>
     */
    public List<String> getValueListByKey(String key) {
        ArrayList<String> valueList = new ArrayList<String>();
        
        if (key == null || key.isEmpty()) {
            return valueList;
        }
        
        if (key.equals(this.key)) {
            valueList.add(value);
        }
        
        for (Node child : children) {
            valueList.addAll(child.getValueListByKey(key));
        }
        
        return valueList;
    }
    
//    public List<String> getValueListByKeyPath(String keyPath) {
//        if (keyPath == null || keyPath.isEmpty()) {
//            return null;
//        }
//        
//        ArrayList<String> valueList = new ArrayList<String>();
//        String[] parts = keyPath.split("\\.", 2);
//        
//        if (parts[0].equals(key)) {
//            if (parts.length == 1) {
//                valueList.add(value);
//            } else {
//                if (children.size() > 0) {
//                    for (Node child : children) {
//                        valueList.addAll(child.getValueListByKeyPath(parts[1]));
//                    }
//                }
//            }
//        }
//        
//        return valueList;
//    }
    /**
     * Returns the list of values that are stored for the given key path.
     * The name of this Node itself is not part of the key path. the key
     * path must start with the key name of a child node or be a single dot
     * denoting this very Node.
     * The list may be empty if the key path is <code>null</code>, empty
     * or doesn't exist. This will never return <code>null</code>.
     * some examples, given the data structure:
     * - root ("")
     *   - data ("")
     *     - work ("IBM")
     *     - home ("San Francisco")
     *       - son ("Aaron")
     *       - daughter ("Beverly")
     *       - son ("Charley")
     *     - garden ("at the bay")
     * 
     * these calls return as follows:
     * - root.getValueListByKeyPath("root.data.work") == () ("root" should not be in the path, empty list)
     * - root.getValueListByKeyPath("data.work") == ("IBM") (correct adress of "data.work" child node)
     * - root.getValueListByKeyPath("data.home.son") == ("Aaron", "Charley")
     * - data.getValueListByKeyPath("work") == ("San Francisco") (correct adress of "work" child node)
     * - work.getValueListByKeyPath(".") == ("San Francisco") (correct dot notation of this node)
     * - home.getValueListByKeyPath("home.daughter") == ("home" should not be in the path)
     * 
     * @param keyPath key path to look up values for
     * @return list of values at key path, may be empty but never <code>null</code>
     */
    public List<String> getValueListByKeyPath(String keyPath) {
        ArrayList<String> valueList = new ArrayList<String>();
        
        if (keyPath == null || keyPath.isEmpty()) {
            return valueList;
        }
        
        if (".".equals(keyPath)) {
            valueList.add(value);
            return valueList;
        }
        
        String[] parts = keyPath.split("\\.", 2);
        for (Node child : children) {
            if (parts[0].equals(child.getKey())) {
                if (parts.length == 1) {
                    valueList.add(child.getValue());
                } else {
                    valueList.addAll(child.getValueListByKeyPath(parts[1]));
                }
            }
        }
        
        return valueList;
    }
    
    /**
     * Returns the list of <code>Node</code>s that is referenced by the key path.
     * The key path has to be fully qualified relatively to this node, whereas
     * the key of this <code>Node</code> must not be included.
     * This will never return null. In case there is no child Node for that key
     * path, an empty list is returned.
     * Example:
     * - this Nodes key is "parent", its value is "parentValue"
     * - this Node has a child with the key "child" and the value "childValue1"
     * - this Node has a second child with the key "child" and the value "childValue2"
     * - this Node has a third child with the key "otherChild" and the value "childValue3"
     * 
     * - parent ("parentValue")
     *   - child ("childValue1")
     *   - child ("childValue2")
     *   - otherChild ("childValue3")
     * 
     * parent.getChildNodeListByKeyPath("parent") returns an empty list (don't use this nodes' key)
     * parent.getChildNodeListByKeyPath("child") returns the first two children
     * 
     * @param keyPath fully qualified key path
     * @return list of Nodes referenced by key path, may be empty but never null
     */
    public List<Node> getChildNodeListByKeyPath(String keyPath) {
        ArrayList<Node> nodeList = new ArrayList<Node>();
        
        if (keyPath == null || keyPath.isEmpty()) {
            return nodeList;
        }
        
        String[] parts = keyPath.split("\\.", 2);
        
        for (Node child : children) {
            if (parts[0].equals(child.getKey())) {
                if (parts.length == 1) {
                    nodeList.add(child);
                } else {
                    nodeList.addAll(child.getChildNodeListByKeyPath(parts[1]));
                }
            }
        }
        
        return nodeList;
    }

    /**
     * Searches for the node defined by the key path and sets its value.
     * If there is no such node, it is created. If there is more than one
     * node of this key path, the value is not set and false is returned.
     * If the key path is null or empty, false is returned.
     * If any value could be set, true is returned.
     * 
     * @param keyPath key path of node to set the value for
     * @param value new value
     * @return true, if the value of a node has been set, false otherwise
     */
    public boolean setValueByKeyPath(String keyPath, Object value) {
        boolean valueSet;
        List<Node> nodeList = getChildNodeListByKeyPath(keyPath);
        
        if (keyPath == null || keyPath.isEmpty()) {
            return false;
        }
        
        if (nodeList == null || nodeList.isEmpty()) {
            createNodeByKeyPath(keyPath).setValue(value.toString());
            valueSet = true;
        } else if (nodeList.size() > 1) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, "found multiple nodes for key path '{0}'", keyPath);
            valueSet = false;
        } else {
            nodeList.get(0).setValue(value.toString());
            valueSet = true;
        }
        
        return valueSet;
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
                newNode.setValue((String)value);
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
