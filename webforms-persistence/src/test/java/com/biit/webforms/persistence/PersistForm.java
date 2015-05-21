package com.biit.webforms.persistence;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import junit.framework.Assert;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidParentException;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOriginException;
import com.biit.webforms.persistence.entity.exceptions.FlowNotAllowedException;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSourceException;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "persistForm" })
public class PersistForm extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private IFormDao formDao;

	@Test
	@Rollback(false)
	@Transactional
	public void testPersistCompleteForm() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			UnexpectedDatabaseException, ChildrenNotFoundException, BadFlowContentException,
			FlowWithoutSourceException, FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException,
			FlowWithoutDestinyException, NotValidTokenType, ElementIsReadOnly, FlowNotAllowedException,
			ElementCannotBeRemovedException, ElementCannotBePersistedException {
		int prevForms = formDao.getRowCount();
		Form form = FormUtils.createCompleteForm();
		formDao.makePersistent(form);
		Assert.assertEquals(prevForms + 1, formDao.getRowCount());
		Assert.assertNotNull(form.getId());

		formDao.makeTransient(form);
		Assert.assertEquals(prevForms, formDao.getRowCount());
	}

	@Test
	public void moveElementsInHierarchyDown() throws NotValidChildException, ChildrenNotFoundException, FieldTooLongException,
			CharacterNotAllowedException, UnexpectedDatabaseException, ElementIsReadOnly,
			ElementCannotBePersistedException, ElementCannotBeRemovedException {
		Form form = new Form();
		form.setOrganizationId(0l);
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
		answer1.setName("Answer1");
		question2.addChild(answer1);

		Answer answer2 = new Answer();
		answer2.setName("Answer2");
		question2.addChild(answer2);

		Answer answer3 = new Answer();
		answer3.setName("Answer3");
		question2.addChild(answer3);

		// Update form with new elements
		formDao.makePersistent(form);

		// Move #group1 to another category3
		Form.move(group1, category3);

		// Update form with this changes
		formDao.merge(form);
				
		Form storedForm = formDao.get(form.getId());
		Assert.assertNotNull(storedForm);

		// Compare order is the same.
		Assert.assertTrue(compare(form, storedForm));
		formDao.makeTransient(storedForm);
	}
	
	@Test
	@Rollback(value=false)
	@Transactional(value=TxType.NEVER)
	public void moveElementsInHierarchyUp() throws NotValidChildException, ChildrenNotFoundException, FieldTooLongException,
			CharacterNotAllowedException, UnexpectedDatabaseException, ElementIsReadOnly,
			ElementCannotBePersistedException, ElementCannotBeRemovedException, NotValidParentException {
		Form form = new Form();
		form.setOrganizationId(0l);
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
		answer1.setName("Answer1");
		question2.addChild(answer1);

		Answer answer2 = new Answer();
		answer2.setName("Answer2");
		question2.addChild(answer2);

		Answer answer3 = new Answer();
		answer3.setName("Answer3");
		question2.addChild(answer3);

		// Update form with new elements
		formDao.makePersistent(form);
		System.out.println(question3.getId());
		
		Form.move(question3, category);	
		
		// Update form with this changes
		formDao.merge(form);
				
		Form storedForm = formDao.get(form.getId());
		Assert.assertNotNull(storedForm);

		// Compare order is the same.
		Assert.assertTrue(compare(form, storedForm));
		formDao.makeTransient(storedForm);
	}
		
	private boolean compare(TreeObject object1, TreeObject object2) {
		if (!object1.getComparationId().equals(object2.getComparationId())) {
			return false;
		}
		if(object1.getChildren().size()!=object2.getChildren().size()){
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
