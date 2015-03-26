package com.biit.webforms.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.security.AuthenticationService;
import com.biit.liferay.security.exceptions.InvalidCredentialsException;
import com.biit.security.exceptions.PBKDF2EncryptorException;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.webpages.WebMap;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.liferay.portal.model.User;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.UI;

public class UserSessionHandler {
	private static final String DEFAULT_SUFIX = "-sessionhandler";
	private User user = null;
	private ApplicationController controller = null;
	// User Id --> List<UI> (A user can have different browsers opened in the same machine)
	private static HashMap<Long, Set<UI>> usersSession = new HashMap<>();
	// User Id --> IP (current UI ip connected)
	private static HashMap<Long, String> usersIp = new HashMap<>();
	// User Id --> Last Page visited
	private static HashMap<Long, WebMap> userLastPage = new HashMap<>();

	/**
	 * Initializes the {@link UserSessionHandler} for the given {@link Application}
	 * 
	 * @param ui
	 */
	public static void initialize(UI ui) {
		if (ui == null) {
			throw new IllegalArgumentException("Application may not be null");
		}
		new UserSessionHandler(ui);
	}

	public static void checkOnlyOneSession(User user, UI ui, String ip) {
		if (usersSession.get(user.getUserId()) != null) {
			if (ip == null || !ip.equals(usersIp.get(user.getUserId()))) {
				for (UI userUI : usersSession.get(user.getUserId())) {
					try {
						WebformsLogger.info(UserSessionHandler.class.getName(),
								"Closing session for user '" + user.getEmailAddress() + "', IP '" + usersIp.get(user.getUserId()) + "'.");
						userUI.getPage().setLocation("./VAADIN/logout.html");
						userUI.close();
					} catch (Exception e) {
						// maybe the session has expired in Vaadin and cannot be closed.
					}
				}
				MessageManager.showWarning(LanguageCodes.INFO_USER_SESSION_EXPIRED);
			}
		}
		if (usersSession.get(user.getUserId()) == null) {
			usersSession.put(user.getUserId(), new HashSet<UI>());
		}
		usersSession.get(user.getUserId()).add(ui);
		usersIp.put(user.getUserId(), ip);
	}

	/**
	 * Constructor
	 * 
	 * @param ui
	 *            Current application instance
	 */
	public UserSessionHandler(UI ui) {
		ui.getSession().setAttribute(ui.getId() + DEFAULT_SUFIX, this);
		controller = new ApplicationController();
	}

	private static UserSessionHandler getCurrent() {
		UI ui = UI.getCurrent();
		UserSessionHandler handler = (UserSessionHandler) ui.getSession().getAttribute(ui.getId() + DEFAULT_SUFIX);
		if (handler == null) {
			// Session Handler not created or expired. Create a new one.
			synchronized (UserSessionHandler.class) {
				new UserSessionHandler(ui);
				return getCurrent();
			}
		}
		return handler;
	}

	/**
	 * Set the User object for the currently inlogged user for this application instance
	 * 
	 * @param user
	 */
	public static void setUser(User user) {
		UserSessionHandler session = getCurrent();
		session.user = user;
		session.controller.setUser(user);
	}

	/**
	 * Get the User object of the currently inlogged user for this application instance.
	 * 
	 * @return The currently inlogged user
	 */
	public static User getUser() {
		UserSessionHandler session = getCurrent();
		return session.user;
	}

	/**
	 * Set the User object for the currently inlogged user for this application instance
	 * 
	 * @param user
	 */
	public static void login(User user) {
		setUser(user);
	}

	/**
	 * Method for logging out a user
	 */
	public static void logout() {
		if (getUser() != null) {
			usersSession.remove(getUser().getUserId());
			usersIp.remove(getUser().getUserId());
			userLastPage.remove(getUser().getUserId());
		}
		setUser(null);
	}

	public static ApplicationController getController() {
		return getCurrent().controller;
	}

	public static WebMap getUserLastPage(User user) {
		return userLastPage.get(user.getUserId());
	}

	public static void setUserLastPage(WebMap page) {
		setUserLastPage(getUser(), page);
	}

	public static void setUserLastPage(User user, WebMap page) {
		if (user != null) {
			if (!WebMap.getMainPage().equals(page) && !WebMap.getLoginPage().equals(page)) {
				userLastPage.put(user.getUserId(), page);
			} else {
				userLastPage.remove(user.getUserId());
			}
		}
	}

	public static User getUser(String userMail, String password) throws JsonParseException, JsonMappingException,
			ClientProtocolException, InvalidCredentialsException, NotConnectedToWebServiceException,
			PBKDF2EncryptorException, IOException, AuthenticationRequired, WebServiceAccessError {
		// Try to log in the user when the button is clicked
		User user = AuthenticationService.getInstance().authenticate(userMail, password);

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
				WebformsLogger.info(UserSessionHandler.class.getName(), message);
			} catch (Exception e) {
				WebformsLogger.errorMessage(UserSessionHandler.class.getName(), e);
			}
			// Store the password.
			user.setPassword(password);

			// The user's password was correct, so set the user as the
			// current user (inlogged)
			UserSessionHandler.setUser(user);
			UserSessionHandler.checkOnlyOneSession(user, UI.getCurrent(), browser.getAddress());
		}
		return user;
	}
}
