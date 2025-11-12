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

import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.components.UpperMenuWebforms;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.Button.ClickListener;

public class UpperMenu extends UpperMenuWebforms {
	private static final long serialVersionUID = 7495354678540982221L;

	private IconButton compareContent;
	private IconButton uploadOriginalXml;
	private IconButton uploadProcessedXml;
	private IconButton removeXml;
	private IconButton cleanXml;

	public UpperMenu() {

		compareContent = new IconButton(LanguageCodes.CAPTION_COMPARE_XML_CONTENT, ThemeIcons.COMPARE_XML_CONTENT,
				LanguageCodes.TOOLTIP_COMPARE_XML_CONTENT, IconSize.BIG);

		uploadOriginalXml = new IconButton(LanguageCodes.CAPTION_UPLOAD_XML_ORIGINAL, ThemeIcons.UPLOAD_XML_ORIGINAL,
				LanguageCodes.TOOLTIP_UPLOAD_XML_ORIGINAL, IconSize.BIG);
		uploadProcessedXml = new IconButton(LanguageCodes.CAPTION_UPLOAD_XML_PROCESSED,
				ThemeIcons.UPLOAD_XML_PROCESSED, LanguageCodes.TOOLTIP_UPLOAD_XML_PROCESSED, IconSize.BIG);	
		
		removeXml = new IconButton(LanguageCodes.CAPTION_REMOVE_XML, ThemeIcons.REMOVE_XML,
				LanguageCodes.TOOLTIP_REMOVE_XML, IconSize.BIG);
		cleanXml = new IconButton(LanguageCodes.CAPTION_CLEAN_XML,
				ThemeIcons.CLEAN_XML, LanguageCodes.TOOLTIP_CLEAN_XML, IconSize.BIG);


		addIconButton(compareContent);
		addIconButton(uploadOriginalXml);
		addIconButton(uploadProcessedXml);
		addIconButton(removeXml);
		addIconButton(cleanXml);

	}

	public void addUploadOriginalListener(ClickListener listener) {
		uploadOriginalXml.addClickListener(listener);
	}

	public void addUploadProcessedListener(ClickListener listener) {
		uploadProcessedXml.addClickListener(listener);
	}

	public void addCompareListener(ClickListener listener) {
		compareContent.addClickListener(listener);
	}

	public void addRemoveListener(ClickListener listener) {
		removeXml.addClickListener(listener);
	}

	public void addCleanListener(ClickListener listener) {
		cleanXml.addClickListener(listener);
	}

}
