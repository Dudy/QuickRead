package de.podolak.quickread.data.datastore;

import de.podolak.quickread.data.datastore.Node;
import de.podolak.quickread.data.datastore.Nodes;
import java.util.ArrayList;
import java.util.List;
import org.junit.Ignore;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Dude
 */
public class NodeNGTest {
    
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
    @Ignore
    public void testGetKey() {
        System.out.println("getKey");
        Node instance = new Node();
        String expResult = "";
        String result = instance.getKey();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setKey method, of class Node.
     */
    @Test
    @Ignore
    public void testSetKey() {
        System.out.println("setKey");
        String key = "";
        Node instance = new Node();
        instance.setKey(key);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getValue method, of class Node.
     */
    @Test
    @Ignore
    public void testGetValue() {
        System.out.println("getValue");
        Node instance = new Node();
        String expResult = "";
        String result = instance.getValue();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setValue method, of class Node.
     */
    @Test
    @Ignore
    public void testSetValue() {
        System.out.println("setValue");
        String value = "";
        Node instance = new Node();
        instance.setValue(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getChildren method, of class Node.
     */
    @Test
    @Ignore
    public void testGetChildren() {
        System.out.println("getChildren");
        Node instance = new Node();
        Nodes expResult = null;
        //Nodes result = instance.getChildren();
        ArrayList<Node> result = instance.getChildren();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

//    /**
//     * Test of setChildren method, of class Node.
//     */
//    @Test
//    @Ignore
//    public void testSetChildren() {
//        System.out.println("setChildren");
//        Nodes children = null;
//        Node instance = new Node();
//        instance.setChildren(children);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of addChild method, of class Node.
     */
    @Test
    @Ignore
    public void testAddChild() {
        System.out.println("addChild");
        Node child = null;
        Node instance = new Node();
        boolean expResult = false;
        boolean result = instance.addChild(child);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeChild method, of class Node.
     */
    @Test
    @Ignore
    public void testRemoveChild() {
        System.out.println("removeChild");
        Node child = null;
        Node instance = new Node();
        boolean expResult = false;
        boolean result = instance.removeChild(child);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNumberOfChildren method, of class Node.
     */
    @Test
    @Ignore
    public void testGetNumberOfChildren() {
        System.out.println("getNumberOfChildren");
        Node instance = new Node();
        int expResult = 0;
        int result = instance.numberOfChildren();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getValueListByKey method, of class Node.
     */
    @Test
    @Ignore
    public void testGetValueListByKey() {
        System.out.println("getValueListByKey");
        
        Node root = new Node("name", "1");
        Node parent = root;
        Node node = new Node("child_version", "1.1");
        parent.addChild(node);
        node = new Node("child_version_new", "1.2");
        parent.addChild(node);
        parent = node;
        node = new Node("child_version", "1.2.1");
        parent.addChild(node);
        node = new Node("child_version", "1.2.2");
        parent.addChild(node);
        
        String key = "name";
        List expResult = null;
        List<String> result = root.getValueListByKey(key);
        
        for (String s : result) {
            System.out.println(s);
        }
        
        fail("The test case is a prototype.");
    }

    /**
     * Test of getValueListByKeyPath method, of class Node.
     */
    @Test
    public void testGetValueListByKeyPath() {
        System.out.println("getValueListByKeyPath");
        
        String keyPath = "serializationVersion";
        Node instance = new Node("root");
        List expResult = null;
        List result = instance.getValueListByKeyPath(keyPath);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Node.
     */
    @Test
    @Ignore
    public void testToString() {
        System.out.println("toString");
        Node instance = new Node();
        String expResult = "";
        String result = instance.toString();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFirstChildByKey method, of class Node.
     */
    @Test
    public void testGetFirstChildByKey() {
        System.out.println("getFirstChildByKey");
        String key = "";
        Node instance = new Node();
        Node expResult = null;
        Node result = instance.getFirstChildByKey(key);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNodeListByKeyPath method, of class Node.
     */
    @Test
    public void testGetNodeListByKeyPath() {
        System.out.println("getNodeListByKeyPath");
        String keyPath = "";
        Node instance = new Node();
        List expResult = null;
        List result = instance.getNodeListByKeyPath(keyPath);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setValueByKeyPath method, of class Node.
     */
    @Test
    public void testSetValueByKeyPath() {
        System.out.println("setValueByKeyPath");
        String keyPath = "";
        Object value = null;
        Node instance = new Node();
        instance.setValueByKeyPath(keyPath, value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createNodeByKeyPath method, of class Node.
     */
    @Test
    public void testCreateNodeByKeyPath() {
        System.out.println("createNodeByKeyPath");
        
        String keyPath = "eins.B.2";
        Node instance = new Node("data");
        
        Node result = instance.createNodeByKeyPath(keyPath);
        assertNotNull(instance);
        assertNotNull(result);
        assertEquals(result.getKey(), "2");
        
        Node nodeEins = instance.getFirstChildByKey("eins");
        assertNotNull(nodeEins);
        assertEquals(nodeEins.getKey(), "eins");
        
        Node nodeB = nodeEins.getFirstChildByKey("B");
        assertNotNull(nodeB);
        assertEquals(nodeB.getKey(), "B");
        
        Node node2 = nodeB.getFirstChildByKey("2");
        assertNotNull(node2);
        assertEquals(node2.getKey(), "2");
        
        System.out.println("fertig");
    }
}
