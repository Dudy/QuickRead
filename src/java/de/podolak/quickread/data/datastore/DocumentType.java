package de.podolak.quickread.data.datastore;

import de.podolak.quickread.data.Book;
import de.podolak.quickread.data.Project;
import de.podolak.quickread.data.Song;
import java.util.List;

/**
 *
 * @author Dude
 */
public enum DocumentType {

    PROJECT,
    BOOK,
    SONG,
    COMMON;
    
    public Document getInstanceOf() {
        switch (this) {
            case PROJECT:
                return new Project();
            case BOOK:
                return new Book();
            case SONG:
                return new Song();
            case COMMON:
                return new Document();
            default:
                throw new AssertionError();
        }
    }

    /**
     * Returns a new instance of a <code>Document</code> or a descendant of it
     * (determined by this enumeration value) and initializes the <code>
     * Document</code> specific data from the given other <code>Document</code>.
     * This is in principal a cast of one type of document into another.
     * 
     * @param document the <code>Document</code> the fetch initial data from
     * @return a new instance of the type determined by this enumeration
     */
    public Document getInstanceOf(Document document) {
        Document newDocument = getInstanceOf();
        newDocument.init(document);
        return newDocument;
    }
    
    public List<String> getStringAttributeList() {
        return getInstanceOf().getAttributeStringList();
    }
    
}
