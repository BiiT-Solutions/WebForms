package com.biit.webforms.authentication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.webpages.WebMap;
import com.biit.webforms.language.LanguageCodes;
import com.liferay.portal.model.User;
import com.vaadin.ui.UI;

public class UserSessionHandler {
	private static final String DEFAULT_SUFIX = "-sessionhandler";
	private User user = null;
	private ApplicationController controller = null;
	// User Id --> List<UI> (A user can have different browsers opened in the same machine)
	private static HashMap<Long, List<UI>> usersSession = new HashMap<>();
	// User Id --> IP (current UI ip connected)
	private static HashMap<Long, String> usersIp = new HashMap<>();

	// Store the user object of the currently inlogged user

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
						userUI.close();
						userUI.getNavigator().navigateTo(WebMap.getLoginPage().toString());
					} catch (Exception e) {
						// maybe the session has expired in Vaadin and cannot be closed.
					}
				}
				MessageManager.showWarning(LanguageCodes.INFO_USER_SESSION_EXPIRED);
			}
		}
		if (usersSession.get(user.getUserId()) == null) {
			usersSession.put(user.getUserId(), new ArrayList<UI>());
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
		}
		setUser(null);
	}

	public static ApplicationController getController() {
		return getCurrent().controller;
	}

}
