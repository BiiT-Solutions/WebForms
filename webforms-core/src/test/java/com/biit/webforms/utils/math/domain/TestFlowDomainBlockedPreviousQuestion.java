package com.biit.webforms.utils.math.domain;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.form.BaseQuestion;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.math.domain.exceptions.FlowDomainBlocked;
import com.biit.webforms.validators.ValidateFormComplete;

@Test(groups={"testFlowDomainBlockedPreviousQuestion"})
public class TestFlowDomainBlockedPreviousQuestion {

	@Test(expectedExceptions={FlowDomainBlocked.class})
	public void testRedundantFlowIncorrect() throws FlowDomainBlocked, IOException {
		Form form = loadForm("arie_blocked_flow"+File.separator+"arie_test_blocking.json");
		// Error
		new FlowDomainBlockedByPreviousQuestion(form, (BaseQuestion) form.getChild("Category/Question4"));
	}
	
	public void testRedundantFlowIncorrectValidator() throws FlowDomainBlocked, IOException {
		Form form = loadForm("arie_blocked_flow"+File.separator+"arie_test_blocking.json");
		// Error
		ValidateFormComplete validator = new ValidateFormComplete();
		Assert.assertFalse(validator.validate(form));
	}
	
	private Form loadForm(String filename) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		String result = IOUtils.toString(classLoader.getResourceAsStream(filename), "UTF-8");

		return Form.fromJson(result);
	}
}
