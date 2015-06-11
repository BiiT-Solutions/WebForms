package com.biit.webforms.gui.webpages.webservice.call;

import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.WebserviceCallLink;
import com.biit.webforms.persistence.entity.WebserviceCallOutputLink;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;

public class WindowEditOutputLink extends WindowEditInputLink{
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
		((WebserviceCallOutputLink)getValue()).setEditable(checkbox.getValue());
	}
	
	@Override
	public void setValue(WebserviceCallLink value) {
		checkbox.setValue(((WebserviceCallOutputLink)value).isEditable());
		super.setValue(value);
	}
}
