package de.podolak.quickread.ui;

import com.google.appengine.api.users.UserServiceFactory;
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
import com.vaadin.ui.Window;
import de.podolak.quickread.QuickReadApplication;
import de.podolak.quickread.Utilities;
import de.podolak.quickread.data.datastore.Node;

@SuppressWarnings("serial")
public class NodeForm extends Form implements ClickListener {

    private Button save = new Button("Save", (ClickListener) this);
    private Button cancel = new Button("Cancel", (ClickListener) this);
    private Button edit = new Button("Edit", (ClickListener) this);

    private final QuickReadApplication app;

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
                Field field = null;
                
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
// omit oither fields (e.g. icon)
//                    field = super.createField(item, propertyId, uiContext);
//                    field.setWidth("100%");
                }
                
                return field;
            }
        });
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Button source = event.getButton();
        
        if (source == save) {
            if (!UserServiceFactory.getUserService().isUserLoggedIn()) {
                app.showMessage(
                        Utilities.getI18NText("authentication.noUser.caption"),
                        Utilities.getI18NText("authentication.save.description"),
                        Window.Notification.TYPE_WARNING_MESSAGE);
                return;
            }
            
            commit();
            setReadOnly(true);
            
            Item itemDataSource = getItemDataSource();
            Node node = (Node) itemDataSource.getItemProperty("node").getValue();
            node.setKey(itemDataSource.getItemProperty("title").getValue().toString());
            node.setValue(itemDataSource.getItemProperty("text").getValue().toString());
            
            app.save();
        } else if (source == cancel) {
            discard();
            setReadOnly(true);
        } else if (source == edit) {
            if (!UserServiceFactory.getUserService().isUserLoggedIn()) {
                app.showMessage(
                        Utilities.getI18NText("authentication.noUser.caption"),
                        Utilities.getI18NText("authentication.save.description"),
                        Window.Notification.TYPE_WARNING_MESSAGE);
                return;
            }
            
            setReadOnly(false);
        }
    }

    @Override
    public void setItemDataSource(Item newDataSource) {
        if (newDataSource != null) {
            super.setItemDataSource(newDataSource);
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