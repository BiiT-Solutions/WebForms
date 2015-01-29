package com.biit.webforms.gui.test.webpage;

import com.biit.gui.tester.VaadinGuiWebpage;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.HorizontalLayoutElement;

public class Designer extends VaadinGuiWebpage {

	public static final String SAVE_BUTTON_CAPTION = "Save";
	public static final String CATEGORY_BUTTON_CAPTION = "Category";
	public static final String FINISH_BUTTON_CAPTION = "Finish";
	public static final Integer LEFT_SCROLL_PIXELS = 500;

	public Designer() {
		super();
	}

	@Override
	public String getWebpageUrl() {
		return null;
	}

	public ButtonElement getSaveButton() {
		return $(ButtonElement.class).caption(SAVE_BUTTON_CAPTION).first();
	}

	public ButtonElement getCategoryButton() {
		return $(ButtonElement.class).caption(CATEGORY_BUTTON_CAPTION).first();
	}
	
	public ButtonElement getFinishButton() {
		$(HorizontalLayoutElement.class).$$(HorizontalLayoutElement.class).first().scrollLeft(LEFT_SCROLL_PIXELS);;
		return $(ButtonElement.class).caption(FINISH_BUTTON_CAPTION).first();
	}

	public void saveDesign() {
		getSaveButton().click();
	}

	public void addNewCategory() {
		getCategoryButton().click();
	}
	
	public void finishForm() {
		getFinishButton().click();
	}
}