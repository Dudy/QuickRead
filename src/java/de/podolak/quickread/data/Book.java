package de.podolak.quickread.data;

import de.podolak.quickread.data.datastore.Document;
import de.podolak.quickread.data.datastore.DocumentType;

/**
 *
 * @author Dude
 */
public class Book extends Document {
    
    public static final String KEY_BOOK_TITLE  = "title";
    public static final String KEY_BOOK_AUTHOR = "author";
    
    public Book() {
        setDocumentType(DocumentType.BOOK);
    }
    
    public String getAuthor() {
        return getFirstValueByKey(KEY_BOOK_AUTHOR);
    }
    
    public void setAuthor(String author) {
        setData(KEY_BOOK_AUTHOR, author);
    }
    
    public String getTitle() {
        return getFirstValueByKey(KEY_BOOK_TITLE);
    }
    
    public void setTitle(String title) {
        setData(KEY_BOOK_TITLE, title);
    }
    
    @Override
    public String getCaption() {
        return getAuthor() + " - " + getTitle();
    }
}
