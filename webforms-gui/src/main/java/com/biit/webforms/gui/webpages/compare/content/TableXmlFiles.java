package com.biit.webforms.gui.webpages.compare.content;

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
}
