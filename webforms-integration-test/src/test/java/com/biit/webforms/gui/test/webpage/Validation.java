package com.biit.webforms.gui.test.webpage;

import com.biit.gui.tester.VaadinGuiWebpage;
import com.biit.webforms.gui.test.window.WindowLinkAbcdFormWindow;
import com.vaadin.testbench.elements.TextAreaElement;

public class Validation extends VaadinGuiWebpage {

	private static final String ALL_BUTTON_CAPTION = "All";
	private static final String STRUCTURE_BUTTON_CAPTION = "Structure";
	private static final String FLOW_BUTTON_CAPTION = "Flow";
	private static final String ABCD_LINK_BUTTON_CAPTION = "ABCD Link";
	private static final String ABCD_DIFF_BUTTON_CAPTION = "ABCD Diff";
	private final WindowLinkAbcdFormWindow windowLinkAbcdFormWindow;

	public Validation() {
		super();
		windowLinkAbcdFormWindow = new WindowLinkAbcdFormWindow();
		addWindow(windowLinkAbcdFormWindow);
	}
	
	public void clickAbcdDiffButton() {
		getButtonElement(ABCD_DIFF_BUTTON_CAPTION).click();
	}
	
	public void clickAbcdLinkButton() {
		getButtonElement(ABCD_LINK_BUTTON_CAPTION).click();
	}

	public void clickAllButton() {
		getButtonElement(ALL_BUTTON_CAPTION).click();
	}

	public void clickFlowButton() {
		getButtonElement(FLOW_BUTTON_CAPTION).click();
	}

	public void clickStructureButton() {
		getButtonElement(STRUCTURE_BUTTON_CAPTION).click();
	}

	private TextAreaElement getTextArea(){
		return $(TextAreaElement.class).first();
	}
	
	public String getTextAreaValue(){
		return getTextArea().getText();
	}
	
	@Override
	public String getWebpageUrl() {
		return null;
	}

	public WindowLinkAbcdFormWindow getWindowLinkAbcdFormWindow(){
		return windowLinkAbcdFormWindow;
	}
}
