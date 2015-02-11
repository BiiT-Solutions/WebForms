package com.biit.webforms.gui.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.webforms.gui.test.exceptions.FieldNotEditableException;

public class FormFlowTests extends WebFormsTester {

	private static final String NEW_FORM_NAME = "new_form_1";
	private static final String QUESTION1_NAME = "Question1";
	private static final String QUESTION2_NAME = "Question2";
	private static final String QUESTION3_NAME = "Question3";
	private static final String ANSWER1_NAME = "Q1Answer1";

	private static final Integer FIRST_ROW = 0;
	private static final Integer FIRST_COLUMN = 0;

	private static final String CLEAN_SIMPLE_OTHERS_FLOW_TEST_SCREENSHOT = "cleanSimpleOthersFlowTest";
	private static final String CLEAN_SIMPLE_OTHERS_FLOW_2_TEST_SCREENSHOT = "cleanSimpleOthersFlow2Test";
	private static final String QUESTION_EQUALS_ANSWER_FLOW_TEST_SCREENSHOT = "createQuestionEqualsAnswerFlowTest";
	private static final String QUESTION_NOT_EQUALS_ANSWER_FLOW_TEST_SCREENSHOT = "createQuestionNotEqualsAnswerFlowTest";
	private static final String ANSWERS_IN_QUESTION_FLOW_TEST_SCREENSHOT = "answersInQuestionFlowTest";
	private static final String CLEAN_SIMPLE_FLOW_TEST_SCREENSHOT = "cleanSimpleFlowTest";
	private static final String REMOVE_FLOW_TEST_SCREENSHOT = "removeFlowTest";
	private static final String CLONE_FLOW_TEST_SCREENSHOT = "cloneFlowTest";
	private static final String VALID_BASIC_FLOW_TEST_SCREENSHOT = "basicValidFlowTest";

	private static final String VALID_FLOW_TAG = "Valid";


	@Test(groups = "formFlow")
	public void validFormWithEverything() {
		try {
			loginFormAdmin1();
			getFormManager().createNewForm(NEW_FORM_NAME);
			goToDesigner();
			getDesigner().createCompleteFormAndSave();
			logOut();
			// Test finished
			// We need to delete the form created for the test, so the database
			// is clean for the next tests
			deleteForm();
		} catch (Exception e) {
			e.printStackTrace();
			// If any unexpected exception is thrown the test should fail
			Assert.fail();
		}
	}

	private void loginAndInitializeForm() {
		loginFormAdmin1();
		getFormManager().createNewForm(NEW_FORM_NAME);
		// Create a couple of categories and questions
		goToDesigner();
		try {
			getDesigner().createAndSaveSimpleFormDesign();
		} catch (FieldNotEditableException e) {
			e.printStackTrace();
		}
	}

	private void redrawSaveTakeScreenshotAndDeleteForm(String screenshotName) {
		getFlowManager().clickRedrawButton();
		takeScreenshot(screenshotName);
		getFlowManager().saveFlow();
		logOut();
		// Delete the form created for the test
		deleteForm();
	}

	@Test(groups = "formFlow")
	public void validStartEndFlow() {
		loginAndInitializeForm();
		// Edit the flow
		goToFlowManager();
		getFlowManager().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
		// Finish the test
		redrawSaveTakeScreenshotAndDeleteForm(VALID_BASIC_FLOW_TEST_SCREENSHOT);
	}

	@Test(groups = "formFlow")
	public void cloneFlow() {
		loginAndInitializeForm();
		// Edit the flow
		goToFlowManager();
		getFlowManager().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
		getFlowManager().saveFlow();
		getFlowManager().clickRedrawButton();
		// Clone rule
		getFlowManager().getFlowRulesTable().getCell(FIRST_ROW, FIRST_COLUMN).click();
		getFlowManager().clickCloneButton();
		// Finish the test
		redrawSaveTakeScreenshotAndDeleteForm(CLONE_FLOW_TEST_SCREENSHOT);

	}

	@Test(groups = "formFlow")
	public void removeFlow() {
		loginAndInitializeForm();
		// Edit the flow
		goToFlowManager();
		getFlowManager().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
		getFlowManager().saveFlow();
		getFlowManager().clickRedrawButton();
		// Clone rule
		getFlowManager().getFlowRulesTable().getCell(FIRST_ROW, FIRST_COLUMN).click();
		getFlowManager().clickRemoveButton();
		// Finish the test
		redrawSaveTakeScreenshotAndDeleteForm(REMOVE_FLOW_TEST_SCREENSHOT);
	}

	@Test(groups = "formFlow")
	public void cleanSimpleFlow() {
		loginAndInitializeForm();
		// Edit the flow
		goToFlowManager();
		getFlowManager().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
		getFlowManager().saveFlow();
		getFlowManager().clickRedrawButton();
		// Clean the rule
		getFlowManager().clickCleanFlowButton();
		getFlowManager().getFlowCleanedWindow().clickCloseButton();
		getFlowManager().saveFlow();
		// Check that it is not failing if there are no rules defined
		getFlowManager().clickCleanFlowButton();
		if (getFlowManager().getFlowCleanedWindow().isWindowVisible()) {
			getFlowManager().getFlowCleanedWindow().clickCloseButton();
		}
		// Finish the test
		redrawSaveTakeScreenshotAndDeleteForm(CLEAN_SIMPLE_FLOW_TEST_SCREENSHOT);
	}

