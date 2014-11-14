package com.biit.webforms.persistence;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.enumerations.FlowType;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOrigin;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestiny;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "ruleTest" })
public class RuleTest extends AbstractTransactionalTestNGSpringContextTests {

	private static final String FORM_NAME = "test_form";
	private static final String CATEGORY_ONE_NAME = "category_1";
	private static final String GROUP_ONE_NAME = "group_1";
	private static final String QUESTION_WO_ANSWERS = "question_wo_answers";
	private static final String QUESTION_W_ANSWERS = "question_w_answers";
	private static final String ANSWER_1 = "answer1";
	private static final String ANSWER_2 = "answer2";
	private static final Long ORGANIZATION_ID = 0L;

	@Autowired
	private IFormDao formDao;

	@Test
	public void testRule() throws FieldTooLongException, CharacterNotAllowedException, InvalidAnswerFormatException,
			InvalidAnswerSubformatException, NotValidChildException, UnexpectedDatabaseException {
		Form form = createForm();

		formDao.makePersistent(form);
		Assert.assertEquals(formDao.getRowCount(), 1);

		Form dbForm = formDao.getForm(FORM_NAME, ORGANIZATION_ID);

		Assert.assertTrue(!dbForm.getFlows().isEmpty());

		Flow dbRule = dbForm.getFlows().iterator().next();
		Assert.assertTrue(!dbRule.getCondition().isEmpty());

		// Check that removing rule doesn't make disappear anything else.
		Assert.assertTrue(dbForm.getAll(Question.class).size() == 2);
		dbForm.removeRule(dbRule);
		formDao.makePersistent(dbForm);
		dbForm = formDao.getForm(FORM_NAME, ORGANIZATION_ID);
		Assert.assertTrue(dbForm.getAll(Question.class).size() == 2);
		Assert.assertTrue(dbForm.getFlows().isEmpty());

		formDao.makeTransient(dbForm);
	}

	private Form createForm() {
		try {
			Form form = new Form();
			form.setLabel(FORM_NAME);
			form.setOrganizationId(ORGANIZATION_ID);

			Category category = new Category();
			category.setName(CATEGORY_ONE_NAME);

			Group group = new Group();
			group.setName(GROUP_ONE_NAME);

			Question question1 = new Question(QUESTION_WO_ANSWERS);
			question1.setAnswerType(AnswerType.INPUT);
			question1.setAnswerFormat(AnswerFormat.TEXT);
			question1.setAnswerSubformat(AnswerSubformat.EMAIL);

			Question question2 = new Question(QUESTION_W_ANSWERS);
			question2.setAnswerType(AnswerType.SINGLE_SELECTION_LIST);

			Answer answer1 = new Answer(ANSWER_1);
			Answer answer2 = new Answer(ANSWER_2);

			question2.addChild(answer1);
			question2.addChild(answer2);

			group.addChild(question1);
			group.addChild(question2);
			category.addChild(group);
			form.addChild(category);

			List<Token> condition = Arrays.asList(new Token[] {
					TokenComparationValue.getTokenEqual(question1, AnswerSubformat.TEXT,null, "test"),
					TokenComparationAnswer.getTokenEqual(question2, answer1) });
			Flow rule1 = createRule(question1, FlowType.NORMAL, question2, false, condition);

			form.addFlow(rule1);

			return form;
		} catch (FieldTooLongException | CharacterNotAllowedException | InvalidAnswerFormatException
				| InvalidAnswerSubformatException | NotValidChildException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Flow createRule(Question question1, FlowType ruletype, Question question2, boolean others,
			List<Token> condition) {

		try {
			Flow rule = new Flow();
			rule.setContent(question1, ruletype, question2, false, condition);
			return rule;
		} catch (BadFlowContentException | FlowWithoutSource | FlowSameOriginAndDestinyException
				| FlowDestinyIsBeforeOrigin | FlowWithoutDestiny e) {
			e.printStackTrace();
			return null;
		}
	}
}
