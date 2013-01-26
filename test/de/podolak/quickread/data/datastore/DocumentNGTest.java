package de.podolak.quickread.data.datastore;

import de.podolak.quickread.data.datastore.Document.Metadata;
import java.util.Date;
import java.util.List;
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
public class DocumentNGTest {

    private static final Node root = new Node("rootNode");
    private static final Long id = 12345L;
    private static final Integer serializationVersion = 99;
    private static final Date createDate = new Date(123478192534891523L);
    private static final Date lastModifyDate = new Date(123478192534891524L);
    private static final DocumentType documentType = DocumentType.BOOK;
    
    private static final String firstKey = "un";
    private static final String secondKey = "two";
    private static final String thirdKey = "drei";
    
    public DocumentNGTest() {
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
     * Test of init method, of class Document.
     */
    @Test
    public void testInit_Document() {
        System.out.println("init");

        // setRoot must be the first as all data is stored therein
        Document document = new Document();
        document.setRoot(root);
        document.setId(id);
        document.setSerializationVersion(serializationVersion);
        document.setCreateDate(createDate);
        document.setLastModifyDate(lastModifyDate);
        document.setDocumentType(documentType);
        assertEquals(document.getId(), id);
        assertEquals(document.getSerializationVersion(), serializationVersion);
        assertEquals(document.getRoot(), root);
        assertEquals(document.getCreateDate(), createDate);
        assertEquals(document.getLastModifyDate(), lastModifyDate);
        assertEquals(document.getDocumentType(), documentType);
        
        Document instance = new Document();
        instance.init(document);
        assertEquals(instance.getRoot(), root);
        assertEquals(instance.getId(), id);
        assertEquals(instance.getSerializationVersion(), serializationVersion);
        assertEquals(instance.getCreateDate(), createDate);
        assertEquals(instance.getLastModifyDate(), lastModifyDate);
        assertEquals(instance.getDocumentType(), documentType);
    }

    /**
     * Test of init method, of class Document.
     */
    @Test
    public void testInit_6args() {
        System.out.println("init");
        
        Document instance = new Document();
        instance.init(id, serializationVersion, root, createDate, lastModifyDate, documentType);
        assertEquals(instance.getRoot(), root);
        assertEquals(instance.getId(), id);
        assertEquals(instance.getSerializationVersion(), serializationVersion);
        assertEquals(instance.getCreateDate(), createDate);
        assertEquals(instance.getLastModifyDate(), lastModifyDate);
        assertEquals(instance.getDocumentType(), documentType);
    }

    /**
     * Test of getId method, of class Document.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        
        // no version set
        Document instance = new Document();
        Long expResult = null;
        Long result = instance.getId();
        assertEquals(result, expResult);
        
        // version set
        instance = new Document();
        expResult = 123L;
        Node rootNode = instance.getRoot();
        Node node = new Node(Document.Metadata.ID.getIdentifier(), expResult.toString());
        rootNode.addChild(node);
        result = instance.getId();
        assertEquals(result, expResult);
    }

    /**
     * Test of setId method, of class Document.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        
        // null id
        Document instance = new Document();
        instance.setId(null);
        Node rootNode = instance.getRoot();
        List<Node> childList = rootNode.getChildren();
        assertNotNull(childList);
        assertEquals(childList.size(), 1);
        assertEquals(childList.get(0).getKey(), Document.Metadata.ID.getIdentifier());
        assertNull(childList.get(0).getValue());
        
        // id set
        instance = new Document();
        Long ID = 22222L;
        instance.setId(ID);
        rootNode = instance.getRoot();
        childList = rootNode.getChildren();
        assertNotNull(childList);
        assertEquals(childList.size(), 1);
        assertEquals(childList.get(0).getKey(), Document.Metadata.ID.getIdentifier());
        assertEquals(childList.get(0).getValue(), ID.toString());
    }

    /**
     * Test of getRoot method, of class Document.
     */
    @Test
    public void testGetRoot() {
        System.out.println("getRoot");
        
        // default root node
        Document instance = new Document();
        Node result = instance.getRoot();
        assertNotNull(result);
        assertEquals(result.getKey(), Document.ROOT_KEY_PREFIX);
        assertEquals(result.getValue(), "");
        
        // custom root node
        instance = new Document();
        Node node = new Node("new root key", "new root value");
        instance.setRoot(node);
        result = instance.getRoot();
        assertNotNull(result);
        assertEquals(result.getKey(), "new root key");
        assertEquals(result.getValue(), "new root value");
    }

    /**
     * Test of setRoot method, of class Document.
     */
    @Test
    public void testSetRoot() {
        System.out.println("setRoot");
        
        // the getter will always return a non empty root, so while you can set
        // the root node to null you can't get null back
        
        // empty root node
        Node rootNode = new Node();
        Document instance = new Document();
        instance.setRoot(rootNode);
        Node result = instance.getRoot();
        assertNotNull(result);
        assertEquals(result.getKey(), "");
        assertEquals(result.getValue(), "");
        
        // root node with key
        rootNode = new Node("rn");
        instance = new Document();
        instance.setRoot(rootNode);
        result = instance.getRoot();
        assertNotNull(result);
        assertEquals(result.getKey(), "rn");
        assertEquals(result.getValue(), "");
        
        // root node with key and value
        rootNode = new Node("rootNode", "rootValue");
        instance = new Document();
        instance.setRoot(rootNode);
        result = instance.getRoot();
        assertNotNull(result);
        assertEquals(result.getKey(), "rootNode");
        assertEquals(result.getValue(), "rootValue");
    }

    /**
     * Test of getSerializationVersion method, of class Document.
     */
    @Test
    public void testGetSerializationVersion() {
        System.out.println("getSerializationVersion");

        // no version set
        Document instance = new Document();
        Integer expResult = null;
        Integer result = instance.getSerializationVersion();
        assertEquals(result, expResult);
        
        // version set
        instance = new Document();
        expResult = 99;
        Node rootNode = instance.getRoot();
        Node node = new Node(Document.Metadata.SERIALIZATION_VERSION.getIdentifier(), expResult.toString());
        rootNode.addChild(node);
        result = instance.getSerializationVersion();
        assertEquals(result, expResult);
    }

    /**
     * Test of setSerializationVersion method, of class Document.
     */
    @Test
    public void testSetSerializationVersion() {
        System.out.println("setSerializationVersion");
        
        // null version
        Document instance = new Document();
        instance.setSerializationVersion(null);
        Node rootNode = instance.getRoot();
        List<Node> childList = rootNode.getChildren();
        assertNotNull(childList);
        assertEquals(childList.size(), 1);
        assertEquals(childList.get(0).getKey(), Document.Metadata.SERIALIZATION_VERSION.getIdentifier());
        assertNull(childList.get(0).getValue());
        
        // version set
        instance = new Document();
        Integer version = 98;
        instance.setSerializationVersion(version);
        rootNode = instance.getRoot();
        childList = rootNode.getChildren();
        assertNotNull(childList);
        assertEquals(childList.size(), 1);
        assertEquals(childList.get(0).getKey(), Document.Metadata.SERIALIZATION_VERSION.getIdentifier());
        assertEquals(childList.get(0).getValue(), version.toString());
    }

    /**
     * Test of getCreateDate method, of class Document.
     */
    @Test
    public void testGetCreateDate() {
        System.out.println("getCreateDate");
        
        // no date set
        Document instance = new Document();
        Date expResult = null;
        Date result = instance.getCreateDate();
        assertEquals(result, expResult);
        
        // date set
        instance = new Document();
        Long l = 1359201715762L;
        expResult = new Date(l);
        Node rootNode = instance.getRoot();
        Node node = new Node(Document.Metadata.CREATE_DATE.getIdentifier(), Long.toString(l));
        rootNode.addChild(node);
        result = instance.getCreateDate();
        assertEquals(result, expResult);
    }

    /**
     * Test of setCreateDate method, of class Document.
     */
    @Test
    public void testSetCreateDate() {
        System.out.println("setCreateDate");
        
        // null date
        Document instance = new Document();
        instance.setCreateDate(null);
        Node rootNode = instance.getRoot();
        List<Node> childList = rootNode.getChildren();
        assertNotNull(childList);
        assertEquals(childList.size(), 1);
        assertEquals(childList.get(0).getKey(), Document.Metadata.CREATE_DATE.getIdentifier());
        assertNull(childList.get(0).getValue());
        
        // some date
        instance = new Document();
        Long expResult = 1359201715760L;
        instance.setCreateDate(new Date(expResult));
        rootNode = instance.getRoot();
        childList = rootNode.getChildren();
        assertNotNull(childList);
        assertEquals(childList.size(), 1);
        assertEquals(childList.get(0).getKey(), Document.Metadata.CREATE_DATE.getIdentifier());
        assertEquals(childList.get(0).getValue(), expResult.toString());
    }

    /**
     * Test of getLastModifyDate method, of class Document.
     */
    @Test
    public void testGetLastModifyDate() {
        System.out.println("getLastModifyDate");
        
        // no date set
        Document instance = new Document();
        Date expResult = null;
        Date result = instance.getLastModifyDate();
        assertEquals(result, expResult);
        
        // date set
        instance = new Document();
        Long l = 1359201715761L;
        expResult = new Date(l);
        Node rootNode = instance.getRoot();
        Node node = new Node(Document.Metadata.LAST_MODIFY_DATE.getIdentifier(), Long.toString(l));
        rootNode.addChild(node);
        result = instance.getLastModifyDate();
        assertEquals(result, expResult);
    }

    /**
     * Test of setLastModifyDate method, of class Document.
     */
    @Test
    public void testSetLastModifyDate() {
        System.out.println("setLastModifyDate");
        
        // null date
        Document instance = new Document();
        instance.setLastModifyDate(null);
        Node rootNode = instance.getRoot();
        List<Node> childList = rootNode.getChildren();
        assertNotNull(childList);
        assertEquals(childList.size(), 1);
        assertEquals(childList.get(0).getKey(), Document.Metadata.LAST_MODIFY_DATE.getIdentifier());
        assertNull(childList.get(0).getValue());
        
        // some date
        instance = new Document();
        Long expResult = 1359201715760L;
        instance.setLastModifyDate(new Date(expResult));
        rootNode = instance.getRoot();
        childList = rootNode.getChildren();
        assertNotNull(childList);
        assertEquals(childList.size(), 1);
        assertEquals(childList.get(0).getKey(), Document.Metadata.LAST_MODIFY_DATE.getIdentifier());
        assertEquals(childList.get(0).getValue(), expResult.toString());
    }

    /**
     * Test of getDocumentType method, of class Document.
     */
    @Test
    public void testGetDocumentType() {
        System.out.println("getDocumentType");
        
        // no type set
        Document instance = new Document();
        DocumentType expResult = null;
        DocumentType result = instance.getDocumentType();
        assertEquals(result, expResult);
        
        // BOOK type
        instance = new Document();
        Node rootNode = instance.getRoot();
        Node node = new Node(Document.Metadata.DOCUMENT_TYPE.getIdentifier(), DocumentType.BOOK.name());
        rootNode.addChild(node);
        expResult = DocumentType.BOOK;
        result = instance.getDocumentType();
        assertEquals(result, expResult);
    }

    /**
     * Test of setDocumentType method, of class Document.
     */
    @Test
    public void testSetDocumentType() {
        System.out.println("setDocumentType");
        
        // null type
        Document instance = new Document();
        instance.setDocumentType(null);
        Node rootNode = instance.getRoot();
        List<Node> childList = rootNode.getChildren();
        assertNotNull(childList);
        assertEquals(childList.size(), 1);
        assertEquals(childList.get(0).getKey(), Document.Metadata.DOCUMENT_TYPE.getIdentifier());
        assertNull(childList.get(0).getValue());
        
        // Project type
        instance = new Document();
        instance.setDocumentType(DocumentType.PROJECT);
        rootNode = instance.getRoot();
        childList = rootNode.getChildren();
        assertNotNull(childList);
        assertEquals(childList.size(), 1);
        assertEquals(childList.get(0).getKey(), Document.Metadata.DOCUMENT_TYPE.getIdentifier());
        assertEquals(childList.get(0).getValue(), DocumentType.PROJECT.name());
    }

    /**
     * Test of getValueListByKey method, of class Document.
     */
    @Test
    public void testGetValueListByKey() {
        System.out.println("getValueListByKey");
        
        // null key
        Document instance = new Document();
        List result = instance.getValueListByKey(null);
        assertNotNull(result);
        assertEquals(result.size(), 0);
        
        // empty key
        instance = new Document();
        result = instance.getValueListByKey("");
        assertNotNull(result);
        assertEquals(result.size(), 0);
        
        // no such key
        instance = new Document();
        result = instance.getValueListByKey("no such key");
        assertNotNull(result);
        assertEquals(result.size(), 0);
        
        // key is root node
        String value = "eins";
        instance = new Document();
        instance.getRoot().setValue(value);
        result = instance.getValueListByKey("root");
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), value);
        
