package com.biit.webforms.gui;

import java.io.IOException;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.security.exceptions.InvalidCredentialsException;
import com.biit.security.exceptions.PBKDF2EncryptorException;
import com.biit.webforms.gui.common.language.CommonComponentsLanguageCodes;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.webpages.WebMap;
import com.biit.webforms.logger.WebformsLogger;
import com.liferay.portal.model.User;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * Main Ui class of application. Ui has been configured to preserve the Ui when a Refresh action has occurred. This way,
 * a UI is only discarded when the user is no longer active in a long time, instead of every time there is a refresh
 * event.
 */
@Push
@Theme("webforms")
@PreserveOnRefresh
public class ApplicationUi extends UI {
	public final static String USER_PARAMETER_TAG = "user";
	public final static String PASSWORD_PARAMETER_TAG = "password";
	private static final long serialVersionUID = -704009283476930001L;
	private Navigator navigator;
	private View currentView;
	private String userEmail;
	private String password;

	private class WebformsErrorHandler extends DefaultErrorHandler {
		private static final long serialVersionUID = -5570064834518413901L;

		@Override
		public void error(com.vaadin.server.ErrorEvent event) {
			// Throw the error to the logger.
			MessageManager.showError(CommonComponentsLanguageCodes.ERROR_UNEXPECTED_ERROR);
			WebformsLogger.errorMessage(ApplicationUi.class.getName(), event.getThrowable());
		}
	};

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("");

		WebformsLogger.debug(ApplicationUi.class.getName(), "UI initialized.");

		defineWebPages();
		UiAccesser.register(UserSessionHandler.getController());

		// Liferay send this data and automatically are used in the login screen.
		userEmail = request.getParameter(USER_PARAMETER_TAG);
		password = request.getParameter(PASSWORD_PARAMETER_TAG);

		setErrorHandler(new WebformsErrorHandler());
	}

	public static void autologin() {
		((ApplicationUi) ApplicationUi.getCurrent()).autologinImplementation();
	}

	private void autologinImplementation() {
		// When accessing from Liferay, user and password are already set.
		if (userEmail != null && userEmail.length() > 0 && password != null && password.length() > 0) {
			WebformsLogger.info(ApplicationUi.class.getName(), "Autologin with user '" + userEmail
					+ "' and password with length of " + password.length());
			try {
				User user = UserSessionHandler.getUser(userEmail, password);
				if (user != null) {
					// Try to go to the last page and last form if user has no logged out.
					if (UserSessionHandler.getUserLastPage(UserSessionHandler.getUser()) != null) {
						navigateTo(UserSessionHandler.getUserLastPage(UserSessionHandler.getUser()));
					} else {
						navigateTo(WebMap.getMainPage());
					}
				}
			} catch (InvalidCredentialsException | NotConnectedToWebServiceException | PBKDF2EncryptorException
					| IOException | AuthenticationRequired | WebServiceAccessError e) {
				WebformsLogger.info(ApplicationUi.class.getClass().getName(), "Autologin with user '" + userEmail
						+ "' failed! Wrong user or password.");
			}
		} else {
			if (userEmail != null && userEmail.length() > 0) {
				WebformsLogger.info(ApplicationUi.class.getClass().getName(), "Autologin with user '" + userEmail
						+ "' but no password provided!");
			} else {
				WebformsLogger.debug(this.getClass().getName(), "Autologin failed.");
			}
		}
	}

	@Override
	public void detach() {
		if (UserSessionHandler.getUser() != null) {
			// Log user ui expired.
			WebformsLogger.info(this.getClass().getName(), UserSessionHandler.getUser().getEmailAddress()
					+ " UI has expired.");
		} else {
			WebformsLogger.debug(this.getClass().getName(), " UI closed.");
		}
		UiAccesser.unregister(UserSessionHandler.getController());
		super.detach();
	}

	private void setChangeViewEvents() {
		navigator.addViewChangeListener(new ViewChangeListener() {
			private static final long serialVersionUID = -668206181478591694L;

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {
				setCurrentView(event.getNewView());
				return true;
			}

			@Override
			public void afterViewChange(ViewChangeEvent event) {
				if (UserSessionHandler.getUser() != null) {
					WebformsLogger.info(this.getClass().getName(), "User '"
							+ UserSessionHandler.getUser().getEmailAddress() + "' has change view to '"
							+ event.getNewView().getClass().getName() + "'.");
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void defineWebPages() {
		// Create a navigator to control the views
		navigator = new Navigator(this, this);
		// Define login page as first one.
		navigator.addView("", WebMap.getLoginPage().getWebPageJavaClass());
		navigator.setErrorView(WebMap.getNotFoundPage().getWebPageJavaClass());
		// Create and register the other web pages.
		for (WebMap page : WebMap.values()) {
			addView(page);
		}
		setChangeViewEvents();
	}

	@SuppressWarnings("unchecked")
	public void addView(WebMap newPage) {
		navigator.addView(newPage.toString(), newPage.getWebPageJavaClass());
	}

	public static void navigateTo(WebMap newPage) {
		UI.getCurrent().getNavigator().navigateTo(newPage.toString());
		UserSessionHandler.setUserLastPage(newPage);
	}

	public View getCurrentView() {
		return currentView;
	}

	private void setCurrentView(View currentView) {
		this.currentView = currentView;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public String getPassword() {
		return password;
	}

}
