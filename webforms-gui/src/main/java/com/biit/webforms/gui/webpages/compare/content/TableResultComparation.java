package com.biit.webforms.gui.webpages.compare.content;

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

import com.biit.webforms.gui.common.theme.CommonThemeIcon;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Table;

public class TableResultComparation extends Table {
	private static final long serialVersionUID = -6481950780301383292L;

	enum Properties {
		STATUS_ICON, RESULT_MESSAGE,
	};

	public TableResultComparation() {
		super();
		initContainerProperties();
	}

	protected void initContainerProperties() {
		addContainerProperty(Properties.STATUS_ICON, Component.class, null, LanguageCodes.CAPTION_COMPARATION.translation(),
				null, Align.CENTER);
		addContainerProperty(Properties.RESULT_MESSAGE, String.class, null, LanguageCodes.CAPTION_LABEL.translation(),
				null, Align.CENTER);

		setVisibleColumns(new Object[] { Properties.STATUS_ICON });
	}

	@SuppressWarnings("unchecked")
	public void addOk(String message) {
		Item item = addItem(getItemIds().size());
		item.getItemProperty(Properties.STATUS_ICON).setValue(getOkIcon());
		item.getItemProperty(Properties.RESULT_MESSAGE).setValue(message);
	}

	@SuppressWarnings("unchecked")
	public void addError(String message) {
		Item item = addItem(getItemIds().size());
		item.getItemProperty(Properties.STATUS_ICON).setValue(getErrorIcon());
		item.getItemProperty(Properties.RESULT_MESSAGE).setValue(message);
	}

	private Object getOkIcon() {
		Image ok = new Image(null, CommonThemeIcon.ACCEPT.getThemeResource());
		ok.setHeight("14px");
		ok.setWidth("14px");
		return ok;
	}

	private Object getErrorIcon() {
		Image error = new Image(null, ThemeIcons.ALERT.getThemeResource());
		error.setHeight("14px");
		error.setWidth("14px");
		return error;
	}

	public String getSelectedResult() {
		if (getValue() != null) {
			Object value = getItem(getValue()).getItemProperty(Properties.RESULT_MESSAGE).getValue();
			if (value != null) {
				return (String) value;
			}
		}
		return "";
	}

}
