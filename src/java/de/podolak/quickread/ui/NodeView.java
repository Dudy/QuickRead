package de.podolak.quickread.ui;

import de.podolak.quickread.ui.editform.EditForm;
import com.vaadin.ui.VerticalSplitPanel;

@SuppressWarnings("serial")
public class NodeView extends VerticalSplitPanel {

    public NodeView(EditForm nodeForm) {
        addStyleName("view");
        addComponent(nodeForm);
    }
    
}