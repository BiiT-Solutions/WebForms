package com.biit.webforms.utils.math.domain;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import com.biit.form.entity.BaseQuestion;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.math.domain.exceptions.BadFormedExpressions;
import com.biit.webforms.utils.math.domain.exceptions.DifferentDateUnitForQuestions;
import com.biit.webforms.utils.math.domain.exceptions.IncompleteLogic;
import com.biit.webforms.utils.math.domain.exceptions.RedundantLogic;

@Test(groups={"testFlowDomainRedundant"})
public class TestFlowDomainRedundant {
	
	@Test(expectedExceptions={RedundantLogic.class})
	public void testRedundantFlowIncorrect() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic, DifferentDateUnitForQuestions{
		Form form = loadForm("arie_redundant_flow"+File.separator+"arie_test_1.json");
		// Error
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category/Question3"));
	}
	
	@Test
	public void testRedundantFlowCorrect() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic, DifferentDateUnitForQuestions{
		Form form = loadForm("arie_redundant_flow"+File.separator+"arie_test_2.json");
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category/Question3"));
	}
	
	@Test
	public void testRedundantFlowCorrectOthers() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic, DifferentDateUnitForQuestions{
		Form form = loadForm("arie_redundant_flow"+File.separator+"arie_test_2_b.json");
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category/Question3"));
	}
	
	@Test
	public void testRedundantFlowCorrectOthersComplex() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic, DifferentDateUnitForQuestions{
		Form form = loadForm("arie_redundant_flow"+File.separator+"arie_test_3.json");
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category/Question4"));
	}
	
	private Form loadForm(String filename) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		String result = IOUtils.toString(classLoader.getResourceAsStream(filename), "UTF-8");

		return Form.fromJson(result);
	}
}