        // key is only child
        value = "zwei";
        instance = new Document();
        Node rootNode = instance.getRoot();
        Node firstNode = new Node("key 1", value);
        rootNode.addChild(firstNode);
        result = instance.getValueListByKey("key 1");
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), value);
        
        // key is first child
        value = "drei";
        instance = new Document();
        rootNode = instance.getRoot();
        firstNode = new Node("key 1", value + "a");
        rootNode.addChild(firstNode);
        Node secondNode = new Node("key 2", value + "b");
        rootNode.addChild(secondNode);
        result = instance.getValueListByKey("key 1");
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), value + "a");
        
        // key is second child
        value = "vier";
        instance = new Document();
        rootNode = instance.getRoot();
        firstNode = new Node("key 1", value + "a");
        rootNode.addChild(firstNode);
        secondNode = new Node("key 2", value + "b");
        rootNode.addChild(secondNode);
        result = instance.getValueListByKey("key 2");
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), value + "b");
        
        // one deep key path
        value = "fünf";
        instance = new Document();
        rootNode = instance.getRoot();
        firstNode = new Node("key 1", value + "1");
        rootNode.addChild(firstNode);
        secondNode = new Node("key 2", value + "2");
        firstNode.addChild(secondNode);
        Node thirdNode = new Node("key 3", value + "3");
        secondNode.addChild(thirdNode);
        result = instance.getValueListByKey("key 3");
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), value + "3");
        
        // two deep key paths
        value = "sechs";
        instance = new Document();
        rootNode = instance.getRoot();
        firstNode = new Node("key 1", value + "1a");
        rootNode.addChild(firstNode);
        secondNode = new Node("key 2", value + "2a");
        firstNode.addChild(secondNode);
        thirdNode = new Node("key 3", value + "3a");
        secondNode.addChild(thirdNode);
        firstNode = new Node("key 2", value + "1b");
        rootNode.addChild(firstNode);
        secondNode = new Node("key 1", value + "2b");
        firstNode.addChild(secondNode);
        thirdNode = new Node("key 3", value + "3b");
        secondNode.addChild(thirdNode);
        result = instance.getValueListByKey("key 3");
        assertNotNull(result);
        assertEquals(result.size(), 2);
        assertEquals(result.get(0), value + "3a");
        assertEquals(result.get(1), value + "3b");
    }

    /**
     * Test of getFirstValueByKey method, of class Document.
     */
    @Test
    public void testGetFirstValueByKey() {
        System.out.println("getFirstValueByKey");
        
        // null key
        String key = null;
        Document instance = new Document();
        String expResult = null;
        String result = instance.getFirstValueByKey(key);
        assertEquals(result, expResult);
        
        // empty key
        key = "";
        instance = new Document();
        expResult = null;
        result = instance.getFirstValueByKey(key);
        assertEquals(result, expResult);
        
        // non-existing key
        key = "not a key";
        instance = new Document();
        expResult = null;
        result = instance.getFirstValueByKey(key);
        assertEquals(result, expResult);
        
        // key is root node
        instance = new Document();
        String value = "eins";
        Node rootNode = instance.getRoot();
        rootNode.setValue(value);
        result = instance.getFirstValueByKey(Document.ROOT_KEY_PREFIX);
        assertEquals(result, value);
        
        // key is only child
        instance = new Document();
        value = "zwei";
        rootNode = instance.getRoot();
        Node node = new Node(firstKey, value);
        rootNode.addChild(node);
        result = instance.getFirstValueByKey(firstKey);
        assertEquals(result, value);
        
        // key is first child
        instance = new Document();
        value = "drei";
        rootNode = instance.getRoot();
        node = new Node(firstKey, value + "a");
        rootNode.addChild(node);
        node = new Node(secondKey, value + "b");
        rootNode.addChild(node);
        result = instance.getFirstValueByKey(firstKey);
        assertEquals(result, value + "a");
        
        // key is second child
        instance = new Document();
        value = "vier";
        rootNode = instance.getRoot();
        node = new Node(firstKey, value + "a");
        rootNode.addChild(node);
        node = new Node(secondKey, value + "b");
        rootNode.addChild(node);
        result = instance.getFirstValueByKey(secondKey);
        assertEquals(result, value + "b");
        
        // one deep key path
        instance = new Document();
        value = "fünf";
        rootNode = instance.getRoot();
        Node firstNode = new Node(firstKey);
        rootNode.addChild(firstNode);
        Node secondNode = new Node(secondKey);
        firstNode.addChild(secondNode);
        Node thirdNode = new Node(thirdKey, value);
        secondNode.addChild(thirdNode);
        result = instance.getFirstValueByKey(thirdKey);
        assertEquals(result, value);
        
        // two deep key paths
        instance = new Document();
        value = "sechs";
        rootNode = instance.getRoot();
        firstNode = new Node(firstKey);
        rootNode.addChild(firstNode);
        secondNode = new Node(secondKey);
        firstNode.addChild(secondNode);
        thirdNode = new Node(thirdKey, value + "a");
        secondNode.addChild(thirdNode);
        firstNode = new Node(secondKey);
        rootNode.addChild(firstNode);
        secondNode = new Node(firstKey);
        firstNode.addChild(secondNode);
        thirdNode = new Node(thirdKey, value + "b");
        secondNode.addChild(thirdNode);
        result = instance.getFirstValueByKey(thirdKey);
        assertEquals(result, value + "a");
    }

    /**
     * Test of getValueListByKeyPath method, of class Document.
     */
    @Test
    public void testGetValueListByKeyPath() {
        System.out.println("getValueListByKeyPath");
        
        // null key path
        String keyPath = null;
        Document instance = new Document();
        List<String> result = instance.getValueListByKeyPath(keyPath);
        assertNotNull(result);
        assertEquals(result.size(), 0);
        
        // empty key path
        keyPath = "";
        instance = new Document();
        result = instance.getValueListByKeyPath(keyPath);
        assertNotNull(result);
        assertEquals(result.size(), 0);
        
        // one shallow key path
        keyPath = firstKey;
        instance = new Document();
        String value = "eins";
        Node rootNode = instance.getRoot();
        Node node = new Node(firstKey, value);
        rootNode.addChild(node);
        result = instance.getValueListByKeyPath(keyPath);
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), value);
        
        // two shallow key paths
        keyPath = firstKey;
        instance = new Document();
        value = "eins";
        rootNode = instance.getRoot();
        node = new Node(firstKey, value + "1");
        rootNode.addChild(node);
        node = new Node(firstKey, value + "2");
        rootNode.addChild(node);
        result = instance.getValueListByKeyPath(keyPath);
        assertNotNull(result);
        assertEquals(result.size(), 2);
        assertEquals(result.get(0), value + "1");
        assertEquals(result.get(1), value + "2");
        
        // one deep key path
        keyPath = firstKey + "." + secondKey + "." + thirdKey;
        instance = new Document();
        value = "eins";
        rootNode = instance.getRoot();
        Node firstNode = new Node(firstKey);
        rootNode.addChild(firstNode);
        Node secondNode = new Node(secondKey);
        firstNode.addChild(secondNode);
        Node thirdNode = new Node(thirdKey, value);
        secondNode.addChild(thirdNode);
        result = instance.getValueListByKeyPath(keyPath);
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), value);
        
        // two deep key paths
        keyPath = firstKey + "." + secondKey + "." + thirdKey;
        instance = new Document();
        value = "eins";
        rootNode = instance.getRoot();
        firstNode = new Node(firstKey);
        rootNode.addChild(firstNode);
        secondNode = new Node(secondKey);
        firstNode.addChild(secondNode);
        thirdNode = new Node(thirdKey, value);
        secondNode.addChild(thirdNode);
        thirdNode = new Node(thirdKey, value + "1");
        secondNode.addChild(thirdNode);
        result = instance.getValueListByKeyPath(keyPath);
        assertNotNull(result);
        assertEquals(result.size(), 2);
        assertEquals(result.get(0), value);
        assertEquals(result.get(1), value + "1");
    }

    /**
     * Test of getFirstValueByKeyPath method, of class Document.
     */
    @Test
    public void testGetFirstValueByKeyPath() {
        System.out.println("getFirstValueByKeyPath");
        
        // null key path
        String keyPath = null;
        Document instance = new Document();
        String expResult = null;
        String result = instance.getFirstValueByKeyPath(keyPath);
        assertEquals(result, expResult);
        
        // empty key path
        keyPath = "";
        instance = new Document();
        expResult = null;
        result = instance.getFirstValueByKeyPath(keyPath);
        assertEquals(result, expResult);
        
        // one shallow key path
        keyPath = firstKey;
        instance = new Document();
        String value = "eins";
        Node rootNode = instance.getRoot();
        Node node = new Node(firstKey, value);
        rootNode.addChild(node);
        result = instance.getFirstValueByKeyPath(keyPath);
        assertEquals(result, value);
        
        // two shallow key paths
        keyPath = firstKey;
        instance = new Document();
        value = "eins";
        rootNode = instance.getRoot();
        node = new Node(firstKey, value);
        rootNode.addChild(node);
        node = new Node(firstKey, "zwei");
        rootNode.addChild(node);
        result = instance.getFirstValueByKeyPath(keyPath);
        assertEquals(result, value);
        
        // one deep key path
        keyPath = firstKey + "." + secondKey + "." + thirdKey;
        instance = new Document();
        value = "eins";
        rootNode = instance.getRoot();
        Node firstNode = new Node(firstKey);
        rootNode.addChild(firstNode);
        Node secondNode = new Node(secondKey);
        firstNode.addChild(secondNode);
        Node thirdNode = new Node(thirdKey, value);
        secondNode.addChild(thirdNode);
        result = instance.getFirstValueByKeyPath(keyPath);
        assertEquals(result, value);
        
        // two deep key paths
        keyPath = firstKey + "." + secondKey + "." + thirdKey;
        instance = new Document();
        value = "eins";
        rootNode = instance.getRoot();
        firstNode = new Node(firstKey);
        rootNode.addChild(firstNode);
        secondNode = new Node(secondKey);
        firstNode.addChild(secondNode);
        thirdNode = new Node(thirdKey, value);
        secondNode.addChild(thirdNode);
        thirdNode = new Node(thirdKey, value + "a");
        secondNode.addChild(thirdNode);
        result = instance.getFirstValueByKeyPath(keyPath);
        assertEquals(result, value);
    }

    /**
     * Test of toString method, of class Document.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Document instance = new Document();
        String expResult = "";
        String result = instance.toString();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashCode method, of class Document.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Document instance = new Document();
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class Document.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        Document instance = new Document();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setData method, of class Document.
     */
    @Test
    public void testSetData() {
        System.out.println("setData");
        
        // no such key path, create new node
        String keyPath = firstKey + "." + secondKey + "." + thirdKey;
        Object value = "Französich - Englisch - Deutsch";
        Document instance = new Document();
        instance.setData(keyPath, value);
        Node dataNode = instance.getDataNode();
        List<Node> nodeList = dataNode.getChildNodeListByKeyPath(keyPath);
        assertEquals(nodeList.size(), 1);
        assertEquals(nodeList.get(0).getKey(), thirdKey);
        assertEquals(nodeList.get(0).getValue(), value);
        
        // more than one such key path, create new node
        instance = new Document();
        dataNode = instance.getDataNode();
        Node firstNode = new Node(firstKey);
        dataNode.addChild(firstNode);
        Node secondNode = new Node(secondKey);
        firstNode.addChild(secondNode);
        Node thirdNode = new Node(thirdKey, value + "a");
        secondNode.addChild(thirdNode);
        thirdNode = new Node(thirdKey, value + "b");
        secondNode.addChild(thirdNode);
        instance.setData(keyPath, value);
        nodeList = dataNode.getChildNodeListByKeyPath(keyPath);
        assertEquals(nodeList.size(), 3);
        assertEquals(nodeList.get(2).getKey(), thirdKey);
        assertEquals(nodeList.get(2).getValue(), value);
        
        // one such key path, update it's value
        instance = new Document();
        dataNode = instance.getDataNode();
        firstNode = new Node(firstKey);
        dataNode.addChild(firstNode);
        secondNode = new Node(secondKey);
        firstNode.addChild(secondNode);
        thirdNode = new Node(thirdKey, value + "a");
        secondNode.addChild(thirdNode);
        instance.setData(keyPath, value);
        nodeList = dataNode.getChildNodeListByKeyPath(keyPath);
        assertEquals(nodeList.size(), 1);
        assertEquals(nodeList.get(0).getKey(), thirdKey);
        assertEquals(nodeList.get(0).getValue(), value);
    }

    /**
     * Test of getMetadata method, of class Document.
     */
    @Test
    public void testGetMetadata() {
        System.out.println("getMetadata");
        
        Metadata metadata = Metadata.ID;
        Document instance = new Document();
        instance.setMetadata(metadata, id);
        Object expResult = id;
        Object result = instance.getMetadata(metadata);
        assertEquals(result, expResult);
    }

    /**
     * Test of setMetadata method, of class Document.
     */
    @Test
    public void testSetMetadata() {
        System.out.println("setMetadata");
        
        Metadata metadata = Metadata.ID;
        Document instance = new Document();
        instance.setMetadata(metadata, id);
        
        Node rootNode = instance.getRoot();
        List<Node> rootChildren = rootNode.getChildren();
        
        Node idChild = null;
        for (Node child : rootChildren) {
            if (metadata.getIdentifier().equals(child.getKey())) {
                if (idChild != null) {
                    fail("found more than one metadata node");
                } else {
                    idChild = child;
                }
            }
        }
        
        assertNotNull(idChild);
        assertEquals(idChild.getKey(), metadata.getIdentifier());
        assertEquals(idChild.getValue(), id.toString());
    }

    /**
     * Test of toJson method, of class Document.
     */
    @Test
    public void testToJson() {
        System.out.println("toJson");
        
        Document instance = new Document();
        instance.setMetadata(Metadata.ID, 123);
        instance.setMetadata(Metadata.CREATE_DATE, new Date(946684800000L)); //01.01.2000 00:00:00
        instance.setMetadata(Metadata.LAST_MODIFY_DATE, new Date(946684801000L)); //01.01.2000 00:00:01
        instance.setMetadata(Metadata.SERIALIZATION_VERSION, 2);
        instance.setMetadata(Metadata.DOCUMENT_TYPE, DocumentType.PROJECT);
        
        String result = "{" + instance.toJson() + "}";
        String expResult = "{\"Document\": {\"Node\": {\"key\": \"root\",\"value\": \"\",\"children\": [{\"Node\": {\"key\": \"id\",\"value\": \"123\",\"children\": []}},{\"Node\": {\"key\": \"createDate\",\"value\": \"946684800000\",\"children\": []}},{\"Node\": {\"key\": \"lastModifyDate\",\"value\": \"946684801000\",\"children\": []}},{\"Node\": {\"key\": \"serializationVersion\",\"value\": \"2\",\"children\": []}},{\"Node\": {\"key\": \"documentType\",\"value\": \"PROJECT\",\"children\": []}}]}}}";
        
        assertEquals(result, expResult);
    }

}
