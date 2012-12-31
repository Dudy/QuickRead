package de.podolak.quickread.data.datastore;

import de.podolak.quickread.data.datastore.serialization.DocumentHandler_v1;
import de.podolak.quickread.data.datastore.serialization.DocumentHandler_v2;
import de.podolak.quickread.data.datastore.serialization.DocumentHandler_v3;

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
            case 3:
                return DocumentHandler_v3.serialize(document);
            default:
                throw new AssertionError("unknown serialization version '" + document.getSerializationVersion() + "'");
        }
    }
    
    public static Document deserialize(String inputDocument) {
        Integer version = getSerializationVersion(inputDocument);
        
        if (version == null) {
            version = 1;
        }
        
        switch (version) {
            case 1:
                return DocumentHandler_v1.deserialize(inputDocument);
            case 2:
                return DocumentHandler_v2.deserialize(inputDocument);
            case 3:
                return DocumentHandler_v3.deserialize(inputDocument);
            default:
                throw new AssertionError("unknown serialization version '" + version+ "'");
        }
    }
    
    private static Integer getSerializationVersion(String inputDocument) {
        Integer version = null;

        if (inputDocument != null) {
            version = getSerializationVersionByMarkers(inputDocument, " serializationVersion=\n", "\"");
            
            if (version == null) {
                version = getSerializationVersionByMarkers(inputDocument, "\"key\":\"serializationVersion\",\"value\":\"", "\",\"children\"");
            }
        }
        
        return version;
    }
    
    private static Integer getSerializationVersionByMarkers(String input, String startMarker, String endMarker) {
        Integer version = null;
        int startIndex;
        int endIndex;
        String versionString;
        
        if (input != null) {
            startIndex = input.indexOf(startMarker);
            if (startIndex >= 0) {
                endIndex = input.indexOf(endMarker, startIndex + startMarker.length());
                if (endIndex > startIndex) {
                    versionString = input.substring(startIndex + startMarker.length(), endIndex);
                    if (versionString != null && !versionString.isEmpty()) {
                        try {
                            version = Integer.parseInt(versionString);
                        } catch (NumberFormatException e) {
                            // noop
                        }
                    }
                }
            }
        }
        
        return version;
    }
    
}
