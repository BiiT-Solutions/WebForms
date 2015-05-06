package com.biit.webforms.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.enumerations.FlowType;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.BlockReference;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOriginException;
import com.biit.webforms.persistence.entity.exceptions.FlowNotAllowedException;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSourceException;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;

public class FormUtils {

	private static final Long ORGANIZATION_ID = 0L;
	private static final String FORM_COMPLETE_LABEL = "complete form test";
	public static final String BLOCK_1 = "block1";
	public static final String CATEGORY_1 = "category1";
	public static final String CATEGORY_2 = "category2";
	public static final String CATEGORY_IN_BLOCK_1 = "categoryInBlock1";
	public static final String CATEGORY_IN_BLOCK_2 = "categoryInBlock2";
	public static final String QUESTION_IN_BLOCK_11 = "questionInBlock1";
	public static final String QUESTION_IN_BLOCK_12 = "questionInBlock2";
	public static final String SYSTEM_FIELD_1 = "sysfield1";
	public static final String SYSTEM_FIELD_1_NAME = "sys.field";
	public static final String INFO_TEXT_1 = "infoText1";
	public static final String IPSUM_LOREM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque imperdiet lacus vitae pulvinar dictum. Nulla sed mollis felis. Nullam vitae tempus turpis, vitae efficitur elit. Maecenas ultricies, ante nec consequat pulvinar,";
	public static final String GROUP_1 = "group1";
	public static final String INPUT_TEXT_TEXT = "question1";
	public static final String INPUT_TEXT_NUMBER_FLOAT = "questionNumberFloat";
	public static final String INPUT_TEXT_POSTAL_CODE = "questionPostalCode";
	public static final String INPUT_TEXT_TEXT_BSN = "questionBsn";
	public static final String INPUT_TEXT_DATE_BIRTHDAY = "questionBirthday";
	public static final String RADIO_BUTTON = "questionRadioButton";
	public static final String MULTI_CHECKBOX = "questionMultiCheckbox";
	public static final String LIST = "questionList";
	public static final String ANSWER_A = "answerA";
	public static final String ANSWER_B = "answerB";
	public static final String ANSWER_C = "answerC";
	public static final String ANSWER_D = "answerD";
	public static final String ANSWER_E = "answerE";
	public static final String ANSWER_F = "answerF";
	public static final String ANSWER_G = "answerG";
	public static final String ANSWER_H = "answerH";
	public static final String ANSWER_I = "answerI";
	public static final String ANSWER_J = "answerJ";

	public static Form createCompleteForm() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			BadFlowContentException, FlowWithoutSourceException, FlowSameOriginAndDestinyException,
			FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException, NotValidTokenType, ElementIsReadOnly,
			FlowNotAllowedException {
		return createCompleteForm(null);
	}

	public static Form createCompleteForm(Block withBlock) throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			BadFlowContentException, FlowWithoutSourceException, FlowSameOriginAndDestinyException,
			FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException, NotValidTokenType, ElementIsReadOnly,
			FlowNotAllowedException {
		return createCompleteForm(withBlock, FORM_COMPLETE_LABEL);
	}

