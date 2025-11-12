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
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;

public class WindowEditOutputLink extends WindowEditLink{
	private static final long serialVersionUID = -8870174498133141330L;
	
	private CheckBox checkbox;
	
	public WindowEditOutputLink() {
		super();
	}

	@Override
	protected Component generateContent() {
		FormLayout rootLayout = (FormLayout) super.generateContent();
		
		checkbox = new CheckBox();
		checkbox.setCaption(LanguageCodes.CAPTION_IS_EDITABLE.translation());
		
		rootLayout.addComponent(checkbox);
			
		return rootLayout;
	}
	
	@Override
	protected void configure() {
		super.configure();
		setCaption(LanguageCodes.WINDOW_EDIT_OUTPUT_LINK.translation());
	}
	
	@Override
	public void updateValue(){
		super.updateValue();
		((WebserviceCallOutputLink)getLink()).setEditable(checkbox.getValue());
	}
	
	@Override
	public void setValue(WebserviceCallLink value, WebservicePort port) {
		checkbox.setValue(((WebserviceCallOutputLink)value).isEditable());
		super.setValue(value,port);
	}
}
