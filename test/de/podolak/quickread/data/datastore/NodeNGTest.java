package de.podolak.quickread.data.datastore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

// TODO mache die Tests auf Mengen so, daß die Mengen auch in der richtigen Reihenfolge da sein müssen

/**
 *
 * @author Dude
 */
public class NodeNGTest {
    
    // test data, use this read only !!!!!
    // when you need a mutable data test set, create a new one locally within the test method
    private static final String KEY_THIS             = ".";
    private static final String KEY_EMPTY_KEY        = "";
    private static final String VALUE_EMPTY_KEY      = "";
    private static final String KEY_KEY_ONLY         = "key only";
    private static final String VALUE_KEY_ONLY       = "";
    private static final String KEY_PRENAME          = "prename";
    private static final String KEY_SURNAME          = "surname";
    private static final String VALUE_PRENAME_1      = "first prename value";
    private static final String VALUE_PRENAME_2      = "second prename value";
    private static final String VALUE_SURNAME        = "surname value";
    private static final String KEY_INTERMEDIATE_1   = "intermediate 1";
    private static final String KEY_INTERMEDIATE_2   = "intermediate 2";
    private static final String KEY_INTERMEDIATE_3   = "intermediate 3";
    private static final String VALUE_INTERMEDIATE_1 = "intermediate 1 value";
    private static final String VALUE_INTERMEDIATE_2 = "intermediate 2 value";
    private static final String VALUE_INTERMEDIATE_3 = "intermediate 3 value";
    private static final String KEY_DATA             = "data";
    private static final String VALUE_DATA           = "data value";
    private static final String KEY_ROOT             = "root";
    private static final String VALUE_ROOT           = "root value";
    
    private static final Node empty         = new Node();
    private static final Node keyOnly       = new Node(KEY_KEY_ONLY);
    private static final Node prename1      = new Node(KEY_PRENAME, VALUE_PRENAME_1);
    private static final Node prename2      = new Node(KEY_PRENAME, VALUE_PRENAME_2);
    private static final Node surname       = new Node(KEY_SURNAME, VALUE_SURNAME);
    private static final Node intermediate1 = new Node(KEY_INTERMEDIATE_1, VALUE_INTERMEDIATE_1);
    private static final Node intermediate2 = new Node(KEY_INTERMEDIATE_2, VALUE_INTERMEDIATE_2, Arrays.asList(new Node[] { prename1, prename2, surname }));
    private static final Node intermediate3 = new Node(KEY_INTERMEDIATE_3, VALUE_INTERMEDIATE_3);
    private static final Node data          = new Node(KEY_DATA, VALUE_DATA, Arrays.asList(new Node[] { intermediate1, intermediate2, intermediate3 }));
    private static final Node root          = new Node(KEY_ROOT, VALUE_ROOT, Arrays.asList(new Node[] { data }));
    
    public NodeNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of getKey method, of class Node.
     */
    @Test
    public void testGetKey() {
        System.out.println("getKey");
        
        // test with default constructor
        String result = empty.getKey();
        assertEquals(result, KEY_EMPTY_KEY);
        
        // test with key setting constructor
        result = keyOnly.getKey();
        assertEquals(result, KEY_KEY_ONLY);
        
        // test with key/value setting constructor
        result = surname.getKey();
        assertEquals(result, KEY_SURNAME);
        
        // test with children setting constructor
        result = intermediate2.getKey();
        assertEquals(result, KEY_INTERMEDIATE_2);

        // test with setter/getter
        Node instance = new Node();
        String key = "getKey test key";
        instance.setKey(key);
        result = instance.getKey();
        assertEquals(result, key);
    }

    /**
     * Test of setKey method, of class Node.
     */
    @Test
    public void testSetKey() {
        System.out.println("setKey");
        
        // test with setter/getter
        Node instance = new Node();
        String key = "setKey test key";
        instance.setKey(key);
        String result = instance.getKey();
        assertEquals(result, key);
    }

