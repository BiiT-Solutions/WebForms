package com.biit.webforms.gui.webpages.formmanager;

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
import com.biit.usermanager.entity.IGroup;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.security.IWebformsSecurityService;
import com.vaadin.data.Item;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Table;

public class TableSimpleFormNameOrganization extends Table {
	private static final long serialVersionUID = 8482204549675894932L;

	private IWebformsSecurityService webformsSecurityService;

	enum Properties {
		FORM_NAME, FORM_ORGANIZATION,
	};

	public TableSimpleFormNameOrganization() {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		webformsSecurityService = (IWebformsSecurityService) helper.getBean("webformsSecurityService");
		configureDataSource();
	}

	private void configureDataSource() {
		addContainerProperty(Properties.FORM_NAME, String.class, null, LanguageCodes.CAPTION_NAME.translation(), null, Align.CENTER);
		addContainerProperty(Properties.FORM_ORGANIZATION, String.class, null, LanguageCodes.CAPTION_ORGANIZATION.translation(), null, Align.CENTER);
		sortByName();
	}

	public void add(IBaseFormView form) {
		addItem(form);
		update(form);
	}

	@SuppressWarnings("unchecked")
	private void update(IBaseFormView form) {
		Item item = getItem(form);
		item.getItemProperty(Properties.FORM_NAME).setValue(form.getLabel());

		IGroup<Long> organization = webformsSecurityService.getOrganization(UserSession.getUser(), form.getOrganizationId());
		if (organization != null) {
			item.getItemProperty(Properties.FORM_ORGANIZATION).setValue(organization.getUniqueName());
		}
	}

	public void sortByName() {
		sort(new Object[] { Properties.FORM_NAME }, new boolean[] { true });
	}
}
