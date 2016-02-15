package com.biit.webforms.gui;

import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.biit.webforms.gui.common.language.CommonComponentsLanguageCodes;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.webpages.WebMap;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * Main Ui class of application. Ui has been configured to preserve the Ui when
 * a Refresh action has occurred. This way, a UI is only discarded when the user
 * is no longer active in a long time, instead of every time there is a refresh
 * event.
 */
@Push
@Theme("webforms")
@PreserveOnRefresh
public class ApplicationUi extends UI {
	public final static String USER_PARAMETER_TAG = "user";
	public final static String PASSWORD_PARAMETER_TAG = "password";
	private static final long serialVersionUID = -704009283476930001L;
	private BiitNavigator navigator;
	private View currentView;
	private String userEmail;
	private String password;

	private class WebformsErrorHandler extends DefaultErrorHandler {
		private static final long serialVersionUID = -5570064834518413901L;

		@Override
		public void error(com.vaadin.server.ErrorEvent event) {
			// Throw the error to the logger.
			MessageManager.showError(CommonComponentsLanguageCodes.ERROR_UNEXPECTED_ERROR);
			WebformsUiLogger.errorMessage(ApplicationUi.class.getName(), event.getThrowable());
		}
	};

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("");

		WebformsUiLogger.debug(ApplicationUi.class.getName(), "UI initialized.");

		defineWebPages();
		UiAccesser.register(UserSessionHandler.getController());

		// Liferay send this data and automatically are used in the login
		// screen.
		userEmail = request.getParameter(USER_PARAMETER_TAG);
		password = request.getParameter(PASSWORD_PARAMETER_TAG);

		setErrorHandler(new WebformsErrorHandler());

		if (UserSessionHandler.getUser() != null) {
			// User already logged in the connection
			WebformsUiLogger.info(ApplicationUi.class.getName(), "New Ui initializated for User '"
					+ UserSessionHandler.getUser().getEmailAddress() + "'");
			defaultStartNavigations();
		} else {
			// User not logged yet
			if (autologinImplementation()) {
				defaultStartNavigations();
			} else {
				navigator.setState(WebMap.LOGIN_PAGE.name());
			}
		}
	}

	private boolean autologinImplementation() {
		// When accessing from Liferay, user and password are already set.
		if (userEmail != null && userEmail.length() > 0 && password != null && password.length() > 0) {
			WebformsUiLogger.info(ApplicationUi.class.getName(), "Autologin with user '" + userEmail + "' and password with length of "
					+ password.length());
			try {
				IUser<Long> user = UserSessionHandler.getUser(userEmail, password);
				if (user != null) {
					return true;
				}
			} catch (UserManagementException | AuthenticationRequired | InvalidCredentialsException e) {
				WebformsUiLogger.info(ApplicationUi.class.getClass().getName(), "Autologin with user '" + userEmail
						+ "' failed! Wrong user or password.");
			}
		} else {
			if (userEmail != null && userEmail.length() > 0) {
				WebformsUiLogger.info(ApplicationUi.class.getClass().getName(), "Autologin with user '" + userEmail
						+ "' but no password provided!");
			} else {
				WebformsUiLogger.debug(this.getClass().getName(), "Autologin failed.");
			}
		}
		return false;
	}

	private void defaultStartNavigations() {
		// Try to go to the last page and last form if user has no logged out.
		if (UserSessionHandler.getUserLastPage(UserSessionHandler.getUser()) != null
				&& UserSessionHandler.getController().getFormInUse() != null) {
			navigator.setState(UserSessionHandler.getUserLastPage(UserSessionHandler.getUser()).name());
		} else {
			navigator.setState(WebMap.getMainPage().name());
		}
	}

	@Override
	public void detach() {
		if (UserSessionHandler.getUser() != null) {
			// Log user ui expired.
			WebformsUiLogger
					.info(this.getClass().getName(), " UI of '" + UserSessionHandler.getUser().getEmailAddress() + "' has expired.");
		} else {
			WebformsUiLogger.debug(this.getClass().getName(), " UI closed.");
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
					WebformsUiLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
							+ "' has change view to '" + event.getNewView().getClass().getName() + "'.");
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void defineWebPages() {
		// Create a navigator to control the views
		navigator = new BiitNavigator(this, this);
		// // Define login page as first one.
		// navigator.addView("", WebMap.getLoginPage().getWebPageJavaClass());
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