	@Test(groups = "formFlow")
	public void cleanSimpleOthersFlow() {
		loginAndInitializeForm();
		// Create a flow
		goToFlowManager();
		getFlowManager().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
		// Edit the flow (Create the others)
		getFlowManager().getFlowRulesTable().getCell(FIRST_ROW, FIRST_COLUMN).click();
		getFlowManager().clickEditRuleButton();
		getFlowManager().getFlowRuleWindow().clickOthersCheckBox();
		getFlowManager().getFlowRuleWindow().clickAcceptButton();
		getFlowManager().saveFlow();
		// Clean the rule
		getFlowManager().clickCleanFlowButton();
		getFlowManager().getFlowCleanedWindow().clickCloseButton();
		// Finish the test
		redrawSaveTakeScreenshotAndDeleteForm(CLEAN_SIMPLE_OTHERS_FLOW_TEST_SCREENSHOT);
	}

	@Test(groups = "formFlow")
	public void cleanSimpleOthersFlow2() {
		loginAndInitializeForm();
		// Create a flow
		goToFlowManager();
		getFlowManager().createSimpleFlowRule(QUESTION1_NAME, QUESTION3_NAME);
		// Edit the flow (Create the others)
		getFlowManager().getFlowRulesTable().getCell(FIRST_ROW, FIRST_COLUMN).click();
		getFlowManager().clickEditRuleButton();
		getFlowManager().getFlowRuleWindow().clickOthersCheckBox();
		getFlowManager().getFlowRuleWindow().clickAcceptButton();
		getFlowManager().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
		// Clean the rule
		getFlowManager().clickCleanFlowButton();
		checkNotificationIsHumanized(getNotification());
		// Finish the test
		redrawSaveTakeScreenshotAndDeleteForm(CLEAN_SIMPLE_OTHERS_FLOW_2_TEST_SCREENSHOT);
	}

	// TODO FIX bug in webforms
	public void createQuestionsFlow() {
		loginAndInitializeForm();
		// Create a flow
		goToFlowManager();
		getFlowManager().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
		getFlowManager().createSimpleFlowRule(QUESTION2_NAME, QUESTION3_NAME);
		// Clean the rule

		// Finish the test
		redrawSaveTakeScreenshotAndDeleteForm(CLEAN_SIMPLE_OTHERS_FLOW_2_TEST_SCREENSHOT);
	}

	@Test(groups = "formFlow")
	public void createQuestionEqualsAnswerFlow() {
		loginAndInitializeForm();
		// Create a flow
		goToFlowManager();
		getFlowManager().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
		getFlowManager().getFlowRulesTable().getCell(FIRST_ROW, FIRST_COLUMN).click();
		getFlowManager().clickEditRuleButton();
		// Add the question == answer condition
		getFlowManager().getFlowRuleWindow().searchForElement(QUESTION1_NAME);
		getFlowManager().getFlowRuleWindow().selectElementInTreeTable(ANSWER1_NAME);
		getFlowManager().getFlowRuleWindow().clickEqualsButton();
		Assert.assertEquals(getFlowManager().getFlowRuleWindow().getValidInvalidTagValue(), VALID_FLOW_TAG);
		getFlowManager().getFlowRuleWindow().clickAcceptButton();
		// Finish the test
		redrawSaveTakeScreenshotAndDeleteForm(QUESTION_EQUALS_ANSWER_FLOW_TEST_SCREENSHOT);
	}

	@Test(groups = "formFlow")
	public void createQuestionNotEqualsAnswerFlow() {
		loginAndInitializeForm();
		// Create a flow
		goToFlowManager();
		getFlowManager().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
		getFlowManager().getFlowRulesTable().getCell(FIRST_ROW, FIRST_COLUMN).click();
		getFlowManager().clickEditRuleButton();
		// Add the question != answer condition
		getFlowManager().getFlowRuleWindow().searchForElement(QUESTION1_NAME);
		getFlowManager().getFlowRuleWindow().selectElementInTreeTable(ANSWER1_NAME);
		getFlowManager().getFlowRuleWindow().clickNotEqualsButton();
		Assert.assertEquals(getFlowManager().getFlowRuleWindow().getValidInvalidTagValue(), VALID_FLOW_TAG);
		getFlowManager().getFlowRuleWindow().clickAcceptButton();
		// Finish the test
		redrawSaveTakeScreenshotAndDeleteForm(QUESTION_NOT_EQUALS_ANSWER_FLOW_TEST_SCREENSHOT);
	}

	@Test(groups = "formFlow")
	public void createAnswersInQuestionFlow() {
		// Initialize the test
		loginAndInitializeForm();
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
		// Finish the test
		redrawSaveTakeScreenshotAndDeleteForm(ANSWERS_IN_QUESTION_FLOW_TEST_SCREENSHOT);
	}
}
