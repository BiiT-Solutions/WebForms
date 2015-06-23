package com.biit.webforms.gui.tests.window;

import com.vaadin.testbench.elements.HorizontalLayoutElement;

public class WindowEditInputField extends GenericAcceptCancelWindow{

	private static final String WINDOW_ID = "com.biit.webforms.gui.webpages.webservice.call.WindowEditInputLink";
	
	public WindowEditInputField() {
		super();	
	}
	
	public void editFormElement(){
		getWindow().$(HorizontalLayoutElement.class).$$(HorizontalLayoutElement.class).first().click();
		
	}
	
	@Override
	protected String getWindowId() {
		return WINDOW_ID;
	}

}
