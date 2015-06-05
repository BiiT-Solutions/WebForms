package com.biit.webforms.authentication;

import com.biit.form.entity.IBaseFormView;
import com.biit.webforms.gui.UiAccesser;
import com.biit.webforms.security.WebformsBasicAuthorizationService;
import com.liferay.portal.model.User;

public class WebformsAuthorizationService extends WebformsBasicAuthorizationService {

	private static WebformsAuthorizationService instance = new WebformsAuthorizationService();

	public WebformsAuthorizationService() {
		super();
	}

	public static WebformsAuthorizationService getInstance() {
		return instance;
	}

	public boolean isFormEditable(IBaseFormView form, User user) {
		boolean userLockedForm = UiAccesser.isUserUserUsingForm(user, form);
		return userLockedForm && isAuthorizedToForm(form, user);
	}

	public boolean isFormReadOnly(IBaseFormView form, User user) {
		boolean formIsInUse = UiAccesser.getUserUsingForm(form) != null;
		return (!formIsInUse && !isAuthorizedToForm(form, user))
				|| (formIsInUse && UiAccesser.getUserUsingForm(form) != user);
	}

	public boolean isFormInUse(IBaseFormView form) {
		return UiAccesser.getUserUsingForm(form) != null;
	}

}
