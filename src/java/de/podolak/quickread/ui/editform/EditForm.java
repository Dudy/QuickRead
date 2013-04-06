package de.podolak.quickread.ui.editform;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.podolak.quickread.QuickReadApplication;
import de.podolak.quickread.data.datastore.DocumentType;
import static de.podolak.quickread.data.datastore.DocumentType.BOOK;
import static de.podolak.quickread.data.datastore.DocumentType.COMMON;
import static de.podolak.quickread.data.datastore.DocumentType.PROJECT;
import static de.podolak.quickread.data.datastore.DocumentType.SONG;

@SuppressWarnings("serial")
public class EditForm extends Form {

    final Button save = new Button("Save");
    final Button cancel = new Button("Cancel");
    final Button edit = new Button("Edit");

    private final QuickReadApplication app;
    private DocumentType documentType;

    public EditForm(final QuickReadApplication app) {
        this.app = app;
        
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setLayout(layout);

        setWriteThrough(false);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.setMargin(true);
        footer.addComponent(save);
        footer.addComponent(cancel);
        footer.addComponent(edit);
        footer.setVisible(false);
        setFooter(footer);
    }

    @Override
    public void setItemDataSource(Item newDataSource) {
        if (newDataSource != null) {
            super.setItemDataSource(newDataSource);
            documentType = (DocumentType) newDataSource.getItemProperty("documenttype").getValue();
            setFormFieldFactory(EditFormHandler.getFormfieldFactory(this, documentType));
            setClickListener(EditFormHandler.getClickListener(this, documentType));
            setReadOnly(true);
            getFooter().setVisible(true);
        } else {
            super.setItemDataSource(null);
            getFooter().setVisible(false);
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        
        switch (documentType) {
            case BOOK:
            case SONG:
                save.setVisible(false);
                cancel.setVisible(false);
                edit.setVisible(false);
                break;
            case PROJECT:
            case COMMON:
                save.setVisible(!readOnly);
                cancel.setVisible(!readOnly);
                edit.setVisible(readOnly);
                break;
            default:
                throw new AssertionError();
        }
    }

    /**
     * @return the app
     */
    public QuickReadApplication getApp() {
        return app;
    }

    private void setClickListener(ClickListener clickListener) {
        removeListener(ClickListener.class, save);
        removeListener(ClickListener.class, edit);
        removeListener(ClickListener.class, cancel);
        
        save.addListener((ClickListener)clickListener);
        edit.addListener((ClickListener)clickListener);
        cancel.addListener((ClickListener)clickListener);
    }
    
}
