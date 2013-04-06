package de.podolak.quickread.ui.editform;

import com.vaadin.ui.Button;
import com.vaadin.ui.FormFieldFactory;
import de.podolak.quickread.data.datastore.DocumentType;
import static de.podolak.quickread.data.datastore.DocumentType.BOOK;
import static de.podolak.quickread.data.datastore.DocumentType.COMMON;
import static de.podolak.quickread.data.datastore.DocumentType.PROJECT;
import static de.podolak.quickread.data.datastore.DocumentType.SONG;

/**
 *
 * @author Dude
 */
public class EditFormHandler {

    public static FormFieldFactory getFormfieldFactory(final EditForm form, DocumentType documentType) {
        FormFieldFactory formFieldFactory = null;
        
        switch (documentType) {
            case PROJECT:
                formFieldFactory = ProjectHandler.getFormfieldFactory(form);
                break;
            case BOOK:
                formFieldFactory = BookHandler.getFormfieldFactory(form);
                break;
            case SONG:
                formFieldFactory = SongHandler.getFormfieldFactory(form);
                break;
            case COMMON:
                formFieldFactory = CommonHandler.getFormfieldFactory(form);
                break;
            default:
                throw new AssertionError();
        }

        return formFieldFactory;
    }

    public static Button.ClickListener getClickListener(EditForm form, DocumentType documentType) {
        Button.ClickListener clickListener = null;
        
        switch (documentType) {
            case PROJECT:
                clickListener = ProjectHandler.getClickListener(form);
                break;
            case BOOK:
                clickListener = BookHandler.getClickListener(form);
                break;
            case SONG:
                clickListener = SongHandler.getClickListener(form);
                break;
            case COMMON:
                clickListener = CommonHandler.getClickListener(form);
                break;
            default:
                throw new AssertionError();
        }

        return clickListener;
    }
}
