package de.podolak.quickread.ui;

import com.vaadin.ui.VerticalSplitPanel;

@SuppressWarnings("serial")
public class NodeView extends VerticalSplitPanel {

    public NodeView(NodeForm nodeForm) {
        addStyleName("view");
        addComponent(nodeForm);
    }
    
}