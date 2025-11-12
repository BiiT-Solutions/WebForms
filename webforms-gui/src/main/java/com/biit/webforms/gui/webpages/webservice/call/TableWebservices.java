package com.biit.webforms.gui.webpages.webservice.call;

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

import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.webservices.Webservice;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

import java.util.Set;

public class TableWebservices extends Table {
	private static final long serialVersionUID = -1963941412534840971L;

	enum Properties {
		NAME, DESCRIPTION, URL
	}

	public TableWebservices() {
		super();

		addContainerProperty(Properties.NAME, String.class, "", LanguageCodes.WEBSERVICES_TABLE_NAME.translation(),
				null, Align.LEFT);
		addContainerProperty(Properties.DESCRIPTION, String.class, "",
				LanguageCodes.WEBSERVICES_TABLE_DESCRIPTION.translation(), null, Align.LEFT);
	}

	public void addRows(Set<Webservice> webservices) {
		for (Webservice webservice : webservices) {
			addRow(webservice);
		}
	}

	private void addRow(Webservice webservice) {
		Item item = addItem(webservice);
		updateRow(item, webservice);
	}

	@SuppressWarnings("unchecked")
	private void updateRow(Item item, Webservice webservice) {
		item.getItemProperty(Properties.NAME).setValue(webservice.getName());
		item.getItemProperty(Properties.DESCRIPTION).setValue(webservice.getDescription());
	}

	public void sortByName() {
		sort(new Object[] { Properties.NAME }, new boolean[] { true });
	}
}
