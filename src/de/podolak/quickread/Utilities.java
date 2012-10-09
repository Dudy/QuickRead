package de.podolak.quickread;

/**
 *
 * @author Dude
 */
public class Utilities {

    public static String getI18NText(String key) {
        return java.util.ResourceBundle.getBundle("de/podolak/quickread/i18n/LanguageBundle").getString(key);
    }
    
}