    /**
     * Test of getValue method, of class Node.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        
        // test with default constructor
        String result = empty.getValue();
        assertEquals(result, VALUE_EMPTY_KEY);
        
        // test with key setting constructor
        result = keyOnly.getValue();
        assertEquals(result, VALUE_KEY_ONLY);
        
        // test with key/value setting constructor
        result = surname.getValue();
        assertEquals(result, VALUE_SURNAME);
        
        // test with children setting constructor
        result = intermediate2.getValue();
        assertEquals(result, VALUE_INTERMEDIATE_2);

        // test with setter/getter
        Node instance = new Node();
        String value = "getValue test value";
        instance.setValue(value);
        result = instance.getValue();
        assertEquals(result, value);
    }

    /**
     * Test of setValue method, of class Node.
     */
    @Test
    public void testSetValue() {
        System.out.println("setValue");
        
        // test with setter/getter
        Node instance = new Node();
        String value = "setValue test value";
        instance.setValue(value);
        String result = instance.getValue();
        assertEquals(result, value);
    }

    /**
     * Test of getChildren method, of class Node.
     */
    @Test
    public void testGetChildren() {
        System.out.println("getChildren");
        
        // test with default constructor
        List<Node> result = empty.getChildren();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        // test with key setting constructor
        result = keyOnly.getChildren();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        // test with key/value setting constructor
        result = surname.getChildren();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        // test with children setting constructor
        result = root.getChildren();
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), data);
    }

    /**
     * Test of addChild method, of class Node.
     */
    @Test
    public void testAddChild() {
        System.out.println("addChild");
        
        Node child = new Node("addChild test key", "addChild test value");
        Node instance = new Node();
        assertTrue(instance.addChild(child));
        assertEquals(instance.numberOfChildren(), 1);
        assertEquals(instance.getChildren().get(0), child);
    }

    /**
     * Test of removeChild method, of class Node.
     */
    @Test
    public void testRemoveChild() {
        System.out.println("removeChild");
        
        // test with add/remove/numberOfChildren/getChildren
        Node one = new Node("removeChild test node one");
        Node two = new Node("removeChild test node two");
        Node three = new Node("removeChild test node three");
        Node instance = new Node();
        instance.addChild(one);
        instance.addChild(two);
        instance.addChild(three);
        boolean result = instance.removeChild(two);
        assertTrue(result);
        assertEquals(instance.numberOfChildren(), 2);
        
        Node noChild = new Node("this is not a child");
        result = instance.removeChild(noChild);
        assertFalse(result);
        assertEquals(instance.numberOfChildren(), 2);
        
        List<Node> expectedChildren = new ArrayList<Node>();
        expectedChildren.add(one);
        expectedChildren.add(three);
        List<Node> children = instance.getChildren();
        
        for (Node child : children) {
            if (!expectedChildren.remove(child)) {
                fail("found a child that was not expected");
            }
        }
        assertEquals(expectedChildren.size(), 0);
        
        // test with remove/getChildren
        one = new Node("removeChild test node one");
        two = new Node("removeChild test node two");
        three = new Node("removeChild test node three");
        instance = new Node();
        children = instance.getChildren();
        children.add(one);
        children.add(two);
        children.add(three);
        result = instance.removeChild(two);
        assertTrue(result);
        assertEquals(children.size(), 2);
        
        expectedChildren = new ArrayList<Node>();
        expectedChildren.add(one);
        expectedChildren.add(three);
        
        for (Node child : children) {
            assertTrue(expectedChildren.remove(child));
        }
        
        assertEquals(expectedChildren.size(), 0);
    }

    /**
     * Test of getFirstChildByKey method, of class Node.
     */
    @Test
    public void testGetFirstChildByKey() {
        System.out.println("getFirstChildByKey");
        
        // null key
        assertNull(root.getFirstChildByKey(null));
        
        // empty key
        assertNull(root.getFirstChildByKey(""));
        
        // non-existing key
        assertNull(empty.getFirstChildByKey("a key that doesn't exist"));
        assertNull(keyOnly.getFirstChildByKey("a key that doesn't exist"));
        assertNull(surname.getFirstChildByKey("a key that doesn't exist"));
        assertNull(root.getFirstChildByKey("a key that doesn't exist"));
        
        // existing key
        assertEquals(intermediate2.getFirstChildByKey(KEY_PRENAME), prename1);
        assertEquals(intermediate2.getFirstChildByKey(KEY_SURNAME), surname);
    }
    
    /**
     * Test of getFirstValueByKey method, of class Node.
     */
    @Test
    public void testGetFirstValueByKey() {
        System.out.println("getFirstValueByKey");
        
        // null key
        String result = intermediate2.getFirstValueByKey(null);
        assertNull(result);
        
        // empty key
        result = intermediate2.getFirstValueByKey("");
        assertNull(result);
        
        // no such key
        result = intermediate2.getFirstValueByKey("no such key");
        assertNull(result);
        
        // key denotes node itself
        result = intermediate2.getFirstValueByKey(KEY_INTERMEDIATE_2);
        assertNotNull(result);
        assertEquals(result, VALUE_INTERMEDIATE_2);
        
        // key denotes child
        result = data.getFirstValueByKey(KEY_INTERMEDIATE_2);
        assertNotNull(result);
        assertEquals(result, VALUE_INTERMEDIATE_2);
        
        // correct hierarchical access of data
        result = root.getFirstValueByKey(KEY_SURNAME);
        assertNotNull(result);
        assertEquals(result, VALUE_SURNAME);
        result = root.getFirstValueByKey(KEY_PRENAME);
        assertNotNull(result);
        assertEquals(result, VALUE_PRENAME_1);
    }
    
    /**
     * Test of getFirstValueByKeyPath method, of class Node.
     */
    @Test
    public void testGetFirstValueByKeyPath() {
        System.out.println("getFirstValueByKeyPath");
        
        // null key
        String result = intermediate2.getFirstValueByKeyPath(null);
        assertNull(result);
        
        // empty key
        result = intermediate2.getFirstValueByKeyPath("");
        assertNull(result);
        
        // no such key
        result = intermediate2.getFirstValueByKeyPath("no.such.key");
        assertNull(result);
        
        // key denotes node itself
        result = intermediate2.getFirstValueByKeyPath(KEY_INTERMEDIATE_2);
        assertNotNull(result);
        assertEquals(result, VALUE_INTERMEDIATE_2);
        
        // key denotes child
        result = data.getFirstValueByKeyPath(KEY_INTERMEDIATE_2);
        assertNotNull(result);
        assertEquals(result, VALUE_INTERMEDIATE_2);
        
        // correct hierarchical access of data
        result = root.getFirstValueByKeyPath(KEY_SURNAME);
        assertNotNull(result);
        assertEquals(result, VALUE_SURNAME);
        result = root.getFirstValueByKeyPath(KEY_PRENAME);
        assertNotNull(result);
        assertEquals(result, VALUE_PRENAME_1);
    }
    
    /**
     * Test of getValueListByKey method, of class Node.
     */
    @Test
    public void testGetValueListByKey() {
        System.out.println("getValueListByKey");
        
        // null key
        List<String> result = intermediate2.getValueListByKey(null);
        assertNotNull(result);
        assertEquals(result.size(), 0);
        
        // empty key
        result = intermediate2.getValueListByKey("");
        assertNotNull(result);
        assertEquals(result.size(), 0);
        
        // no such key
        result = intermediate2.getValueListByKey("no such key");
        assertNotNull(result);
        assertEquals(result.size(), 0);
        
        // key denotes node itself
        result = intermediate2.getValueListByKey(KEY_INTERMEDIATE_2);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), VALUE_INTERMEDIATE_2);
        
        // key denotes child
        result = data.getValueListByKey(KEY_INTERMEDIATE_2);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), VALUE_INTERMEDIATE_2);
        
        // correct hierarchical access of data
        result = root.getValueListByKey(KEY_SURNAME);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), VALUE_SURNAME);
        
        result = root.getValueListByKey(KEY_PRENAME);
        assertEquals(result.size(), 2);
        assertTrue(result.remove(VALUE_PRENAME_1));
        assertTrue(result.remove(VALUE_PRENAME_2));
        assertEquals(result.size(), 0);
    }

    /**
     * Test of getValueListByKeyPath method, of class Node.
     */
    @Test
    public void testGetValueListByKeyPath() {
        System.out.println("getValueListByKeyPath");
        
        // null key path
        List<String> result = root.getValueListByKeyPath(null);
        assertNotNull(result);
        assertEquals(result.size(), 0);
        
        // empty key path
        result = root.getValueListByKeyPath("");
        assertNotNull(result);
        assertEquals(result.size(), 0);
        
        // no such key path
        result = root.getValueListByKeyPath("wrong.key.path");
        assertNotNull(result);
        assertEquals(result.size(), 0);
        result = root.getValueListByKeyPath(KEY_ROOT + "." + KEY_INTERMEDIATE_2 + "." + KEY_PRENAME);
        assertNotNull(result);
        assertEquals(result.size(), 0);
        result = root.getValueListByKeyPath(KEY_ROOT + "." + KEY_DATA); // don't incluse this node's name
        assertNotNull(result);
        assertEquals(result.size(), 0);
        
        // key path denotes node itself (dot notation)
        result = surname.getValueListByKeyPath(KEY_THIS);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), VALUE_SURNAME);
        
        // key path denotes child
        List<String> expectedResult = new ArrayList<String>(Arrays.asList(new String[] { VALUE_PRENAME_1, VALUE_PRENAME_2 }));
        result = intermediate2.getValueListByKeyPath(KEY_PRENAME);
        assertEquals(result.size(), 2);
        for (String prename : result) {
            assertTrue(expectedResult.remove(prename));
        }
        assertEquals(expectedResult.size(), 0);
        
        // correct hierarchical access of data
        expectedResult = new ArrayList<String>(Arrays.asList(new String[] { VALUE_PRENAME_1, VALUE_PRENAME_2 }));
        result = root.getValueListByKeyPath(KEY_DATA + "." + KEY_INTERMEDIATE_2 + "." + KEY_PRENAME);
        assertEquals(result.size(), 2);
        for (String prename : result) {
            assertTrue(expectedResult.remove(prename));
        }
        assertEquals(expectedResult.size(), 0);
    }

    /**
     * Test of getNodeListByKeyPath method, of class Node.
     */
    @Test
    public void testGetChildNodeListByKeyPath() {
        System.out.println("getChildNodeListByKeyPath");
        
        // null key path
        List<Node> result = root.getChildNodeListByKeyPath(null);
        assertNotNull(result);
        assertEquals(result.size(), 0);
        
        // empty key path
        result = root.getChildNodeListByKeyPath("");
        assertNotNull(result);
        assertEquals(result.size(), 0);
        
        // no such key path
        result = root.getChildNodeListByKeyPath("wrong.key.path");
        assertEquals(result.size(), 0);
        result = root.getChildNodeListByKeyPath(KEY_ROOT + "." + KEY_INTERMEDIATE_2 + "." + KEY_PRENAME);
        assertEquals(result.size(), 0);
        result = root.getChildNodeListByKeyPath(KEY_ROOT); 
        assertEquals(result.size(), 0);
        
        // key denotes child
        result = root.getChildNodeListByKeyPath(KEY_DATA);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), data);
        assertEquals(result.get(0).getKey(), KEY_DATA);
        assertEquals(result.get(0).getValue(), VALUE_DATA);
        
        // correct hierarchical access of data
        List<Node> expectedResult = new ArrayList<Node>(Arrays.asList(new Node[] { prename2, prename1 }));
        result = root.getChildNodeListByKeyPath(KEY_DATA + "." + KEY_INTERMEDIATE_2 + "." + KEY_PRENAME);
        
        for (Node prenameNode : result) {
            assertTrue(expectedResult.remove(prenameNode));
        }
        assertEquals(expectedResult.size(), 0);
    }

    /**
     * Test of setValueByKeyPath method, of class Node.
     */
    @Test
    public void testSetValueByKeyPath() {
        System.out.println("setValueByKeyPath");
        
        // mutable data structure
        // when checking data in this structure, be aware of all previous
        // operations on your data so far within this method
        Node nodeOne = new Node("name", "1");
        Node nodeOneOne = new Node("child_version", "1.1");
        nodeOne.addChild(nodeOneOne);
        Node nodeOneTwo = new Node("child_version_new", "1.2");
        nodeOne.addChild(nodeOneTwo);
        Node nodeOneTwoOne = new Node("child_version", "1.2.1");
        nodeOneTwo.addChild(nodeOneTwoOne);
        Node nodeOneTwoTwo = new Node("child_version", "1.2.2");
        nodeOneTwo.addChild(nodeOneTwoTwo);
        Node nodeOneTwoThree = new Node("other_child_version", "1.2.3");
        nodeOneTwo.addChild(nodeOneTwoThree);
        Node nodeOneThree = new Node("child_version", "1.3");
        nodeOne.addChild(nodeOneThree);
        
        String newValue = "new Value";
        
        // null key path
        assertFalse(nodeOne.setValueByKeyPath(null, newValue));
        assertEquals(nodeOne.getValue(), "1");
        
        // empty key path
        assertFalse(nodeOne.setValueByKeyPath("", newValue));
        assertEquals(nodeOne.getValue(), "1");
        
        // no such key path (= create new Node)
        assertTrue(nodeOne.setValueByKeyPath("very.new.key.path", newValue));
        assertEquals(nodeOne.getFirstValueByKeyPath("very.new.key.path"), newValue);
        List<String> valueList = nodeOne.getValueListByKeyPath("very.new.key.path");
        assertNotNull(valueList);
        assertEquals(valueList.size(), 1);
        assertEquals(valueList.get(0), newValue);
        
        // key path not unique
        assertFalse(nodeOne.setValueByKeyPath("child_version", newValue));
        assertEquals(nodeOneOne.getValue(), "1.1");
        assertEquals(nodeOneThree.getValue(), "1.3");
        
        // key denotes one child
        assertTrue(nodeOne.setValueByKeyPath("child_version_new", newValue));
        assertEquals(nodeOneTwo.getValue(), newValue);
        assertNotEquals(nodeOne.getValue(), newValue);
        assertNotEquals(nodeOneOne.getValue(), newValue);
        assertNotEquals(nodeOneTwoOne.getValue(), newValue);
        assertNotEquals(nodeOneTwoTwo.getValue(), newValue);
        assertNotEquals(nodeOneTwoThree.getValue(), newValue);
        assertNotEquals(nodeOneThree.getValue(), newValue);
        
        // key denotes distant descendant
        //other_child_version
        assertTrue(nodeOne.setValueByKeyPath("child_version_new.other_child_version", newValue));
        assertEquals(nodeOneTwoThree.getValue(), newValue);
        assertEquals(nodeOneTwo.getValue(), newValue); // has been altered already (see above)
        assertNotEquals(nodeOne.getValue(), newValue);
        assertNotEquals(nodeOneOne.getValue(), newValue);
        assertNotEquals(nodeOneTwoOne.getValue(), newValue);
        assertNotEquals(nodeOneTwoTwo.getValue(), newValue);
        assertNotEquals(nodeOneThree.getValue(), newValue);
        
        // setter operates on objects and stores their toString() representation
        // integer to string conversion
        assertTrue(nodeOne.setValueByKeyPath("another.new.key.path", 10));
        assertEquals(nodeOne.getFirstValueByKeyPath("another.new.key.path"), "10");
        
        // boolean to string conversion
        assertTrue(nodeOne.setValueByKeyPath("another.new.key.path", true));
        assertEquals(nodeOne.getFirstValueByKeyPath("another.new.key.path"), "true");
    }

    /**
     * Test of createNodeByKeyPath method, of class Node.
     */
    @Test
