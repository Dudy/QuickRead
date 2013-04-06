package de.podolak.quickread.ui;

import com.google.appengine.api.users.UserServiceFactory;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import de.podolak.quickread.QuickReadApplication;
import de.podolak.quickread.Utilities;
import de.podolak.quickread.data.Project;
import de.podolak.quickread.data.datastore.Document;
import de.podolak.quickread.data.datastore.DocumentType;
import static de.podolak.quickread.data.datastore.DocumentType.BOOK;
import static de.podolak.quickread.data.datastore.DocumentType.COMMON;
import static de.podolak.quickread.data.datastore.DocumentType.PROJECT;
import static de.podolak.quickread.data.datastore.DocumentType.SONG;
import de.podolak.quickread.data.datastore.Node;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class OldNodeForm extends Form implements ClickListener {
    
    private static final Logger LOGGER = Logger.getLogger(OldNodeForm.class.getName());

    private final Button save = new Button("Save", (ClickListener) this);
    private final Button cancel = new Button("Cancel", (ClickListener) this);
    private final Button edit = new Button("Edit", (ClickListener) this);
    private final Label label = new Label("this is not editable, edit child nodes instead"); // TODO
    private final QuickReadApplication app;
    private DocumentType documentType;
    private boolean isAttribute;

    public OldNodeForm(final QuickReadApplication app) {
        this.app = app;

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setLayout(layout);

        setWriteThrough(false);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.setMargin(true);
        footer.addComponent(label);
        footer.addComponent(save);
        footer.addComponent(cancel);
        footer.addComponent(edit);
        footer.setVisible(false);

        setFooter(footer);

        setFormFieldFactory(new DefaultFieldFactory() {
            @Override
            public Field createField(Item item, Object propertyId, Component uiContext) {
                Field field = null;

                switch (documentType) {
                    case PROJECT:
                        if ("title".equals(propertyId)) {
                            field = new TextField(Utilities.getI18NText("project.name"));
                            field.setWidth("100%");
                            field.addStyleName("edit-title");
                        }
                        break;
                    case BOOK:
                        break;
                    case SONG:
                        break;
                    case COMMON:
                        if ("text".equals(propertyId)) {
                            field = new TextArea(Utilities.getI18NText("edit.text"));
                            field.setWidth("100%");
                            field.setHeight("15em");
                            field.addStyleName("edit-text");
                        } else if ("title".equals(propertyId)) {
                            field = new TextField(Utilities.getI18NText("edit.key"));
                            field.setWidth("100%");
                            field.addStyleName("edit-key");
                            field.setVisible(!isAttribute);
                        }
                        break;
                    default:
                        throw new AssertionError();
                }

                return field;
            }
        });
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Button source = event.getButton();
        Item itemDataSource = getItemDataSource();

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

            switch (documentType) {
                case PROJECT:
                    Project project = (Project) itemDataSource.getItemProperty("object").getValue();
                    project.setName(itemDataSource.getItemProperty("title").getValue().toString());
                    break;
                case BOOK:
                    break;
                case SONG:
                    break;
                case COMMON:
                    Node node = (Node) itemDataSource.getItemProperty("object").getValue();
                    if (!isAttribute) {
                        node.setKey(itemDataSource.getItemProperty("title").getValue().toString());
                    }
                    node.setValue(itemDataSource.getItemProperty("text").getValue().toString());
                    break;
                default:
                    throw new AssertionError();
            }

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
            LOGGER.log(Level.FINE, "newDataSource is not null but {0}", newDataSource);
            isAttribute = false;
            documentType = (DocumentType) newDataSource.getItemProperty("documenttype").getValue();
            LOGGER.log(Level.FINE, "DocumentType of newDataSource is {0}", documentType);

            Object thisObject = newDataSource.getItemProperty("object").getValue();
            Object thisItemId = newDataSource.getItemProperty("itemId").getValue();
            Object parentItemId = ((HierarchicalContainer) app.getDataContainer()).getParent(thisItemId);

            if (parentItemId != null) {
                LOGGER.log(Level.FINE, "parentItemId of newDataSource is {0}", parentItemId);
                Item parentItem = ((HierarchicalContainer) app.getDataContainer()).getItem(parentItemId);
                Object parentObject = parentItem.getItemProperty("object").getValue();

                if (thisObject instanceof Node) {
                    Node node = (Node) thisObject;
                    LOGGER.log(Level.FINE, "object of newDataSource is of type node");
                    String key = node.getKey();
                    if (parentObject instanceof Document) {
                        LOGGER.log(Level.FINE, "parentObject of newDataSource is of type {0}", parentObject.getClass());
                        for (String attribute : ((Document) parentObject).getAttributeStringList()) {
                            if (attribute.equals(key)) {
                                isAttribute = true;
                                LOGGER.log(Level.FINE, "parent object's attribute {0} equals {1}", new Object[] { attribute, key });
                                break;
                            } else {
                                LOGGER.log(Level.FINER, "parent object's attribute {0} does not equal {1}", new Object[] { attribute, key });
                            }
                        }
                    } else {
                        if (parentObject == null) {
                            LOGGER.log(Level.FINE, "parentObject of newDataSource is null");
                        } else {
                            LOGGER.log(Level.FINE, "parentObject of newDataSource is not of type Document but of type {0}", parentObject.getClass());
                        }
                    }
                } else {
                    if (thisObject == null) {
                        LOGGER.log(Level.FINE, "object of newDataSource is null");
                    } else {
                        LOGGER.log(Level.FINE, "object of newDataSource is not of type Node but of type {0}", thisObject.getClass());
                    }
                }
            } else {
                LOGGER.log(Level.FINE, "parentItemId of newDataSource is null");
            }

            super.setItemDataSource(newDataSource);

            setReadOnly(true);
            getFooter().setVisible(true);
        } else {
            LOGGER.log(Level.FINE, "newDataSource is null");
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
                label.setVisible(true);
                break;
            case PROJECT:
            case COMMON:
                save.setVisible(!readOnly);
                cancel.setVisible(!readOnly);
                edit.setVisible(readOnly);
                label.setVisible(false);
                break;
            default:
                throw new AssertionError();
        }
    }
}