package com.biit.webforms.gui.test;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "formFlow")
public class FormFlowTests extends WebFormsTester {

	private static final String NEW_FORM_NAME = "new_form_1";
	private static final String NEW_BLOCK_NAME = "new_block_1";
	private static final String ORGANIZATION_NAME = "Bii1";
	private static final String STATUS_FINISHED = "Final Design";

	@Test
	public void validFormWithEverithing() {
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

}
