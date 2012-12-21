package de.podolak.quickread.data;

import de.podolak.quickread.data.datastore.Document;

/**
 *
 * @author Dude
 */
public class Project extends Document {

    public String getName() {
        String name = getFirstValueByKey("name");
        
        //TODO: check for null and always (empty is necessary) return non null string? 
        
        return name;
    }

}
