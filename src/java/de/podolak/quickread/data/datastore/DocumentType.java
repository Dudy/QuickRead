package de.podolak.quickread.data.datastore;

import de.podolak.quickread.data.Book;
import de.podolak.quickread.data.Project;
import de.podolak.quickread.data.Song;

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

    public Document getInstanceOf(Document document) {
        Document newDocument = getInstanceOf();
        newDocument.init(document);
        return newDocument;
    }
    
}
