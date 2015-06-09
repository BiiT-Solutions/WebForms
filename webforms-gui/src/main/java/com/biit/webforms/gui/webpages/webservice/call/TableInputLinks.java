package com.biit.webforms.gui.webpages.webservice.call;

import java.util.Set;

import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.WebserviceCallLink;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class TableInputLinks extends Table{
	private static final long serialVersionUID = 561989680943245108L;
	
	enum Properties{
		PORT_NAME,
		FORM_ELEMENT,
	};

	public TableInputLinks() {
		super();
		addContainerProperty(Properties.PORT_NAME, String.class, "", LanguageCodes.PORT_TABLE_NAME.translation(), null, Align.LEFT);
		addContainerProperty(Properties.FORM_ELEMENT, String.class, "", LanguageCodes.PORT_TABLE_FORM_ELEMENT.translation(), null, Align.LEFT);
	}
	
	public void addRows(Set<WebserviceCallLink> links){
		for(WebserviceCallLink link: links){
			addRow(link);
		}
	}
	
	public void addRow(WebserviceCallLink port){
		Item item = addItem(port);
		updateRow(item,port);
	}

	@SuppressWarnings("unchecked")
	protected void updateRow(Item item, WebserviceCallLink link) {
		item.getItemProperty(Properties.PORT_NAME).setValue(link.getWebservicePort().getName());
		if(link.getFormElement()!=null){
			item.getItemProperty(Properties.FORM_ELEMENT).setValue(link.getFormElement().getPathName());
		}
	}

}
