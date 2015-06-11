package com.biit.webforms.gui.webpages.webservice.call;

import java.util.Set;

import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.WebserviceCall;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class WebserviceCallTable extends Table{
	private static final long serialVersionUID = 6695070714351142950L;
	
	enum Property{
		NAME;
	}

	public WebserviceCallTable() {
		addContainerProperty(Property.NAME, String.class, "",LanguageCodes.WEBSERVICE_CALL_TABLE_NAME.translation(),null,Align.LEFT);
	}

	public void addRows(Set<WebserviceCall> calls) {
		for(WebserviceCall call: calls){
			addRow(call);
		}
	}
	
	public void addRow(WebserviceCall call) {
		Item item = addItem(call);
		updateRow(item, call);
	}
	
	@SuppressWarnings("unchecked")
	public void updateRow(Item item, WebserviceCall call){
		item.getItemProperty(Property.NAME).setValue(call.getName());
	}
	
	public void sortByName() {
		sort(new Object[]{Property.NAME}, new boolean[]{true});
	}
}
