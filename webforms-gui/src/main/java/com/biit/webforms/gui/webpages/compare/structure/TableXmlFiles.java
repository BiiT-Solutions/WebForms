package com.biit.webforms.gui.webpages.compare.structure;

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

import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.common.theme.CommonThemeIcon;
import com.biit.webforms.gui.common.utils.UploadedFile;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.biit.webforms.xml.XmlUtils;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

import java.io.UnsupportedEncodingException;

public class TableXmlFiles extends Table {
	private static final long serialVersionUID = 490387849893506016L;
	private static final int STATUS_COLUMN_WIDTH = 32;

	public enum Properties {
		LABEL, STATUS, XML_FILE, RESULT
	};

	public TableXmlFiles() {
		super();
		initContainerProperties();
	}

	protected void initContainerProperties() {
		addContainerProperty(Properties.LABEL, String.class, null, LanguageCodes.CAPTION_LABEL.translation(), null, Align.LEFT);
		addContainerProperty(Properties.STATUS, Component.class, null, "", null, Align.LEFT);
		addContainerProperty(Properties.XML_FILE, String.class, new String(), "", null, Align.LEFT);
		addContainerProperty(Properties.RESULT, String.class, new String(), "", null, Align.LEFT);
		setColumnExpandRatio(Properties.LABEL, 1.0f);
		setColumnWidth(Properties.STATUS, STATUS_COLUMN_WIDTH);
		setVisibleColumns(new Object[] { Properties.LABEL, Properties.STATUS });
	}

	public void addRow(UploadedFile file) {
		addItem(file);
		updateRow(file);
	}

	@SuppressWarnings("unchecked")
	private void updateRow(UploadedFile file) {
		Item item = getItem(file);
		item.getItemProperty(Properties.LABEL).setValue(file.getFileName());
		item.getItemProperty(Properties.XML_FILE).setValue(getPrettyXml(file));
	}

	private String getPrettyXml(UploadedFile file) {
		try {
			return XmlUtils.format(file.getStream().toString("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		}
		return "Not a valid file";
	}

	public void removeRow(UploadedFile itemId) {
		removeItem(itemId);
	}

	public String getXmlText(UploadedFile itemId) {
		return (String) getItem(itemId).getItemProperty(Properties.XML_FILE).getValue();
	}

	@SuppressWarnings("unchecked")
	public void setResult(UploadedFile itemId, String validationResult) {
		Item item = getItem(itemId);
		item.getItemProperty(Properties.RESULT).setValue(validationResult);
	}

	public String getResult(UploadedFile itemId) {
		return (String) getItem(itemId).getItemProperty(Properties.RESULT).getValue();
	}

	@SuppressWarnings("unchecked")
	public void setStatusMark(UploadedFile itemId, boolean value) {
		Item item = getItem(itemId);
		if (value) {
			item.getItemProperty(Properties.STATUS).setValue(new IconButton(CommonThemeIcon.ACCEPT, IconSize.MEDIUM, ""));
		} else {
			item.getItemProperty(Properties.STATUS).setValue(new IconButton(ThemeIcons.ALERT, IconSize.MEDIUM, ""));
		}
	}
}
