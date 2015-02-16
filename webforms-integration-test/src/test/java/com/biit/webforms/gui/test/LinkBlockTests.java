package com.biit.webforms.gui.test;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.webforms.gui.test.exceptions.FieldNotEditableException;

public class LinkBlockTests extends WebFormsTester {

	private static final String NEW_FORM_NAME = "new_form_1";
	private static final String NEW_BLOCK_NAME = "new_block_1";
	private static final Integer TREE_TABLE_ROW = 1;
	private static final Object CATEGORY_NAME = "Category";
	private static final Integer BLOCK_TABLE_ROW = 0;
	private static final Integer BLOCK_TABLE_COLUMN = 0;

	private void createSimpleBlock() {
		try {
			loginFormAdmin1();
			goToBlockManagerPage();
			getBlockManagerPage().createNewBlock(NEW_BLOCK_NAME);
			goToDesignerPage();
			getDesignerPage().addNewCategory();
			getDesignerPage().addNewInputDateQuestion();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewMultiCheckboxQuestion();
			getDesignerPage().addNewAnswer();
			getDesignerPage().addNewAnswer();
			getDesignerPage().addNewAnswer();
			getDesignerPage().saveDesign();
			goToFormManagerPage();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	private void addLinkedBlockToForm() {
		getFormManagerPage().createNewForm(NEW_FORM_NAME);
		goToDesignerPage();
		getDesignerPage().clickBlocksButton();
		getDesignerPage().clickLinkBlockButton();
		getDesignerPage().getLinkBlockWindow().clickAccceptButton();
		getDesignerPage().saveDesign();
	}

	private void deleteFormAndBlock() {
		goToFormManagerPage();
		getFormManagerPage().deleteForm(TREE_TABLE_ROW);
		goToBlockManagerPage();
		getBlockManagerPage().deleteBlock();
		logOut();
	}

	@Test(groups = "linkedBlocks")
	public void addLinkedBlockToFormTest() {
		createSimpleBlock();
		addLinkedBlockToForm();
		deleteFormAndBlock();
	}

	@Test(groups = "linkedBlocks")
	public void moveUpDownLinkedBlock() {
		createSimpleBlock();
		addLinkedBlockToForm();
		getDesignerPage().addNewCategory();
		getDesignerPage().addNewQuestion();
		getDesignerPage().clickInTreeTableRow(TREE_TABLE_ROW);
		getDesignerPage().clickUpButton();
		getDesignerPage().clickDownButton();
		getDesignerPage().saveDesign();
		deleteFormAndBlock();
	}

	@Test(groups = "linkedBlocks")
	public void removeLinkedBlockFromForm() {
		createSimpleBlock();
		addLinkedBlockToForm();
		getDesignerPage().clickInTreeTableRow(TREE_TABLE_ROW);
		getDesignerPage().clickDeleteButton();
		getDesignerPage().saveDesign();
		// Check that the category of the block is still there
		goToFormManagerPage();
		goToBlockManagerPage();
		goToDesignerPage();
		getDesignerPage().clickInTreeTableRow(TREE_TABLE_ROW);
		getDesignerPage().getCategoryPropertiesView().getTechnicalName().equals(CATEGORY_NAME);
		getDesignerPage().saveDesign();
		deleteFormAndBlock();
	}

	@Test(groups = "linkedBlocks")
	public void removeLinkedBlock() {
		createSimpleBlock();
		addLinkedBlockToForm();
		goToFormManagerPage();
		goToBlockManagerPage();
		closeNotificationIfExists();
		getBlockManagerPage().deleteBlock();
		checkNotificationIsError(getNotification());
		closeNotificationIfExists();
		deleteFormAndBlock();
	}

	@Test(groups = "linkedBlocks")
	public void removeFormWithLinkedBlock() {
		createSimpleBlock();
		addLinkedBlockToForm();
		goToFormManagerPage();
		getFormManagerPage().deleteForm(TREE_TABLE_ROW);
		goToBlockManagerPage();
		try {
			getBlockManagerPage().getBlockTable().getCell(BLOCK_TABLE_ROW, BLOCK_TABLE_COLUMN);
		} catch (NoSuchElementException e) {
			Assert.fail();
		}
		getBlockManagerPage().deleteBlock();
		logOut();
	}
}
