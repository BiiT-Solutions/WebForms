package com.biit.webforms.gui.webpages;

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

import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.components.WebPageComponent;
import com.biit.webforms.gui.common.language.CommonComponentsLanguageCodes;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.exceptions.SessionHasAlreadyUser;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class Login extends WebPageComponent {
	private static final long serialVersionUID = 1559169232291159835L;
	private static String FIELD_SIZE = "160px";
	private VerticalLayout rootLayout;
	private TextField usernameField;
	private PasswordField passwordField;
	private static final String NAME_LOGIN_FIELD_ID = "userNameLoginForm";
	private static final String PASS_LOGIN_FIELD_ID = "userPassLoginForm";
	private static final String USER_PASS_LOGIN_BUTTON_ID = "loginButton";

	public Login() {
		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		setCompositionRoot(rootLayout);
		setSizeFull();

		VerticalLayout loginLayout = new VerticalLayout();
		loginLayout.setSizeUndefined();

		Panel loginPanel = buildLoginForm();
		Component loginLabel = createNameVersion();
		loginLayout.addComponent(loginPanel);
		loginLayout.addComponent(loginLabel);
		loginLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
		loginLayout.setComponentAlignment(loginLabel, Alignment.MIDDLE_CENTER);

		rootLayout.addComponent(loginLayout);
		rootLayout.setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER);
		usernameField.focus();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// // Nothing to do. Autologin managed by ApplicationUI.
		// ApplicationUi.autologin();
	}

	private Panel buildLoginForm() {

		Panel panel = new Panel();
		panel.setSizeUndefined();

		// Create input fields for user name and password
		usernameField = new TextField(ServerTranslate.translate(CommonComponentsLanguageCodes.LOGIN_CAPTION_EMAIL));
		usernameField.setRequired(true);
		usernameField.setRequiredError(ServerTranslate.translate(CommonComponentsLanguageCodes.LOGIN_ERROR_EMAIL));
		usernameField.setWidth(FIELD_SIZE);
		usernameField.setId(NAME_LOGIN_FIELD_ID);

		passwordField = new PasswordField(ServerTranslate.translate(CommonComponentsLanguageCodes.LOGIN_CAPTION_PASSWORD));
		passwordField.setRequired(true);
		passwordField.setWidth(FIELD_SIZE);
		passwordField.setRequiredError(ServerTranslate.translate(CommonComponentsLanguageCodes.LOGIN_ERROR_PASSWORD));
		passwordField.setId(PASS_LOGIN_FIELD_ID);

		// If you press enter. Login operation.
		passwordField.addShortcutListener(new ShortcutListener("Shortcut Name", ShortcutAction.KeyCode.ENTER, null) {
			private static final long serialVersionUID = -3780782610097189332L;

			@Override
			public void handleAction(Object sender, Object target) {
				// limit the enters to only from the password field from this
				// form
				if (target == passwordField) {
					loginAction();
				}
				// If write user name and press enter, go to pass field.
				if (target == usernameField) {
					passwordField.focus();
				}
			}
		});
		// Add the login button
		Button loginButton = new Button(ServerTranslate.translate(CommonComponentsLanguageCodes.LOGIN_CAPTION_SIGN_IN), new ClickListener() {
			private static final long serialVersionUID = 1239035599265918788L;

			@Override
			public void buttonClick(ClickEvent event) {
				loginAction();
			}
		});
		loginButton.setWidth(FIELD_SIZE);
		loginButton.setId(USER_PASS_LOGIN_BUTTON_ID);

		// Alignment and sizes.
		FormLayout layout = new FormLayout();
		layout.setMargin(true);
		layout.setSizeUndefined();
		layout.addComponent(usernameField);
		layout.addComponent(passwordField);
		layout.addComponent(loginButton);
		panel.setContent(layout);
		return panel;
	}

	protected void loginAction() {
		try {
			IUser<Long> user = UserSession.login((String) usernameField.getValue(), (String) passwordField.getValue());
			if (user != null) {
				ApplicationUi.navigateTo(WebMap.getMainPage());
			}
		} catch (InvalidCredentialsException | AuthenticationRequired | UserDoesNotExistException e) {
			passwordField.setComponentError(new UserError(ServerTranslate.translate(CommonComponentsLanguageCodes.LOGIN_ERROR_MESSAGE_MESSAGE_USER,
					new Object[] { (String) usernameField.getValue() })));
			MessageManager.showError(CommonComponentsLanguageCodes.LOGIN_ERROR_MESSAGE_MESSAGE_BADUSERPSWD,
					CommonComponentsLanguageCodes.LOGIN_ERROR_MESSAGE_TRYAGAIN);
		} catch (UserManagementException e) {
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
			MessageManager.showError(CommonComponentsLanguageCodes.LOGIN_ERROR_MESSAGE_USER_SERVICE, CommonComponentsLanguageCodes.LOGIN_ERROR_MESSAGE_CONTACT);
		} catch (SessionHasAlreadyUser e) {
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
			MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
			ApplicationUi.navigateTo(WebMap.ERROR_PAGE);
		}
	}

	private String getVersion() {
		ServletContext context = VaadinServlet.getCurrent().getServletContext();
		Manifest manifest;
		String version = null;
		try {
			manifest = new Manifest(context.getResourceAsStream("/META-INF/MANIFEST.MF"));
			Attributes attributes = manifest.getMainAttributes();
			version = attributes.getValue("Implementation-Version");
		} catch (IOException e) {
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		}
		return version;
	}

	private Component createNameVersion() {
		Label label = new Label(LanguageCodes.APPLICATION_NAME.translation() + " - v" + getVersion());
		label.setWidth(null);
		return label;
	}
}
