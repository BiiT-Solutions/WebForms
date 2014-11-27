package com.biit.webforms.gui.webpages.compare.content;

import com.biit.webforms.gui.webpages.compare.content.TableResultComparation.Properties;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;

public class TableOriginalProcessedComparation  extends Table {
	private static final long serialVersionUID = -6481950780301383292L;

	enum Properties {
		ORIGINAL_FILENAME, ORIGINAL_FILE, PROCESSED_FILENAME, PROCESSED_FILE, STATUS_ICON, RESULT_MESSAGE,
	};

	public TableOriginalProcessedComparation() {
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
}
