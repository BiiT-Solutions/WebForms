package com.biit.webforms.gui.common.utils;

import java.io.IOException;
import java.util.Set;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.security.AuthorizationService;
import com.biit.liferay.security.IActivity;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;

public class WebpageAuthorizationService extends AuthorizationService {

	private static WebpageAuthorizationService instance = new WebpageAuthorizationService();

	public WebpageAuthorizationService() {
		super();
	}
	
	public boolean isAuthorizedActivity(User user,IActivity activity){
		try {
			return super.isAuthorizedActivity(user, activity);
		} catch (IOException | AuthenticationRequired e) {
			MessageManager.showError(e.getMessage());
			return false;
		}
	}

	public static WebpageAuthorizationService getInstance() {
		return instance;
	}

	@Override
	public Set<IActivity> getRoleActivities(Role arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
