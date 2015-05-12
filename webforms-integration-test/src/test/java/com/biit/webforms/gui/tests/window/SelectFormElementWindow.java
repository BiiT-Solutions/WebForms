package com.biit.webforms.gui.tests.window;

import java.util.List;

import org.openqa.selenium.StaleElementReferenceException;

import com.vaadin.testbench.elements.CssLayoutElement;
import com.vaadin.testbench.elements.CustomComponentElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.TreeTableElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;

public class SelectFormElementWindow extends GenericAcceptCancelWindow {

	private static final String CLASS_NAME = "com.biit.webforms.gui.components.WindowTreeObject";
	private static final String SEARCH_FIELD_CAPTION = "Search";

	public TextFieldElement getSearchField() {
		return getWindow().$(TextFieldElement.class).caption(SEARCH_FIELD_CAPTION).first();
	}

	public TreeTableElement getTreeTable() {
		return getWindow().$$(VerticalLayoutElement.class).$$(CustomComponentElement.class)
				.$$(VerticalLayoutElement.class).$$(TreeTableElement.class).first();
	}

	public void searchForElement(String elementName) {
		getSearchField().sendKeys(elementName);
		getSearchField().waitForVaadin();
	}

	public List<LabelElement> getTreeTableLabels() {
		return getWindow().$(VerticalLayoutElement.class).$(CustomComponentElement.class)
				.$(VerticalLayoutElement.class).$(TreeTableElement.class).$(CustomComponentElement.class)
				.$(CssLayoutElement.class).$(LabelElement.class).all();
	}

	public void selectElementInTable(String elementName) {
		getTreeTable().waitForVaadin();
		List<LabelElement> labels = getTreeTableLabels();
		for (int index = 0; index < labels.size(); index++) {
			try {
				if (labels.get(index).getText().equals(elementName)) {
					labels.get(index).click();
					break;
				}
			} catch (StaleElementReferenceException e) {
				// Retry
				labels = getTreeTableLabels();
				index = 0;
			}
		}
	}

	@Override
	protected String getWindowId() {
		return CLASS_NAME;
	}
}
