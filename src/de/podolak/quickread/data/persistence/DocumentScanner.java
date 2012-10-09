package de.podolak.quickread.data.persistence;

import de.podolak.quickread.data.Document;
import de.podolak.quickread.data.Node;
import java.util.Stack;

/**
 *
 * @author Dude
 */
public class DocumentScanner {
    /**
     * org.w3c.dom.Document inputDocument
     */
    org.w3c.dom.Document inputDocument;
    
    private Document document;
    private Stack<Node> nodeStack;

    /**
     * Create new Book1Scanner with org.w3c.dom.Document.
     */
    public DocumentScanner(org.w3c.dom.Document document) {
        this.inputDocument = document;
        this.nodeStack = new Stack<Node>();
    }
    
    public Document getDocument() {
        return document;
    }
    
    /**
     * Scan through org.w3c.dom.Document inputDocument.
     */
    public void visitDocument() {
        org.w3c.dom.Element element = inputDocument.getDocumentElement();
        if ((element != null) && element.getTagName().equals("document")) {
            visitElement_document(element);
        }
        if ((element != null) && element.getTagName().equals("node")) {
            visitElement_node(element);
        }
        if ((element != null) && element.getTagName().equals("text")) {
            visitElement_text(element);
        }
    }

    /**
     * Scan through org.w3c.dom.Element named document.
     */
    void visitElement_document(org.w3c.dom.Element element) {
        document = new Document();
        
        // <document>
        // element.getValue();
        org.w3c.dom.NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            org.w3c.dom.Attr attr = (org.w3c.dom.Attr) attrs.item(i);
            if (attr.getName().equals("id")) {
                // <document id="???">
                // attr.getValue();
                document.setId(Integer.parseInt(attr.getValue()));
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
    void visitElement_node(org.w3c.dom.Element element) {
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
            if (attr.getName().equals("title")) {
                // <node title="???">
                // attr.getValue();
                n.setTitle(attr.getValue());
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
                    if (nodeElement.getTagName().equals("text")) {
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
    void visitElement_text(org.w3c.dom.Element element) {
        // <text>
        // element.getValue();
        org.w3c.dom.NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            org.w3c.dom.Node node = nodes.item(i);
            switch (node.getNodeType()) {
                case org.w3c.dom.Node.CDATA_SECTION_NODE:
                    // ((org.w3c.dom.CDATASection)node).getData();
                    break;
                case org.w3c.dom.Node.ELEMENT_NODE:
                    org.w3c.dom.Element nodeElement = (org.w3c.dom.Element) node;
                    break;
                case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                    // ((org.w3c.dom.ProcessingInstruction)node).getTarget();
                    // ((org.w3c.dom.ProcessingInstruction)node).getData();
                    break;
                case org.w3c.dom.Node.TEXT_NODE:
                    // ((org.w3c.dom.Text)node).getData();
                    
                    nodeStack.peek().setText(((org.w3c.dom.Text)node).getData());
                    
                    break;
            }
        }
    }
    
}
