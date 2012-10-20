package de.podolak.quickread.ui;

import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Tree;
import de.podolak.quickread.QuickReadApplication;

@SuppressWarnings("serial")
public class NavigationTree extends Tree {
    
    public NavigationTree(QuickReadApplication app) {
        setContainerDataSource(app.getDataContainer());
        
        setItemCaptionPropertyId("title");
        setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);

        setImmediate(true);
        setSelectable(true);
        setMultiSelect(false);
        setNullSelectionAllowed(false);

        addListener((ItemClickListener) app);
        addListener((ValueChangeListener) app);
        addListener((ItemSetChangeListener) app);
        addListener((PropertySetChangeListener) app);
    }
    
    public Item getSelectedItem() {
        return getContainerDataSource().getItem(getValue());
    }
    
}
