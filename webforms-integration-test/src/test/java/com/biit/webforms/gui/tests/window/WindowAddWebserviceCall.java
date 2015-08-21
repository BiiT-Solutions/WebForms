package com.biit.webforms.gui.tests.window;

import com.vaadin.testbench.elements.TableElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.WindowElement;

public class WindowAddWebserviceCall extends GenericAcceptCancelWindow {

	private static final String WINDOW_ID = "com.biit.webforms.gui.webpages.webservice.call.WindowWebservices";

	private static final String NAME_FIELD_CAPTION = "Name";

	private TextFieldElement getNameTextField() {
		return getWindow().$(TextFieldElement.class).caption(NAME_FIELD_CAPTION).first();
	}

	private TableElement getWebserviceTable() {
		return $$(WindowElement.class).id("com.biit.webforms.gui.webpages.webservice.call.WindowWebservices")
				.$(TableElement.class).first();
	}

	public void createNewWebserviceCall(String name, int row) {
		getNameTextField().setValue(name);
		getWebserviceTable().getCell(row, 0).click();
		clickAccept();
	}

	@Override
	protected String getWindowId() {
		return WINDOW_ID;
	}

}
