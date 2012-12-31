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
        Document document = null;
        Document instance = new Document();
        instance.init(document);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of init method, of class Document.
     */
    @Test
    public void testInit_6args() {
        System.out.println("init");
        Long id = null;
        Integer serializationVersion = null;
        Node root = null;
        Date createDate = null;
        Date lastModifyDate = null;
        DocumentType documentType = null;
        Document instance = new Document();
        instance.init(id, serializationVersion, root, createDate, lastModifyDate, documentType);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getId method, of class Document.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        Document instance = new Document();
        Long expResult = null;
        Long result = instance.getId();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setId method, of class Document.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        Long id = null;
        Document instance = new Document();
        instance.setId(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRoot method, of class Document.
     */
    @Test
    public void testGetRoot() {
        System.out.println("getRoot");
        Document instance = new Document();
        Node expResult = null;
        Node result = instance.getRoot();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setRoot method, of class Document.
     */
    @Test
    public void testSetRoot() {
        System.out.println("setRoot");
        Node root = null;
        Document instance = new Document();
        instance.setRoot(root);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSerializationVersion method, of class Document.
     */
    @Test
    public void testGetSerializationVersion() {
        System.out.println("getSerializationVersion");
        Document instance = new Document();
        Integer expResult = null;
        Integer result = instance.getSerializationVersion();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSerializationVersion method, of class Document.
     */
    @Test
    public void testSetSerializationVersion() {
        System.out.println("setSerializationVersion");
        Integer serializationVersion = null;
        Document instance = new Document();
        instance.setSerializationVersion(serializationVersion);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCreateDate method, of class Document.
     */
    @Test
    public void testGetCreateDate() {
        System.out.println("getCreateDate");
        Document instance = new Document();
        Date expResult = null;
        Date result = instance.getCreateDate();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCreateDate method, of class Document.
     */
    @Test
    public void testSetCreateDate() {
        System.out.println("setCreateDate");
        Date createDate = null;
        Document instance = new Document();
        instance.setCreateDate(createDate);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLastModifyDate method, of class Document.
     */
    @Test
    public void testGetLastModifyDate() {
        System.out.println("getLastModifyDate");
        Document instance = new Document();
        Date expResult = null;
        Date result = instance.getLastModifyDate();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLastModifyDate method, of class Document.
     */
    @Test
    public void testSetLastModifyDate() {
        System.out.println("setLastModifyDate");
        Date lastModifyDate = null;
        Document instance = new Document();
        instance.setLastModifyDate(lastModifyDate);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDocumentType method, of class Document.
     */
    @Test
    public void testGetDocumentType() {
        System.out.println("getDocumentType");
        Document instance = new Document();
        DocumentType expResult = null;
        DocumentType result = instance.getDocumentType();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setDocumentType method, of class Document.
     */
    @Test
    public void testSetDocumentType() {
        System.out.println("setDocumentType");
        DocumentType documentType = null;
        Document instance = new Document();
        instance.setDocumentType(documentType);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getValueListByKey method, of class Document.
     */
    @Test
    public void testGetValueListByKey() {
        System.out.println("getValueListByKey");
        String key = "";
        Document instance = new Document();
        List expResult = null;
        List result = instance.getValueListByKey(key);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFirstValueByKey method, of class Document.
     */
    @Test
    public void testGetFirstValueByKey() {
        System.out.println("getFirstValueByKey");
        String key = "";
        Document instance = new Document();
        String expResult = "";
        String result = instance.getFirstValueByKey(key);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getValueListByKeyPath method, of class Document.
     */
    @Test
    public void testGetValueListByKeyPath() {
        System.out.println("getValueListByKeyPath");
        String keyPath = "";
        Document instance = new Document();
        List expResult = null;
        List result = instance.getValueListByKeyPath(keyPath);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFirstValueByKeyPath method, of class Document.
     */
    @Test
    public void testGetFirstValueByKeyPath() {
        System.out.println("getFirstValueByKeyPath");
        String keyPath = "";
        Document instance = new Document();
        String expResult = "";
        String result = instance.getFirstValueByKeyPath(keyPath);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMetadata method, of class Document.
     */
    @Test
    public void testGetMetadata_DocumentMetadata() {
        System.out.println("getMetadata");
        Metadata metadata = null;
        Document instance = new Document();
        Object expResult = null;
        Object result = instance.getMetadata(metadata);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMetadata method, of class Document.
     */
    @Test
    public void testSetMetadata_DocumentMetadata_Object() {
        System.out.println("setMetadata");
        
        Metadata metadata = Metadata.SERIALIZATION_VERSION;
        Integer value = 10;
        Document instance = new Document();
        instance.setMetadata(metadata, value);
        
        assertEquals(value, instance.getMetadata(metadata));
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
        String keyPath = "";
        Object value = null;
        Document instance = new Document();
        instance.setData(keyPath, value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMetadata method, of class Document.
     */
    @Test
    public void testGetMetadata() {
        System.out.println("getMetadata");
        Metadata metadata = null;
        Document instance = new Document();
        Object expResult = null;
        Object result = instance.getMetadata(metadata);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMetadata method, of class Document.
     */
    @Test
    public void testSetMetadata() {
        System.out.println("setMetadata");
        Metadata metadata = null;
        Object value = null;
        Document instance = new Document();
        instance.setMetadata(metadata, value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
