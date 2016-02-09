package com.biit.webforms.gui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.gui.webpages.WebMap;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.security.IWebformsSecurityService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.UI;

public class UserSessionHandler {
	private static final String DEFAULT_SUFIX = "-sessionhandler";
	private IUser<Long> user = null;
	private ApplicationController controller = null;
	// User Id --> List<UI> (A user can have different browsers opened in the
	// same machine)
	private static HashMap<Long, Set<UI>> usersSession = new HashMap<>();
	// User Id --> IP (current UI ip connected)
	private static HashMap<Long, String> usersIp = new HashMap<>();
	// User Id --> Last Page visited
	private static HashMap<Long, WebMap> userLastPage = new HashMap<>();

	private static IWebformsSecurityService webformsSecurityService;

	/**
	 * Initializes the {@link UserSessionHandler} for the given
	 * 
	 * @param ui
	 */
	public static void initialize(UI ui) {
		if (ui == null) {
			throw new IllegalArgumentException("Application may not be null");
		}
		new UserSessionHandler(ui);
	}

	public static void checkOnlyOneSession(IUser<Long> user, UI ui, String ip) {
		if (usersSession.get(user.getId()) != null) {
			if (ip == null || !ip.equals(usersIp.get(user.getId()))) {
				for (UI userUI : usersSession.get(user.getId())) {
					try {
						WebformsUiLogger.info(UserSessionHandler.class.getName(), "Closing session for user '" + user.getEmailAddress()
								+ "', IP '" + usersIp.get(user.getId()) + "'.");
						userUI.getPage().setLocation("./VAADIN/logout.html");
						userUI.close();
					} catch (Exception e) {
						// maybe the session has expired in Vaadin and cannot be
						// closed.
					}
				}
				MessageManager.showWarning(LanguageCodes.INFO_USER_SESSION_EXPIRED);
			}
		}
		if (usersSession.get(user.getId()) == null) {
			usersSession.put(user.getId(), new HashSet<UI>());
		}
		usersSession.get(user.getId()).add(ui);
		usersIp.put(user.getId(), ip);
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
	 * Set the User object for the currently inlogged user for this application
	 * instance
	 * 
	 * @param user
	 */
	public static void setUser(IUser<Long> user) {
		UserSessionHandler session = getCurrent();
		session.user = user;
		session.controller.setUser(user);
	}

	/**
	 * Get the User object of the currently inlogged user for this application
	 * instance.
	 * 
	 * @return The currently inlogged user
	 */
	public static IUser<Long> getUser() {
		UserSessionHandler session = getCurrent();
		return session.user;
	}

	/**
	 * Set the User object for the currently inlogged user for this application
	 * instance
	 * 
	 * @param user
	 */
	public static void login(IUser<Long> user) {
		setUser(user);
	}

	/**
	 * Method for logging out a user
	 */
	public static void logout() {
		if (getUser() != null) {
			usersSession.remove(getUser().getId());
			usersIp.remove(getUser().getId());
			userLastPage.remove(getUser().getId());
		}
		setUser(null);
	}

	public static ApplicationController getController() {
		return getCurrent().controller;
	}

	public static WebMap getUserLastPage(IUser<Long> user) {
		return userLastPage.get(user.getId());
	}

	public static void setUserLastPage(WebMap page) {
		setUserLastPage(getUser(), page);
	}

	public static void setUserLastPage(IUser<Long> user, WebMap page) {
		if (user != null) {
			if (!WebMap.getMainPage().equals(page) && !WebMap.getLoginPage().equals(page)) {
				userLastPage.put(user.getId(), page);
			} else {
				userLastPage.remove(user.getId());
			}
		}
	}

	public static IUser<Long> getUser(String userMail, String password) throws UserManagementException, AuthenticationRequired,
			InvalidCredentialsException {
		// Try to log in the user when the button is clicked
		IUser<Long> user = getAuthenticationService().getAuthenticationService().authenticate(userMail, password);

		if (user != null) {
			WebBrowser browser = (WebBrowser) UI.getCurrent().getPage().getWebBrowser();
			try {
				String message = "User '" + user.getEmailAddress() + "' logged successfully. Using '" + browser.getBrowserApplication()
						+ "'";
				if (browser.getAddress() != null) {
					message += " (IP: " + browser.getAddress() + ").";
				} else {
					message += ".";
				}
				WebformsUiLogger.info(UserSessionHandler.class.getName(), message);
			} catch (Exception e) {
				WebformsUiLogger.errorMessage(UserSessionHandler.class.getName(), e);
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
}
