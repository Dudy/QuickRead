package de.podolak.quickread.data;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.podolak.quickread.data.datastore.Document;
import de.podolak.quickread.data.datastore.DocumentPersistence;
import de.podolak.quickread.data.datastore.DocumentType;
import de.podolak.quickread.data.datastore.Node;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dude
 */
public class JsonLoader {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Document loadDocument(String jsonStringObject) {
        return new Document(loadNode(jsonStringObject));
    }
    
    public static Node loadNode(String jsonStringObject) {
        Node node = null;

        try {
            node = mapper.readValue(jsonStringObject, Node.class);
        } catch (JsonParseException ex) {
            Logger.getLogger(JsonLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JsonMappingException ex) {
            Logger.getLogger(JsonLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JsonLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return node;
    }
    
    public static void storeDocument(Document document, String filename) {
        if (document != null) {
            storeNode(document.getRoot(), filename);
        }
    }

    public static void storeNode(Node node, String filename) {
        try {
            mapper.writeValue(new File(filename), node);
        } catch (JsonGenerationException ex) {
            Logger.getLogger(JsonLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JsonMappingException ex) {
            Logger.getLogger(JsonLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JsonLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    // ///// Test
    // ////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        createDataStructure();
        Document document = loadDocument("d:/temp/08/project1.json");
        System.out.println(document.toJson());
    }

    public static void createDataStructure() {
        Project project = new Project();
        project.setId(1L);
        project.setSerializationVersion(3);
        project.setCreateDate(new Date());
        project.setLastModifyDate(new Date());
        project.setDocumentType(DocumentType.PROJECT);
        project.addData("documentID", 2);
        project.addData("documentID", 3);
        storeDocument(project, "d:/temp/08/project1.json");
        
        Book book = new Book();
        book.setId(2L);
        book.setSerializationVersion(3);
        book.setCreateDate(new Date());
        book.setLastModifyDate(new Date());
        book.setDocumentType(DocumentType.BOOK);
        book.setAuthor("Dirk Podolak");
        book.setTitle("the joy of programming");
        book.addData("introduction", "Dirk Podolak talks about the joy of programming.");
        book.addData("contents", "Start here to explore why Dirk Podolak thinks it is a joy to write computer programs.");
        book.addData("index", "lookup table of some important terms");
        book.addData("contents.chapter 1", "todo: summary of chapter 1");
        book.addData("contents.chapter 2", "todo: summary of chapter 2");
        book.addData("contents.chapter 3", "todo: summary of chapter 3");
        storeDocument(book, "d:/temp/08/book1.json");
        
        book = new Book();
        book.setId(3L);
        book.setSerializationVersion(3);
        book.setCreateDate(new Date());
        book.setLastModifyDate(new Date());
        book.setDocumentType(DocumentType.BOOK);
        book.setAuthor("Dirk Podolak");
        book.setTitle("the pain of programming");
        book.addData("introduction", "Dirk Podolak talks about the pain of programming.");
        book.addData("contents", "Start here to explore why Dirk Podolak thinks it is a pain to write computer programs.");
        book.addData("index", "lookup table of some important terms");
        book.addData("contents.chapter 1", "todo: summary of chapter 1");
        book.addData("contents.chapter 2", "todo: summary of chapter 2");
        book.addData("contents.chapter 3", "todo: summary of chapter 3");
        storeDocument(book, "d:/temp/08/book2.json");
        
        project = new Project();
        project.setId(4L);
        project.setSerializationVersion(3);
        project.setCreateDate(new Date());
        project.setLastModifyDate(new Date());
        project.setDocumentType(DocumentType.PROJECT);
        project.addData("documentID", 6);
        project.addData("documentID", 3);
        project.addData("documentID", 5);
        storeDocument(project, "d:/temp/08/project2.json");
        
        Song song = new Song();
        song.setId(5L);
        song.setSerializationVersion(3);
        song.setCreateDate(new Date());
        song.setLastModifyDate(new Date());
        song.setDocumentType(DocumentType.SONG);
        song.setTitle("Phuture Paradise");
        song.setArtist("Dirk Podolak");
        song.addData("text", "the future ... is paradise (30 times)");
        song.addData("music", "three instruments");
        song.addData("music.percussion", "dumm de dumm ... blizzzzzz (15 times)");
        song.addData("music.triangle", "dschingggg dschingggg (60 times)");
        song.addData("music.piano", "pling pling plong plang pling (22 times)");
        storeDocument(song, "d:/temp/08/song1.json");
        
        book = new Book();
        book.setId(6L);
        book.setSerializationVersion(3);
        book.setCreateDate(new Date());
        book.setLastModifyDate(new Date());
        book.setDocumentType(DocumentType.BOOK);
        book.setAuthor("Dirk Podolak");
        book.setTitle("Music for the People");
        book.addData("introduction", "Dirk Podolak talks about how music can reach peoples hearts easier than words.");
        book.addData("contents", "Start here to explore how Dirk Podolak thinks music can change the future.");
        book.addData("index", "lookup table of some important terms");
        book.addData("contents.chapter 1", "todo: summary of chapter 1");
        book.addData("contents.chapter 2", "todo: summary of chapter 2");
        book.addData("contents.chapter 3", "todo: summary of chapter 3");
        book.addData("contents.chapter 4", "todo: summary of chapter 4");
        book.addData("contents.chapter 5", "todo: summary of chapter 5");
        storeDocument(book, "d:/temp/08/book3.json");
    }
}
