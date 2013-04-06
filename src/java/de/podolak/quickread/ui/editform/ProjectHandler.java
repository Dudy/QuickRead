package de.podolak.quickread.ui.editform;

import com.google.appengine.api.users.UserServiceFactory;
import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import de.podolak.quickread.Utilities;
import de.podolak.quickread.data.Project;
import de.podolak.quickread.data.datastore.DocumentType;
import static de.podolak.quickread.data.datastore.DocumentType.BOOK;
import static de.podolak.quickread.data.datastore.DocumentType.COMMON;
import static de.podolak.quickread.data.datastore.DocumentType.PROJECT;
import static de.podolak.quickread.data.datastore.DocumentType.SONG;
import de.podolak.quickread.data.datastore.Node;

/**
 *
 * @author Dude
 */
public class ProjectHandler {

    public static FormFieldFactory getFormfieldFactory(final EditForm form) {
        return new DefaultFieldFactory() {
            @Override
            public Field createField(Item item, Object propertyId, Component uiContext) {
                Field field = null;

                if ("title".equals(propertyId)) {
                    field = new TextField(Utilities.getI18NText("project.name"));
                    field.setWidth("100%");
                    field.addStyleName("edit-title");
                }

                return field;
            }
        };
    }
    
    public static Button.ClickListener getClickListener(final EditForm form) {
        return new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Button source = event.getButton();

                Item itemDataSource = form.getItemDataSource();
                DocumentType documentType = (DocumentType) itemDataSource.getItemProperty("documenttype").getValue();

                if (source == form.save) {
                    if (!UserServiceFactory.getUserService().isUserLoggedIn()) {
                        form.getApp().showMessage(
                                Utilities.getI18NText("authentication.noUser.caption"),
                                Utilities.getI18NText("authentication.save.description"),
                                Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }

                    form.commit();
                    form.setReadOnly(true);

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
                            node.setKey(itemDataSource.getItemProperty("title").getValue().toString());
                            node.setValue(itemDataSource.getItemProperty("text").getValue().toString());
                            break;
                        default:
                            throw new AssertionError();
                    }

                    form.getApp().save();
                } else if (source == form.cancel) {
                    form.discard();
                    form.setReadOnly(true);
                } else if (source == form.edit) {
                    if (!UserServiceFactory.getUserService().isUserLoggedIn()) {
                        form.getApp().showMessage(
                                Utilities.getI18NText("authentication.noUser.caption"),
                                Utilities.getI18NText("authentication.save.description"),
                                Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                }

                switch (documentType) {
                    case BOOK:
//                        label.setCaption(Utilities.getI18NText("book.captionNotEditable"));
                        form.setReadOnly(true);
//                        label.setVisible(true);
                        break;
                    case SONG:
//                        label.setCaption(Utilities.getI18NText("song.captionNotEditable"));
                        form.setReadOnly(true);
//                        label.setVisible(true);
                        break;
                    case PROJECT:
                    case COMMON:
                        form.setReadOnly(false);
//                        label.setVisible(false);
                        break;
                    default:
                        throw new AssertionError();
                }
            }
        };
    }
    
}
