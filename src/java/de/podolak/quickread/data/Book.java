package de.podolak.quickread.data;

import de.podolak.quickread.data.datastore.Document;
import de.podolak.quickread.data.datastore.DocumentType;
import java.util.List;

/**
 *
 * @author Dude
 */
public class Book extends Document {
    
    public static final String KEY_BOOK_TITLE  = "title";
    public static final String KEY_BOOK_AUTHOR = "author";
    public static final String[] KEYS = new String[] { KEY_BOOK_TITLE, KEY_BOOK_AUTHOR };
    
    public Book() {
        setDocumentType(DocumentType.BOOK);
        setAuthor("no author");
        setTitle("no title");
    }
    
    public String getAuthor() {
//        return getFirstValueByKey(KEY_BOOK_AUTHOR);
        return getFirstData(KEY_BOOK_AUTHOR);
    }
    
    public void setAuthor(String author) {
        setData(KEY_BOOK_AUTHOR, author);
    }
    
    public String getTitle() {
//        return getFirstValueByKey(KEY_BOOK_TITLE);
        return getFirstData(KEY_BOOK_TITLE);
    }
    
    public void setTitle(String title) {
        setData(KEY_BOOK_TITLE, title);
    }
    
    @Override
    public String getCaption() {
        return getAuthor() + " - " + getTitle();
    }
    
    @Override
    public List<String> getAttributeStringList() {
        List<String> attributeStringList = super.getAttributeStringList();
        
        attributeStringList.add("title");
        attributeStringList.add("author");
        
        return attributeStringList;
    }
}
