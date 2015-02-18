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
	public void cleanSimpleFlow() {
		System.out.println("TEST_NAME: cleanSimpleFlow");
		loginAndInitializeForm();
		// Edit the flow
		goToFlowManagerPage();
		getFlowManagerPage().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
		getFlowManagerPage().saveFlow();
		getFlowManagerPage().clickRedrawButton();
		// Clean the rule
		getFlowManagerPage().clickCleanFlowButton();
		getFlowManagerPage().getFlowCleanedWindow().clickCloseButton();
		getFlowManagerPage().saveFlow();
		// Check that it is not failing if there are no rules defined
		getFlowManagerPage().clickCleanFlowButton();
		if (getFlowManagerPage().getFlowCleanedWindow().isWindowVisible()) {
			getFlowManagerPage().getFlowCleanedWindow().clickCloseButton();
		}
		// Finish the test
		redrawSaveTakeScreenshotAndDeleteForm(CLEAN_SIMPLE_FLOW_TEST_SCREENSHOT);
	}

	@Test(groups = "formFlow")
	public void cleanSimpleOthersFlow() {
		System.out.println("TEST_NAME: cleanSimpleOthersFlow");
		loginAndInitializeForm();
		// Create a flow
		goToFlowManagerPage();
		getFlowManagerPage().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
		// Edit the flow (Create the others)
		getFlowManagerPage().getFlowRulesTable().getCell(FIRST_ROW, FIRST_COLUMN).click();
		getFlowManagerPage().clickEditRuleButton();
		getFlowManagerPage().getFlowRuleWindow().clickOthersCheckBox();
		getFlowManagerPage().getFlowRuleWindow().clickAcceptButton();
		getFlowManagerPage().saveFlow();
		// Clean the rule
		getFlowManagerPage().clickCleanFlowButton();
		getFlowManagerPage().getFlowCleanedWindow().clickCloseButton();
		// Finish the test
		redrawSaveTakeScreenshotAndDeleteForm(CLEAN_SIMPLE_OTHERS_FLOW_TEST_SCREENSHOT);
	}

	@Test(groups = "formFlow")
	public void cleanSimpleOthersFlow2() {
		System.out.println("TEST_NAME: cleanSimpleOthersFlow2");
		loginAndInitializeForm();
		// Create a flow
		goToFlowManagerPage();
		getFlowManagerPage().createSimpleFlowRule(QUESTION1_NAME, QUESTION3_NAME);
		// Edit the flow (Create the others)
		getFlowManagerPage().getFlowRulesTable().getCell(FIRST_ROW, FIRST_COLUMN).click();
		getFlowManagerPage().clickEditRuleButton();
		getFlowManagerPage().getFlowRuleWindow().clickOthersCheckBox();
		getFlowManagerPage().getFlowRuleWindow().clickAcceptButton();
		getFlowManagerPage().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
		getFlowManagerPage().saveFlow();
		closeNotificationIfExists();
		// Clean the rule
		getFlowManagerPage().clickCleanFlowButton();
		checkNotificationIsHumanized(getNotification());
		// Finish the test
		redrawSaveTakeScreenshotAndDeleteForm(CLEAN_SIMPLE_OTHERS_FLOW_2_TEST_SCREENSHOT);
	}

	@Test(groups = "formFlow")
	public void cloneFlow() {
		System.out.println("TEST_NAME: cloneFlow");
		loginAndInitializeForm();
		// Edit the flow
		goToFlowManagerPage();
		getFlowManagerPage().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
		getFlowManagerPage().saveFlow();
		getFlowManagerPage().clickRedrawButton();
		// Clone rule
		getFlowManagerPage().getFlowRulesTable().getCell(FIRST_ROW, FIRST_COLUMN).click();
		getFlowManagerPage().clickCloneButton();
		// Finish the test
		redrawSaveTakeScreenshotAndDeleteForm(CLONE_FLOW_TEST_SCREENSHOT);
	}

	@Test(groups = "formFlow")
	public void createAnswersInQuestionFlow() {
		System.out.println("TEST_NAME: createAnswersInQuestionFlow");
		// Initialize the test
		loginAndInitializeForm();
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
		getFlowManagerPage().getFlowRuleWindow().clickAcceptButton();
		// Finish the test
		redrawSaveTakeScreenshotAndDeleteForm(ANSWERS_IN_QUESTION_FLOW_TEST_SCREENSHOT);
	}

	@Test(groups = "formFlow")
	public void createQuestionEqualsAnswerFlow() {
		System.out.println("TEST_NAME: createQuestionEqualsAnswerFlow");
		loginAndInitializeForm();
		// Create a flow
		goToFlowManagerPage();
		getFlowManagerPage().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
		getFlowManagerPage().getFlowRulesTable().getCell(FIRST_ROW, FIRST_COLUMN).click();
		getFlowManagerPage().clickEditRuleButton();
		// Add the question == answer condition
		getFlowManagerPage().getFlowRuleWindow().searchForElement(QUESTION1_NAME);
		getFlowManagerPage().getFlowRuleWindow().selectElementInTreeTable(ANSWER1_NAME);
		getFlowManagerPage().getFlowRuleWindow().clickEqualsButton();
		Assert.assertEquals(getFlowManagerPage().getFlowRuleWindow().getValidInvalidTagValue(), VALID_FLOW_TAG);
		getFlowManagerPage().getFlowRuleWindow().clickAcceptButton();
		// Finish the test
		redrawSaveTakeScreenshotAndDeleteForm(QUESTION_EQUALS_ANSWER_FLOW_TEST_SCREENSHOT);
	}

	@Test(groups = "formFlow")
	public void createQuestionNotEqualsAnswerFlow() {
		System.out.println("TEST_NAME: createQuestionNotEqualsAnswerFlow");
		loginAndInitializeForm();
		// Create a flow
		goToFlowManagerPage();
		getFlowManagerPage().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
		getFlowManagerPage().getFlowRulesTable().getCell(FIRST_ROW, FIRST_COLUMN).click();
		getFlowManagerPage().clickEditRuleButton();
		// Add the question != answer condition
		getFlowManagerPage().getFlowRuleWindow().searchForElement(QUESTION1_NAME);
		getFlowManagerPage().getFlowRuleWindow().selectElementInTreeTable(ANSWER1_NAME);
		getFlowManagerPage().getFlowRuleWindow().clickNotEqualsButton();
		Assert.assertEquals(getFlowManagerPage().getFlowRuleWindow().getValidInvalidTagValue(), VALID_FLOW_TAG);
		getFlowManagerPage().getFlowRuleWindow().clickAcceptButton();
		// Finish the test
		redrawSaveTakeScreenshotAndDeleteForm(QUESTION_NOT_EQUALS_ANSWER_FLOW_TEST_SCREENSHOT);
	}

	private void loginAndInitializeForm() {
		loginFormAdmin1();
		getFormManagerPage().createNewForm(NEW_FORM_NAME);
		// Create a couple of categories and questions
		goToDesignerPage();
		try {
			getDesignerPage().createAndSaveSimpleFormDesign();
		} catch (FieldNotEditableException e) {
			e.printStackTrace();
		}
	}

	private void redrawSaveTakeScreenshotAndDeleteForm(String screenshotName) {
		getFlowManagerPage().clickRedrawButton();
		takeScreenshot(screenshotName);
		getFlowManagerPage().saveFlow();
		logOut();
		// Delete the form created for the test
		deleteForm();
	}

	@Test(groups = "formFlow")
	public void removeFlow() {
		System.out.println("TEST_NAME: removeFlow");
		loginAndInitializeForm();
		// Edit the flow
		goToFlowManagerPage();
		getFlowManagerPage().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
		getFlowManagerPage().saveFlow();
		getFlowManagerPage().clickRedrawButton();
		// Clone rule
		getFlowManagerPage().getFlowRulesTable().getCell(FIRST_ROW, FIRST_COLUMN).click();
		getFlowManagerPage().clickRemoveButton();
		// Finish the test
		redrawSaveTakeScreenshotAndDeleteForm(REMOVE_FLOW_TEST_SCREENSHOT);
	}

	@Test(groups = "formFlow")
	public void validStartEndFlow() {
		System.out.println("TEST_NAME: validStartEndFlow");
		loginAndInitializeForm();
		// Edit the flow
		goToFlowManagerPage();
		getFlowManagerPage().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
		// Finish the test
		redrawSaveTakeScreenshotAndDeleteForm(VALID_BASIC_FLOW_TEST_SCREENSHOT);
	}
}
