package com.biit.webforms.security;

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

import com.biit.form.entity.IBaseFormView;
import com.biit.usermanager.entity.IUser;
import com.biit.webforms.gui.UiAccesser;
import com.biit.webforms.persistence.entity.Form;
import com.vaadin.ui.UI;

public class WebformsSecurityService extends SecurityService implements IWebformsSecurityService {

	public WebformsSecurityService() {
		super();
	}

	@Override
	public boolean isFormEditable(IBaseFormView form, IUser<Long> user) {
		boolean userLockedForm = UiAccesser.isUserUiUsingForm(user, UI.getCurrent(), form);
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
