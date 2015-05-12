package com.biit.webforms.gui.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.webforms.gui.tests.exceptions.FieldNotEditableException;

public class ValidationTests extends WebFormsTester {

	private static final String NEW_FORM_NAME = "new_form_1";
	private static final String QUESTION1_NAME = "Question1";
	private static final String QUESTION2_NAME = "Question2";
	private static final String QUESTION3_NAME = "Question3";
	private static final String ANSWER1_NAME = "Q1Answer1";

	private static final Integer FIRST_ROW = 0;
	private static final Integer FIRST_COLUMN = 0;

	private static final String VALID_FLOW_TAG = "Valid";
	private static final String VALIDATION_CORRECT_TEXT = "Validation finished correctly.";
	private static final String VALIDATION_TWO_OTHERS_SAME_ELEMENT_TEXT = "Element 'Category/Question' has more than one others flow.";
	private static final String VALIDATION_OTHERS_FLOW_WITHOUT_NORMAL_FLOW = "Element 'Category/Question' has a others flow without any normal flow.";
	private static final String VALIDATION_QUESTION_WITHOUT_FLOW_IN = "Element 'Category/Question2' doesnt have flow in.";
	private static final String VALIDATION_QUESTION_NOT_MANDATORY = "Flow 'Question3 to END_FORM' origin 'Category3/Question3' is not mandatory.";

	private void createSimpleForm() {
		try {
			loginFormAdmin1();
			getFormManagerPage().createNewForm(NEW_FORM_NAME);
			// Create a couple of categories and questions
			goToDesignerPage();
			getDesignerPage().createAndSaveSimpleFormDesign();
			goToFormManagerPage();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	private void createSimpleFlow() {
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
		goToFormManagerPage();
	}

	private void validateForm(String validationText) {
		goToValidationPage();
		getValidationPage().clickAllButton();
		getValidationPage().getTextAreaValue().equals(validationText);
		logOut();
		deleteForm();
	}

	@Test(groups = "validation")
	public void validateCorrectForm() {
		printTestNameInDebugTrace("validateCorrectForm");
		createSimpleForm();
		createSimpleFlow();
		validateForm(VALIDATION_CORRECT_TEXT);
	}

	@Test(groups = "validation")
	public void validateFormWithTwoOthersFromTheSameElement() {
		printTestNameInDebugTrace("validateFormWithTwoOthersFromTheSameElement");
		createSimpleForm();
		createSimpleFlow();
		goToFlowManagerPage();
		getFlowManagerPage().createOthersFlow(QUESTION1_NAME, QUESTION2_NAME);
		getFlowManagerPage().createOthersFlow(QUESTION1_NAME, QUESTION2_NAME);
		validateForm(VALIDATION_TWO_OTHERS_SAME_ELEMENT_TEXT);
	}

	@Test(groups = "validation")
	public void validateFormOthersFlowWithoutNormalFlow() {
		printTestNameInDebugTrace("validateFormOthersFlowWithoutNormalFlow");
		createSimpleForm();
		goToFlowManagerPage();
		getFlowManagerPage().createOthersFlow(QUESTION1_NAME, QUESTION2_NAME);
		validateForm(VALIDATION_OTHERS_FLOW_WITHOUT_NORMAL_FLOW);
	}

	@Test(groups = "validation")
	public void validateFormWithQuestionWithoutFlowIn() {
		printTestNameInDebugTrace("validateFormWithQuestionWithoutFlowIn");
		createSimpleForm();
		goToFlowManagerPage();
		getFlowManagerPage().createEndFlow(QUESTION1_NAME);
		validateForm(VALIDATION_QUESTION_WITHOUT_FLOW_IN);
	}

	@Test(groups = "validation")
	public void validateFormWithQuestionNotMandatoryInFlow() {
		printTestNameInDebugTrace("validateFormWithQuestionNotMandatoryInFlow");
		createSimpleForm();
		goToFlowManagerPage();
		getFlowManagerPage().createEndFlow(QUESTION3_NAME);
		validateForm(VALIDATION_QUESTION_NOT_MANDATORY);
	}
}
