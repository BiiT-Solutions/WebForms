package com.biit.webforms.security;

import com.biit.form.entity.IBaseFormView;
import com.biit.usermanager.entity.IUser;

public interface IWebformsSecurityService extends ISecurityService {

	boolean isFormEditable(IBaseFormView form, IUser<Long> user);

	boolean isElementEditable(IBaseFormView form, IUser<Long> user);

	boolean isFormReadOnly(IBaseFormView form, IUser<Long> user);

	boolean isFormInUse(IBaseFormView form);

}
