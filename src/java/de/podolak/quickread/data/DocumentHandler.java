package de.podolak.quickread.data;

import de.podolak.quickread.data.serialization.DocumentHandler_v1;
import de.podolak.quickread.data.serialization.DocumentHandler_v2;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 *
 * @author Dude
 */
public class DocumentHandler {
    
    public static String serialize(Document document) {
        switch (document.getSerializationVersion()) {
            case 1:
                return DocumentHandler_v1.serialize(document);
            case 2:
                return DocumentHandler_v2.serialize(document);
            default:
                throw new AssertionError("unknown serialization version '" + document.getSerializationVersion() + "'");
        }
    }
    
    public static Document deserialize(org.w3c.dom.Document inputDocument) {
        Integer version = getSerializationVersion(inputDocument);
        
        if (version == null) {
            version = 1;
        }
        
        switch (version) {
            case 1:
                return DocumentHandler_v1.deserialize(inputDocument);
            case 2:
                return DocumentHandler_v2.deserialize(inputDocument);
            default:
                throw new AssertionError("unknown serialization version '" + version+ "'");
        }
    }
    
    private static Integer getSerializationVersion(org.w3c.dom.Document inputDocument) {
        Integer version = null;
        NodeList documentElements = inputDocument.getElementsByTagName("document");
        
        if (documentElements.getLength() > 0) {
            NamedNodeMap attributes = documentElements.item(0).getAttributes();
            Attr namedItem = (Attr) attributes.getNamedItem("serializationVersion");
            
            if (namedItem != null) {
                version = Integer.parseInt(namedItem.getValue());
            }
        }
        
        return version;
    }
    
}
