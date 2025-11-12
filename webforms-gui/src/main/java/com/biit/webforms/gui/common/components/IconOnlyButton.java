package com.biit.webforms.gui.common.components;

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

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.BaseTheme;

public class IconOnlyButton extends Button{

	private static final long serialVersionUID = 755173150169409609L;
	private static final String BUTTON_MIN_SIZE = "20px";

	public IconOnlyButton(Resource icon) {
		super();
		setStyleName(BaseTheme.BUTTON_LINK);
		addStyleName("IconOnlyButton");
		setIcon(icon);
		setWidth(BUTTON_MIN_SIZE);
	}
}
