package com.biit.webforms.gui.tests;

import com.biit.webforms.gui.tests.exceptions.FieldNotEditableException;
import com.biit.webforms.gui.tests.exceptions.IncorrectFileGenerationException;
import com.biit.webforms.logger.WebformsLogger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDateTime;

public class ImpactAnalysisTests extends WebFormsTester {

    private static final String NEW_FORM_NAME = "new_form_1";
    private static final String QUESTION1_NAME = "Question1";
    private static final String QUESTION2_NAME = "Question2";
    private static final String ANSWER1_NAME = "Q1Answer1";

    private static final Integer FIRST_ROW = 0;
    private static final Integer FIRST_COLUMN = 0;

    private static final String VALID_FLOW_TAG = "Valid";

    private void createFormElementsFlowAndFinishDesign() {
        try {
            loginFormAdmin1();
            getFormManagerPage().createNewForm(NEW_FORM_NAME);
            // Create a couple of categories and questions
            goToDesignerPage();
            getDesignerPage().createAndSaveSimpleFormDesign();
            // Create a flow
            goToFlowManagerPage();
            getFlowManagerPage().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
            getFlowManagerPage().getFlowRulesTable().getCell(FIRST_ROW, FIRST_COLUMN).click();
            getFlowManagerPage().clickEditRuleButton();
            // Add the question IN [answer1 answer2] condition
            getFlowManagerPage().getFlowRuleWindow().searchForElement(QUESTION1_NAME);
            getFlowManagerPage().getFlowRuleWindow().selectElementAndNextElementInSubTreeTable(ANSWER1_NAME);
            getFlowManagerPage().getFlowRuleWindow().clickInButton();
            Assert.assertEquals(getFlowManagerPage().getFlowRuleWindow().getValidInvalidTagValue(), VALID_FLOW_TAG);
            getFlowManagerPage().getFlowRuleWindow().clickAccept();
            // Redraw the graph
            getFlowManagerPage().clickRedrawButton();
            getFlowManagerPage().saveFlow();
            takeScreenshot(LocalDateTime.now() + "_saved_flow");
            goToFormManagerPage();
            goToDesignerPage();
            takeScreenshot(LocalDateTime.now() + "_before_finishing");
            getDesignerPage().finishForm();
            takeScreenshot(LocalDateTime.now() + "_after_finishing");
            clickAcceptButtonIfExists();
            takeScreenshot(LocalDateTime.now() + "_after_finishing_accept");
        } catch (FieldNotEditableException e) {
            WebformsLogger.errorMessage(this.getClass().getName(), e);
            Assert.fail();
        }
    }

    @Test(groups = "impactAnalysis")
    public void performImpactAnalysis() {
        try {
            createFormElementsFlowAndFinishDesign();
            getFormManagerPage().clickNewButton();
            takeScreenshot(LocalDateTime.now() + "_before_clicking_new_version");
            getFormManagerPage().clickNewVersionButton();
            takeScreenshot(LocalDateTime.now() + "_after_clicking_new_version");
            // Modify the form
            goToDesignerPage();
            takeScreenshot(LocalDateTime.now() + "_after_clicking_new_version_new_category");
            getDesignerPage().addNewCategory();
            getDesignerPage().addNewGroup();
            getDesignerPage().addNewInputPostalCodeQuestion();
            getDesignerPage().addNewMultiCheckboxQuestion();
            getDesignerPage().saveDesign();
            closeNotificationIfExists();
            // Clone flow
            goToFlowManagerPage();
            getFlowManagerPage().getFlowRulesTable().getCell(FIRST_ROW, FIRST_COLUMN).click();
            getFlowManagerPage().clickCloneButton();
            // Modify the original rule flow
            getFlowManagerPage().clickEditRuleButton();
            getFlowManagerPage().getFlowRuleWindow().addAndExpression();
            getFlowManagerPage().getFlowRuleWindow().selectElementInSubTreeTable(0);
            getFlowManagerPage().getFlowRuleWindow().clickEqualsButton();
            getFlowManagerPage().getFlowRuleWindow().clickAccept();
            getFlowManagerPage().saveFlow();
            goToFormManagerPage();
            // Analyze the impact between versions
            getFormManagerPage().clickImpactButton();
            getFormManagerPage().getImpactWindow().clickAcceptButton();
            getFormManagerPage().getDownloadWindow().checkCorrectFileGeneration();
            getFormManagerPage().getDownloadWindow().closeWindow();
            getFormManagerPage().getImpactWindow().closeWindow();
            logOut();
            // Delete the two versions of the form
            getFormManagerPage().deleteAllCreatedForms();
        } catch (FieldNotEditableException | IncorrectFileGenerationException e) {
            WebformsLogger.errorMessage(this.getClass().getName(), e);
            Assert.fail();
        }
    }
}
