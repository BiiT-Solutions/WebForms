package com.biit.webforms.gui.test.window;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.StaleElementReferenceException;

import com.biit.gui.tester.VaadinGuiWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TreeTableElement;
import com.vaadin.testbench.elements.WindowElement;

public class LinkBlockWindow extends VaadinGuiWindow {

	private static final String WINDOW_CAPTION = "Link Block";
	private static final String ACCEPT_BUTTON_CAPTION = "Accept";
	private static final String CANCEL_BUTTON_CAPTION = "Cancel";

	private ButtonElement getAcceptButton() {
		return $(ButtonElement.class).caption(ACCEPT_BUTTON_CAPTION).first();
	}

	private ButtonElement getCancelButton() {
		return $(ButtonElement.class).caption(CANCEL_BUTTON_CAPTION).first();
	}

	public TreeTableElement getTreeTable() {
		return $$(WindowElement.class).caption(WINDOW_CAPTION).$(TreeTableElement.class).first();
	}

	public void clickAccceptButton() {
		Assert.assertTrue(getAcceptButton().isEnabled());
		getAcceptButton().click();
	}

	public void clickCancelButton() {
		getCancelButton().click();
	}

	public List<LabelElement> getTreeTableLabels() {
		return $$(WindowElement.class).caption(WINDOW_CAPTION).$(LabelElement.class).all();
	}

	public void selectElementInTable(String elementName) {
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
}

