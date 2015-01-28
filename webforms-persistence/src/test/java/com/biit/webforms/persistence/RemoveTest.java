package com.biit.webforms.persistence;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "removeElement" })
public class RemoveTest extends AbstractTransactionalTestNGSpringContextTests {
	private static final String FORM_COMPLETE_LABEL = "complete form error";
	public static final String CATEGORY_1 = "category1";
	public static final String CATEGORY_2 = "category2";
	public static final String CATEGORY_3 = "category3";

	public static final String GROUP_1 = "group1";
	public static final String GROUP_2 = "group2";
	public static final String GROUP_3 = "group3";

	public static final String QUESTION_1 = "question1";
	public static final String QUESTION_2 = "question2";
	public static final String QUESTION_3 = "question3";

	private Long organizationId = 0L;

	@Autowired
	private IFormDao formDao;

	public Form createForm() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
			InvalidAnswerFormatException, InvalidAnswerSubformatException, UnexpectedDatabaseException {
		Form form = new Form();
		form.setOrganizationId(++organizationId);
		form.setLabel(FORM_COMPLETE_LABEL);

		form.addChild(createCategory(CATEGORY_1));
		form.addChild(createCategory(CATEGORY_2));
		form.addChild(createCategory(CATEGORY_3));
		Assert.assertEquals(3, form.getAllChildrenInHierarchy(Category.class).size());

		form.getChild(CATEGORY_1).addChild(createGroup(GROUP_1));
		form.getChild(CATEGORY_2).addChild(createGroup(GROUP_2));
		form.getChild(CATEGORY_3).addChild(createGroup(GROUP_3));
		Assert.assertEquals(3, form.getAllChildrenInHierarchy(Group.class).size());

		form.getChild(CATEGORY_1, GROUP_1).addChild(createQuestion(QUESTION_1));
		form.getChild(CATEGORY_2, GROUP_2).addChild(createQuestion(QUESTION_2));
		form.getChild(CATEGORY_3, GROUP_3).addChild(createQuestion(QUESTION_3));
		Assert.assertEquals(3, form.getAllChildrenInHierarchy(Question.class).size());

		return form;
	}

	/**
	 * (27-01-2015) Arie has an exception when doing an operation similar to the described in this test.
	 * 
	 * @throws DependencyExistException
	 * @throws UnexpectedDatabaseException
	 * @throws FieldTooLongException
	 * @throws NotValidChildException
	 * @throws CharacterNotAllowedException
	 * @throws InvalidAnswerFormatException
	 * @throws InvalidAnswerSubformatException
	 */
	@Test
	public void removeElements() throws DependencyExistException, UnexpectedDatabaseException, FieldTooLongException,
			NotValidChildException, CharacterNotAllowedException, InvalidAnswerFormatException,
			InvalidAnswerSubformatException {
		Form form = createForm();

		formDao.makePersistent(form);
		Assert.assertNotNull(form.getId());

		form.getChild(CATEGORY_2, GROUP_2).remove();
		form.getChild(CATEGORY_2).remove();
		form.getChild(CATEGORY_1).remove();

		formDao.makePersistent(form);
	}

	public static Category createCategory(String name) throws FieldTooLongException, CharacterNotAllowedException,
			NotValidChildException, InvalidAnswerFormatException, InvalidAnswerSubformatException {
		Category category = new Category();
		category.setName(name);
		category.setLabel(name);

		return category;
	}

	public static Group createGroup(String name) throws FieldTooLongException, CharacterNotAllowedException,
			NotValidChildException, InvalidAnswerFormatException, InvalidAnswerSubformatException {
		Group group = new Group();
		group.setName(name);
		group.setLabel(name);

		return group;
	}

	public static Question createQuestion(String name) throws FieldTooLongException, CharacterNotAllowedException,
			NotValidChildException, InvalidAnswerFormatException, InvalidAnswerSubformatException {
		Question question = new Question();
		question.setName(name);
		question.setLabel(name);

		return question;
	}
}
