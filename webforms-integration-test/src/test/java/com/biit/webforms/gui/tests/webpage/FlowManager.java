package com.biit.webforms.gui.tests.webpage;

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

import org.openqa.selenium.NoSuchElementException;

import com.biit.gui.tester.VaadinGuiTester;
import com.biit.gui.tester.VaadinGuiWebpage;
import com.biit.webforms.gui.tests.window.FlowCleanedWindow;
import com.biit.webforms.gui.tests.window.FlowRuleWindow;
import com.biit.webforms.gui.tests.window.SelectFormElementWindow;
import com.vaadin.testbench.elements.ImageElement;
import com.vaadin.testbench.elements.NotificationElement;
import com.vaadin.testbench.elements.TableElement;

public class FlowManager extends VaadinGuiWebpage {

	private static final String SAVE_BUTTON_CAPTION = "Save";
	private static final String NEW_RULE_BUTTON_CAPTION = "New rule";
	private static final String EDIT_RULE_BUTTON_CAPTION = "Edit rule";
	private static final String CLONE_BUTTON_CAPTION = "Clone";
	private static final String REMOVE_BUTTON_CAPTION = "Remove";
	private static final String CLEAN_FLOW_BUTTON_CAPTION = "Clean Flow";

	private static final String REDRAW_BUTTON_CAPTION = "Redraw";

	private static final String COMBOBOX_END_FORM_VALUE = "End form";

	private final FlowRuleWindow flowRuleWindow;
	private final SelectFormElementWindow selectFormElementWindow;
	private final FlowCleanedWindow flowCleanedWindow;

	public FlowManager() {
		super();
		flowRuleWindow = new FlowRuleWindow();
		addWindow(flowRuleWindow);
		selectFormElementWindow = new SelectFormElementWindow();
		addWindow(selectFormElementWindow);
		flowCleanedWindow = new FlowCleanedWindow();
		addWindow(flowCleanedWindow);
	}

	public void clickCleanFlowButton() {
		getButtonElement(CLEAN_FLOW_BUTTON_CAPTION).click();
	}

	public void clickCloneButton() {
		getButtonElement(CLONE_BUTTON_CAPTION).click();
	}

	public void clickEditRuleButton() {
		getButtonElement(EDIT_RULE_BUTTON_CAPTION).click();
	}

	public void clickNewRuleButton() {
		getButtonElement(NEW_RULE_BUTTON_CAPTION).click();
		getButtonElement(NEW_RULE_BUTTON_CAPTION).waitForVaadin();
	}

	public void clickRedrawButton() {
		getButtonElement(REDRAW_BUTTON_CAPTION).click();
	}

	public void clickRemoveButton() {
		getButtonElement(REMOVE_BUTTON_CAPTION).click();
	}

	/**
	 * Creates an 'others' flow from the start to the end element
	 *
	 * @param startNodeName
	 * @param endNodeName
	 */
	public void createEndFlow(String nodeName) {
		clickNewRuleButton();
		getFlowRuleWindow().clickFromButton();
		getSelectFormElementWindow().searchForElement(nodeName);
		getSelectFormElementWindow().selectElementInTable(nodeName);
		getSelectFormElementWindow().clickAccept();
		if (getSelectFormElementWindow().getWindow() != null) {
			try {
				getSelectFormElementWindow().getWindow().waitForVaadin();
			} catch (NullPointerException e) {
				// Do nothing
			}
		}
		getFlowRuleWindow().getTypeCombobox().selectByText(COMBOBOX_END_FORM_VALUE);
		getFlowRuleWindow().clickAccept();
	}

	/**
	 * Creates an 'others' flow from the element to the end of the form
	 *
	 * @param startNodeName
	 * @param endNodeName
	 */
	public void createOthersEndFlow(String nodeName) {
		clickNewRuleButton();
		getFlowRuleWindow().clickFromButton();
		getSelectFormElementWindow().searchForElement(nodeName);
		getSelectFormElementWindow().selectElementInTable(nodeName);
		getSelectFormElementWindow().clickAccept();
		if (getSelectFormElementWindow().getWindow() != null) {
			try {
				getSelectFormElementWindow().getWindow().waitForVaadin();
			} catch (NullPointerException e) {
				// Do nothing
			}
		}
		getFlowRuleWindow().getTypeCombobox().selectByText(COMBOBOX_END_FORM_VALUE);
		getFlowRuleWindow().clickOthersCheckBox();
		getFlowRuleWindow().clickAccept();
	}

	/**
	 * Creates an 'others' flow from the start to the end element
	 *
	 * @param startNodeName
	 * @param endNodeName
	 */
	public void createOthersFlow(String startNodeName, String endNodeName) {
		clickNewRuleButton();
		selectFromToElement(startNodeName, endNodeName);
		getFlowRuleWindow().clickOthersCheckBox();
		getFlowRuleWindow().clickAccept();
	}

	/**
	 * Creates a simple rule from A to B.<br>
	 * Nodes used here must be defined previously in the designer.
	 *
	 * @param startNodeName
	 * @param endNodeName
	 */
	public void createSimpleFlowRule(String startNodeName, String endNodeName) {
		clickNewRuleButton();
		selectFromToElement(startNodeName, endNodeName);
		getFlowRuleWindow().clickAccept();
	}

	public FlowCleanedWindow getFlowCleanedWindow() {
		return flowCleanedWindow;
	}

	public TableElement getFlowRulesTable() {
		return $(TableElement.class).first();
	}

	public FlowRuleWindow getFlowRuleWindow() {
		return flowRuleWindow;
	}

	public ImageElement getGraph() {
		return $(ImageElement.class).first();
	}

	public SelectFormElementWindow getSelectFormElementWindow() {
		return selectFormElementWindow;
	}

	@Override
	public String getWebpageUrl() {
		return null;
	}

	public void saveFlow() {
		getButtonElement(SAVE_BUTTON_CAPTION).click();
		getButtonElement(SAVE_BUTTON_CAPTION).waitForVaadin();
		
		try{
			NotificationElement notification = $(NotificationElement.class).first();
			VaadinGuiTester.checkNotificationIsHumanized(notification);
			VaadinGuiTester.closeNotification(notification);
		}catch(NoSuchElementException e){
			//Notification closed			
		}
	}

	public void selectFromToElement(String startNodeName, String endNodeName) {
		getFlowRuleWindow().clickFromButton();
		getSelectFormElementWindow().searchForElement(startNodeName);
		sleep(500);
		getSelectFormElementWindow().selectElementInTable(startNodeName);
		getSelectFormElementWindow().clickAccept();

		getFlowRuleWindow().getWindow().waitForVaadin();
		getFlowRuleWindow().clickToButton();
		getSelectFormElementWindow().searchForElement(endNodeName);
		sleep(500);
		getSelectFormElementWindow().selectElementInTable(endNodeName);
		getSelectFormElementWindow().clickAccept();
	}
}
