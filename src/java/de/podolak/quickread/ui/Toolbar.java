package de.podolak.quickread.ui;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.podolak.quickread.QuickReadApplication;
import de.podolak.quickread.Utilities;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dude
 */
public class Toolbar extends HorizontalLayout {

    private Button addNode = new Button(Utilities.getI18NText("action.addNode.button.caption"));
    private Button removeNode = new Button(Utilities.getI18NText("action.removeNode.button.caption"));
    private Button search = new Button(Utilities.getI18NText("action.search.button.caption"));
    private Button help = new Button(Utilities.getI18NText("action.help.button.caption"));
    private AuthenticationInfo authenticationInfo;
    
    private QuickReadApplication application;
    
    public Toolbar(QuickReadApplication application) {
        this.application = application;
        initcomponents();
    }

    private void initcomponents() {
        addComponent(addNode);
        addComponent(removeNode);

        addComponent(search);
        addComponent(help);

        addNode.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                application.addNode();
            }
        });
        
        removeNode.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                application.removeNode();
            }
        });

        search.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                // noop
            }
        });
        help.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                application.showHelpWindow();
            }
        });

        addNode.setIcon(new ThemeResource("icons/32x32/actions/edit_add.png"));
        removeNode.setIcon(new ThemeResource("icons/32x32/actions/editdelete.png"));

        addNode.addStyleName("multiline");
        removeNode.addStyleName("multiline");

        search.setIcon(new ThemeResource("icons/32x32/actions/find.png"));
        help.setIcon(new ThemeResource("icons/32x32/actions/contents.png"));

        setMargin(true);
        setSpacing(true);
        setStyleName("toolbar");
        setWidth("100%");
        
        VerticalLayout vl = new VerticalLayout();

        // user handling
        authenticationInfo = new AuthenticationInfo(application);
        vl.addComponent(authenticationInfo);
        vl.setComponentAlignment(authenticationInfo, Alignment.TOP_RIGHT);
        
        // Quickread image
        Embedded em = new Embedded("", new ThemeResource("images/Quickread06_height64.png"));
        vl.addComponent(em);
        vl.setComponentAlignment(em, Alignment.MIDDLE_RIGHT);
        
        addComponent(vl);
        setComponentAlignment(vl, Alignment.TOP_RIGHT);
        setExpandRatio(vl, 1);
    }

    public void update(HttpServletRequest request) {
        if (authenticationInfo != null) {
            authenticationInfo.update(request);
        }
    }
    
}
