package com.biit.webforms.security;

import com.biit.form.entity.IBaseFormView;
import com.biit.usermanager.entity.IUser;
import com.biit.webforms.gui.UiAccesser;
import com.biit.webforms.persistence.entity.Form;

public class WebformsSecurityService extends SecurityService implements IWebformsSecurityService {

	public WebformsSecurityService() {
		super();
	}

	@Override
	public boolean isFormEditable(IBaseFormView form, IUser<Long> user) {
		boolean userLockedForm = UiAccesser.isUserUserUsingForm(user, form);
		return userLockedForm && isAuthorizedToForm(form, user);
	}

	@Override
	public boolean isElementEditable(IBaseFormView form, IUser<Long> user) {
		boolean isLinkedForm = false;
		if (form instanceof Form) {
			isLinkedForm = ((Form) form).getFormReference() != null;
		}
		return isFormEditable(form, user) && !isLinkedForm;
	}

	@Override
	public boolean isFormReadOnly(IBaseFormView form, IUser<Long> user) {
		boolean formIsInUse = UiAccesser.getUserUsingForm(form) != null;
		return (!formIsInUse && !isAuthorizedToForm(form, user))
				|| (formIsInUse && UiAccesser.getUserUsingForm(form) != user);
	}

	@Override
	public boolean isFormInUse(IBaseFormView form) {
		return UiAccesser.getUserUsingForm(form) != null;
	}

}