	/**
	 * Creates a form with two categories.
	 * 
	 * @param withBlock
	 * @param label
	 * @return
	 * @throws FieldTooLongException
	 * @throws NotValidChildException
	 * @throws CharacterNotAllowedException
	 * @throws InvalidAnswerFormatException
	 * @throws InvalidAnswerSubformatException
	 * @throws BadFlowContentException
	 * @throws FlowWithoutSourceException
	 * @throws FlowSameOriginAndDestinyException
	 * @throws FlowDestinyIsBeforeOriginException
	 * @throws FlowWithoutDestinyException
	 * @throws NotValidTokenType
	 * @throws ElementIsReadOnly
	 * @throws FlowNotAllowedException
	 */
	public static Form createCompleteForm(Block withBlock, String label) throws FieldTooLongException,
			NotValidChildException, CharacterNotAllowedException, InvalidAnswerFormatException,
			InvalidAnswerSubformatException, BadFlowContentException, FlowWithoutSourceException,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException,
			NotValidTokenType, ElementIsReadOnly, FlowNotAllowedException {
		Form form = new Form();
		form.setOrganizationId(ORGANIZATION_ID);
		form.setLabel(label);
		form.setCreatedBy(0l);
		form.setUpdatedBy(0l);

		form.addChild(createCategory(CATEGORY_1));
		if (withBlock != null) {
			form.addChild(createReferencedBlock(BLOCK_1, withBlock));
		}
		form.addChild(createCategory(CATEGORY_2));

		((Group) form.getChild(CATEGORY_2, GROUP_1)).setRepeatable(true);

		// Create empty go to end form
		form.addFlow(createEndFormflow((BaseQuestion) form.getChild(CATEGORY_1, SYSTEM_FIELD_1), false,
				emptyCondition()));
		// Create others flow
		form.addFlow(createEndFormflow((BaseQuestion) form.getChild(CATEGORY_1, SYSTEM_FIELD_1), true, emptyCondition()));
		// Create flow from A to B empty condition.
		form.addFlow(createFlow((BaseQuestion) form.getChild(CATEGORY_1, SYSTEM_FIELD_1),
				(BaseQuestion) form.getChild(CATEGORY_1, INFO_TEXT_1), false, emptyCondition()));
		// Create flow from A to B empty condition.
		Token token1 = token(form.getChild(CATEGORY_1, GROUP_1, INPUT_TEXT_NUMBER_FLOAT), TokenTypes.LT, "3.0",
				AnswerSubformat.FLOAT);
		form.addFlow(createFlow((BaseQuestion) form.getChild(CATEGORY_2, SYSTEM_FIELD_1),
				(BaseQuestion) form.getChild(CATEGORY_2, INFO_TEXT_1), false, condition(token1)));

		return form;
	}

	public static Token token(TreeObject question, TokenTypes type, String value, AnswerSubformat subformat)
			throws NotValidTokenType {
		TokenComparationValue token = new TokenComparationValue(type);
		token.setQuestion((Question) question);
		token.setValue(value);
		token.setSubformat(subformat);
		return token;
	}

	public static List<Token> emptyCondition() {
		return new ArrayList<>();
	}

	public static List<Token> condition(Token... tokens) {
		return Arrays.asList(tokens);
	}

	public static Flow createFlow(BaseQuestion origin, BaseQuestion destiny, boolean others, List<Token> conditions)
			throws BadFlowContentException, FlowWithoutSourceException, FlowSameOriginAndDestinyException,
			FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException {
		Flow flow = new Flow();
		flow.setContent(origin, FlowType.NORMAL, destiny, others, conditions);
		return flow;
	}

	public static Flow createEndFormflow(BaseQuestion origin, boolean others, List<Token> condition)
			throws BadFlowContentException, FlowWithoutSourceException, FlowSameOriginAndDestinyException,
			FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException {
		Flow flow = new Flow();
		flow.setContent(origin, FlowType.END_FORM, null, others, condition);
		return flow;
	}

	public static BlockReference createReferencedBlock(String name, Block block) throws NotValidChildException,
			FieldTooLongException, CharacterNotAllowedException, InvalidAnswerFormatException,
			InvalidAnswerSubformatException {
		BlockReference blockReference = new BlockReference();
		blockReference.setReference(block);
		return blockReference;
	}

	public static Block createBlock() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			ElementIsReadOnly {
		Block block = new Block();
		Category category = createCategory(CATEGORY_IN_BLOCK_1);
		block.addChild(category);
		category.addChild(createQuestionAnswers(QUESTION_IN_BLOCK_11, AnswerType.INPUT));
		category.addChild(createQuestionAnswers(QUESTION_IN_BLOCK_12, AnswerType.INPUT));
		block.setOrganizationId(ORGANIZATION_ID);
		block.setLabel(BLOCK_1);
		return block;
	}

