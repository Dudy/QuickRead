package de.podolak.quickread.data;

import de.podolak.quickread.data.datastore.Document;
import de.podolak.quickread.data.datastore.DocumentType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Dude
 */
public class Song extends Document {

    public static final String KEY_SONG_TITLE  = "title";
    public static final String KEY_SONG_ARTIST = "artist";
    
    public Song() {
        setDocumentType(DocumentType.SONG);
    }
    
    public String getTitle() {
        return getFirstValueByKey(KEY_SONG_TITLE);
    }
    
    public void setTitle(String title) {
        setData(KEY_SONG_TITLE, title);
    }

    public String getArtist() {
        return getFirstValueByKey(KEY_SONG_ARTIST);
    }
    
    public void setArtist(String artist) {
        setData(KEY_SONG_ARTIST, artist);
    }

    @Override
    public String getCaption() {
        return getArtist() + " - " + getTitle();
    }
    
    @Override
    public List<String> getAttributeStringList() {
        List<String> attributeStringList = super.getAttributeStringList();
        
        attributeStringList.add("title");
        attributeStringList.add("artist");
        
        return attributeStringList;
    }
}