package com.biit.webforms.gui.test.webpage;

import org.openqa.selenium.NoSuchElementException;

import com.biit.gui.tester.VaadinGuiWebpage;
import com.biit.webforms.gui.test.window.NewBlockWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TableElement;

public class BlockManager extends VaadinGuiWebpage {

	private static final String NEW_BLOCK_BUTTON_CAPTION = "New Block";
	private static final String REMOVE_BLOCK_BUTTON_CAPTION = "Remove Block";

	private final NewBlockWindow newBlockWindow;

	public BlockManager() {
		super();
		newBlockWindow = new NewBlockWindow();
		addWindow(newBlockWindow);
	}

	public NewBlockWindow getNewBlockWindow() {
		return newBlockWindow;
	}

	@Override
	public String getWebpageUrl() {
		return null;
	}

	public ButtonElement getNewBlockButton() {
		return $(ButtonElement.class).caption(NEW_BLOCK_BUTTON_CAPTION).first();
	}

	public ButtonElement getRemoveBlockButton() {
		return $(ButtonElement.class).caption(REMOVE_BLOCK_BUTTON_CAPTION).first();
	}

	public void createNewBlock(String blockName) {
		if(isBlockAlreadyCreated(blockName)){
			deleteBlock();
		}
		openNewBlockWindow();
		getNewBlockWindow().createNewBlock(blockName);
	}

	private void openNewBlockWindow() {
		getNewBlockButton().click();
	}

	public void deleteBlock() {
		getRemoveBlockButton().click();
		clickAcceptButtonIfExists();
	}

	public TableElement getBlockTable() {
		return $(TableElement.class).first();
	}

	public void selectBlock(int row) {
		getBlockTable().getCell(row, 0).click();
	}

	public boolean isBlockAlreadyCreated(String blockName) {
		try {
			getBlockTable().getCell(0, 0).getText().equals(blockName);
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}
}