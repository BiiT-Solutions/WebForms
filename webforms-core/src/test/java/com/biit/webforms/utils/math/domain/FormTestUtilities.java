package com.biit.webforms.utils.math.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.enumerations.FlowType;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOrigin;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestiny;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSource;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;

public class FormTestUtilities {

	public static Form createFormTest1() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, BadFlowContentException, FlowWithoutSource,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin, FlowWithoutDestiny {

		// Structure
		Question qu1 = createQuestionAnswer("qu1", "a", "b", "c");
		Question qu2 = createQuestionAnswer("qu2", "d", "e");
		Question qu3 = createQuestionAnswer("qu3", "f", "g");

		Category cat1 = createCategory("cat1", qu1, qu2, qu3);
		Form form = createForm("test1", cat1);

		// Flow
		Flow flow1 = createNormalFlow(qu1, qu2, false, token(qu1, "==", "a"));
		Flow flow2 = createEndFlow(qu1, true);
		Flow flow3 = createNormalFlow(qu2, qu3, false, token(qu2, "==", "d"));
		Flow flow4 = createEndFlow(qu2, true);

		form.addFlow(flow1);
		form.addFlow(flow2);
		form.addFlow(flow3);
		form.addFlow(flow4);

		return form;
	}

	/**
	 * Generates a form test similar to the first one except this one has one
	 * others missing causing a situation of incomplete logic.
	 * 
	 * @return
	 * @throws FieldTooLongException
	 * @throws NotValidChildException
	 * @throws CharacterNotAllowedException
	 * @throws BadFlowContentException
	 * @throws FlowWithoutSource
	 * @throws FlowSameOriginAndDestinyException
	 * @throws FlowDestinyIsBeforeOrigin
	 * @throws FlowWithoutDestiny
	 */
	public static Form createFormTest2() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, BadFlowContentException, FlowWithoutSource,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin, FlowWithoutDestiny {
		// Structure
		Question qu1 = createQuestionAnswer("qu1", "a", "b", "c");
		Question qu2 = createQuestionAnswer("qu2", "d", "e");
		Question qu3 = createQuestionAnswer("qu3", "f", "g");

		Category cat1 = createCategory("cat1", qu1, qu2, qu3);
		Form form = createForm("test1", cat1);

		// Flow
		Flow flow1 = createNormalFlow(qu1, qu2, false, token(qu1, "==", "a"));
		Flow flow3 = createNormalFlow(qu2, qu3, false, token(qu2, "==", "d"));
		Flow flow4 = createEndFlow(qu2, true);

		form.addFlow(flow1);
		form.addFlow(flow3);
		form.addFlow(flow4);

		return form;
	}

	public static Form createFormTest3() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, BadFlowContentException, FlowWithoutSource,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin, FlowWithoutDestiny {
		// Structure
		Question qu1 = createQuestionAnswer("qu1", "a", "b", "c");
		Question qu2 = createQuestionAnswer("qu2", "d", "e");
		Question qu3 = createQuestionAnswer("qu3", "f", "g");

		Category cat1 = createCategory("cat1", qu1, qu2, qu3);
		Form form = createForm("test1", cat1);

		// Flow
		Flow flow1 = createNormalFlow(qu1, qu2, false, token(qu1, "==", "a"), or(), token(qu1, "!=", "a"));
		Flow flow3 = createNormalFlow(qu2, qu3, false, token(qu2, "==", "d"));
		Flow flow4 = createEndFlow(qu2, true);

		form.addFlow(flow1);
		form.addFlow(flow3);
		form.addFlow(flow4);

		return form;
	}

	public static Form createFormTest4() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, BadFlowContentException, FlowWithoutSource,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin, FlowWithoutDestiny {

		// Structure
		Question qu1 = createQuestionAnswer("qu1", "a", "b", "c");
		Question qu2 = createQuestionAnswer("qu2", "d", "e");
		Question qu3 = createQuestionAnswer("qu3", "f", "g");

		Category cat1 = createCategory("cat1", qu1, qu2, qu3);
		Form form = createForm("test1", cat1);

		// Flow
		Flow flow1 = createNormalFlow(qu1, qu2, false, token(qu1, "==", "a"));
		Flow flow1rep = createNormalFlow(qu1, qu2, false, token(qu1, "==", "a"), or(), token(qu1, "!=", "c"));
		Flow flow2 = createEndFlow(qu1, true);
		Flow flow3 = createNormalFlow(qu2, qu3, false, token(qu2, "==", "d"));
		Flow flow4 = createEndFlow(qu2, true);

		form.addFlow(flow1);
		form.addFlow(flow1rep);
		form.addFlow(flow2);
		form.addFlow(flow3);
		form.addFlow(flow4);

		return form;
	}

