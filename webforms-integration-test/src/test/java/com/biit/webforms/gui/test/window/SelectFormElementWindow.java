package com.biit.webforms.gui.test.window;

import java.util.List;

import com.biit.gui.tester.VaadinGuiWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.CssLayoutElement;
import com.vaadin.testbench.elements.CustomComponentElement;
import com.vaadin.testbench.elements.HorizontalLayoutElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.TreeTableElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;
import com.vaadin.testbench.elements.WindowElement;

public class SelectFormElementWindow extends VaadinGuiWindow {

	private static final String WINDOW_CAPTION = "Select form element";
	private static final String ACCEPT_BUTTON_CAPTION = "Accept";
	private static final String CANCEL_BUTTON_CAPTION = "Cancel";
	private static final String SEARCH_FIELD_CAPTION = "Search";

	private TextFieldElement getSearchField() {
		return $(TextFieldElement.class).caption(SEARCH_FIELD_CAPTION).first();
	}

	private ButtonElement getAcceptButton() {
		return $$(WindowElement.class).caption(WINDOW_CAPTION).$$(VerticalLayoutElement.class)
				.$$(HorizontalLayoutElement.class).$$(ButtonElement.class).caption(ACCEPT_BUTTON_CAPTION).first();
	}

	private ButtonElement getCancelButton() {
		return $$(WindowElement.class).caption(WINDOW_CAPTION).$$(VerticalLayoutElement.class)
				.$$(HorizontalLayoutElement.class).$$(ButtonElement.class).caption(CANCEL_BUTTON_CAPTION).first();
	}

	public TreeTableElement getTreeTable() {
		return $$(WindowElement.class).caption(WINDOW_CAPTION).$$(VerticalLayoutElement.class)
				.$$(CustomComponentElement.class).$$(VerticalLayoutElement.class).$$(TreeTableElement.class).first();
	}

	public void clickAccceptButton() {
		getAcceptButton().click();
	}

	public void clickCancelButton() {
		getCancelButton().click();
	}

	public void searchForElement(String elementName) {
		getSearchField().sendKeys(elementName);
	}

	public void selectElementInTable(String elementName) {
		List<LabelElement> labels = $$(WindowElement.class).caption(WINDOW_CAPTION).$$(VerticalLayoutElement.class)
				.$$(CustomComponentElement.class).$$(VerticalLayoutElement.class).$$(TreeTableElement.class)
				.$$(CustomComponentElement.class).$$(CssLayoutElement.class).$$(LabelElement.class).all();
		for (LabelElement label : labels) {
			if(label.getText().equals(elementName)){
				label.click();
				break;
			}
		}
	}

}
