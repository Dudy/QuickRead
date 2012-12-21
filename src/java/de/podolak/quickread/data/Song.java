package de.podolak.quickread.data;

import de.podolak.quickread.data.datastore.Document;

/**
 *
 * @author Dude
 */
public class Song extends Document {

    public String getArtist() {
        String author = getFirstValueByKey("artist");
        
        //TODO: check for null and always (empty is necessary) return non null string? 
        
        return author;
    }
    
    public String getTitle() {
        String title = getFirstValueByKey("title");
        
        //TODO: check for null and always (empty is necessary) return non null string? 
        
        return title;
    }
    
}