	public static Form createFormTest5() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, BadFlowContentException, FlowWithoutSource,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin, FlowWithoutDestiny,
			InvalidAnswerFormatException, InvalidAnswerSubformatException {

		// Structure
		Question qu1 = createQuestionValue("qu1", AnswerFormat.NUMBER, AnswerSubformat.FLOAT);
		Question qu2 = createQuestionValue("qu2", AnswerFormat.NUMBER, AnswerSubformat.NUMBER);
		Question qu3 = createQuestionAnswer("qu3", "f", "g");

		Category cat1 = createCategory("cat1", qu1, qu2, qu3);
		Form form = createForm("test1", cat1);

		// Flow
		Flow flow1 = createNormalFlow(qu1, qu2, false, token(qu1, "<", AnswerSubformat.FLOAT, "2.0"));
		Flow flow2 = createEndFlow(qu1, true);
		Flow flow3 = createNormalFlow(qu2, qu3, false, token(qu2, ">=", AnswerSubformat.NUMBER, "5"));
		Flow flow4 = createEndFlow(qu2, true);

		form.addFlow(flow1);
		form.addFlow(flow2);
		form.addFlow(flow3);
		form.addFlow(flow4);

		return form;
	}

	private static Token or() {
		return Token.or();
	}

	public static Form createForm(String name, Category... categories) throws FieldTooLongException,
			NotValidChildException {
		Form form = new Form();
		form.setLabel(name);

		for (Category category : categories) {
			form.addChild(category);
		}

		return form;
	}

	public static Category createCategory(String name, TreeObject... elements) throws FieldTooLongException,
			NotValidChildException, CharacterNotAllowedException {
		Category category = new Category(name);

		for (TreeObject element : elements) {
			category.addChild(element);
		}

		return category;
	}

	public static Question createQuestionAnswer(String name, String... answers) throws FieldTooLongException,
			CharacterNotAllowedException, NotValidChildException {
		Question question = new Question(name);
		question.setAnswerType(AnswerType.SINGLE_SELECTION_RADIO);

		for (String answer : answers) {
			question.addChild(new Answer(answer));
		}

		return question;
	}

	public static Question createQuestionValue(String name, AnswerFormat answerFormat, AnswerSubformat answerSubformat)
			throws FieldTooLongException, CharacterNotAllowedException, InvalidAnswerFormatException,
			InvalidAnswerSubformatException {
		Question question = new Question(name);
		question.setAnswerType(AnswerType.INPUT);
		question.setAnswerFormat(answerFormat);
		question.setAnswerSubformat(answerSubformat);
		return question;
	}

	public static Flow createNormalFlow(BaseQuestion origin, BaseQuestion destiny, boolean others, Token... tokens)
			throws BadFlowContentException, FlowWithoutSource, FlowSameOriginAndDestinyException,
			FlowDestinyIsBeforeOrigin, FlowWithoutDestiny {
		List<Token> condition = new ArrayList<Token>();
		condition.addAll(Arrays.asList(tokens));
		Flow flow = new Flow();
		flow.setContent(origin, FlowType.NORMAL, destiny, others, condition);
		return flow;
	}

	public static Flow createEndFlow(BaseQuestion origin, boolean others, Token... tokens)
			throws BadFlowContentException, FlowWithoutSource, FlowSameOriginAndDestinyException,
			FlowDestinyIsBeforeOrigin, FlowWithoutDestiny {
		List<Token> condition = new ArrayList<Token>();
		condition.addAll(Arrays.asList(tokens));
		Flow flow = new Flow();
		flow.setContent(origin, FlowType.END_FORM, null, others, condition);
		return flow;
	}

	public static List<Token> tokens(Token... tokens) {
		List<Token> condition = new ArrayList<>();
		for (Token token : tokens) {
			condition.add(token);
		}
		return condition;
	}

	public static Token token(Question question, String type, String answer) {
		return TokenComparationAnswer.getToken(question, TokenTypes.fromString(type), question.getAnswer(answer));
	}

	public static Token token(Question question, String type, AnswerSubformat subformat, String value) {
		return TokenComparationValue.getToken(TokenTypes.fromString(type), question, subformat, value);
	}

}
