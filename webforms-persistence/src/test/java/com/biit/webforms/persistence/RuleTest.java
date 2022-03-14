package com.biit.webforms.persistence;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.biit.utils.annotations.FindBugsSuppressWarnings;
import org.testng.Assert;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
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
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOriginException;
import com.biit.webforms.persistence.entity.exceptions.FlowNotAllowedException;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSourceException;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "ruleTest" })
public class RuleTest extends AbstractTransactionalTestNGSpringContextTests {

	private static final String FORM_NAME = "test_form";
	private static final String CATEGORY_ONE_NAME = "category_1";
	private static final String GROUP_ONE_NAME = "group_1";
	private static final String QUESTION_WITHOUT_ANSWERS = "question_wo_answers";
	private static final String QUESTION_WITH_ANSWERS = "question_w_answers";
	private static final String ANSWER_1 = "answer1";
	private static final String ANSWER_2 = "answer2";

	@Autowired
	private IFormDao formDao;

	@Test
	public void testRule() throws FieldTooLongException, CharacterNotAllowedException, InvalidAnswerFormatException,
			InvalidAnswerSubformatException, NotValidChildException, UnexpectedDatabaseException, ElementIsReadOnly,
			BadFlowContentException, FlowWithoutSourceException, FlowSameOriginAndDestinyException,
			FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException, FlowNotAllowedException,
			ElementCannotBeRemovedException, ElementCannotBePersistedException {
		Form form = createForm();

		int prevForm = formDao.getRowCount();

		formDao.makePersistent(form);
		Assert.assertEquals(formDao.getRowCount(), prevForm + 1);

		Form dbForm = formDao.get(form.getId());

		Assert.assertTrue(!dbForm.getFlows().isEmpty());

		Flow dbRule = dbForm.getFlows().iterator().next();
		Assert.assertTrue(!dbRule.getComputedCondition().isEmpty());

		// Check that removing rule doesn't make disappear anything else.
		Assert.assertTrue(dbForm.getAll(Question.class).size() == 2);
		dbForm.removeRule(dbRule);
		formDao.makePersistent(formDao.merge(dbForm));
		dbForm = formDao.get(form.getId());
		Assert.assertTrue(dbForm.getAll(Question.class).size() == 2);
		Assert.assertTrue(dbForm.getFlows().isEmpty());

		formDao.makeTransient(dbForm);
	}

	@FindBugsSuppressWarnings("DMI_RANDOM_USED_ONLY_ONCE")
	private Form createForm() throws ElementIsReadOnly, BadFlowContentException, FlowWithoutSourceException,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException,
			FlowNotAllowedException {
		try {
			Form form = new Form();
			form.setLabel(FORM_NAME);
			form.setOrganizationId(new Long(new Random().nextInt()));

			Category category = new Category();
			category.setName(CATEGORY_ONE_NAME);

			Group group = new Group();
			group.setName(GROUP_ONE_NAME);

			Question question1 = new Question(QUESTION_WITHOUT_ANSWERS);
			question1.setAnswerType(AnswerType.INPUT);
			question1.setAnswerFormat(AnswerFormat.TEXT);
			question1.setAnswerSubformat(AnswerSubformat.EMAIL);

			Question question2 = new Question(QUESTION_WITH_ANSWERS);
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
					TokenComparationValue.getTokenEqual(question1, AnswerSubformat.TEXT, null, "test"), Token.getAndToken(),
					TokenComparationAnswer.getTokenEqual(question2, answer1) });
			Flow rule1 = FormUtils.createFlow(question1, question2, false, condition);

			form.addFlow(rule1);

			return form;
		} catch (FieldTooLongException | CharacterNotAllowedException | InvalidAnswerFormatException
				| InvalidAnswerSubformatException | NotValidChildException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Test(expectedExceptions = { DependencyExistException.class })
	public void removeFlowWithQuestion() throws UnexpectedDatabaseException, DependencyExistException,
			ElementIsReadOnly, BadFlowContentException, FlowWithoutSourceException, FlowSameOriginAndDestinyException,
			FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException, FlowNotAllowedException {
		Form form = createForm();
		Question question2 = (Question) form.getChild(CATEGORY_ONE_NAME + "/" + GROUP_ONE_NAME + "/"
				+ QUESTION_WITH_ANSWERS);
		Assert.assertNotNull(question2);
		question2.remove();
	}

	@Test(expectedExceptions = { DependencyExistException.class })
	public void removeFlowWithQuestionPersisted() throws UnexpectedDatabaseException, DependencyExistException,
			ElementIsReadOnly, BadFlowContentException, FlowWithoutSourceException, FlowSameOriginAndDestinyException,
			FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException, FlowNotAllowedException,
			ElementCannotBeRemovedException, ElementCannotBePersistedException {
		Form form = createForm();
		formDao.makePersistent(form);

		Question question2 = (Question) form.getChild(CATEGORY_ONE_NAME + "/" + GROUP_ONE_NAME + "/"
				+ QUESTION_WITH_ANSWERS);
		Assert.assertNotNull(question2);

		try {
			question2.remove();
		} finally {
			formDao.makeTransient(form);
		}
	}

	@Test
	public void dummyTest() throws ElementIsReadOnly, BadFlowContentException, FlowWithoutSourceException,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException,
			FlowNotAllowedException, ElementCannotBeRemovedException {
		Form form = createForm();
		formDao.makePersistent(form);
		formDao.makeTransient(form);
	}

	@Test
	public void removeFlowAndQuestionPersisted() throws UnexpectedDatabaseException, DependencyExistException,
			ElementIsReadOnly, BadFlowContentException, FlowWithoutSourceException, FlowSameOriginAndDestinyException,
			FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException, FlowNotAllowedException,
			ElementCannotBeRemovedException, ElementCannotBePersistedException {
		Form form = createForm();
		formDao.makePersistent(form);

		// Remove first flow.
		form.getFlows().remove(form.getFlows().iterator().next());

		formDao.makePersistent(form);

		// Remove question.
		Question question2 = (Question) form.getChild(CATEGORY_ONE_NAME + "/" + GROUP_ONE_NAME + "/"
				+ QUESTION_WITH_ANSWERS);
		Assert.assertNotNull(question2);
		question2.remove();

		// persist
		formDao.makePersistent(form);

		formDao.makeTransient(form);
	}

}
