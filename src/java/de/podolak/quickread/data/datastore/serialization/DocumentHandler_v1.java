package de.podolak.quickread.data.datastore.serialization;

import de.podolak.quickread.data.datastore.Document;
import de.podolak.quickread.data.datastore.DocumentType;
import de.podolak.quickread.data.datastore.Node;
import java.util.Date;
import java.util.Stack;

/**
 *
 * @author Dude
 */
public class DocumentHandler_v1 {

    // this is the initial version
    
    public static final Integer VERSION = 1;
    
    // <editor-fold defaultstate="collapsed" desc=" deserialization ">
    private static Document document;
    private static Stack<Node> nodeStack;

    public static Document deserialize(org.w3c.dom.Document inputDocument) {
        DocumentHandler_v1.nodeStack = new Stack<Node>();
        org.w3c.dom.Element element = inputDocument.getDocumentElement();
        visitElement_document(element);
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
                    if (nodeElement.getTagName().equals("value")) {
                        visitElement_value(nodeElement);
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

    /**
     * Scan through org.w3c.dom.Element named text.
     */
    private static void visitElement_value(org.w3c.dom.Element element) {
        // <value>
        org.w3c.dom.NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            org.w3c.dom.Node node = nodes.item(i);
            switch (node.getNodeType()) {
                case org.w3c.dom.Node.CDATA_SECTION_NODE:
                    // ((org.w3c.dom.CDATASection)node).getData();
                    break;
                case org.w3c.dom.Node.ELEMENT_NODE:
                    // org.w3c.dom.Element nodeElement = (org.w3c.dom.Element) node;
                    break;
                case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                    // ((org.w3c.dom.ProcessingInstruction)node).getTarget();
                    // ((org.w3c.dom.ProcessingInstruction)node).getData();
                    break;
                case org.w3c.dom.Node.TEXT_NODE:
                    // ((org.w3c.dom.Text)node).getData();
                    
                    nodeStack.peek().setValue(((org.w3c.dom.Text)node).getData());
                    
                    break;
            }
        }
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
        stringBuilder.append("\">");
        
        // add text if present
        if (node.getValue() != null && !node.getValue().trim().isEmpty()) {
            stringBuilder.append("<value>");
            stringBuilder.append(node.getValue());
            stringBuilder.append("</value>");
        }
        
        // add child nodes if present
        for (Node child : node.getChildren()) {
            appendNode(stringBuilder, child);
        }
        
        // add closing tag
        stringBuilder.append("</node>");
    }
    
    private static String getFormattedId(Long id) {
        return id == null ? "" : id.toString();
    }
    // </editor-fold>
    
}
