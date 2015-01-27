package com.biit.webforms.gui.test.window;

import com.biit.gui.tester.VaadinGuiWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class NewFormWindow extends VaadinGuiWindow {

	private TextFieldElement getNewFormNameTextField() {
		return $(TextFieldElement.class).caption("Name").first();
	}

	private ButtonElement getAcceptButton() {
		return $(ButtonElement.class).caption("Accept").first();
	}

	public void createNewForm(String formName) {
		getNewFormNameTextField().setValue(formName);
		getAcceptButton().click();
	}

}
