package de.podolak.quickread.ui;

import com.vaadin.ui.VerticalSplitPanel;

@SuppressWarnings("serial")
public class OldNodeView extends VerticalSplitPanel {

    public OldNodeView(OldNodeForm nodeForm) {
        addStyleName("view");
        addComponent(nodeForm);
    }
    
}