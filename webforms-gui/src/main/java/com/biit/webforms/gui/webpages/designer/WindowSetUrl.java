package com.biit.webforms.gui.webpages.designer;

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

import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.ui.TextField;

public class WindowSetUrl extends WindowAcceptCancel {
	private static final long serialVersionUID = 2517104955287396556L;
	private static final String width = "640px";
	private static final String height = "180px";
	private final TextField urlField;

	public WindowSetUrl() {
		super();
		setCaption(LanguageCodes.CAPTION_PROPERTIES_IMAGE_URL.translation());
		urlField = new TextField(LanguageCodes.CAPTION_WINDOW_IMAGE_URL.translation());
		urlField.setWidth("80%");
		setContent(urlField);
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(width);
		setHeight(height);
	}

	public String getUrl() {
		return urlField.getValue();
	}

}
