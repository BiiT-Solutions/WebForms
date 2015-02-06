package com.biit.webforms.gui.test;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FormFlowTests extends WebFormsTester {

	private static final String NEW_FORM_NAME = "new_form_1";
	private static final String QUESTION1_NAME = "Question1";
	private static final String QUESTION2_NAME = "Question2";
	private static final String QUESTION3_NAME = "Question3";
	private static final String ANSWER1_NAME = "Q1Answer1";
	private static final String ANSWER2_NAME = "Q1Answer2";
	private static final String NEW_BLOCK_NAME = "new_block_1";
	private static final String ORGANIZATION_NAME = "Bii1";
	private static final String STATUS_FINISHED = "Final Design";

	private static final Integer FIRST_ROW = 0;
	private static final Integer FIRST_COLUMN = 0;

	private static final String CLEAN_SIMPLE_OTHERS_FLOW_TEST_SCREENSHOT = "cleanSimpleOthersFlowTest";
	private static final String CLEAN_SIMPLE_OTHERS_FLOW_2_TEST_SCREENSHOT = "cleanSimpleOthersFlow2Test";
	private static final String QUESTION_EQUALS_ANSWER_FLOW_TEST_SCREENSHOT = "createQuestionEqualsAnswerFlow";
	private static final String QUESTION_NOT_EQUALS_ANSWER_FLOW_TEST_SCREENSHOT = "createQuestionNotEqualsAnswerFlow";

	private static final String VALID_FLOW_TAG = "Valid";

	@Test(groups = "formFlow")
	public void validFormWithEverything() {
		try {
			loginFormEdit1();
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

	@Test(groups = "formFlow")
	public void validStartEndFlow() {
		try {
			loginFormEdit1();
			getFormManager().createNewForm(NEW_FORM_NAME);
			// Create a couple of categories and questions
			goToDesigner();
			getDesigner().createAndSaveSimpleForm();
			// Edit the flow
			goToFlowManager();
			getFlowManager().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
			getFlowManager().saveFlow();
			getFlowManager().clickRedrawButton();
			logOut();
			// Delete the form created for the test
			deleteForm();

		} catch (Exception e) {
			e.printStackTrace();
			// If any unexpected exception is thrown the test should fail
			Assert.fail();
		}
	}

	@Test(groups = "formFlow")
	public void cloneFlow() {
		try {
			loginFormEdit1();
			getFormManager().createNewForm(NEW_FORM_NAME);
			// Create a couple of categories and questions
			goToDesigner();
			getDesigner().createAndSaveSimpleForm();
			// Edit the flow
			goToFlowManager();
			getFlowManager().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
			getFlowManager().saveFlow();
			getFlowManager().clickRedrawButton();
			// Clone rule
			getFlowManager().getFlowRulesTable().getCell(FIRST_ROW, FIRST_COLUMN).click();
			getFlowManager().clickCloneButton();
			getFlowManager().saveFlow();
			getFlowManager().clickRedrawButton();
			logOut();
			// Delete the form created for the test
			deleteForm();

		} catch (Exception e) {
			e.printStackTrace();
			// If any unexpected exception is thrown the test should fail
			Assert.fail();
		}
	}

	@Test(groups = "formFlow")
	public void removeFlow() {
		try {
			loginFormEdit1();
			getFormManager().createNewForm(NEW_FORM_NAME);
			// Create a couple of categories and questions
			goToDesigner();
			getDesigner().createAndSaveSimpleForm();
			// Edit the flow
			goToFlowManager();
			getFlowManager().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
			getFlowManager().saveFlow();
			getFlowManager().clickRedrawButton();
			// Clone rule
			getFlowManager().getFlowRulesTable().getCell(FIRST_ROW, FIRST_COLUMN).click();
			getFlowManager().clickRemoveButton();
			getFlowManager().saveFlow();
			getFlowManager().clickRedrawButton();
			logOut();
			// Delete the form created for the test
			deleteForm();

		} catch (Exception e) {
			e.printStackTrace();
			// If any unexpected exception is thrown the test should fail
			Assert.fail();
		}
	}

	@Test(groups = "formFlow")
	public void cleanSimpleFlow() {
		try {
			loginFormEdit1();
			getFormManager().createNewForm(NEW_FORM_NAME);
			// Create a couple of categories and questions
			goToDesigner();
			getDesigner().createAndSaveSimpleForm();
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
			getFlowManager().saveFlow();
			logOut();
			// Delete the form created for the test
			deleteForm();

		} catch (Exception e) {
			e.printStackTrace();
			// If any unexpected exception is thrown the test should fail
			Assert.fail();
		}
	}

	@Test(groups = "formFlow")
	public void cleanSimpleOthersFlow() {
		try {
			loginFormEdit1();
			getFormManager().createNewForm(NEW_FORM_NAME);
			// Create a couple of categories and questions
			goToDesigner();
			getDesigner().createAndSaveSimpleForm();
			// Create a flow
			goToFlowManager();
			getFlowManager().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
			// Edit the flow (Create the others)
			getFlowManager().getFlowRulesTable().getCell(FIRST_ROW, FIRST_COLUMN).click();
			getFlowManager().clickEditRuleButton();
			getFlowManager().getFlowRuleWindow().clickOthersCheckBox();
			getFlowManager().getFlowRuleWindow().clickAcceptButton();
			getFlowManager().saveFlow();
			getFlowManager().clickRedrawButton();
			if (isHeadlessTesting()) {
				takeScreenshot(CLEAN_SIMPLE_OTHERS_FLOW_TEST_SCREENSHOT);
			}
			// Clean the rule
			getFlowManager().clickCleanFlowButton();
			getFlowManager().getFlowCleanedWindow().clickCloseButton();
			getFlowManager().saveFlow();
			logOut();
			// Delete the form created for the test
			deleteForm();

		} catch (Exception e) {
			e.printStackTrace();
			// If any unexpected exception is thrown the test should fail
			Assert.fail();
		}
	}

	@Test(groups = "formFlow")
	public void cleanSimpleOthersFlow2() {
		try {
			loginFormEdit1();
			getFormManager().createNewForm(NEW_FORM_NAME);
			// Create a couple of categories and questions
			goToDesigner();
			getDesigner().createAndSaveSimpleForm();
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
			getFlowManager().clickRedrawButton();
			if (isHeadlessTesting()) {
				takeScreenshot(CLEAN_SIMPLE_OTHERS_FLOW_2_TEST_SCREENSHOT);
			}
			checkNotificationIsHumanized(getNotification());
			getFlowManager().saveFlow();
			logOut();
			// Delete the form created for the test
			deleteForm();

		} catch (Exception e) {
			e.printStackTrace();
			// If any unexpected exception is thrown the test should fail
			Assert.fail();
		}
	}

	// TODO FIX bug in webforms
	public void createQuestionsFlow() {
		try {
			loginFormEdit1();
			getFormManager().createNewForm(NEW_FORM_NAME);
			// Create a couple of categories and questions
			goToDesigner();
			getDesigner().createAndSaveSimpleForm();
			// Create a flow
			goToFlowManager();
			getFlowManager().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
			getFlowManager().createSimpleFlowRule(QUESTION2_NAME, QUESTION3_NAME);
			// Clean the rule
			getFlowManager().clickRedrawButton();
			if (isHeadlessTesting()) {
				takeScreenshot(CLEAN_SIMPLE_OTHERS_FLOW_2_TEST_SCREENSHOT);
			}
			getFlowManager().saveFlow();
			logOut();
			// Delete the form created for the test
			deleteForm();

		} catch (Exception e) {
			e.printStackTrace();
			// If any unexpected exception is thrown the test should fail
			Assert.fail();
		}
	}

	@Test(groups = "formFlow")
	public void createQuestionEqualsAnswerFlow() {
		try {
			loginFormEdit1();
			getFormManager().createNewForm(NEW_FORM_NAME);
			// Create a couple of categories and questions
			goToDesigner();
			getDesigner().createAndSaveSimpleForm();
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
			// Redraw the graph
			getFlowManager().clickRedrawButton();
			takeScreenshot(QUESTION_EQUALS_ANSWER_FLOW_TEST_SCREENSHOT);
			getFlowManager().saveFlow();
			logOut();
			// Delete the form created for the test
			deleteForm();

		} catch (Exception e) {
			e.printStackTrace();
			// If any unexpected exception is thrown the test should fail
			Assert.fail();
		}
	}

	@Test(groups = "formFlow")
	public void createQuestionNotEqualsAnswerFlow() {
		try {
			loginFormEdit1();
			getFormManager().createNewForm(NEW_FORM_NAME);
			// Create a couple of categories and questions
			goToDesigner();
			getDesigner().createAndSaveSimpleForm();
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
			// Redraw the graph
			getFlowManager().clickRedrawButton();
			if (isHeadlessTesting()) {
				takeScreenshot(QUESTION_NOT_EQUALS_ANSWER_FLOW_TEST_SCREENSHOT);
			}
			getFlowManager().saveFlow();
			logOut();
			// Delete the form created for the test
			deleteForm();

		} catch (Exception e) {
			e.printStackTrace();
			// If any unexpected exception is thrown the test should fail
			Assert.fail();
		}
	}

	@Test(groups = "formFlow")
	public void createAnswersInQuestionFlow() {
		try {
			loginFormEdit1();
			getFormManager().createNewForm(NEW_FORM_NAME);
			// Create a couple of categories and questions
			goToDesigner();
			getDesigner().createAndSaveSimpleForm();
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
			takeScreenshot(QUESTION_NOT_EQUALS_ANSWER_FLOW_TEST_SCREENSHOT);
			getFlowManager().saveFlow();
			logOut();
			// Delete the form created for the test
			deleteForm();

		} catch (Exception e) {
			e.printStackTrace();
			// If any unexpected exception is thrown the test should fail
			Assert.fail();
		}
	}

}
