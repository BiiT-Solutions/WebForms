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

    private static final String VALID_FLOW_TAG = "Valid";

    private void checkCorrectFileGenerationAndFinishTest() throws IncorrectFileGenerationException {
        getFormManagerPage().getDownloadWindow().checkCorrectFileGeneration();
        getFormManagerPage().getDownloadWindow().closeWindow();
        logOut();
        deleteForm();
    }

    @Test(groups = "export")
    public void exportTestXmls() throws IncorrectFileGenerationException, FieldNotEditableException {
        generateFormAndFlow();
        getFormManagerPage().clickExportButton();
        getFormManagerPage().clickExportTestXmlsButton();
        getFormManagerPage().getTestXmlWindow().clickAcceptButton();
        getFormManagerPage().getDownloadWindow().checkCorrectFileGeneration();
        getFormManagerPage().getDownloadWindow().closeWindow();
        getFormManagerPage().getTestXmlWindow().closeWindow();
        logOut();
        deleteForm();
    }

    @Test(groups = "export")
    public void exportToFlow() throws FieldNotEditableException, IncorrectFileGenerationException {
        generateFormAndFlow();
        getFormManagerPage().clickExportButton();
        getFormManagerPage().clickExportFlowButton();
        checkCorrectFileGenerationAndFinishTest();
    }

    @Test(groups = "export")
    public void exportToJson() throws IncorrectFileGenerationException, FieldNotEditableException {
        generateFormAndFlow();
        getFormManagerPage().clickExportButton();
        getFormManagerPage().clickExportJsonButton();
        getFormManagerPage().getDownloadWindowJson().checkCorrectFileGeneration();
        getFormManagerPage().getDownloadWindowJson().closeWindow();
        logOut();
        deleteForm();
    }

    @Test(groups = "export")
    public void exportToPdf() throws FieldNotEditableException, IncorrectFileGenerationException {
        generateFormAndFlow();
        getFormManagerPage().clickExportButton();
        getFormManagerPage().clickExportFormButton();
        checkCorrectFileGenerationAndFinishTest();
    }

    @Test(groups = "export")
    public void exportToXsd() throws IncorrectFileGenerationException, FieldNotEditableException {
        generateFormAndFlow();
        getFormManagerPage().clickExportButton();
        getFormManagerPage().clickExportXsdButton();
        getFormManagerPage().getDownloadWindowXsd().checkCorrectFileGeneration();
        getFormManagerPage().getDownloadWindowXsd().closeWindow();
        logOut();
        deleteForm();
    }

    private void generateFormAndFlow() throws FieldNotEditableException {
        loginFormAdmin1();
        getFormManagerPage().createNewForm(NEW_FORM_NAME);
        // Create a couple of categories and questions
        goToDesignerPage();
        getDesignerPage().createAndSaveSimpleFormDesign();
        // Create a flow
        goToFlowManagerPage();
        getFlowManagerPage().clickNewRuleButton();
        getFlowManagerPage().selectFromToElement(QUESTION1_NAME, QUESTION2_NAME);
        // Add the question == answer condition
        getFlowManagerPage().getFlowRuleWindow().searchForElement(QUESTION1_NAME);
        getFlowManagerPage().getFlowRuleWindow().selectElementInAnswerTreeTable(ANSWER1_NAME);
        getFlowManagerPage().getFlowRuleWindow().clickEqualsButton();
        Assert.assertEquals(getFlowManagerPage().getFlowRuleWindow().getValidInvalidTagValue(), VALID_FLOW_TAG);
        getFlowManagerPage().getFlowRuleWindow().clickAccept();
        // Add the question OTHERS condition
        getFlowManagerPage().clickNewRuleButton();
        getFlowManagerPage().selectFromToElement(QUESTION1_NAME, QUESTION2_NAME);
        getFlowManagerPage().getFlowRuleWindow().clickOthersCheckBox();
        getFlowManagerPage().getFlowRuleWindow().clickAccept();
        // Redraw the graph
        getFlowManagerPage().clickRedrawButton();
        getFlowManagerPage().saveFlow();
        goToFormManagerPage();
    }
}
