package com.biit.webforms.gui.webpages.webservice.call;

import com.biit.webforms.language.LanguageCodes;
import com.vaadin.ui.Table;

public class WebserviceCallTable extends Table{
	private static final long serialVersionUID = 6695070714351142950L;
	
	enum Property{
		NAME;
	}

	public WebserviceCallTable() {
		addContainerProperty(Property.NAME, String.class, "",LanguageCodes.WEBSERVICE_CALL_TABLE_NAME.translation(),null,Align.LEFT);
	}
}
