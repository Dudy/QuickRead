package de.podolak.quickread.data.persistence;

import de.podolak.quickread.data.Node;

/**
 *
 * @author Dude
 */
public class SimpleDocumentWriter {

    public static de.podolak.quickread.data.persistence.ejb.Document write(de.podolak.quickread.data.Document document) {
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        
        appendDocument(sb, document);
        
        de.podolak.quickread.data.persistence.ejb.Document newDoc = new de.podolak.quickread.data.persistence.ejb.Document();
        newDoc.setId(document.getId());
        newDoc.setContent(sb.toString());
        
        return newDoc;
    }
    
    private static void appendDocument(StringBuilder stringBuilder, de.podolak.quickread.data.Document document) {
        stringBuilder
                .append("<document")
                
                .append(" id=\"").append(getFormattedId(document.getId())).append("\"")
                .append(" createDate=\"").append(document.getCreateDate().getTime()).append("\"")
                .append(" lastModifyDate=\"").append(document.getLastModifyDate().getTime()).append("\"")
                
                .append(">");
        
        appendNode(stringBuilder, document.getRoot());
        stringBuilder.append("</document>");
    }
    
    private static void appendNode(StringBuilder stringBuilder, Node node) {
        // add opening tag
        stringBuilder.append("<node title=\"");
        if (node.getTitle() != null) {
            stringBuilder.append(node.getTitle());
        }
        stringBuilder.append("\">");
        
        // add text if present
        if (node.getText() != null && !node.getText().trim().isEmpty()) {
            stringBuilder.append("<text>");
            stringBuilder.append(node.getText());
            stringBuilder.append("</text>");
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
}
