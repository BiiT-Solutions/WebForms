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

import com.biit.webforms.gui.common.language.ILanguageCode;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.theme.IThemeIcon;
import com.vaadin.ui.Button;

public class IconButton extends Button {
	private static final long serialVersionUID = -8287465276670542699L;
	private static final IconSize defaultIconSize = IconSize.SMALL;

	public IconButton(IThemeIcon icon, IconSize size, ILanguageCode tooltip) {
		super("");
		createButton(icon, size, tooltip);
		addStyleName("link");
	}

	public IconButton(IThemeIcon icon, IconSize size, String tooltip) {
		super("");
		createButton(icon, size, tooltip);
		addStyleName("link");
	}

	public IconButton(IThemeIcon icon, ILanguageCode tooltip, IconSize size, ClickListener clickListener) {
		super("", clickListener);
		createButton(icon, size, tooltip);
		addStyleName("link");
	}

	public IconButton(IThemeIcon icon, ILanguageCode tooltip, ClickListener clickListener) {
		super("", clickListener);
		createButton(icon, defaultIconSize, tooltip);
		addStyleName("link");
	}

	public IconButton(ILanguageCode caption, IThemeIcon icon, ILanguageCode tooltip, IconSize size,
			ClickListener clickListener) {
		super(ServerTranslate.translate(caption), clickListener);
		createButton(icon, size, tooltip);
	}

	public IconButton(ILanguageCode caption, IThemeIcon icon, ILanguageCode tooltip, ClickListener clickListener) {
		super(ServerTranslate.translate(caption), clickListener);
		createButton(icon, defaultIconSize, tooltip);
	}

	public IconButton(ILanguageCode caption, IThemeIcon icon, ILanguageCode tooltip, IconSize size) {
		super(ServerTranslate.translate(caption));
		createButton(icon, size, tooltip);
	}

	public IconButton(ILanguageCode caption, IThemeIcon icon, ILanguageCode tooltip) {
		super(ServerTranslate.translate(caption));
		createButton(icon, defaultIconSize, tooltip);
	}

	public IconButton(ILanguageCode caption, IThemeIcon icon, String tooltip) {
		super(ServerTranslate.translate(caption));
		createButton(icon, defaultIconSize, tooltip);
	}

	public void setIcon(IThemeIcon icon) {
		setIcon(icon, defaultIconSize);
	}

	public void setIcon(IThemeIcon icon, IconSize size) {
		if (icon != null && (!size.equals(IconSize.NULL))) {
			addStyleName(size.getSyle());
			setIcon(icon.getThemeResource());

		}
	}

	private void createButton(IThemeIcon icon, IconSize size, ILanguageCode tooltip) {
		setIcon(icon, size);
		setDescription(tooltip);
		setImmediate(true);
	}

	private void createButton(IThemeIcon icon, IconSize size, String tooltip) {
		setIcon(icon, size);
		setDescription(tooltip);
		setImmediate(true);
	}

	public void setDescription(ILanguageCode tooltip) {
		setDescription(ServerTranslate.translate(tooltip));
	}
}
