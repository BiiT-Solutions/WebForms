package com.biit.webforms.utils.math.domain;

import org.testng.annotations.Test;

import com.biit.form.BaseQuestion;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOrigin;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestiny;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSource;
import com.biit.webforms.utils.math.domain.exceptions.BadFormedExpressions;
import com.biit.webforms.utils.math.domain.exceptions.IncompleteLogic;

@Test(groups = { "testFlowDomain" })
public class TestFlowDomain {

	@Test
	public void testFlowDomain() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
			BadFlowContentException, FlowWithoutSource, FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin,
			FlowWithoutDestiny, BadFormedExpressions, IncompleteLogic {
		System.out.println("testDiscreteDomain");

		Form form = FormTestUtilities.createFormTest1();
		System.out.println(form.getFlows());

		FlowUnitDomain flowUnitDomain = new FlowUnitDomain(form, (BaseQuestion) form.getChild("cat1", "qu1"));

	}
	
	@Test(expectedExceptions ={IncompleteLogic.class})
	public void testFlowDomainNotCompleteLogicOnlyQuestionAnswer() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
			BadFlowContentException, FlowWithoutSource, FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin,
			FlowWithoutDestiny, BadFormedExpressions, IncompleteLogic {

		Form form = FormTestUtilities.createFormTest2();
		System.out.println(form.getFlows());

		FlowUnitDomain flowUnitDomain = new FlowUnitDomain(form, (BaseQuestion) form.getChild("cat1", "qu1"));

	}

}
