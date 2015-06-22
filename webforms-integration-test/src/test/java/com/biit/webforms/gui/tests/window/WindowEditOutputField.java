package com.biit.webforms.gui.tests.window;

import com.vaadin.testbench.elements.CheckBoxElement;
import com.vaadin.testbench.elements.HorizontalLayoutElement;

public class WindowEditOutputField extends GenericAcceptCancelWindow {

	private static final String WINDOW_ID = "com.biit.webforms.gui.webpages.webservice.call.WindowEditOutputLink";
	private static final String CAPTION_EDITABLE = "Editable";
	
	public void editFormElement(){
		getWindow().$(HorizontalLayoutElement.class).$$(HorizontalLayoutElement.class).first().click();
	}
	
	public void setEditableValue(boolean editable){
		CheckBoxElement editableCheckBox = $(CheckBoxElement.class).caption(CAPTION_EDITABLE).first();
		if(editable && editableCheckBox.getValue().equals("unchecked")){
			editableCheckBox.click();
			return;
		}
		if(!editable && editableCheckBox.getValue().equals("checked")){
			editableCheckBox.click();
			return;
		}
	}
	
	@Override
	protected String getWindowId() {
		return WINDOW_ID;
	}

}
