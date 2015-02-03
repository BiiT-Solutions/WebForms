package com.biit.webforms.gui.test.webpage.designer;

import com.biit.webforms.gui.test.exceptions.FieldNotEditableException;

public class CommonAnswerSubanswerProperties extends CommonTreeElementProperties {
	
	private static final String VALUE_FIELD_CAPTION = "Value";
	private static final String LABEL_TEXT_AREA_CAPTION = "Label";
	private static final String DESCRIPTION_TEXT_AREA_CAPTION = "Description";

	
	public String getValue(){
		return getTextFieldValue(VALUE_FIELD_CAPTION);
	}
	
	public void setValue(String value) throws FieldNotEditableException{
		setTextFieldValue(VALUE_FIELD_CAPTION, value);
	}
	
	public String getLabelTextAraValue(){
		return getTextAreaValue(LABEL_TEXT_AREA_CAPTION);
	}
	
	public void setLabelTextAreaValue(String value) throws FieldNotEditableException{
		setTextAreaValue(LABEL_TEXT_AREA_CAPTION, value);
	}
	
	public String getDescriptionTextAreaValue(){
		return getTextAreaValue(DESCRIPTION_TEXT_AREA_CAPTION);
	}
	
	public void setDescriptionTextAreaValue(String value) throws FieldNotEditableException {
		setTextAreaValue(DESCRIPTION_TEXT_AREA_CAPTION, value);
	}
}
