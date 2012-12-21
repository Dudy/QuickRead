package de.podolak.quickread.data;

import de.podolak.quickread.data.datastore.Document;

/**
 *
 * @author Dude
 */
public class Book extends Document {
    
    public String getAuthor() {
        String author = getFirstValueByKey("author");
        
        //TODO: check for null and always (empty is necessary) return non null string? 
        
        return author;
    }
    
    public String getName() {
        String name = getFirstValueByKey("name");
        
        //TODO: check for null and always (empty is necessary) return non null string? 
        
        return name;
    }
    
}
