package com.biit.webforms.gui.tests.window;

import java.util.List;

import org.openqa.selenium.StaleElementReferenceException;

import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class LinkBlockWindow extends GenericAcceptCancelWindow {

	private static final String CLASS_NAME = "com.biit.webforms.gui.webpages.designer.WindowBlocks";

	public TreeTableElement getTreeTable() {
		return getWindow().$(TreeTableElement.class).first();
	}

	public List<LabelElement> getTreeTableLabels() {
		return getWindow().$(LabelElement.class).all();
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

	@Override
	protected String getWindowId() {
		return CLASS_NAME;
	}
}

