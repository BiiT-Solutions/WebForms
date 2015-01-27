package com.biit.webforms.gui.test.window;

import com.biit.gui.tester.VaadinGuiWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class NewSomething extends VaadinGuiWindow{

	public static final String NAME_FIELD_CAPTION = "Name";
	public static final String ACCEPT_BUTTON_CAPTION = "Accept";
	
	private TextFieldElement getNewFormNameTextField(){
		return $(TextFieldElement.class).caption(NAME_FIELD_CAPTION).first();
	}
	
	private ButtonElement getAcceptButton(){
		return $(ButtonElement.class).caption(ACCEPT_BUTTON_CAPTION).first();
	}
	
	public void createNewForm(String formName) {
		getNewFormNameTextField().setValue(formName);
		getAcceptButton().click();
	}

}