	/**
	 * Creates a category with @param{name} as name and label.
	 * 
	 * @param name
	 * @return
	 * @throws CharacterNotAllowedException
	 * @throws FieldTooLongException
	 * @throws NotValidChildException
	 * @throws InvalidAnswerSubformatException
	 * @throws InvalidAnswerFormatException
	 * @throws ElementIsReadOnly
	 */
	public static Category createCategory(String name) throws FieldTooLongException, CharacterNotAllowedException,
			NotValidChildException, InvalidAnswerFormatException, InvalidAnswerSubformatException, ElementIsReadOnly {
		Category category = new Category();
		category.setName(name);
		category.setLabel(name);

		category.addChild(createSystemField(SYSTEM_FIELD_1));
		category.addChild(createInfoText(INFO_TEXT_1));
		category.addChild(createGroup(GROUP_1));

		return category;
	}

	private static TreeObject createGroup(String name) throws FieldTooLongException, CharacterNotAllowedException,
			NotValidChildException, InvalidAnswerFormatException, InvalidAnswerSubformatException, ElementIsReadOnly {
		Group group = new Group();
		group.setName(name);
		group.setLabel(name);

		group.addChild(createTextField(INPUT_TEXT_TEXT, AnswerFormat.TEXT, AnswerSubformat.TEXT));
		group.addChild(createTextField(INPUT_TEXT_NUMBER_FLOAT, AnswerFormat.NUMBER, AnswerSubformat.FLOAT));
		group.addChild(createTextField(INPUT_TEXT_POSTAL_CODE, AnswerFormat.POSTAL_CODE, AnswerSubformat.POSTAL_CODE));
		group.addChild(createTextField(INPUT_TEXT_TEXT_BSN, AnswerFormat.TEXT, AnswerSubformat.BSN));
		group.addChild(createTextField(INPUT_TEXT_DATE_BIRTHDAY, AnswerFormat.DATE, AnswerSubformat.DATE_BIRTHDAY));
		group.addChild(createQuestionAnswers(RADIO_BUTTON, AnswerType.SINGLE_SELECTION_RADIO, ANSWER_A, ANSWER_B,
				ANSWER_C));
		group.addChild(createQuestionAnswers(MULTI_CHECKBOX, AnswerType.MULTIPLE_SELECTION, ANSWER_D, ANSWER_E,
				ANSWER_F, ANSWER_G));
		group.addChild(createQuestionAnswers(LIST, AnswerType.SINGLE_SELECTION_LIST, ANSWER_H, ANSWER_I, ANSWER_J));

		return group;
	}

	private static TreeObject createTextField(String name, AnswerFormat format, AnswerSubformat subformat)
			throws FieldTooLongException, CharacterNotAllowedException, InvalidAnswerFormatException,
			InvalidAnswerSubformatException {
		Question question = new Question();
		question.setName(name);
		question.setLabel(name);

		question.setAnswerType(AnswerType.INPUT);
		question.setAnswerFormat(format);
		question.setAnswerSubformat(subformat);

		return question;
	}

	private static TreeObject createAnswer(String name) throws FieldTooLongException, CharacterNotAllowedException {
		Answer answer = new Answer();
		answer.setName(name);
		answer.setLabel(name);
		return answer;
	}

	public static TreeObject createQuestionAnswers(String name, AnswerType answerType, String... answers)
			throws NotValidChildException, FieldTooLongException, CharacterNotAllowedException, ElementIsReadOnly {
		Question question = new Question();
		question.setName(name);
		question.setLabel(name);

		question.setAnswerType(answerType);
		for (String answer : answers) {
			question.addChild(createAnswer(answer));
		}
		return question;
	}

	private static TreeObject createSystemField(String systemField1) throws FieldTooLongException,
			CharacterNotAllowedException {
		SystemField systemField = new SystemField();
		systemField.setName(systemField1);
		systemField.setFieldName(SYSTEM_FIELD_1_NAME);
		return systemField;
	}

	private static TreeObject createInfoText(String infoText) throws FieldTooLongException,
			CharacterNotAllowedException {
		Text text = new Text();
		text.setName(infoText);
		text.setDescription(IPSUM_LOREM);
		return text;
	}

	public static Token token(Question question, String type, String answer) {
		return TokenComparationAnswer.getToken(question, TokenTypes.fromString(type), question.getAnswer(answer));
	}

	public static Token token(Question question, String type, AnswerSubformat subformat, String value) {
		return TokenComparationValue.getToken(TokenTypes.fromString(type), question, subformat, null, value);
	}

}
