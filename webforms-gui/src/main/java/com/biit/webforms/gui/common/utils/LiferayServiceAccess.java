package com.biit.webforms.gui.common.utils;

import java.io.IOException;

import com.biit.liferay.access.UserService;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.webforms.logger.WebformsLogger;
import com.liferay.portal.model.User;

public class LiferayServiceAccess {
	private static LiferayServiceAccess instance = new LiferayServiceAccess();

	private UserService userService;
	
	private LiferayServiceAccess(){
	}

	public static LiferayServiceAccess getInstance() {
		return instance;
	}

	private UserService getUserService() {
		if (userService == null) {
			userService = new UserService();
			userService.serverConnection();
		}
		return userService;
	}

	public User getUserById(long userId) throws UserDoesNotExistException {
		try {
			return getUserService().getUserById(userId);
		} catch (NotConnectedToWebServiceException | IOException | AuthenticationRequired | WebServiceAccessError e) {
			WebformsLogger.errorMessage(LiferayServiceAccess.class.getName(), e);
		}
		return null;
	}
}