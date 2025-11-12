package com.biit.webforms.gui.common.theme;

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

import com.vaadin.server.ThemeResource;

public enum CommonThemeIcon implements IThemeIcon {

	ACCEPT("button.accept.svg"),

	CANCEL("button.cancel.svg"),

	ELEMENT_EXPAND("element.expand.svg"),

	ELEMENT_COLLAPSE("element.collapse.svg"), 
	
	SEARCH("search.svg"),
	
	REMOVE("remove.svg"), 
	
	SAVE_FORM("form.save.svg"),
	
	FILE_DOWNLOAD("file.download.svg");

	private String value;

	CommonThemeIcon(String value) {
		this.value = value;
	}

	@Override
	public ThemeResource getThemeResource() {
		return new ThemeResource(value);
	}

	@Override
	public String getFile() {
		return value;
	}

}
