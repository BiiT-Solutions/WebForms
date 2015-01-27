package com.biit.webforms.gui.test.window;

import com.biit.gui.tester.VaadinGuiWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class NewBlockWindow extends NewSomething {

	private TextFieldElement getNewBlockNameTextField() {
		return $(TextFieldElement.class).caption("Name").first();
	}

	private ButtonElement getAcceptButton() {
		return $(ButtonElement.class).caption("Accept").first();
	}

	public void createNewForm(String blockName) {
		getNewBlockNameTextField().setValue(blockName);
		getAcceptButton().click();
	}

}
