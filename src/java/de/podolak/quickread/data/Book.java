package de.podolak.quickread.data;

import de.podolak.quickread.Utilities;
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
    }
    
    public final String getAuthor() {
        String author = getFirstData(KEY_BOOK_AUTHOR);
        
        if (author == null) {
            author = Utilities.getI18NText("data.newBook.author");
        }
        
        return author;
    }
    
    public final void setAuthor(String author) {
        setData(KEY_BOOK_AUTHOR, author);
    }
    
    public final String getTitle() {
        String title = getFirstData(KEY_BOOK_TITLE);
        
        if (title == null) {
            title = Utilities.getI18NText("data.newBook.title");
        }
        
        return title;
    }
    
    public final void setTitle(String title) {
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
