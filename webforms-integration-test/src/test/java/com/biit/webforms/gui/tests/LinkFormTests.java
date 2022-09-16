package com.biit.webforms.gui.tests;

import com.biit.webforms.gui.tests.exceptions.FieldNotEditableException;
import com.biit.webforms.logger.WebformsLogger;
import com.vaadin.testbench.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LinkFormTests extends WebFormsTester {

    private static final String NEW_FORM_NAME = "new_form_1";
    private static final String QUESTION1_NAME = "Question1";
    private static final String QUESTION2_NAME = "Question2";
    private static final String QUESTION3_NAME = "Question3";
    private static final String ANSWER1_NAME = "Q1Answer1";
    private static final String ANSWER2_NAME = "Q1Answer2";
    private static final String NEW_FORM_REFERENCE_NAME = NEW_FORM_NAME + "_reference";

    private static final String VALID_FLOW_TAG = "Valid";

    private void loginAndInitializeSimpleForm() {
        loginFormAdmin1();
        // Remove previous forms.
        deleteFormAndFormReference();

        getFormManagerPage().createNewForm(NEW_FORM_NAME);
        // Create a couple of categories and questions
        goToDesignerPage();
        try {
            getDesignerPage().createAndSaveSimpleFormDesign();
            closeNotificationIfExists();
        } catch (FieldNotEditableException e) {
            WebformsLogger.errorMessage(this.getClass().getName(), e);
        }
        addSomeFlow(QUESTION1_NAME, QUESTION2_NAME, ANSWER1_NAME, 0);
        addSomeFlow(QUESTION1_NAME, QUESTION3_NAME, ANSWER2_NAME, 1);
    }

    private void addSomeFlow(String originName, String destinationName, String answer, int flowRow) {
        // Create a flow
        goToFlowManagerPage();
        getFlowManagerPage().createSimpleFlowRule(originName, destinationName);
        getFlowManagerPage().getFlowRulesTable().getCell(flowRow, 0).click();
        getFlowManagerPage().clickEditRuleButton();
        // Add the question != answer condition
        getFlowManagerPage().getFlowRuleWindow().searchForElement(originName);
        getFlowManagerPage().getFlowRuleWindow().selectElementInAnswerTreeTable(answer);
        getFlowManagerPage().getFlowRuleWindow().clickNotEqualsButton();
        Assert.assertEquals(getFlowManagerPage().getFlowRuleWindow().getValidInvalidTagValue(), VALID_FLOW_TAG);
        getFlowManagerPage().getFlowRuleWindow().clickAccept();
        getFlowManagerPage().saveFlow();
    }

    private void loginAndCreateFormReference() {
        loginAndInitializeSimpleForm();
        goToFormManagerPage();
        getFormManagerPage().createNewFormReference(NEW_FORM_REFERENCE_NAME, 1);
    }

    private void deleteFormAndFormReference() {
        goToFormManagerPage();
        try {
            getFormManagerPage().deleteForm(3);
            getFormManagerPage().deleteForm(1);
        } catch (NoSuchElementException e) {
            return;
        }
        getFormManagerPage().deleteAllCreatedForms();
        goToBlockManagerPage();
        getFormManagerPage().deleteAllBuildingBlocks();
        goToFormManagerPage();
    }

    @Test(groups = "linkedForms")
    public void createFormReference() {
        loginAndCreateFormReference();
        deleteFormAndFormReference();
        logOut();
    }

    @Test(groups = "linkedForms")
    public void formCannotBeRemovedIfUsedAsReference() {
        loginAndCreateFormReference();
        goToFormManagerPage();
        getFormManagerPage().deleteForm(1);
        checkNotificationIsError(getNotification());
        sleep();
        deleteFormAndFormReference();
        logOut();
    }

    /**
     * 0 Hidding a referenced question also hides the flow related to this
     * question.
     */
    @Test(groups = "linkedForms")
    public void hideElement1() {
        loginAndCreateFormReference();
        goToFlowManagerPage();
        Assert.assertNotNull(getFlowManagerPage().getFlowRulesTable().getRow(0));
        goToDesignerPage();

        // Hide question1
        // Select category and uncollapse it.
        getDesignerPage().getTreeTable().getRow(1).toggleExpanded();
        getDesignerPage().getTreeTable().waitForVaadin();
        // Click on a row sometimes fails. Use cell better.
        getDesignerPage().getTreeTable().getCell(2, 0).click();
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

        deleteFormAndFormReference();
        logOut();
    }

    @Test(groups = "linkedForms")
    public void modifyFormReferenceAlsoModifyForm() throws FieldNotEditableException {
        loginAndCreateFormReference();
        goToFlowManagerPage();
        Assert.assertNotNull(getFlowManagerPage().getFlowRulesTable().getRow(0));

        // Go to form manager and edit first form.
        goToFormManagerPage();
        // To avoid errors, first we select other element of the table
        getFormManagerPage().getFormTable().getCell(0, 0).click();
        getFormManagerPage().getFormTable().getCell(1, 0).click();

        goToDesignerPage();
        // Modify Question1
        // Select category and uncollapse it.
        getDesignerPage().getTreeTable().getRow(1).toggleExpanded();
        getDesignerPage().getTreeTable().waitForVaadin();
        // Click on a row sometimes fails. Use cell better.
        getDesignerPage().getTreeTable().getCell(2, 0).click();
        getDesignerPage().getTreeTable().waitForVaadin();
        getDesignerPage().getQuestionPropertiesView().setTechnicalName(QUESTION1_NAME + "_modif");
        // Launch event.
        getFormManagerPage().getFormTable().getCell(0, 0).click();
        getDesignerPage().saveDesign();

        // Check the element in the other form.
        goToFormManagerPage();
        getFormManagerPage().getFormTable().getCell(3, 0).click();
        goToDesignerPage();
        // Select category and uncollapse it.
        getDesignerPage().getTreeTable().getRow(1).toggleExpanded();
        getDesignerPage().getTreeTable().waitForVaadin();
        getDesignerPage().getTreeTable().getCell(2, 0).click();
        getDesignerPage().getTreeTable().waitForVaadin();
        Assert.assertEquals(getDesignerPage().getQuestionPropertiesView().getTechnicalName(), QUESTION1_NAME + "_modif");

        goToFormManagerPage();
        deleteFormAndFormReference();
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
