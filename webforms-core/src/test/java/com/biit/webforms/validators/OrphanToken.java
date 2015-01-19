package com.biit.webforms.validators;

import java.io.IOException;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.FormJsonLoaderTest;

@Test(groups = { "orphanToken" })
public class OrphanToken extends FormJsonLoaderTest {

	@Test
	public void testMissingFlowIn() throws IOException {
		Form form = loadForm("test_validator_orphan_token.json");

		//Assert.assertFalse(new ValidateFormComplete().validate(form));
	}

}
