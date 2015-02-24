package com.biit.webforms.gui.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.webforms.gui.tests.exceptions.FieldNotEditableException;
import com.biit.webforms.gui.tests.exceptions.IncorrectFileGenerationException;

public class ExportTests extends WebFormsTester {

	private static final String NEW_FORM_NAME = "new_form_1";
	private static final String QUESTION1_NAME = "Question1";
	private static final String QUESTION2_NAME = "Question2";
	private static final String ANSWER1_NAME = "Q1Answer1";

	private static final Integer FIRST_ROW = 0;
	private static final Integer FIRST_COLUMN = 0;

	private static final String VALID_FLOW_TAG = "Valid";

	private void checkCorrectFileGenerationAndFinishTest() {
		try {
			getFormManagerPage().getDownloadWindow().checkCorrectFileGeneration();
			getFormManagerPage().getDownloadWindow().closeWindow();
			logOut();
			deleteForm();
		} catch (IncorrectFileGenerationException e) {
			Assert.fail();
		}
	}

	@Test(groups = "export")
	public void exportTestXmls() {
		printTestNameInDebugTrace("exportTestXmls");
		try {
			generateFormAndFlow();
			getFormManagerPage().clickExportButton();
			getFormManagerPage().clickExportTestXmlsButton();
			getFormManagerPage().getTestXmlWindow().clickAcceptButton();
			getFormManagerPage().getDownloadWindow().checkCorrectFileGeneration();
			getFormManagerPage().getDownloadWindow().closeWindow();
			getFormManagerPage().getTestXmlWindow().closeWindow();
			logOut();
			deleteForm();
		} catch (IncorrectFileGenerationException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = "export")
	public void exportToFlow() {
		printTestNameInDebugTrace("exportToFlow");
		generateFormAndFlow();
		getFormManagerPage().clickExportButton();
		getFormManagerPage().clickExportFlowButton();
		checkCorrectFileGenerationAndFinishTest();
	}

	// Test not needed yet
//	@Test(groups = "export")
	public void exportToJson() {
		printTestNameInDebugTrace("exportToJson");
		try {
			generateFormAndFlow();
			getFormManagerPage().clickExportButton();
			getFormManagerPage().clickExportJsonButton();
			getFormManagerPage().getDownloadWindowJson().checkCorrectFileGeneration();
			getFormManagerPage().getDownloadWindowJson().closeWindow();
			logOut();
			deleteForm();
		} catch (IncorrectFileGenerationException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = "export")
	public void exportToPdf() {
		printTestNameInDebugTrace("exportToPdf");
		generateFormAndFlow();
		getFormManagerPage().clickExportButton();
		getFormManagerPage().clickExportFormButton();
		checkCorrectFileGenerationAndFinishTest();
	}

	@Test(groups = "export")
	public void exportToXsd() {
		printTestNameInDebugTrace("exportToXsd");
		try {
			generateFormAndFlow();
			getFormManagerPage().clickExportButton();
			getFormManagerPage().clickExportXsdButton();
			getFormManagerPage().getDownloadWindowXsd().checkCorrectFileGeneration();
			getFormManagerPage().getDownloadWindowXsd().closeWindow();
			logOut();
			deleteForm();
		} catch (IncorrectFileGenerationException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	private void generateFormAndFlow() {
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
			goToFormManagerPage();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}
}
