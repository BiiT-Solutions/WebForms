package com.biit.webforms.utils.math.domain;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.utils.validation.ValidateReport;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOriginException;
import com.biit.webforms.persistence.entity.exceptions.FlowNotAllowedException;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSourceException;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;
import com.biit.webforms.utils.math.domain.exceptions.BadFormedExpressions;
import com.biit.webforms.utils.math.domain.exceptions.DifferentDateUnitForQuestions;
import com.biit.webforms.utils.math.domain.exceptions.IncompleteLogic;
import com.biit.webforms.utils.math.domain.exceptions.RedundantLogic;
import com.biit.webforms.validators.ValidateLogic;

@Test(groups = { "testFlowDomain" })
public class TestFlowDomain {

	@Test
	public void testFlowDomain() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
			BadFlowContentException, FlowWithoutSourceException, FlowSameOriginAndDestinyException,
			FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException, BadFormedExpressions, IncompleteLogic,
			RedundantLogic, DifferentDateUnitForQuestions, ElementIsReadOnly, FlowNotAllowedException {
		Form form = FormTestUtilities.createFormTest1();
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("cat1", "qu1"));

	}

	@Test(dependsOnMethods = { "testFlowDomain" }, expectedExceptions = { IncompleteLogic.class })
	public void testFlowDomainNotCompleteLogicOnlyQuestionAnswer() throws FieldTooLongException,
			NotValidChildException, CharacterNotAllowedException, BadFlowContentException, FlowWithoutSourceException,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException,
			BadFormedExpressions, IncompleteLogic, RedundantLogic, DifferentDateUnitForQuestions, ElementIsReadOnly,
			FlowNotAllowedException {

		Form form = FormTestUtilities.createFormTest2();
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("cat1", "qu1"));
	}

	@Test(dependsOnMethods = { "testFlowDomainNotCompleteLogicOnlyQuestionAnswer" })
	public void testFlowDomainCompleteLogicWithOr() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, BadFlowContentException, FlowWithoutSourceException,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException,
			BadFormedExpressions, IncompleteLogic, RedundantLogic, DifferentDateUnitForQuestions, ElementIsReadOnly,
			FlowNotAllowedException {

		Form form = FormTestUtilities.createFormTest3();
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("cat1", "qu1"));
	}

	@Test(dependsOnMethods = { "testFlowDomainCompleteLogicWithOr" }, expectedExceptions = { RedundantLogic.class })
	public void testFlowDomainOverlappedLogicQuestionAnswer() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, BadFlowContentException, FlowWithoutSourceException,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException,
			BadFormedExpressions, IncompleteLogic, RedundantLogic, DifferentDateUnitForQuestions, ElementIsReadOnly,
			FlowNotAllowedException {

		Form form = FormTestUtilities.createFormTest4();
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("cat1", "qu1"));
	}

	@Test(dependsOnMethods = { "testFlowDomainOverlappedLogicQuestionAnswer" })
	public void testAndOrQuestionAnswer() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, BadFlowContentException, FlowWithoutSourceException,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException,
			BadFormedExpressions, IncompleteLogic, RedundantLogic, InvalidAnswerFormatException,
			InvalidAnswerSubformatException, DifferentDateUnitForQuestions, ElementIsReadOnly, FlowNotAllowedException {

		Form form = FormTestUtilities.createFormTest6();
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("cat1", "qu3"));

	}

	// Tests using Json
	// Only answers, incomplete, redundant and complete logic only one question referenced on tokens.
	@Test(dependsOnMethods = { "testAndOrQuestionAnswer" }, expectedExceptions = { IncompleteLogic.class })
	public void testJson1() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_logic_answers_1.json");

		// Incomplete logic a, b but c is missing
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Question"));
	}

	@Test(dependsOnMethods = { "testJson1" }, expectedExceptions = { IncompleteLogic.class })
	public void testJson2() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_logic_answers_1.json");

		// Incomplete logic d or e, f or g but h ans i is missing
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Question2"));
	}

	@Test(dependsOnMethods = { "testJson2" })
	public void testJson3() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_logic_answers_1.json");

		// Complete logic without other
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Text"));
	}

	@Test(dependsOnMethods = { "testJson3" })
	public void testJson4() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_logic_answers_1.json");

		// Complete logic with others
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Text2"));
	}

	@Test(dependsOnMethods = { "testJson4" }, expectedExceptions = { RedundantLogic.class })
	public void testJson5() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_logic_answers_1.json");

		// Reduntant logic !a, b
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Text3"));
	}

	// Only answers, incomplete, redundant and complete logic only two questions referenced on tokens.
	@Test(dependsOnMethods = { "testJson5" })
	public void testJson6() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_logic_answers_2.json");

		// Complete logic
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Text"));
	}

	@Test(dependsOnMethods = { "testJson6" }, expectedExceptions = { IncompleteLogic.class })
	public void testJson7() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_logic_answers_2.json");

		// Incomplete logic a and d or e || b or c - a and (!d and !e) is left out.
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Text2"));
	}

	@Test(dependsOnMethods = { "testJson7" }, expectedExceptions = { RedundantLogic.class })
	public void testJson8() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_logic_answers_2.json");

		// Redundant Logic
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Text3"));
	}

	@Test(dependsOnMethods = { "testJson8" }, expectedExceptions = { IncompleteLogic.class })
	public void testJson9() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_logic_answers_2.json");

		// Incomplete logic
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Text4"));
	}

	@Test(dependsOnMethods = { "testJson9" })
	public void testJson10() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_logic_answers_2.json");

		// Complete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Text5"));
	}

	// Integer tests
	@Test(dependsOnMethods = { "testJson10" })
	public void testJson11() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_integer.json");

		// Complete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Question"));
	}

	@Test(dependsOnMethods = { "testJson11" })
	public void testJson12() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_integer.json");

		// Complete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Text"));
	}

	@Test(dependsOnMethods = { "testJson12" }, expectedExceptions = { IncompleteLogic.class })
	public void testJson13() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_integer.json");

		// Incomplete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Text2"));
	}

	@Test(dependsOnMethods = { "testJson13" }, expectedExceptions = { IncompleteLogic.class })
	public void testJson14() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_integer.json");

		// Incomplete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Text3"));
	}

	@Test(dependsOnMethods = { "testJson14" }, expectedExceptions = { IncompleteLogic.class })
	public void testJson15() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_integer.json");

		// Incomplete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Text4"));
	}

	// Floats
	@Test(dependsOnMethods = { "testJson15" })
	public void testJson16() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_float.json");

		// Complete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Question"));
	}

	@Test(dependsOnMethods = { "testJson16" }, expectedExceptions = { IncompleteLogic.class })
	public void testJson17() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_float.json");

		// Incomplete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Question2"));
	}

	@Test(dependsOnMethods = { "testJson17" })
	public void testJson18() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_float.json");

		// Complete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Question3"));
	}

	// Date
	@Test(dependsOnMethods = { "testJson18" })
	public void testJson19() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_date.json");

		// Complete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Question"));
	}

	@Test(dependsOnMethods = { "testJson19" }, expectedExceptions = { RedundantLogic.class })
	public void testJson20() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_date.json");

		// Overlap of two dates
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Question2"));
	}

	@Test(dependsOnMethods = { "testJson20" })
	public void testJson21() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_date.json");

		// Complete, date and int
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Question3"));
	}

	@Test(dependsOnMethods = { "testJson21" }, expectedExceptions = { IncompleteLogic.class })
	public void testJson22() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_date.json");

		// Incomplete, date and int
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Text"));
	}

	// Date period
	@Test(dependsOnMethods = { "testJson22" })
	public void testJson23() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_date_period.json");

		// Incomplete, date and int
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Question"));
	}

	@Test(dependsOnMethods = { "testJson23" })
	public void testJson24() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_date_period.json");

		// Complete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Question2"));
	}

	// Postal Codes
	@Test(dependsOnMethods = { "testJson24" })
	public void testJson25() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_postal_code.json");

		// Complete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Question"));
	}

	// Text
	@Test(dependsOnMethods = { "testJson25" })
	public void testJson26() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_text.json");

		// Complete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Question"));
	}

	@Test(dependsOnMethods = { "testJson26" })
	public void testJson27() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_text.json");

		// Complete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Question2"));
	}

	@Test(dependsOnMethods = { "testJson27" }, expectedExceptions = { IncompleteLogic.class })
	public void testJson28() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_text.json");

		// Incomplete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Text"));
	}

	@Test(dependsOnMethods = { "testJson28" })
	public void testJson29() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_text.json");
		// Incomplete
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category", "Group", "Text2"));
	}

	@Test(dependsOnMethods = { "testJson29" })
	public void testJson30() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("De Haagse Passage_v6.json");

		ValidateReport report = new ValidateReport();
		ValidateLogic validator = new ValidateLogic();
		Assert.assertFalse(validator.validate(form, report));

	}

	@Test(dependsOnMethods = { "testJson30" }/* , expectedExceptions = { IncompleteLogic.class } */)
	public void testJson31() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("De Haagse Passage_v6.json");

		// Error
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("Opleidingen", "Opleiding", "Opleiding", "Welke"));
	}

	@Test(dependsOnMethods = { "testJson31" }, expectedExceptions = { RedundantLogic.class })
	public void testJson32() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("De Haagse Passage_v6.json");

		// Error
		new FlowUnitDomain(form,
				(BaseQuestion) form.getChild("EigenMogelijkheden/SociaalNetwerk/EigenMogelijkhedenFamilie/Afstand"));
	}

	@Test(dependsOnMethods = { "testJson22" })
	public void testJson33() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Form form = loadForm("test_OrAnd.json");
		// Error
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("CAT/NUMBER"));
	}

	private Form loadForm(String filename) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		String result = IOUtils.toString(classLoader.getResourceAsStream(filename), "UTF-8");

		return Form.fromJson(result);
	}

}
