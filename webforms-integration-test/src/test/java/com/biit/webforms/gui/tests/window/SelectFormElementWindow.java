package com.biit.webforms.gui.tests.window;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Test)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
		return getWindow().$$(VerticalLayoutElement.class).$$(CustomComponentElement.class).$$(VerticalLayoutElement.class)
				.$$(TextFieldElement.class).caption(SEARCH_FIELD_CAPTION).first();
	}

	public TreeTableElement getTreeTable() {
		return getWindow().$$(VerticalLayoutElement.class).$$(CustomComponentElement.class).$$(VerticalLayoutElement.class)
				.$$(TreeTableElement.class).first();
	}

	public void searchForElement(String elementName) {
		getWindow().waitForVaadin();
		getSearchField().sendKeys(elementName);
		getSearchField().waitForVaadin();
	}

	public List<LabelElement> getTreeTableLabels() {
		return getWindow().$$(VerticalLayoutElement.class).$$(CustomComponentElement.class).$$(VerticalLayoutElement.class)
				.$$(TreeTableElement.class).$$(CustomComponentElement.class).$$(CssLayoutElement.class).$$(LabelElement.class).all();
	}

	/**
	 * Caution, this function will fail randomly if there are elements with the
	 * same name. The order is not always repeatable.
	 *
	 * @param elementName
	 */
	public void selectElementInTable(String elementName) {
		getTreeTable().waitForVaadin();
		List<LabelElement> labels = getTreeTableLabels();
		for (int index = 0; index < labels.size(); index++) {
			try {
				if (labels.get(index).getText().equals(elementName)) {
					labels.get(index).click();
					labels.get(index).waitForVaadin();
					getTreeTable().waitForVaadin();
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

	public void selectFormElement(int formElement) {
		getTreeTable().getCell(formElement, 0).click();
		clickAccept();
	}
}
