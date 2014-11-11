package com.biit.webforms.gui.webpages;

import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.security.AuthenticationService;
import com.biit.liferay.security.exceptions.InvalidCredentialsException;
import com.biit.security.exceptions.PBKDF2EncryptorException;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.common.components.WebPageComponent;
import com.biit.webforms.gui.common.language.CommonComponentsLanguageCodes;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.liferay.portal.model.User;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class Login extends WebPageComponent {
	private static final long serialVersionUID = 1559169232291159835L;
	private static String FIELD_SIZE = "160px";
	private VerticalLayout rootLayout;
	private TextField usernameField;
	private PasswordField passwordField;

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
		// Nothing to do.
	}

	private Panel buildLoginForm() {

		Panel panel = new Panel();
		panel.setSizeUndefined();

		// Create input fields for user name and password
		usernameField = new TextField(ServerTranslate.translate(CommonComponentsLanguageCodes.LOGIN_CAPTION_EMAIL));
		usernameField.setRequired(true);
		usernameField.setRequiredError(ServerTranslate.translate(CommonComponentsLanguageCodes.LOGIN_ERROR_EMAIL));
		usernameField.setWidth(FIELD_SIZE);

		passwordField = new PasswordField(
				ServerTranslate.translate(CommonComponentsLanguageCodes.LOGIN_CAPTION_PASSWORD));
		passwordField.setRequired(true);
		passwordField.setWidth(FIELD_SIZE);
		passwordField.setRequiredError(ServerTranslate.translate(CommonComponentsLanguageCodes.LOGIN_ERROR_PASSWORD));

		// If you press enter. Login operation.
		passwordField.addShortcutListener(new ShortcutListener("Shortcut Name", ShortcutAction.KeyCode.ENTER, null) {
			private static final long serialVersionUID = -3780782610097189332L;

			@Override
			public void handleAction(Object sender, Object target) {
				// limit the enters to only from the password field from this
				// form
				if (target == passwordField) {
					checkUserAndPassword();
				}
				// If write user name and press enter, go to pass field.
				if (target == usernameField) {
					passwordField.focus();
				}
			}
		});

		// Add the login button
		Button loginButton = new Button(ServerTranslate.translate(CommonComponentsLanguageCodes.LOGIN_CAPTION_SIGN_IN),
				new ClickListener() {
					private static final long serialVersionUID = 1239035599265918788L;

					@Override
					public void buttonClick(ClickEvent event) {
						checkUserAndPassword();
					}
				});
		loginButton.setWidth(FIELD_SIZE);

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

	private void checkUserAndPassword() {
		// Try to log in the user when the button is clicked
		String userMail = (String) usernameField.getValue();
		String password = (String) passwordField.getValue();

		User user = null;
		try {
			user = AuthenticationService.getInstance().authenticate(userMail, password);
		} catch (InvalidCredentialsException | AuthenticationRequired e) {
			passwordField.setComponentError(new UserError(ServerTranslate.translate(
					CommonComponentsLanguageCodes.LOGIN_ERROR_MESSAGE_MESSAGE_USER, new Object[] { userMail })));
			MessageManager.showError(CommonComponentsLanguageCodes.LOGIN_ERROR_MESSAGE_MESSAGE_BADUSERPSWD,
					CommonComponentsLanguageCodes.LOGIN_ERROR_MESSAGE_USER_SERVICE);
		} catch (IOException | WebServiceAccessError | NotConnectedToWebServiceException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			MessageManager.showError(CommonComponentsLanguageCodes.LOGIN_ERROR_MESSAGE_USER_SERVICE,
					CommonComponentsLanguageCodes.LOGIN_ERROR_MESSAGE_CONTACT);
		} catch (PBKDF2EncryptorException e) {
			MessageManager.showError(CommonComponentsLanguageCodes.LOGIN_ERROR_MESSAGE_ENCRYPTINGPASSWORD,
					CommonComponentsLanguageCodes.LOGIN_ERROR_MESSAGE_CONTACT);
		}

		if (user != null) {
			WebBrowser browser = (WebBrowser) UI.getCurrent().getPage().getWebBrowser();
			try {
				String message = "User '" + user.getEmailAddress() + "' logged successfully. Using '"
						+ browser.getBrowserApplication() + "'";
				if (browser.getAddress() != null) {
					message += " (IP: " + browser.getAddress() + ").";
				} else {
					message += ".";
				}
				WebformsLogger.info(this.getClass().getName(), message);
			} catch (Exception e) {
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
			// Store the password.
			user.setPassword(password);

			// The user's password was correct, so set the user as the
			// current user (inlogged)
			UserSessionHandler.setUser(user);
			ApplicationUi.navigateTo(WebMap.getMainPage());
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
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
		return version;
	}
	
	private Component createNameVersion() {
		Label label = new Label(LanguageCodes.APPLICATION_NAME.translation()+" - v" + getVersion());
		label.setWidth(null);
		return label;
	}
}
