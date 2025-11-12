package com.biit.webforms.gui.webpages.formmanager;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.webservice.rest.client.KnowledgeManagerService;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class WindowLoginKnowledgeManager extends WindowAcceptCancel {

    private static final String WINDOW_WIDTH = "400px";
    private static final String WINDOW_HEIGHT = "300px";
    private static final String USERNAME_LABEL_CONTENT = LanguageCodes.WINDOW_LOGIN_KNOWLEDGE_MANAGER_USERNAME_LABEL_CONTENT.translation();
    private static final String PASSWORD_LABEL_CONTENT = LanguageCodes.WINDOW_LOGIN_KNOWLEDGE_MANAGER_PASSWORD_LABEL_CONTENT.translation();
    private TextField usernameField;
    private PasswordField passwordField;
    private Label usernameLabel;
    private Label passwordLabel;

    private KnowledgeManagerService knowledgeManagerService;

    public WindowLoginKnowledgeManager() {
        configure();
        setContent(generate());
        SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
        knowledgeManagerService = (KnowledgeManagerService) helper.getBean("knowledgeManagerService");
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
        try {
            CloseableHttpResponse response = knowledgeManagerService.login(usernameField.getValue(), passwordField.getValue(), UserSession.getUser().getUniqueId());
            if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() <= 299) {
                return true;
            } else {
                if(response.getStatusLine().getStatusCode() >= 400 && response.getStatusLine().getStatusCode() <= 404) {
                    MessageManager.showError(LanguageCodes.CAPTION_BAD_LOGIN_KNOWLEDGE_MANAGER);
                } else {
                    MessageManager.showError(LanguageCodes.CAPTION_ERROR_LOGIN_KNOWLEDGE_MANAGER);
                }
                return false;
            }
        }catch (IOException e) {
            WebformsLogger.errorMessage(WindowLoginKnowledgeManager.class.toString(), e.toString());
            return false;
        }

    }
}
