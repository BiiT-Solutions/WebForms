package com.biit.webforms.utils.math.domain;

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
	public void testFlowDomain() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
			BadFlowContentException, FlowWithoutSource, FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin,
			FlowWithoutDestiny, BadFormedExpressions, IncompleteLogic, RedundantLogic {
		System.out.println("testDiscreteDomain");

		Form form = FormTestUtilities.createFormTest1();
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("cat1", "qu1"));

	}

	@Test(dependsOnMethods = { "testFlowDomain" }, expectedExceptions = { IncompleteLogic.class })
	public void testFlowDomainNotCompleteLogicOnlyQuestionAnswer() throws FieldTooLongException,
			NotValidChildException, CharacterNotAllowedException, BadFlowContentException, FlowWithoutSource,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin, FlowWithoutDestiny, BadFormedExpressions,
			IncompleteLogic, RedundantLogic {

		Form form = FormTestUtilities.createFormTest2();
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("cat1", "qu1"));
	}

	@Test(dependsOnMethods = { "testFlowDomainNotCompleteLogicOnlyQuestionAnswer" })
	public void testFlowDomainCompleteLogicWithOr() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, BadFlowContentException, FlowWithoutSource,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin, FlowWithoutDestiny, BadFormedExpressions,
			IncompleteLogic, RedundantLogic {

		Form form = FormTestUtilities.createFormTest3();
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("cat1", "qu1"));
	}

	@Test(dependsOnMethods = { "testFlowDomainCompleteLogicWithOr" }, expectedExceptions = { RedundantLogic.class })
	public void testFlowDomainOverlappedLogicQuestionAnswer() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, BadFlowContentException, FlowWithoutSource,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin, FlowWithoutDestiny, BadFormedExpressions,
			IncompleteLogic, RedundantLogic {

		Form form = FormTestUtilities.createFormTest4();
		new FlowUnitDomain(form, (BaseQuestion) form.getChild("cat1", "qu1"));
	}

	@Test(dependsOnMethods = { "testFlowDomainOverlappedLogicQuestionAnswer" })
	public void testAndOrQuestionAnswer() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, BadFlowContentException, FlowWithoutSource,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin, FlowWithoutDestiny, BadFormedExpressions,
			IncompleteLogic, RedundantLogic, InvalidAnswerFormatException, InvalidAnswerSubformatException {

			Form form = FormTestUtilities.createFormTest6();
			new FlowUnitDomain(form, (BaseQuestion) form.getChild("cat1", "qu3"));
		
	}

	// @Test(expectedExceptions = { RedundantLogic.class })
	// public void testFlowDomainOverlappedLogicQuestionAnswer() throws
	// FieldTooLongException, NotValidChildException,
	// CharacterNotAllowedException, BadFlowContentException, FlowWithoutSource,
	// FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin,
	// FlowWithoutDestiny, BadFormedExpressions,
	// IncompleteLogic, RedundantLogic {
	//
	// Form form = FormTestUtilities.createFormTest4();
	// System.out.println(form.getFlows());
	//
	// FlowUnitDomain flowUnitDomain = new FlowUnitDomain(form, (BaseQuestion)
	// form.getChild("cat1", "qu1"));
	// }

	// @Test()
	// public void testFlowDomainOverlappedLogicQuestionAnswer() throws
	// FieldTooLongException, NotValidChildException,
	// CharacterNotAllowedException, BadFlowContentException, FlowWithoutSource,
	// FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin,
	// FlowWithoutDestiny, BadFormedExpressions,
	// IncompleteLogic, RedundantLogic, InvalidAnswerFormatException,
	// InvalidAnswerSubformatException {
	//
	// Form form = FormTestUtilities.createFormTest5();
	// System.out.println(form.getFlows());
	//
	// FlowUnitDomain flowUnitDomain = new FlowUnitDomain(form, (BaseQuestion)
	// form.getChild("cat1", "qu1"));
	// }
}
