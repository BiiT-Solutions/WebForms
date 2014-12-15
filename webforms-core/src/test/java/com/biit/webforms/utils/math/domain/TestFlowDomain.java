package com.biit.webforms.utils.math.domain;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import com.biit.form.BaseQuestion;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOrigin;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestiny;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSource;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;
import com.biit.webforms.utils.math.domain.exceptions.BadFormedExpressions;
import com.biit.webforms.utils.math.domain.exceptions.IncompleteLogic;
import com.biit.webforms.utils.math.domain.exceptions.RedundantLogic;

@Test(groups = { "testFlowDomain" })
public class TestFlowDomain {

	@Test
	public void testFlowDomain() throws FieldTooLongException,
			NotValidChildException, CharacterNotAllowedException,
			BadFlowContentException, FlowWithoutSource,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin,
			FlowWithoutDestiny, BadFormedExpressions, IncompleteLogic,
			RedundantLogic {
		Form form = FormTestUtilities.createFormTest1();
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("cat1", "qu1"));

	}

	@Test(dependsOnMethods = { "testFlowDomain" }, expectedExceptions = { IncompleteLogic.class })
	public void testFlowDomainNotCompleteLogicOnlyQuestionAnswer()
			throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, BadFlowContentException,
			FlowWithoutSource, FlowSameOriginAndDestinyException,
			FlowDestinyIsBeforeOrigin, FlowWithoutDestiny,
			BadFormedExpressions, IncompleteLogic, RedundantLogic {

		Form form = FormTestUtilities.createFormTest2();
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("cat1", "qu1"));
	}

	@Test(dependsOnMethods = { "testFlowDomainNotCompleteLogicOnlyQuestionAnswer" })
	public void testFlowDomainCompleteLogicWithOr()
			throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, BadFlowContentException,
			FlowWithoutSource, FlowSameOriginAndDestinyException,
			FlowDestinyIsBeforeOrigin, FlowWithoutDestiny,
			BadFormedExpressions, IncompleteLogic, RedundantLogic {

		Form form = FormTestUtilities.createFormTest3();
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("cat1", "qu1"));
	}

	@Test(dependsOnMethods = { "testFlowDomainCompleteLogicWithOr" }, expectedExceptions = { RedundantLogic.class })
	public void testFlowDomainOverlappedLogicQuestionAnswer()
			throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, BadFlowContentException,
			FlowWithoutSource, FlowSameOriginAndDestinyException,
			FlowDestinyIsBeforeOrigin, FlowWithoutDestiny,
			BadFormedExpressions, IncompleteLogic, RedundantLogic {

		Form form = FormTestUtilities.createFormTest4();
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("cat1", "qu1"));
	}

	@Test(dependsOnMethods = { "testFlowDomainOverlappedLogicQuestionAnswer" })
	public void testAndOrQuestionAnswer() throws FieldTooLongException,
			NotValidChildException, CharacterNotAllowedException,
			BadFlowContentException, FlowWithoutSource,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin,
			FlowWithoutDestiny, BadFormedExpressions, IncompleteLogic,
			RedundantLogic, InvalidAnswerFormatException,
			InvalidAnswerSubformatException {

		Form form = FormTestUtilities.createFormTest6();
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("cat1", "qu3"));

	}

	//Tests using Json
	//Only answers, incomplete, redundant and complete logic only one question referenced on tokens.
	@Test(dependsOnMethods = { "testAndOrQuestionAnswer" },expectedExceptions = { IncompleteLogic.class })
	public void testJson1() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic {
		System.out.println("Test Json1");
		Form form = loadForm("test_logic_answers_1.json");
		
		//Incomplete logic a, b but c is missing
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category","Group","Question"));
	}
	
	@Test(dependsOnMethods = { "testJson1" }, expectedExceptions = { IncompleteLogic.class })
	public void testJson2() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic {
		System.out.println("Test Json2");
		Form form = loadForm("test_logic_answers_1.json");
		
		//Incomplete logic d or e, f or g but h ans i is missing
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category","Group","Question2"));
	}
	
	@Test(dependsOnMethods = { "testJson2" })
	public void testJson3() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic {
		System.out.println("Test Json3");
		Form form = loadForm("test_logic_answers_1.json");
		
		//Complete logic without other
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category","Group","Text"));
	}
	
	@Test(dependsOnMethods = { "testJson3" })
	public void testJson4() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic {
		System.out.println("Test Json4");
		Form form = loadForm("test_logic_answers_1.json");
		
		//Complete logic with others
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category","Group","Text2"));
	}
	
	@Test(dependsOnMethods = { "testJson4" }, expectedExceptions = { RedundantLogic.class })
	public void testJson5() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic {
		System.out.println("Test Json5");
		Form form = loadForm("test_logic_answers_1.json");
		
		//Reduntant logic !a, b
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category","Group","Text3"));
	}
	
	//Only answers, incomplete, redundant and complete logic only two questions referenced on tokens.
	@Test(dependsOnMethods = { "testJson5" })
	public void testJson6() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic {
		System.out.println("Test Json6");
		Form form = loadForm("test_logic_answers_2.json");
		
		//Complete logic
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category","Group","Text"));
	}
	
	@Test(dependsOnMethods = { "testJson6" }, expectedExceptions = { IncompleteLogic.class })
	public void testJson7() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic {
		System.out.println("Test Json7");
		Form form = loadForm("test_logic_answers_2.json");
		
		//Incomplete logic a and d or e || b or c  - a and (!d and !e) is left out.
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category","Group","Text2"));
	}
	
	@Test(dependsOnMethods = { "testJson7" }, expectedExceptions = { RedundantLogic.class })
	public void testJson8() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic {
		System.out.println("Test Json8");
		Form form = loadForm("test_logic_answers_2.json");
		
		//Redundant Logic
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category","Group","Text3"));
	}
	
	@Test(dependsOnMethods = { "testJson8" }, expectedExceptions = { IncompleteLogic.class })
	public void testJson9() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic {
		System.out.println("Test Json9");
		Form form = loadForm("test_logic_answers_2.json");
		
		//Incomplete logic
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category","Group","Text4"));
	}
	
	@Test(dependsOnMethods = { "testJson9" })
	public void testJson10() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic {
		System.out.println("Test Json10");
		Form form = loadForm("test_logic_answers_2.json");
		
		//Complete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category","Group","Text5"));
	}
	
	//Integer tests
	@Test(dependsOnMethods = { "testJson10" })
	public void testJson11() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic {
		System.out.println("Test Json11");
		Form form = loadForm("test_integer.json");
		
		//Complete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category","Group","Question"));
	}
	
	@Test(dependsOnMethods = { "testJson11" })
	public void testJson12() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic {
		System.out.println("Test Json12");
		Form form = loadForm("test_integer.json");
		
		//Complete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category","Group","Text"));
	}
	
	@Test(dependsOnMethods = { "testJson12" }, expectedExceptions = { IncompleteLogic.class })
	public void testJson13() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic {
		System.out.println("Test Json13");
		Form form = loadForm("test_integer.json");
		
		//Incomplete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category","Group","Text2"));
	}
	
	@Test(dependsOnMethods = { "testJson13" }, expectedExceptions = { IncompleteLogic.class })
	public void testJson14() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic {
		System.out.println("Test Json14");
		Form form = loadForm("test_integer.json");
		
		//Incomplete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category","Group","Text3"));
	}
	
	@Test(dependsOnMethods = { "testJson14" }, expectedExceptions = { IncompleteLogic.class })
	public void testJson15() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic {
		System.out.println("Test Json15");
		Form form = loadForm("test_integer.json");
		
		//Incomplete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category","Group","Text4"));
	}

	@Test(dependsOnMethods = { "testJson15" })
	public void testJson16() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic {
		System.out.println("Test Json16");
		Form form = loadForm("test_float.json");
		
		//Complete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category","Group","Question"));
	}
	
	@Test(dependsOnMethods = { "testJson16" }, expectedExceptions = { IncompleteLogic.class })
	public void testJson17() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic {
		System.out.println("Test Json17");
		Form form = loadForm("test_float.json");
		
		//Incomplete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category","Group","Question2"));
	}
	@Test(dependsOnMethods = { "testJson17" })
	public void testJson18() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic {
		System.out.println("Test Json18");
		Form form = loadForm("test_float.json");
		
		//Complete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category","Group","Question3"));
	}
	
	
	public Form loadForm(String filename) throws IOException {

		ClassLoader classLoader = getClass().getClassLoader();
		String result = IOUtils.toString(classLoader
				.getResourceAsStream(filename));

		return Form.fromJson(result);
	}

}
