package com.biit.webforms.gui.webpages.webservice.call;

import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.WebserviceCallLink;
import com.biit.webforms.persistence.entity.WebserviceCallValidationLink;
import com.vaadin.data.Item;

public class TableValidationLinks extends TableInputLinks{
	private static final long serialVersionUID = 3440411045197887543L;

	enum Properties{
		ERROR_CODE,
		MESSAGE,
	};
	
	public TableValidationLinks() {
		super();
		addContainerProperty(Properties.ERROR_CODE, String.class, "", LanguageCodes.TABLE_VALIDATION_LINK_ERROR_CODE.translation(), null, Align.LEFT);
		addContainerProperty(Properties.MESSAGE, String.class, "", LanguageCodes.TABLE_VALIDATION_LINK_MESSAGE.translation(), null, Align.LEFT);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void updateRow(Item item, WebserviceCallLink link) {
		super.updateRow(item,link);
		WebserviceCallValidationLink validation = (WebserviceCallValidationLink) link;
		
		item.getItemProperty(Properties.ERROR_CODE).setValue(validation.getErrorCode());
		item.getItemProperty(Properties.MESSAGE).setValue(validation.getErrorMessage());
	}
	
	@Override
	public void sortByName() {
		sort(new Object[]{TableInputLinks.Properties.PORT_NAME, Properties.ERROR_CODE}, new boolean[]{true,true});
	}
}
