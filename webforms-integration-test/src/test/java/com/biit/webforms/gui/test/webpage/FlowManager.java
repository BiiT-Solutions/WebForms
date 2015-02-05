package com.biit.webforms.gui.test.webpage;

import com.biit.gui.tester.VaadinGuiWebpage;
import com.biit.webforms.gui.test.window.FlowCleanedWindow;
import com.biit.webforms.gui.test.window.FlowRuleWindow;
import com.biit.webforms.gui.test.window.SelectFormElementWindow;
import com.vaadin.testbench.elements.ImageElement;
import com.vaadin.testbench.elements.TableElement;

public class FlowManager extends VaadinGuiWebpage {

	private static final String SAVE_BUTTON_CAPTION = "Save";
	private static final String NEW_RULE_BUTTON_CAPTION = "New rule";
	private static final String EDIT_RULE_BUTTON_CAPTION = "Edit rule";
	private static final String CLONE_BUTTON_CAPTION = "Clone";
	private static final String REMOVE_BUTTON_CAPTION = "Remove";
	private static final String CLEAN_FLOW_BUTTON_CAPTION = "Clean Flow";
	
	private static final String REDRAW_BUTTON_CAPTION = "Redraw";

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

	public FlowRuleWindow getFlowRuleWindow() {
		return flowRuleWindow;
	}

	public SelectFormElementWindow getSelectFormElementWindow() {
		return selectFormElementWindow;
	}
	
	public FlowCleanedWindow getFlowCleanedWindow() {
		return flowCleanedWindow;
	}

	public void saveFlow() {
		getButtonElement(SAVE_BUTTON_CAPTION).click();
	}

	public void clickNewRuleButton() {
		getButtonElement(NEW_RULE_BUTTON_CAPTION).click();
	}

	public void clickEditRuleButton() {
		getButtonElement(EDIT_RULE_BUTTON_CAPTION).click();
	}

	public void clickCloneButton() {
		getButtonElement(CLONE_BUTTON_CAPTION).click();
	}

	public void clickRemoveButton() {
		getButtonElement(REMOVE_BUTTON_CAPTION).click();
	}

	public void clickCleanFlowButton() {
		getButtonElement(CLEAN_FLOW_BUTTON_CAPTION).click();
	}
	
	public void clickRedrawButton() {
		getButtonElement(REDRAW_BUTTON_CAPTION).click();
	}

	@Override
	public String getWebpageUrl() {
		return null;
	}

	public TableElement getFlowRulesTable() {
		return $(TableElement.class).first();
	}

	public ImageElement getGraph() {
		return $(ImageElement.class).first();
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
		getFlowRuleWindow().clickFromButton();
		getSelectFormElementWindow().searchForElement(startNodeName);
		getSelectFormElementWindow().selectElementInTable(startNodeName);
		getSelectFormElementWindow().clickAccceptButton();
		getFlowRuleWindow().clickToButton();
		getSelectFormElementWindow().searchForElement(endNodeName);
		getSelectFormElementWindow().selectElementInTable(endNodeName);
		getSelectFormElementWindow().clickAccceptButton();
		getFlowRuleWindow().clickAcceptButton();
	}

}
