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

import com.biit.webforms.gui.common.utils.UploadedFile;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class TableXmlFiles extends Table {
	private static final long serialVersionUID = -134266638122689112L;

	public enum Properties {
		LABEL,
	};

	public TableXmlFiles() {
		super();
		initContainerProperties();
	}

	protected void initContainerProperties() {
		addContainerProperty(Properties.LABEL, String.class, null, LanguageCodes.CAPTION_NAME.translation(), null,
				Align.LEFT);
	}

	public void addRow(UploadedFile file) {
		addItem(file);
		updateRow(file);
	}

	@SuppressWarnings("unchecked")
	private void updateRow(UploadedFile file) {
		Item item = getItem(file);
		item.getItemProperty(Properties.LABEL).setValue(file.getFileName());
	}

	public void removeRow(UploadedFile itemId) {
		removeItem(itemId);
	}

	public void selectItemAtPosition(Integer value) {
		int i = 0;
		for (Object itemId : this.getItemIds()) {
			if (i == value) {
				setValue(itemId);
				break;
			}
			i++;
		}
	}

	public void removeCurrentSelection() {
		if (getValue() != null) {
			removeItem(getValue());
		}
	}

	public Integer getSelectedPosition() {
		int i = 0;
		for (Object itemId : this.getItemIds()) {
			if (itemId == getValue()) {
				return i;
			}
			i++;
		}
		return -1;
	}
}
