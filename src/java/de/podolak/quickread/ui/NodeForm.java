package de.podolak.quickread.ui;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import de.podolak.quickread.QuickReadApplication;
import de.podolak.quickread.Utilities;
import de.podolak.quickread.data.DocumentContainer;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class NodeForm extends Form implements ClickListener {

    private Button save = new Button("Save", (ClickListener) this);
    private Button cancel = new Button("Cancel", (ClickListener) this);
    private Button edit = new Button("Edit", (ClickListener) this);

    private final QuickReadApplication app;
    private boolean newNodeMode = false;

    public NodeForm(final QuickReadApplication app) {
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

        setFormFieldFactory(new DefaultFieldFactory() {
            @Override
            public Field createField(Item item, Object propertyId, Component uiContext) {
                Field field;
                
                if ("text".equals(propertyId)) {
                    field = new TextArea(Utilities.getI18NText("edit.text"));
                    field.setWidth("100%");
                    field.setHeight("15em");
                    field.addStyleName("edit-text");
                } else if ("title".equals(propertyId)) {
                    field = new TextField(Utilities.getI18NText("edit.title"));
                    field.setWidth("100%");
                    field.addStyleName("edit-title");
                } else {
                    field = super.createField(item, propertyId, uiContext);
                    field.setWidth("100%");
                }
                
                return field;
            }
        });
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Button source = event.getButton();
        
        if (source == save) {
            commit();

            if (newNodeMode) {
//                Item addedItem = app.getDataSource().addItem(document);
//                setItemDataSource(addedItem);
//                newNodeMode = false;
                //TODO
            }
            setReadOnly(true);
            
            app.save();
        } else if (source == cancel) {
            if (newNodeMode) {
                newNodeMode = false;
                setItemDataSource(null);
            } else {
                discard();
            }
            setReadOnly(true);
        } else if (source == edit) {
            setReadOnly(false);
        }
    }

    @Override
    public void setItemDataSource(Item newDataSource) {
        newNodeMode = false;

        if (newDataSource != null) {
            List<Object> orderedProperties = Arrays.asList(DocumentContainer.NATURAL_COL_ORDER);
            super.setItemDataSource(newDataSource, orderedProperties);
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
        save.setVisible(!readOnly);
        cancel.setVisible(!readOnly);
        edit.setVisible(readOnly);
    }

}