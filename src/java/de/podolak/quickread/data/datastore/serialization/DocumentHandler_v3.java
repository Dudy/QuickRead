package de.podolak.quickread.data.datastore.serialization;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.podolak.quickread.data.JsonLoader;
import de.podolak.quickread.data.datastore.Document;
import de.podolak.quickread.data.datastore.Node;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dude
 */
public class DocumentHandler_v3 {
    
    // completely new version based on JSON and jackson
    
    public static final Integer VERSION = 3;
    private static final ObjectMapper mapper = new ObjectMapper();
    
    // <editor-fold defaultstate="collapsed" desc=" deserialization ">
    public static Document deserialize(String content) {
        // deserialize data
        Document document = new Document(deserializeNode(content));
        // cast to correct type
        document = document.getDocumentType().getInstanceOf(document);
        // return document of correct type
        return document;
    }
    
    private  static Node deserializeNode(String content) {
        Node node = null;

        try {
            node = mapper.readValue(content, Node.class);
        } catch (JsonParseException ex) {
            Logger.getLogger(JsonLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JsonMappingException ex) {
            Logger.getLogger(JsonLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JsonLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return node;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" serialization ">
    public static String serialize(Document document) {
        String content = null;
        
        if (document != null) {
            content = serializeNode(document.getRoot());
        }
        
        return content;
    }

    private static String serializeNode(Node node) {
        try {
            return mapper.writeValueAsString(node);
        } catch (JsonGenerationException ex) {
            Logger.getLogger(JsonLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JsonMappingException ex) {
            Logger.getLogger(JsonLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JsonLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    // </editor-fold>
    
}
