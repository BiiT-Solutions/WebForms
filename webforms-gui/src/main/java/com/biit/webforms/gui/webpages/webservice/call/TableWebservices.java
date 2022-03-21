package com.biit.webforms.gui.webpages.webservice.call;

import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.webservices.Webservice;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

import java.util.Set;

public class TableWebservices extends Table {
	private static final long serialVersionUID = -1963941412534840971L;

	enum Properties {
		NAME, DESCRIPTION, URL
	}

	public TableWebservices() {
		super();

		addContainerProperty(Properties.NAME, String.class, "", LanguageCodes.WEBSERVICES_TABLE_NAME.translation(),
				null, Align.LEFT);
		addContainerProperty(Properties.DESCRIPTION, String.class, "",
				LanguageCodes.WEBSERVICES_TABLE_DESCRIPTION.translation(), null, Align.LEFT);
	}

	public void addRows(Set<Webservice> webservices) {
		for (Webservice webservice : webservices) {
			addRow(webservice);
		}
	}

	private void addRow(Webservice webservice) {
		Item item = addItem(webservice);
		updateRow(item, webservice);
	}

	@SuppressWarnings("unchecked")
	private void updateRow(Item item, Webservice webservice) {
		item.getItemProperty(Properties.NAME).setValue(webservice.getName());
		item.getItemProperty(Properties.DESCRIPTION).setValue(webservice.getDescription());
	}

	public void sortByName() {
		sort(new Object[] { Properties.NAME }, new boolean[] { true });
	}
}
