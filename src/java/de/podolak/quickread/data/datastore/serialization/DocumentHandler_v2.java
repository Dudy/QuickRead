package de.podolak.quickread.data.datastore.serialization;

import de.podolak.quickread.data.datastore.Document;
import de.podolak.quickread.data.datastore.DocumentType;
import de.podolak.quickread.data.datastore.Node;
import java.io.StringReader;
import java.util.Date;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;

/**
 *
 * @author Dude
 */
public class DocumentHandler_v2 {
    
    // This version differs from v1 in the storage mechanism for node value.
    // While v1 stored the node value in a <value> subtag this v2 stores the
    // value within another node tag attribute "value".
    
    public static final Integer VERSION = 2;
    
    // <editor-fold defaultstate="collapsed" desc=" deserialization ">
    private static Document document;
    private static Stack<Node> nodeStack;
    
    public static Document deserialize(String content) {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            org.w3c.dom.Document inputDocument = builder.parse(new InputSource(new StringReader(content)));
            DocumentHandler_v2.nodeStack = new Stack<Node>();
            org.w3c.dom.Element element = inputDocument.getDocumentElement();
            visitElement_document(element);
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            Logger.getLogger(DocumentHandler_v2.class.getName()).log(Level.SEVERE, "Fehler: kann keinen Parser erzeugen", e);
        } catch (org.xml.sax.SAXException e) {
            Logger.getLogger(DocumentHandler_v2.class.getName()).log(Level.SEVERE, "Fehler: kann das document nicht parsen", e);
        } catch (java.io.IOException e) {
            Logger.getLogger(DocumentHandler_v2.class.getName()).log(Level.SEVERE, "Fehler: kann das document nicht öffnen", e);
        }
        
        return document;
    }
    
    /**
     * Scan through org.w3c.dom.Element named document.
     */
    private static void visitElement_document(org.w3c.dom.Element element) {
        Long id = null;
        Date createDate = null;
        Date lastModifyDate = null;
        DocumentType documentType = null;
        
        // <document>
        org.w3c.dom.NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            org.w3c.dom.Attr attr = (org.w3c.dom.Attr) attrs.item(i);
            if (attr.getName().equals("id")) {
                id = Long.parseLong(attr.getValue());
            } else if (attr.getName().equals("createDate")) {
                createDate = new Date(Long.parseLong(attr.getValue()));
            } else if (attr.getName().equals("lastModifyDate")) {
                lastModifyDate = new Date(Long.parseLong(attr.getValue()));
            } else if (attr.getName().equals("documentType")) {
                documentType = DocumentType.valueOf(attr.getValue());
                document = documentType.getInstanceOf();
            }
        }
        
        document.setId(id);
        document.setSerializationVersion(VERSION);
        document.setCreateDate(createDate);
        document.setLastModifyDate(lastModifyDate);
        document.setDocumentType(documentType);
        
        org.w3c.dom.NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            org.w3c.dom.Node node = nodes.item(i);
            switch (node.getNodeType()) {
                case org.w3c.dom.Node.CDATA_SECTION_NODE:
                    // ((org.w3c.dom.CDATASection)node).getData();
                    break;
                case org.w3c.dom.Node.ELEMENT_NODE:
                    org.w3c.dom.Element nodeElement = (org.w3c.dom.Element) node;
                    if (nodeElement.getTagName().equals("node")) {
                        visitElement_node(nodeElement);
                    }
                    break;
                case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                    // ((org.w3c.dom.ProcessingInstruction)node).getTarget();
                    // ((org.w3c.dom.ProcessingInstruction)node).getData();
                    break;
            }
        }
    }

    /**
     * Scan through org.w3c.dom.Element named node.
     */
    private static void visitElement_node(org.w3c.dom.Element element) {
        Node n = new Node();
        
        if (document.getRoot() == null) {
            document.setRoot(n); // set as document root, no parent node
        } else {
            nodeStack.peek().addChild(n); // add to parent's child list
        }
        
        nodeStack.push(n);
        
        // <node>
        // element.getValue();
        org.w3c.dom.NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            org.w3c.dom.Attr attr = (org.w3c.dom.Attr) attrs.item(i);
            if (attr.getName().equals("key")) {
                n.setKey(attr.getValue());
            } else if (attr.getName().equals("value")) {
                n.setValue(attr.getValue());
            }
        }
        org.w3c.dom.NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            org.w3c.dom.Node node = nodes.item(i);
            switch (node.getNodeType()) {
                case org.w3c.dom.Node.CDATA_SECTION_NODE:
                    // ((org.w3c.dom.CDATASection)node).getData();
                    break;
                case org.w3c.dom.Node.ELEMENT_NODE:
                    org.w3c.dom.Element nodeElement = (org.w3c.dom.Element) node;
                    if (nodeElement.getTagName().equals("node")) {
                        visitElement_node(nodeElement);
                    }
                    break;
                case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                    // ((org.w3c.dom.ProcessingInstruction)node).getTarget();
                    // ((org.w3c.dom.ProcessingInstruction)node).getData();
                    break;
            }
        }
        
        nodeStack.pop();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" serialization ">
    public static String serialize(Document document) {
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        
        appendDocument(sb, document);
        
        return sb.toString();
    }
    
    private static void appendDocument(StringBuilder stringBuilder, de.podolak.quickread.data.datastore.Document document) {
        stringBuilder
                .append("<document")
                
                .append(" serializationVersion=\"").append(document.getSerializationVersion()).append("\"")
                .append(" id=\"").append(getFormattedId(document.getId())).append("\"")
                .append(" createDate=\"").append(document.getCreateDate().getTime()).append("\"")
                .append(" lastModifyDate=\"").append(document.getLastModifyDate().getTime()).append("\"")
                .append(" documentType=\"").append(document.getDocumentType().name()).append("\"")
                
                .append(">");
        
        appendNode(stringBuilder, document.getRoot());
        stringBuilder.append("</document>");
    }
    
    private static void appendNode(StringBuilder stringBuilder, Node node) {
        // add opening tag
        stringBuilder.append("<node key=\"");
        if (node.getKey() != null) {
            stringBuilder.append(node.getKey());
        }
        stringBuilder.append("\" value=\"");
        if (node.getValue() != null) {
            stringBuilder.append(node.getValue());
        }
        
        if (node.getChildren().isEmpty()) {
            // immediately close tag
            stringBuilder.append("\"/>");
        } else {
            // open tag and add closing tag later
            stringBuilder.append("\">");

            // add child nodes
            for (Node child : node.getChildren()) {
                appendNode(stringBuilder, child);
            }

            // add closing tag
            stringBuilder.append("</node>");
        }
    }
    
    private static String getFormattedId(Long id) {
        return id == null ? "" : id.toString();
    }
    // </editor-fold>
    
}
