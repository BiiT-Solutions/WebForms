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

import org.openqa.selenium.Keys;

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.CheckBoxElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.CustomComponentElement;
import com.vaadin.testbench.elements.HorizontalLayoutElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TabSheetElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.TreeTableElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;

public class FlowRuleWindow extends GenericAcceptCancelWindow {

	private static final String CLASS_NAME = "com.biit.webforms.gui.webpages.floweditor.WindowFlow";
	private static final String MAIN_TREE_TABLE_ID = "com.biit.webforms.gui.webpages.floweditor.ConditionEditorControls.MainTreeTable";
	private static final String ANSWER_TREE_TABLE_ID = "com.biit.webforms.gui.webpages.floweditor.ComponentInsertAnswer.AnswerTable";
	private static final String ANSWER_COMPONENT_ID = "com.biit.webforms.gui.webpages.floweditor.ComponentInsertAnswer";

	// The system is using an empty string to select the component
	public static final String FROM_BUTTON_CAPTION = "";
	public static final String TO_BUTTON_CAPTION = "To";
	public static final String TYPE_COMBOBOX_CAPTION = "Type";
	public static final String OTHERS_CHECKBOX_CAPTION = "Others";

	private static final String CHECKBOX_RETURN_UNCHECKED = "unchecked";
	private static final String CHECKBOX_RETURN_CHECKED = "checked";

	private static final String EQUALS_BUTTON_CAPTION = "==";
	private static final String NOT_EQUALS_BUTTON_CAPTION = "<>";
	private static final String IN_BUTTON_CAPTION = "IN";

	private static final Integer TREE_ELEMENT_TAB = 0;
	private static final Integer LOGIC_EXPRESSIONS_TAB = 1;
	private static final String AND_BUTTON_CAPTION = "AND";

	public ButtonElement getFromButton(){
		return getWindow().$(ButtonElement.class).first();
	}
	
	public ButtonElement getToButton(){
		return getWindow().$(CustomComponentElement.class).caption(TO_BUTTON_CAPTION).$(ButtonElement.class).first();
	}
	
	public void clickFromButton() {
		getFromButton().click();
		getFromButton().waitForVaadin();
	}

	public void clickFromField() {
		getWindow().$(HorizontalLayoutElement.class).$(HorizontalLayoutElement.class).first().click();
	}

	public void clickRemoveFromButton() {
		getWindow().$(ButtonElement.class).get(1);
	}

	public void clickToButton() {
		getToButton().click();
		getToButton().waitForVaadin();
	}

	public void clickToField() {
		getWindow().$(CustomComponentElement.class).caption(TO_BUTTON_CAPTION).$(HorizontalLayoutElement.class).$(HorizontalLayoutElement.class).first().click();
	}

	public void clickRemoveToButton() {
		getWindow().$(CustomComponentElement.class).caption(TO_BUTTON_CAPTION).$(ButtonElement.class).get(1).click();
	}

	public ComboBoxElement getTypeCombobox() {
		return getWindow().$(ComboBoxElement.class).caption(TYPE_COMBOBOX_CAPTION).first();
	}

	public void setTypeComboboxValue(String value) {
		getTypeCombobox().selectByText(value);
	}

	public void checkOthersCheckbox() {
		if (getOthersCheckBoxValue().equals(CHECKBOX_RETURN_UNCHECKED)) {
			clickOthersCheckBox();
		}
	}

	public void uncheckOthersCheckbox() {
		if (getOthersCheckBoxValue().equals(CHECKBOX_RETURN_CHECKED)) {
			clickOthersCheckBox();
		}
	}

	public String getOthersCheckBoxValue() {
		return getWindow().$(CheckBoxElement.class).caption(OTHERS_CHECKBOX_CAPTION).first().getValue();
	}

	public void clickOthersCheckBox() {
		getWindow().$(CheckBoxElement.class).caption(OTHERS_CHECKBOX_CAPTION).first().click();
	}

	public TreeTableElement getMainTreeElementTable() {
		return getWindow().$(TreeTableElement.class).id(MAIN_TREE_TABLE_ID);
	}

	public TreeTableElement getSubTreeElementTable() {
		return getWindow().$(TreeTableElement.class).id(ANSWER_TREE_TABLE_ID);
	}

	public ButtonElement getEqualsButton() {
		return getWindow().$(ButtonElement.class).caption(EQUALS_BUTTON_CAPTION).first();
	}

	public ButtonElement getNotEqualsButton() {
		return getWindow().$(ButtonElement.class).caption(NOT_EQUALS_BUTTON_CAPTION).first();
	}

	public ButtonElement getInButton() {
		return getWindow().$(ButtonElement.class).caption(IN_BUTTON_CAPTION).first();
	}

	public void clickEqualsButton() {
		getEqualsButton().click();
		getEqualsButton().waitForVaadin();
	}

	public void clickNotEqualsButton() {
		getNotEqualsButton().click();
	}

	public void clickInButton() {
		getInButton().click();
	}

	private TextFieldElement getSearchField() {
		return getWindow().$(TextFieldElement.class).first();
	}

	public void searchForElement(String elementName) {
		getSearchField().sendKeys(elementName);
		getSearchField().waitForVaadin();
	}

	public void selectElementInAnswerTreeTable(String elementName) {
		getWindow().waitForVaadin();
		
		while(!getWindow().isDisplayed() || !getWindow().isEnabled()){
			getWindow().waitForVaadin();
		}
		while(true){
			try{
				if(getWindow().$(CustomComponentElement.class).id(ANSWER_COMPONENT_ID)!=null){
					break;
				}
			}
			catch(Exception e){
				getWindow().waitForVaadin();
			}
		}
		
		getWindow().$(CustomComponentElement.class).id(ANSWER_COMPONENT_ID).waitForVaadin();
		getMainTreeElementTable().waitForVaadin();
		
		List<LabelElement> labels = getWindow().$(CustomComponentElement.class).id(ANSWER_COMPONENT_ID)
				.$(LabelElement.class).all();
		for (LabelElement label : labels) {
			if (label.getText().equals(elementName)) {
				label.click();
				label.waitForVaadin();
				break;
			}
		}
	}

	/**
	 * Selects the element and the next element of the sub table.<br>
	 * The main element must not be the last of the table
	 */
	public void selectElementAndNextElementInSubTreeTable(String elementName) {
		getSubTreeElementTable().focus();
		getSubTreeElementTable().getCell(0, 0).click();
		sendKeyDown(Keys.SHIFT);
		pressKeys(Keys.ARROW_DOWN);
		sendKeyUp(Keys.SHIFT);
	}

	public String getValidInvalidTagValue() {
		return getWindow().$(VerticalLayoutElement.class).$$(HorizontalLayoutElement.class).$$(LabelElement.class)
				.first().getText();
	}

	private TabSheetElement getTabSheet() {
		return getWindow().$(TabSheetElement.class).first();
	}

	private ButtonElement getAndButton() {
		return getWindow().$(ButtonElement.class).caption(AND_BUTTON_CAPTION).first();
	}

	public void addAndExpression() {
		getTabSheet().openTab(LOGIC_EXPRESSIONS_TAB);
		getAndButton().click();
		getTabSheet().openTab(TREE_ELEMENT_TAB);
	}

	public void selectElementInSubTreeTable(int row) {
		getSubTreeElementTable().getCell(row, 0).click();
	}

	@Override
	protected String getWindowId() {
		return CLASS_NAME;
	}
}
