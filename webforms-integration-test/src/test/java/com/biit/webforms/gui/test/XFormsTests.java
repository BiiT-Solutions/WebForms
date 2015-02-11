package com.biit.webforms.gui.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.webforms.gui.test.exceptions.FieldNotEditableException;
import com.biit.webforms.gui.test.exceptions.IncorrectFileGenerationException;

public class XFormsTests extends WebFormsTester {

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

	@Test(groups = "xForms")
	public void performImpactAnalysis() {
		try {
			createFormElementsFlowAndFinishDesign();
			// Analyze the impact between versions
			getFormManager().clickXFormsButton();
			getFormManager().clickXFormsDownloadButton();
			getFormManager().getDownloadWindow().checkCorrectFileGeneration();
			getFormManager().getDownloadWindow().closeWindow();
			logOut();
			deleteForm();
		} catch ( IncorrectFileGenerationException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
