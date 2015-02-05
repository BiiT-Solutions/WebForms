package com.biit.webforms.validators;

import java.io.IOException;

import org.testng.annotations.Test;

import com.biit.webforms.utils.FormJsonLoaderTest;

@Test(groups = { "orphanToken" })
public class OrphanToken extends FormJsonLoaderTest {

	@Test
	public void testMissingFlowIn() throws IOException {
		loadForm("test_validator_orphan_token.json");

		//Assert.assertFalse(new ValidateFormComplete().validate(form));
	}

}
