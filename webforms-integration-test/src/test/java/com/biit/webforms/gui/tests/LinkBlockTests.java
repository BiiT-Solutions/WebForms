package com.biit.webforms.gui.tests;

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

import com.biit.webforms.gui.tests.exceptions.FieldNotEditableException;
import com.biit.webforms.logger.WebformsLogger;
import com.vaadin.testbench.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LinkBlockTests extends WebFormsTester {

    private static final String NEW_FORM_NAME = "new_form_1";
    private static final String NEW_BLOCK_NAME = "new_block_1";
    private static final Integer TREE_TABLE_ROW = 1;
    private static final Object CATEGORY_NAME = "Category";
    private static final Integer BLOCK_TABLE_ROW = 0;
    private static final Integer BLOCK_TABLE_COLUMN = 0;

    private static final String QUESTION1_NAME = "question1";
    private static final String QUESTION2_NAME = "question2";

    private static final Integer FORM_ROW = 0;

    private void createSimpleBlock() throws FieldNotEditableException {
        loginFormAdmin1();
        deleteFormAndBlock();
        goToBlockManagerPage();
        getBlockManagerPage().createNewBlock(NEW_BLOCK_NAME);
        // Design building block.
        goToDesignerPage();
        getDesignerPage().addNewCategory();
        getDesignerPage().addNewInputDateQuestion();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewMultiCheckboxQuestion();
        getDesignerPage().addNewAnswer();
        getDesignerPage().addNewAnswer();
        getDesignerPage().addNewAnswer();
        getDesignerPage().addNewQuestion();
        getDesignerPage().saveDesign();
        goToFormManagerPage();
        clickAcceptButtonIfExists();
    }

    private void createSimpleBlockWithFlow() {
        try {
            loginFormAdmin1();
            deleteFormAndBlock();
            goToBlockManagerPage();
            getBlockManagerPage().createNewBlock(NEW_BLOCK_NAME);
            // Design building block.
            goToDesignerPage();
            getDesignerPage().addNewCategory();
            getDesignerPage().addNewInputDateQuestion();
            getDesignerPage().addNewGroup();
            getDesignerPage().addNewMultiCheckboxQuestion();
            getDesignerPage().getQuestionPropertiesView().setTechnicalName(QUESTION1_NAME);
            getDesignerPage().addNewAnswer();
            getDesignerPage().addNewAnswer();
            getDesignerPage().addNewAnswer();
            getDesignerPage().addNewQuestion();
            getDesignerPage().saveDesign();
            // Add some flow.
            goToFlowManagerPage();
            getFlowManagerPage().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
            getFlowManagerPage().saveFlow();
            goToFormManagerPage();
        } catch (FieldNotEditableException e) {
            WebformsLogger.errorMessage(this.getClass().getName(), e);
            Assert.fail();
        }
    }

    private void addElementToBlock() throws FieldNotEditableException {
        goToBlockManagerPage();
        try {
            getBlockManagerPage().getBlockTable().getCell(BLOCK_TABLE_ROW, BLOCK_TABLE_COLUMN);
        } catch (NoSuchElementException e) {
            WebformsLogger.errorMessage(this.getClass().getName(), e);
            Assert.fail();
        }
        goToDesignerPage();
        // Uncollapse category.
        getDesignerPage().getTreeTable().getCell(1, 0).click();
        getDesignerPage().addNewQuestion();
        getDesignerPage().saveDesign();
    }

    private void addLinkedBlockToForm() {
        getFormManagerPage().createNewForm(NEW_FORM_NAME);
        goToDesignerPage();
        getDesignerPage().clickBlocksButton();
        getDesignerPage().clickLinkBlockButton();
        getDesignerPage().getLinkBlockWindow().clickAccept();
        getDesignerPage().saveDesign();
    }

    private void deleteFormAndBlock() {
        goToFormManagerPage();
        clickAcceptButtonIfExists();
        getFormManagerPage().deleteAllCreatedForms();
        goToBlockManagerPage();
        getBlockManagerPage().deleteAllCreatedBlocks();
    }

    @Test(groups = "linkedBlocks")
    public void addLinkedBlockToFormTest() throws FieldNotEditableException {
        createSimpleBlock();
        addLinkedBlockToForm();
        deleteFormAndBlock();
        logOut();
    }

    @Test(groups = "linkedBlocks")
    public void moveUpDownLinkedBlock() throws FieldNotEditableException {
        createSimpleBlock();
        addLinkedBlockToForm();
        getDesignerPage().clickInTreeTableRow(0);
        getDesignerPage().addNewCategory();
        getDesignerPage().addNewQuestion();
        getDesignerPage().clickInTreeTableRow(TREE_TABLE_ROW);
        getDesignerPage().clickUpButton();
        getDesignerPage().clickDownButton();
        getDesignerPage().saveDesign();
        deleteFormAndBlock();
        logOut();
    }

    @Test(groups = "linkedBlocks")
    public void removeLinkedBlockFromForm() throws FieldNotEditableException {
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
        logOut();
    }

    @Test(groups = "linkedBlocks")
    public void removeLinkedBlock() throws FieldNotEditableException {
        createSimpleBlock();
        addLinkedBlockToForm();
        goToFormManagerPage();
        goToBlockManagerPage();
        closeNotificationIfExists();
        getBlockManagerPage().deleteBlock();
        checkNotificationIsError(getNotification());
        closeNotificationIfExists();
        deleteFormAndBlock();
        logOut();
    }

    @Test(groups = "linkedBlocks")
    public void removeFormWithLinkedBlock() throws FieldNotEditableException {
        createSimpleBlock();
        addLinkedBlockToForm();
        goToFormManagerPage();
        sleep();
        getFormManagerPage().deleteForm(TREE_TABLE_ROW);
        goToBlockManagerPage();
        try {
            getBlockManagerPage().getBlockTable().getCell(BLOCK_TABLE_ROW, BLOCK_TABLE_COLUMN);
        } catch (NoSuchElementException e) {
            WebformsLogger.errorMessage(this.getClass().getName(), e);
            Assert.fail();
        }
        getBlockManagerPage().deleteBlock();
        logOut();
    }

    @Test(groups = "linkedBlocks")
    public void hideElement1() {
        createSimpleBlockWithFlow();
        addLinkedBlockToForm();
        goToFlowManagerPage();
        Assert.assertNotNull(getFlowManagerPage().getFlowRulesTable().getRow(FORM_ROW));
        goToDesignerPage();

        // Hide question1
        // Click on a row sometimes fails. Use cell better.
        getDesignerPage().getTreeTable().getCell(4, 0).click();
        getDesignerPage().getTreeTable().waitForVaadin();

        // hide element.
        sleep();
        Assert.assertTrue(getDesignerPage().getHideButton().isEnabled());
        getDesignerPage().getHideButton().click();
        getDesignerPage().saveDesign();

        goToFlowManagerPage();
        // Flow table has a dummy list. Then always exists one row.
        Assert.assertEquals(getFlowManagerPage().getFlowRulesTable().findElements(By.vaadin("#row[0]")).size(), 1);
        Assert.assertEquals(getFlowManagerPage().getFlowRulesTable().findElements(By.vaadin("#row[1]")).size(), 0);

        deleteFormAndBlock();
        logOut();
    }

    @Test(groups = "linkedBlocks")
    public void hideElement2() {
        createSimpleBlockWithFlow();
        addLinkedBlockToForm();
        goToFlowManagerPage();
        Assert.assertNotNull(getFlowManagerPage().getFlowRulesTable().getRow(FORM_ROW));
        goToDesignerPage();

        // Hide question2
        // Click on a row sometimes fails. Use cell better.
        getDesignerPage().getTreeTable().getCell(8, 0).click();
        getDesignerPage().getTreeTable().waitForVaadin();
        // hide element.
        sleep();
        Assert.assertTrue(getDesignerPage().getHideButton().isEnabled());
        getDesignerPage().getHideButton().click();
        getDesignerPage().saveDesign();

        goToFlowManagerPage();
        // Flow table has a dummy list. Then always exists one row.
        Assert.assertEquals(getFlowManagerPage().getFlowRulesTable().findElements(By.vaadin("#row[0]")).size(), 1);
        Assert.assertEquals(getFlowManagerPage().getFlowRulesTable().findElements(By.vaadin("#row[1]")).size(), 0);

        deleteFormAndBlock();
        logOut();
    }

    @Test(groups = "linkedBlocks")
    public void modifyBlockAlsoModifyForm() throws FieldNotEditableException {
        createSimpleBlock();
        addLinkedBlockToForm();

        // Go to form manager due to designer has a button called blocks also!
        goToFormManagerPage();
        // Add a new element to the building block.
        addElementToBlock();

        // Check that the element is visible in the form designer.
        goToFormManagerPage(); // Form already selected.
        goToDesignerPage();
        // Select category and uncollapse it.
        getDesignerPage().getTreeTable().getRow(1).toggleExpanded();
        getDesignerPage().getTreeTable().waitForVaadin();

        // Select the new element. If no error, it exists.
        getDesignerPage().getTreeTable().getCell(4, 0);

        deleteFormAndBlock();
        logOut();
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
