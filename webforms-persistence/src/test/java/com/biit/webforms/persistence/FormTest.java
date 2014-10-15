package com.biit.webforms.persistence;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "formDao" })
@DirtiesContext
public class FormTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_FORM = "Dummy Form";
	private final static String FULL_FORM = "Complete Form";
	private final static String OTHER_FORM = "Other Form";
	private final static String CATEGORY_LABEL = "Category1";
	// private final static String ACTION_EXPRESSION = "Score=3";
	private static final Long ORGANIZATION_ID = 0L;

	@Autowired
	private IFormDao formDao;

	@Test
	public void storeDummyForm() throws FieldTooLongException {
		Form form = new Form();
		form.setLabel(DUMMY_FORM);
		form.setOrganizationId(ORGANIZATION_ID);
		formDao.makePersistent(form);
		Assert.assertEquals(formDao.getRowCount(), 1);
		Assert.assertEquals(formDao.getForm(DUMMY_FORM,ORGANIZATION_ID).getLabel(), DUMMY_FORM);
		formDao.makeTransient(form);
		Assert.assertEquals(formDao.getRowCount(), 0);
		Assert.assertNull(formDao.getForm(DUMMY_FORM,ORGANIZATION_ID));
	}

	@Test
	public void storeFormWithCategory() throws NotValidChildException, FieldTooLongException {
		Form form = new Form();
		form.setLabel(FULL_FORM);
		Category category = new Category();
		category.setLabel(CATEGORY_LABEL);
		form.addChild(category);
		formDao.makePersistent(form);
		Form retrievedForm = formDao.read(form.getId());
		Assert.assertEquals(retrievedForm.getId(), form.getId());
		Assert.assertEquals(retrievedForm.getChildren().size(), 1);
		formDao.makeTransient(form);
	}

	@Test
	public void storeOtherFormWithSameLabelCategory() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException {
		Form form = new Form();
		form.setLabel(OTHER_FORM);
		Category category = new Category();
		category.setName(CATEGORY_LABEL);
		form.addChild(category);
		formDao.makePersistent(form);
		Form retrievedForm = formDao.read(form.getId());

		Assert.assertEquals(retrievedForm.getId(), form.getId());
		Assert.assertEquals(retrievedForm.getChildren().size(), 1);
		formDao.makeTransient(form);
	}

	@Test
	public void moveElementsUp() throws NotValidChildException, ChildrenNotFoundException, FieldTooLongException,
			CharacterNotAllowedException {
		Form form = new Form();
		form.setLabel("MoveUp");

		Category category = new Category();
		category.setName("Category1");
		form.addChild(category);

		Category category2 = new Category();
		category2.setName("Category2");
		form.addChild(category2);

		Category category3 = new Category();
		category3.setName("Category3");
		form.addChild(category3);

		Group group1 = new Group();
		group1.setName("Group1");
		category2.addChild(group1);

		Group group2 = new Group();
		group2.setName("Group2");
		category2.addChild(group2);

		Group group3 = new Group();
		group3.setName("Group3");
		category2.addChild(group3);

		Question question1 = new Question();
		question1.setName("Question1");
		group2.addChild(question1);

		Question question2 = new Question();
		question2.setName("Question2");
		group2.addChild(question2);

		Question question3 = new Question();
		question3.setName("Question3");
		group2.addChild(question3);

		Answer answer1 = new Answer();
		answer1.setValue("Answer1");
		question2.addChild(answer1);

		Answer answer2 = new Answer();
		answer2.setValue("Answer2");
		question2.addChild(answer2);

		Answer answer3 = new Answer();
		answer3.setValue("Answer3");
		question2.addChild(answer3);

		// Update form with new elements
		formDao.makePersistent(form);

		// Move #2 up
		form.switchChildren(1, 0, null);
		category2.switchChildren(1, 0, null);
		group2.switchChildren(1, 0, null);
		question2.switchChildren(1, 0, null);

		// Update form with this changes
		formDao.makePersistent(form);
		Form storedForm = formDao.read(form.getId());

		// Compare order is the same.
		Assert.assertTrue(compare(form, storedForm));
		formDao.makeTransient(form);
	}

	private boolean compare(TreeObject object1, TreeObject object2) {
		if (!object1.getComparationId().equals(object2.getComparationId())) {
			return false;
		}
		for (int i = 0; i < object1.getChildren().size(); i++) {
			if (!compare(object1.getChildren().get(i), object2.getChildren().get(i))) {
				return false;
			}
		}
		return true;
	}
}
