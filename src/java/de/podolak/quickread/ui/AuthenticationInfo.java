package de.podolak.quickread.ui;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.vaadin.Application;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import de.podolak.quickread.Utilities;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dude
 */
public class AuthenticationInfo extends HorizontalLayout {

    private Application application;
    
    public AuthenticationInfo(Application application) {
        this.application = application;
        setSpacing(true);
    }
    
    public void update(HttpServletRequest request) {
        Principal user = request.getUserPrincipal();

        if (application != null && getComponentIterator().hasNext() && (
                    (user != null && application.getUser() != null) ||
                    (user == null && application.getUser() == null))) {
            return; // Layout initialized, and no login/logout detected
        }

        UserService userService = UserServiceFactory.getUserService();
        Link link;
        Label username;
        
        if (request.getUserPrincipal() != null) {
            // Login detected, store username to application
            application.setUser(request.getUserPrincipal().getName());

            username = new Label(request.getUserPrincipal().getName());
            username.addStyleName("authenticationLabel");
            link = new Link(Utilities.getI18NText("action.logout.button.caption"),
                    new ExternalResource(userService.createLogoutURL(request.getRequestURI())));
            link.addStyleName("authenticationLabel");
        } else {
            // Logout detected, or application initialized without login info
            application.setUser(null);

            username = new Label(Utilities.getI18NText("authentication.noUser.caption"));
            username.addStyleName("authenticationLabel");
            link = new Link(Utilities.getI18NText("action.login.button.caption"),
                    new ExternalResource(userService.createLoginURL(request.getRequestURI())));
            link.addStyleName("authenticationLabel");
        }

        removeAllComponents();
        addComponent(username);
        addComponent(link);
    }
    
}
