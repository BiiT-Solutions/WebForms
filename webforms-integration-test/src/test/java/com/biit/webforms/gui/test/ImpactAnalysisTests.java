package com.biit.webforms.gui.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.webforms.gui.test.exceptions.FieldNotEditableException;
import com.biit.webforms.gui.test.exceptions.IncorrectFileGenerationException;

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
			getFormManager().createNewForm(NEW_FORM_NAME);
			// Create a couple of categories and questions
			goToDesigner();
			getDesigner().createAndSaveSimpleFormDesign();
			// Create a flow
			goToFlowManager();
			getFlowManager().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
			getFlowManager().getFlowRulesTable().getCell(FIRST_ROW, FIRST_COLUMN).click();
			getFlowManager().clickEditRuleButton();
			// Add the question IN [answer1 answer2] condition
			getFlowManager().getFlowRuleWindow().searchForElement(QUESTION1_NAME);
			getFlowManager().getFlowRuleWindow().selectElementAndNextElementInSubTreeTable(ANSWER1_NAME);
			getFlowManager().getFlowRuleWindow().clickInButton();
			Assert.assertEquals(getFlowManager().getFlowRuleWindow().getValidInvalidTagValue(), VALID_FLOW_TAG);
			getFlowManager().getFlowRuleWindow().clickAcceptButton();
			// Redraw the graph
			getFlowManager().clickRedrawButton();
			getFlowManager().saveFlow();
			goToFormManager();
			goToDesigner();
			getDesigner().finishForm();
			clickAcceptButtonIfExists();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = "impactAnalysis")
	public void performImpactAnalysis() {
		try {
			createFormElementsFlowAndFinishDesign();
			getFormManager().clickNewButton();
			getFormManager().clickNewVersionButton();
			// Modify the form
			goToDesigner();
			getDesigner().addNewCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputPostalCodeQuestion();
			getDesigner().addNewMultiCheckboxQuestion();
			getDesigner().saveDesign();
			// Clone flow
			goToFlowManager();
			getFlowManager().getFlowRulesTable().getCell(FIRST_ROW, FIRST_COLUMN).click();
			getFlowManager().clickCloneButton();
			// Modify the original rule flow
			getFlowManager().clickEditRuleButton();
			takeScreenshot("WEBFORMS_ERROR_SCREEN_1");
			getFlowManager().getFlowRuleWindow().addAndExpression();
			takeScreenshot("WEBFORMS_ERROR_SCREEN_2");
			getFlowManager().getFlowRuleWindow().selectElementInSubTreeTable(0);
			takeScreenshot("WEBFORMS_ERROR_SCREEN_3");
			getFlowManager().getFlowRuleWindow().clickEqualsButton();
			takeScreenshot("WEBFORMS_ERROR_SCREEN_4");
//			getFlowManager().getFlowRuleWindow().addAndExpression();
//			takeScreenshot("WEBFORMS_ERROR_SCREEN_5");
//			getFlowManager().getFlowRuleWindow().selectElementInSubTreeTable(1);
//			takeScreenshot("WEBFORMS_ERROR_SCREEN_6");
//			getFlowManager().getFlowRuleWindow().clickNotEqualsButton();
//			takeScreenshot("WEBFORMS_ERROR_SCREEN_7");
//			getFlowManager().getFlowRuleWindow().getValidInvalidTagValue().equals(VALID_FLOW_TAG);
//			takeScreenshot("WEBFORMS_ERROR_SCREEN_8");
			getFlowManager().getFlowRuleWindow().clickAcceptButton();
			takeScreenshot("WEBFORMS_ERROR_SCREEN_9");
			getFlowManager().saveFlow();
			checkNotificationIsHumanized(getNotification());
			goToFormManager();
			// Analyze the impact between versions
			getFormManager().clickImpactButton();
			getFormManager().getImpactWindow().clickAcceptButton();
			getFormManager().getDownloadWindow().checkCorrectFileGeneration();
			getFormManager().getDownloadWindow().closeWindow();
			getFormManager().getTestXmlWindow().closeWindow();
			logOut();
			deleteForm();
			deleteForm();
		} catch (FieldNotEditableException | IncorrectFileGenerationException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
