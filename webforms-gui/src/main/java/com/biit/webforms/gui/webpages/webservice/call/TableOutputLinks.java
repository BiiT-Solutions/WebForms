package com.biit.webforms.gui.webpages.webservice.call;

import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.WebserviceCallLink;
import com.biit.webforms.persistence.entity.WebserviceCallOutputLink;
import com.vaadin.data.Item;

public class TableOutputLinks extends TableInputLinks{
	private static final long serialVersionUID = -1820106018066926540L;

	enum Properties{
		IS_EDITABLE
	};
	
	public TableOutputLinks() {
		super();
		addContainerProperty(Properties.IS_EDITABLE, String.class, "", LanguageCodes.TABLE_OUTPUT_LINK_IS_EDITABLE.translation(), null, Align.LEFT);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void updateRow(Item item, WebserviceCallLink link) {
		super.updateRow(item,link);
		WebserviceCallOutputLink outputLink = (WebserviceCallOutputLink) link;
		if(outputLink.isEditable()){
			item.getItemProperty(Properties.IS_EDITABLE).setValue(LanguageCodes.IS_EDITABLE.translation());
		}else{
			item.getItemProperty(Properties.IS_EDITABLE).setValue(LanguageCodes.IS_NOT_EDITABLE.translation());
		}
	}
}
