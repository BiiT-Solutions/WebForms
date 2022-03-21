package com.biit.webforms.gui.webpages.webservice.call;

import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

import java.util.Set;

public class WebserviceCallTable extends Table {
	private static final long serialVersionUID = 6695070714351142950L;

	enum Property {
		NAME, TRIGGER, ;
	}

	public WebserviceCallTable() {
		addContainerProperty(Property.NAME, String.class, "", LanguageCodes.WEBSERVICE_CALL_TABLE_NAME.translation(),
				null, Align.LEFT);
		addContainerProperty(Property.TRIGGER, String.class, "",
				LanguageCodes.CAPTION_WEBSERVICE_CALL_TRIGGER.translation(), null, Align.LEFT);
		setCellStyleGenerator(new CellStyleGenerator() {
			private static final long serialVersionUID = -4599536268716190030L;

			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				String styles = "";
				if (itemId instanceof WebserviceCall) {
					if (((WebserviceCall) itemId).isReadOnly()) {
						styles += "tree-cell-disabled ";
					}
				}
				return styles;

			}
		});
	}

	public void addRows(Set<WebserviceCall> calls) {
		for (WebserviceCall call : calls) {
			addRow(call);
		}
	}

	public void addRow(WebserviceCall call) {
		Item item = addItem(call);
		updateRow(item, call);
	}

	@SuppressWarnings("unchecked")
	public void updateRow(Item item, WebserviceCall call) {
		item.getItemProperty(Property.NAME).setValue(call.getName());
		if(call.getFormElementTrigger()!=null){
			item.getItemProperty(Property.TRIGGER).setValue(call.getFormElementTrigger().getPathName());
		}
	}

	public void sortByName() {
		sort(new Object[] { Property.NAME }, new boolean[] { true });
	}
}
