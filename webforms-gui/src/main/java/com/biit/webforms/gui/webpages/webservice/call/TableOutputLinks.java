package com.biit.webforms.gui.webpages.webservice.call;

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

import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallOutputLink;
import com.biit.webforms.webservices.WebservicePort;
import com.vaadin.data.Item;

public class TableOutputLinks extends TableInputLinks{
	private static final long serialVersionUID = -1820106018066926540L;

	enum Properties{
		IS_EDITABLE
	};
	
	public TableOutputLinks() {
		super();
		addContainerProperty(Properties.IS_EDITABLE, String.class, "", LanguageCodes.TABLE_OUTPUT_LINK_IS_EDITABLE.translation(), null, Align.LEFT);
		
		setVisibleColumns(TableInputLinks.Properties.PORT_NAME,TableInputLinks.Properties.FORM_ELEMENT,Properties.IS_EDITABLE,TableInputLinks.Properties.ELEMENT_TYPE,TableInputLinks.Properties.ELEMENT_FORMAT,TableInputLinks.Properties.ELEMENT_SUBFORMAT);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void updateRow(Item item, WebserviceCallLink link, WebservicePort webservicePort) {
		super.updateRow(item,link,webservicePort);
		WebserviceCallOutputLink outputLink = (WebserviceCallOutputLink) link;
		if(outputLink.isEditable()){
			item.getItemProperty(Properties.IS_EDITABLE).setValue(LanguageCodes.IS_EDITABLE.translation());
		}else{
			item.getItemProperty(Properties.IS_EDITABLE).setValue(LanguageCodes.IS_NOT_EDITABLE.translation());
		}
	}
}
