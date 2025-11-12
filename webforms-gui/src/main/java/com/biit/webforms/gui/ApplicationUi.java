package com.biit.webforms.gui;

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
import com.biit.webforms.gui.common.language.CommonComponentsLanguageCodes;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.gui.exceptions.SessionHasAlreadyUser;
import com.biit.webforms.gui.webpages.WebMap;
import com.biit.webforms.security.IWebformsSecurityService;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.UI;

import java.util.Objects;

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
	public static final String USER_PARAMETER_TAG = "user";
	public static final String PASSWORD_PARAMETER_TAG = "password";
	private static final long serialVersionUID = -704009283476930001L;
	private BiitNavigator navigator;
	private View currentView;
	private String userEmail;
	private String password;

	private ApplicationController controller = null;
	private static IWebformsSecurityService webformsSecurityService;

	private class WebformsErrorHandler extends DefaultErrorHandler {
		private static final long serialVersionUID = -5570064834518413901L;

		@Override
		public void error(com.vaadin.server.ErrorEvent event) {
			// Throw the error to the logger.
			MessageManager.showError(CommonComponentsLanguageCodes.ERROR_UNEXPECTED_ERROR);
			WebformsUiLogger.errorMessage(ApplicationUi.class.getName(), event.getThrowable());
		}
	};

	public ApplicationUi() {
		controller = new ApplicationController();
		addDetachListener(new DetachListener() {
			private static final long serialVersionUID = -3577213711157021631L;

			@Override
			public void detach(DetachEvent event) {
				WebformsUiLogger.debug(this.getClass().getName(), "has been detached.");
				UiAccesser.unregister(controller);
			}
		});
	}

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("");

		WebformsUiLogger.debug(ApplicationUi.class.getName(), "UI initialized.");

		defineWebPages();
		UiAccesser.register(controller);

		// Liferay send this data and automatically are used in the login
		// screen.
		userEmail = request.getParameter(USER_PARAMETER_TAG);
		password = request.getParameter(PASSWORD_PARAMETER_TAG);

		setErrorHandler(new WebformsErrorHandler());

		if (UserSession.getUser() != null) {
			// User already logged in the connection
			defaultStartNavigations();
		} else {
			// User not logged yet
			try {
				if (autologinImplementation()) {
					defaultStartNavigations();
				} else {
					navigator.setState(WebMap.LOGIN_PAGE.name());
				}
			} catch (SessionHasAlreadyUser e) {
				WebformsUiLogger.info(ApplicationUi.class.getClass().getName(), "Autologin with user '" + userEmail
						+ "' is trying to access a session with another user already logged.");
			}
		}
	}

	private boolean autologinImplementation() throws SessionHasAlreadyUser {
		// When accessing from Liferay, user and password are already set.
		if (userEmail != null && userEmail.length() > 0 && password != null && password.length() > 0) {
			WebformsUiLogger.info(ApplicationUi.class.getName(), "Autologin with user '" + userEmail + "' and password with length of " + password.length());
			try {
				IUser<Long> user = login(userEmail, password);
				if (user != null) {
					return true;
				}
			} catch (UserManagementException | AuthenticationRequired | InvalidCredentialsException | UserDoesNotExistException e) {
				WebformsUiLogger.info(ApplicationUi.class.getClass().getName(), "Autologin with user '" + userEmail + "' failed! Wrong user or password.");
			}
		} else {
			if (userEmail != null && userEmail.length() > 0) {
				WebformsUiLogger.info(ApplicationUi.class.getClass().getName(), "Autologin with user '" + userEmail + "' but no password provided!");
			} else {
				WebformsUiLogger.debug(this.getClass().getName(), "Autologin failed.");
			}
		}
		return false;
	}

	public IUser<Long> login(String userEmail, String password) throws UserManagementException, AuthenticationRequired, InvalidCredentialsException,
			SessionHasAlreadyUser, UserDoesNotExistException {
		IUser<Long> currentUser = UserSession.getUser();
		if (currentUser != null) {
			if (Objects.equals(currentUser.getEmailAddress(), userEmail) && Objects.equals(currentUser.getPassword(), password)) {
				return currentUser;
			}
			throw new SessionHasAlreadyUser();
		}
		// Try to log in the user when the button is clicked
		IUser<Long> user = getAuthenticationService().getAuthenticationService().authenticate(userEmail, password);

		if (user != null) {
			UserSession.setUser(user);

			WebBrowser browser = (WebBrowser) UI.getCurrent().getPage().getWebBrowser();
			try {
				String message = "Logged successfully. Using '" + browser.getBrowserApplication() + "'";
				if (browser.getAddress() != null) {
					message += " (IP: " + browser.getAddress() + ").";
				} else {
					message += ".";
				}
				WebformsUiLogger.info(ApplicationUi.class.getName(), message);
			} catch (Exception e) {
				WebformsUiLogger.errorMessage(ApplicationUi.class.getName(), e);
			}

			// Store the password.
			user.setPassword(password);
		}
		return user;
	}

	private void defaultStartNavigations() {
		// Try to go to the last page and last form if user has no logged out.
		WebMap lastPage = UserSession.getUserLastPage();
		if (lastPage != null && getController().getFormInUse() != null) {
			navigator.setState(lastPage.name());
		} else {
			navigator.setState(WebMap.getMainPage().name());
		}
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
				WebformsUiLogger.info(this.getClass().getName(), "View changed to '" + event.getNewView().getClass().getName() + "'.");
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
		UserSession.setUserLastPage(newPage);
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

	/**
	 * Autowired not working correctly in this version of Vaadin. Use the helper
	 * if needed in a static method.
	 *
	 * @return
	 */
	private static IWebformsSecurityService getAuthenticationService() {
		if (webformsSecurityService == null) {
			SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
			webformsSecurityService = (IWebformsSecurityService) helper.getBean("webformsSecurityService");
		}
		return webformsSecurityService;
	}

	public ApplicationController getControllerInstance() {
		return controller;
	}

	public static ApplicationController getController() {
		return ((ApplicationUi) getCurrent()).controller;
	}
}
