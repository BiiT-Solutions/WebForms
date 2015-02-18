package com.biit.webforms.gui.test.window;

import com.vaadin.testbench.elements.TreeTableElement;


public class ImportAbcdFormWindow extends GenericAcceptCancelWindow {

	private static final Integer TREE_TABLE_FORM_ROW = 1;
	private static final Integer TREE_TABLE_FORM_COLUMN = 0;
	private static final String CLASS_NAME = "com.biit.webforms.gui.webpages.formmanager.WindowImportAbcdForms";
	
	@Override
	protected String getWindowId() {
		return CLASS_NAME;
	}
	
	private TreeTableElement getTreeTable(){
		return getWindow().$(TreeTableElement.class).first();
	}
	
	public void selectAbcdForm(){
		getTreeTable().getCell(TREE_TABLE_FORM_ROW, TREE_TABLE_FORM_COLUMN).click();
	}

}
