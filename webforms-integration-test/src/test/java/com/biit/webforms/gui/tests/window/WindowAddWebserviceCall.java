package com.biit.webforms.gui.tests.window;

import com.vaadin.testbench.elements.TextFieldElement;

public class WindowAddWebserviceCall extends GenericAcceptCancelWindow {

	private static final String WINDOW_ID = "com.biit.webforms.gui.webpages.webservice.call.WindowWebservices";

	private static final String NAME_FIELD_CAPTION = "Name";
	
	private TextFieldElement getNameTextField() {
		return getWindow().$(TextFieldElement.class).caption(NAME_FIELD_CAPTION).first();
	}
	
	public void createNewWebserviceCall(String name){
		getNameTextField().setValue(name);
		clickAccept();
	}
	
	@Override
	protected String getWindowId() {
		return WINDOW_ID;
	}
	
}
