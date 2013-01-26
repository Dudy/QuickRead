package de.podolak.quickread.ui.wizard;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;
import de.podolak.quickread.QuickReadApplication;
import de.podolak.quickread.Utilities;
import de.podolak.quickread.data.datastore.Document;
import de.podolak.quickread.data.datastore.DocumentType;
import java.util.Arrays;

/**
 *
 * @author Dude
 */
@SuppressWarnings("serial")
public class NewDocumentWindow extends Window implements Property.ValueChangeListener {

    private static final String COMMON_FIELD_WIDTH = "25em";
    private QuickReadApplication app;
    private ComboBox documentTypeCombobox;
    private Document document;
    private BeanItem<Document> documentItem;
    private Form documentForm;

    public NewDocumentWindow(final QuickReadApplication app) {
        this.app = app;

        setWidth("500px");
        setHeight("300px");
        
        showDocumentForm();
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
        if (documentTypeCombobox.getValue() instanceof DocumentType) {
            DocumentType selectedDocumentType = (DocumentType) documentTypeCombobox.getValue();
            document = selectedDocumentType.getInstanceOf(document);
            documentItem = new BeanItem<Document>(document);
            documentForm.setItemDataSource(documentItem);
            documentForm.setVisibleItemProperties(document.getAttributeStringList());
        }
    }
    
    private void showDocumentForm() {
        document = new Document();
        documentItem = new BeanItem<Document>(document);

        // Create the Form
        documentForm = new Form();

        documentForm.setCaption(Utilities.getI18NText("document.new"));
        documentForm.setWriteThrough(true);
        documentForm.setImmediate(true);

        // FieldFactory for customizing the fields and adding validators
        documentForm.setFormFieldFactory(new DocumentFieldFactory(this));
        documentForm.setItemDataSource(documentItem); // bind to POJO via BeanItem

        // Determines which properties are shown, and in which order:
        documentForm.setVisibleItemProperties(document.getAttributeStringList());

        // Add form to layout
        addComponent(documentForm);

        // The cancel / apply buttons
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        Button discardChanges = new Button(Utilities.getI18NText("action.cancel"),
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        documentForm.discard();
                        app.closeNewDocumentWindow(null);
                    }
                });
        buttons.addComponent(discardChanges);
        buttons.setComponentAlignment(discardChanges, Alignment.MIDDLE_LEFT);

        Button apply = new Button(Utilities.getI18NText("action.create"), new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    documentForm.commit();
                    app.closeNewDocumentWindow(document);
                } catch (Exception e) {
                    // Ignored, we'll let the Form handle the errors
                }
            }
        });
        buttons.addComponent(apply);
        documentForm.getFooter().addComponent(buttons);
        documentForm.getFooter().setMargin(false, false, true, true);
    }
    
    private class DocumentFieldFactory extends DefaultFieldFactory {

        protected NewDocumentWindow window;
        
        public DocumentFieldFactory(NewDocumentWindow window) {
            this.window = window;
        }

        @Override
        public Field createField(Item item, Object propertyId, Component uiContext) {
            Field f;
            
            if ("documentType".equals(propertyId)) {
                return getDocumentTypeCombobox();
            } else {
                f = super.createField(item, propertyId, uiContext);
            }

            return f;
        }
    }
    
    private ComboBox getDocumentTypeCombobox() {
        if (documentTypeCombobox == null) {
            documentTypeCombobox = new ComboBox(Utilities.getI18NText("action.newDocument.documentType"));
            BeanItemContainer<DocumentType> documentTypeContainer = new BeanItemContainer<DocumentType>(DocumentType.class);

            documentTypeContainer.addAll(Arrays.asList(DocumentType.values()));
            documentTypeCombobox.setWidth(COMMON_FIELD_WIDTH);
            documentTypeCombobox.setContainerDataSource(documentTypeContainer);
            documentTypeCombobox.addListener(this);
        }
        
        return documentTypeCombobox;
    }
}
