package com.biit.webforms.gui.test.webpage.designer;

import com.biit.webforms.gui.test.exceptions.FieldNotEditableException;


public class TextPropertiesView extends TechnicalNameProperty {
	
	private static final String TEXT_TEXT_FIELD_CAPTION = "Text";

	public String getTextValue(){
		return getTextAreaValue(TEXT_TEXT_FIELD_CAPTION);
	}
	
	public void setTextValue(String value) throws FieldNotEditableException{
		setTextAreaValue(TEXT_TEXT_FIELD_CAPTION, value);
	}
}
