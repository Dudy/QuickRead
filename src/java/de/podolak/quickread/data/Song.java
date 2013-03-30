package de.podolak.quickread.data;

import de.podolak.quickread.Utilities;
import de.podolak.quickread.data.datastore.Document;
import de.podolak.quickread.data.datastore.DocumentType;
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
        String title = getFirstData(KEY_SONG_TITLE);
        
        if (title == null) {
            title = Utilities.getI18NText("data.newSong.title");
        }
        
        return title;
    }
    
    public void setTitle(String title) {
        setData(KEY_SONG_TITLE, title);
    }

    public String getArtist() {
        String artist = getFirstData(KEY_SONG_ARTIST);
        
        if (artist == null) {
            artist = Utilities.getI18NText("data.newSong.artist");
        }
        
        return artist;
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
