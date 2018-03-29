package com.biit.webforms.gui.tests.window;

import com.vaadin.testbench.elements.TextFieldElement;

public class AnswerRangesWindow extends GenericAcceptCancelWindow {

	
	private static final String CLASS_NAME = "com.biit.webforms.gui.webpages.designer.WindowCreateAnswerRanges";
	private static final String LOWER_VALUE_CAPTION = "Lower value:";
	private static final String UPPER_VALUE_CAPTION = "Upper value:";
	private static final String INCREASE_VALUE_CAPTION = "Increase value:";
	
	
	
	public TextFieldElement getLowerValueField() {
		return getWindow().$(TextFieldElement.class).caption(LOWER_VALUE_CAPTION).first();
	}
	
	public TextFieldElement getUpperValueField() {
		return getWindow().$(TextFieldElement.class).caption(UPPER_VALUE_CAPTION).first();
	}
	
	public TextFieldElement getIncreaseValueField() {
		return getWindow().$(TextFieldElement.class).caption(INCREASE_VALUE_CAPTION).first();
	}
	
	public void fillRangeWindow(String lower, String upper, String steap) {
		getLowerValueField().setValue(lower);
		getUpperValueField().setValue(upper);
		getIncreaseValueField().setValue(steap);
	}
	
	@Override
	protected String getWindowId() {
		return CLASS_NAME;
	}

}
