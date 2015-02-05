package com.biit.webforms.gui.test.window;

import java.util.List;

import org.openqa.selenium.Keys;

import com.biit.gui.tester.VaadinGuiWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.CheckBoxElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.CustomComponentElement;
import com.vaadin.testbench.elements.HorizontalLayoutElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.TreeTableElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;
import com.vaadin.testbench.elements.WindowElement;

public class FlowRuleWindow extends VaadinGuiWindow {

	// The system is using an empty string to select the component
	public static final String FROM_BUTTON_CAPTION = "";
	public static final String TO_BUTTON_CAPTION = "To";
	public static final String TYPE_COMBOBOX_CAPTION = "Type";
	public static final String OTHERS_CHECKBOX_CAPTION = "Others";

	private static final String CHECKBOX_RETURN_UNCHECKED = "unchecked";
	private static final String CHECKBOX_RETURN_CHECKED = "checked";

	private static final String ACCEPT_BUTTON_CAPTION = "Accept";
	private static final String CANCEL_BUTTON_CAPTION = "Cancel";

	private static final String EQUALS_BUTTON_CAPTION = "==";
	private static final String NOT_EQUALS_BUTTON_CAPTION = "<>";
	private static final String IN_BUTTON_CAPTION = "IN";

	public void clickFromButton() {
		$$(WindowElement.class).caption(FROM_BUTTON_CAPTION).$(ButtonElement.class).first().click();
	}

	public void clickRemoveFromButton() {
		$$(WindowElement.class).caption(FROM_BUTTON_CAPTION).$(ButtonElement.class).get(1);
	}

	public void clickToButton() {
		$(CustomComponentElement.class).caption(TO_BUTTON_CAPTION).$(ButtonElement.class).first().click();
	}

	public void clickRemoveToButton() {
		$(CustomComponentElement.class).caption(TO_BUTTON_CAPTION).$(ButtonElement.class).get(1).click();
	}

	public ComboBoxElement getTypeCombobox() {
		return $(ComboBoxElement.class).caption(TYPE_COMBOBOX_CAPTION).first();
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
		return $(CheckBoxElement.class).caption(OTHERS_CHECKBOX_CAPTION).first().getValue();
	}

	public void clickOthersCheckBox() {
		$(CheckBoxElement.class).caption(OTHERS_CHECKBOX_CAPTION).first().click();
	}

	public void clickAcceptButton() {
		$(ButtonElement.class).caption(ACCEPT_BUTTON_CAPTION).first().click();
	}

	public void clickCancelButton() {
		$(ButtonElement.class).caption(CANCEL_BUTTON_CAPTION).first().click();
	}

	public TreeTableElement getMainTreeElementTable() {
		return $(TreeTableElement.class).first();
	}

	public TreeTableElement getSubTreeElementTable() {
		return $(TreeTableElement.class).get(1);
	}

	public ButtonElement getEqualsButton() {
		return $(ButtonElement.class).caption(EQUALS_BUTTON_CAPTION).first();
	}

	public ButtonElement getNotEqualsButton() {
		return $(ButtonElement.class).caption(NOT_EQUALS_BUTTON_CAPTION).first();
	}

	public ButtonElement getInButton() {
		return $(ButtonElement.class).caption(IN_BUTTON_CAPTION).first();
	}

	public void clickEqualsButton() {
		getEqualsButton().click();
	}

	public void clickNotEqualsButton() {
		getNotEqualsButton().click();
	}

	public void clickInButton() {
		getInButton().click();
	}

	private TextFieldElement getSearchField() {
		return $(TextFieldElement.class).first();
	}

	public void searchForElement(String elementName) {
		getSearchField().sendKeys(elementName);
	}

	public void selectElementInTreeTable(String elementName) {
		List<LabelElement> labels = $(TreeTableElement.class).$(LabelElement.class).all();
		for (LabelElement label : labels) {
			if (label.getText().equals(elementName)) {
				label.click();
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
//		pressKeys(Keys.ARROW_DOWN);
		sendKeyDown(Keys.SHIFT);
		pressKeys(Keys.ARROW_DOWN);
//		pressKeys(Keys.ARROW_UP);
		sendKeyUp(Keys.SHIFT);
	}

	public String getValidInvalidTagValue() {
		return $(VerticalLayoutElement.class).$$(HorizontalLayoutElement.class).$$(LabelElement.class).first()
				.getText();
	}
}
