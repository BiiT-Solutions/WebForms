package com.biit.webforms.gui.tests.window;

import com.vaadin.testbench.elements.OptionGroupElement;
import com.vaadin.testbench.elements.TableElement;


public class LinkAbcdFormWindow extends GenericAcceptCancelWindow {

	private static final String CLASS_NAME = "com.biit.webforms.gui.webpages.formmanager.WindowLinkAbcdForm";
	private static final String OPTION_GROUP_VALUE = "Version: 1";
	
	@Override
	protected String getWindowId() {
		return CLASS_NAME;
	}
	
	private OptionGroupElement getOptionGroupElement(){
		return $(OptionGroupElement.class).first();
	}
	
	public void selectOptionGroupCheckBox(){
		getOptionGroupElement().selectByText(OPTION_GROUP_VALUE);
	}
	
	private TableElement getTable(){
		return getWindow().$(TableElement.class).first();
	}
	
	public void clickTableRow(int row){
		getTable().getRow(row).click();
	}

}
