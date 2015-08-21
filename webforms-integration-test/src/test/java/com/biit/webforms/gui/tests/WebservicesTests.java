package com.biit.webforms.gui.tests;

import org.testng.annotations.Test;

import com.biit.webforms.gui.tests.exceptions.FieldNotEditableException;

public class WebservicesTests extends WebFormsTester {

	private static final String NEW_FORM_NAME = "webservice form";
	private static final String NAME_BSN = "BSN";
	private static final String NAME_NAME = "name";
	private static final String NAME_SURNAME = "surname";
	private static final String NAME_ADDRESS = "address";
	private static final String WEBSERVICE_CALL_NAME = "bsn_call";
	private static final int WEBSERVICE_ROW = 1;

	@Test(groups = "webservices")
	public void generateFormWithWebservices() throws FieldNotEditableException {
		printTestNameInDebugTrace("createFormfinishFormCheckStatus");
		loginFormAdmin1();
		getFormManagerPage().deleteAllCreatedForms();

		getFormManagerPage().createNewForm(NEW_FORM_NAME);
		goToDesignerPage();

		getDesignerPage().addNewCategory();
		getDesignerPage().addNewInputTextSubformatBsnQuestion();
		getDesignerPage().getQuestionPropertiesView().setTechnicalName(NAME_BSN);

		getDesignerPage().addNewInputTextQuestion();
		getDesignerPage().getQuestionPropertiesView().setTechnicalName(NAME_NAME);
		getDesignerPage().addNewInputTextQuestion();
		getDesignerPage().getQuestionPropertiesView().setTechnicalName(NAME_SURNAME);
		getDesignerPage().addNewInputTextQuestion();
		getDesignerPage().getQuestionPropertiesView().setTechnicalName(NAME_ADDRESS);

		getDesignerPage().saveDesign();

		goToWebservicePage();
		waitFor(500);
		getWebservicePage().addWebserviceCall(WEBSERVICE_CALL_NAME, WEBSERVICE_ROW);
		getWebservicePage().save();

		getWebservicePage().editInputField(0, 2);
		getWebservicePage().editOutputField(0, 4, true);
		getWebservicePage().editOutputField(1, 2, false);
		getWebservicePage().editOutputField(2, 3, false);

		getWebservicePage().save();

		getWebservicePage().selectTrigger(3);
		getWebservicePage().save();

		logOut();
		deleteForm();
	}

	private void waitFor(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
