package com.biit.webforms.gui.webpages.formmanager;

import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.security.IWebformsSecurityService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

public class WindowLoginKnowledgeManager extends WindowAcceptCancel {

    private static final String WINDOW_WIDTH = "400px";
    private static final String WINDOW_HEIGHT = "300px";
    private static final String USERNAME_LABEL_CONTENT = "Username:";
    private static final String PASSWORD_LABEL_CONTENT = "Password:";

    private IWebformsSecurityService webformsSecurityService;

    private TextField usernameField;
    private PasswordField passwordField;
    private Label usernameLabel;
    private Label passwordLabel;

    public WindowLoginKnowledgeManager() {
        SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
        webformsSecurityService = (IWebformsSecurityService) helper.getBean("webformsSecurityService");
        configure();
        setContent(generate());
    }

    private void configure() {
        setDraggable(true);
        setResizable(false);
        setModal(true);
        setWidth(WINDOW_WIDTH);
        setHeight(WINDOW_HEIGHT);
    }

    private Component generate() {
        VerticalLayout rootLayout = new VerticalLayout();
        usernameField = new TextField();
        passwordField = new PasswordField();
        usernameLabel = new Label(USERNAME_LABEL_CONTENT);
        passwordLabel = new Label(PASSWORD_LABEL_CONTENT);

        HorizontalLayout usernameLayout = new HorizontalLayout();
        usernameLayout.setSpacing(true);
        usernameLayout.addComponent(usernameLabel);
        usernameLayout.addComponent(usernameField);

        HorizontalLayout passwordLayout = new HorizontalLayout();
        passwordLayout.setSpacing(true);
        passwordLayout.addComponent(passwordLabel);
        passwordLayout.addComponent(passwordField);

        rootLayout.setSpacing(true);
        rootLayout.setSizeFull();

        rootLayout.addComponent(usernameLayout);
        rootLayout.addComponent(passwordLayout);

        return rootLayout;
    }

    @Override
    protected boolean acceptAction() {
        int result = ApplicationUi.getController().loginToKnowledgeManager(usernameField.getValue(), passwordField.getValue());
        if (result == 200) {
            return true;
        } else {
            if(result == 401) {
                MessageManager.showError("Bad username or password");
            } else {
                MessageManager.showError("Some rare error ocurred");
            }
        }
        return false;
    }
}
