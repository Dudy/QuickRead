package de.podolak.quickread.data.serialization;

import de.podolak.quickread.data.Document;
import de.podolak.quickread.data.DocumentType;
import de.podolak.quickread.data.Node;
import java.util.Date;
import java.util.Stack;

/**
 *
 * @author Dude
 */
public class DocumentHandler_v2 {
    
    public static final Integer VERSION = 2;
    
    // <editor-fold defaultstate="collapsed" desc=" deserialization ">
    private static Document document;
    private static Stack<Node> nodeStack;

    public static Document deserialize(org.w3c.dom.Document inputDocument) {
        DocumentHandler_v2.nodeStack = new Stack<Node>();
        org.w3c.dom.Element element = inputDocument.getDocumentElement();
        visitElement_document(element);
        return document;
    }
    
    /**
     * Scan through org.w3c.dom.Element named document.
     */
    private static void visitElement_document(org.w3c.dom.Element element) {
        document = new Document();
        document.setSerializationVersion(VERSION);
        
        // <document>
        org.w3c.dom.NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            org.w3c.dom.Attr attr = (org.w3c.dom.Attr) attrs.item(i);
            if (attr.getName().equals("id")) {
                document.setId(Long.parseLong(attr.getValue()));
            } else if (attr.getName().equals("createDate")) {
                document.setCreateDate(new Date(Long.parseLong(attr.getValue())));
            } else if (attr.getName().equals("lastModifyDate")) {
                document.setLastModifyDate(new Date(Long.parseLong(attr.getValue())));
            } else if (attr.getName().equals("documentType")) {
                document.setDocumentType(DocumentType.valueOf(attr.getValue()));
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
                        visitElement_text(nodeElement);
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
    private static void visitElement_text(org.w3c.dom.Element element) {
        // <text>
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
    
    private static void appendDocument(StringBuilder stringBuilder, de.podolak.quickread.data.Document document) {
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
            stringBuilder.append("\"\\>");
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
