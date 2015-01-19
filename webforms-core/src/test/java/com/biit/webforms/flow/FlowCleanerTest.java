package com.biit.webforms.flow;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import com.biit.webforms.persistence.entity.Form;

@Test(groups = { "flowCleaner" })
public class FlowCleanerTest {

	/**
	 * In this test a form has a simple flow that goes from question1 and question2 (is useless) and other flow that is
	 * defined as 'OTHERS' that goes from question2 to question3. 'OTHERS' must be removed and then, is also a useless
	 * flow.
	 * 
	 * @throws IOException
	 */
	@Test
	public void basicFormWithOthersAndUselessFlow() throws IOException {
		Form form = loadForm("FormCleanFlow.json");
		Assert.assertEquals(2, form.getFlows().size());
		FlowCleaner flowCleaner = new FlowCleaner(form);
		flowCleaner.cleanFlow();
		Assert.assertEquals(0, form.getFlows().size());
	}

	private Form loadForm(String filename) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		String result = IOUtils.toString(classLoader.getResourceAsStream(filename),"UTF-8");

		return Form.fromJson(result);
	}
}