//    public void testCreateNodeByKeyPath() {
//        System.out.println("createNodeByKeyPath");
//        
//        String keyPath = null;
//        Node instance = new Node("newRoot");
//        Node result = instance.createNodeByKeyPath(keyPath);
//        assertNotNull(instance);
//        assertNull(result);
//        
//        keyPath = "";
//        instance = new Node("newRoot");
//        result = instance.createNodeByKeyPath(keyPath);
//        assertNotNull(instance);
//        assertNull(result);
//        
//        keyPath = "eins.B.2";
//        instance = new Node("newRoot");
//        
//        result = instance.createNodeByKeyPath(keyPath);
//        assertNotNull(instance);
//        assertNotNull(result);
//        assertEquals(result.getKey(), "2");
//        
//        Node nodeEins = instance.getFirstChildByKey("eins");
//        assertNotNull(nodeEins);
//        assertEquals(nodeEins.getKey(), "eins");
//        
//        Node nodeB = nodeEins.getFirstChildByKey("B");
//        assertNotNull(nodeB);
//        assertEquals(nodeB.getKey(), "B");
//        
//        Node node2 = nodeB.getFirstChildByKey("2");
//        assertNotNull(node2);
//        assertEquals(node2.getKey(), "2");
//        
//        System.out.println("fertig");
//    }
    public void testCreateNodeByKeyPath() {
        System.out.println("createNodeByKeyPath");
        
        // null key path
        String keyPath = null;
        Node instance = new Node("newRoot");
        Node result = instance.createNodeByKeyPath(keyPath);
        assertNotNull(instance);
        assertNull(result);
        
        // empty key path
        keyPath = "";
        instance = new Node("newRoot");
        result = instance.createNodeByKeyPath(keyPath);
        assertNotNull(instance);
        assertNull(result);
        
        // correct key path
        keyPath = "eins.B.2";
        instance = new Node("newRoot");
        result = instance.createNodeByKeyPath(keyPath);
        assertNotNull(instance);
        assertNotNull(result);
        assertEquals(result.getKey(), "2");
        
        List<Node> children = instance.getChildren();
        assertNotNull(children);
        assertEquals(children.size(), 1);
        instance = children.get(0);
        assertEquals(instance.getKey(), "eins");
        
        children = instance.getChildren();
        assertNotNull(children);
        assertEquals(children.size(), 1);
        instance = children.get(0);
        assertEquals(instance.getKey(), "B");
        
        children = instance.getChildren();
        assertNotNull(children);
        assertEquals(children.size(), 1);
        instance = children.get(0);
        assertEquals(instance.getKey(), "2");
        
        children = instance.getChildren();
        assertNotNull(children);
        assertEquals(children.size(), 0);
    }

    /**
     * Test of numberOfChildren method, of class Node.
     */
    @Test
    public void testNumberOfChildren() {
        System.out.println("numberOfChildren");

        // test of constructor
        assertEquals(empty.numberOfChildren(), 0);
        assertEquals(keyOnly.numberOfChildren(), 0);
        assertEquals(prename1.numberOfChildren(), 0);
        assertEquals(root.numberOfChildren(), 1);
        assertEquals(data.numberOfChildren(), 3);
        
        // test of add/remove
        Node instance = new Node();
        Node one = new Node("getNumberOfChildren test node 1");
        Node two = new Node("getNumberOfChildren test node 2");
        Node three = new Node("getNumberOfChildren test node 3");
        Node four = new Node("getNumberOfChildren test node 4");
        
        assertEquals(instance.numberOfChildren(), 0);
        instance.addChild(one);
        assertEquals(instance.numberOfChildren(), 1);
        instance.addChild(two);
        assertEquals(instance.numberOfChildren(), 2);
        instance.addChild(three);
        assertEquals(instance.numberOfChildren(), 3);
        instance.addChild(four);
        assertEquals(instance.numberOfChildren(), 4);
        
        Node noChild = new Node("this is not a child");
        instance.removeChild(noChild);
        assertEquals(instance.numberOfChildren(), 4);
        
        instance.removeChild(two);
        assertEquals(instance.numberOfChildren(), 3);
        instance.removeChild(one);
        assertEquals(instance.numberOfChildren(), 2);
        instance.removeChild(four);
        assertEquals(instance.numberOfChildren(), 1);
        instance.removeChild(three);
        assertEquals(instance.numberOfChildren(), 0);
    }

    /**
     * Test of createNodeByKeyPathWithValue method, of class Node.
     */
    @Test
    public void testCreateNodeByKeyPathWithValue() {
        System.out.println("createNodeByKeyPathWithValue");
        
        // null key path
        String keyPath = null;
        String value = null;
        Node instance = new Node("newRoot");
        Node result = instance.createNodeByKeyPathWithValue(keyPath, value);
        assertNotNull(instance);
        assertNull(result);
        
        // empty key path
        keyPath = "";
        instance = new Node("newRoot");
        result = instance.createNodeByKeyPathWithValue(keyPath, value);
        assertNotNull(instance);
        assertNull(result);
        
        // correct key path and value
        keyPath = "eins.B.2";
        value = "fünf";
        instance = new Node("newRoot");
        result = instance.createNodeByKeyPathWithValue(keyPath, value);
        assertNotNull(instance);
        assertNotNull(result);
        assertEquals(result.getKey(), "2");
        assertEquals(result.getValue(), value);
        
        List<Node> children = instance.getChildren();
        assertNotNull(children);
        assertEquals(children.size(), 1);
        instance = children.get(0);
        assertEquals(instance.getKey(), "eins");
        assertEquals(instance.getValue(), "");
        
        children = instance.getChildren();
        assertNotNull(children);
        assertEquals(children.size(), 1);
        instance = children.get(0);
        assertEquals(instance.getKey(), "B");
        assertEquals(instance.getValue(), "");
        
        children = instance.getChildren();
        assertNotNull(children);
        assertEquals(children.size(), 1);
        instance = children.get(0);
        assertEquals(instance.getKey(), "2");
        assertEquals(instance.getValue(), value);
        assertEquals(instance, result);
        
        children = instance.getChildren();
        assertNotNull(children);
        assertEquals(children.size(), 0);
        
        // null value
        keyPath = "zwei.C.3 a";
        value = null;
        instance = new Node("newRoot");
        result = instance.createNodeByKeyPathWithValue(keyPath, value);
        assertNotNull(instance);
        assertNotNull(result);
        assertEquals(result.getKey(), "3 a");
        assertEquals(result.getValue(), "");
        
        // empty value
        keyPath = "zwei.C.3 b";
        instance = new Node("newRoot");
        result = instance.createNodeByKeyPathWithValue(keyPath, value);
        assertNotNull(instance);
        assertNotNull(result);
        assertEquals(result.getKey(), "3 b");
        assertEquals(result.getValue(), "");
    }

    /**
     * Test of toJson method, of class Node.
     */
    @Test
    public void testToJson() {
        System.out.println("toJson");
        Node instance = new Node();
        String expResult = "";
        String result = instance.toJson();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    /**
     * Test of toString method, of class Node.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Node instance = new Node();
        String expResult = "";
        String result = instance.toString();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
