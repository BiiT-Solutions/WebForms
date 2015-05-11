package com.biit.webforms.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import junit.framework.Assert;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.persistence.dao.IBlockDao;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.dao.ISimpleFormViewDao;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.BlockReference;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.CompleteFormView;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SimpleFormView;
import com.biit.webforms.persistence.entity.condition.Token;
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
@Test(groups = { "blockLink" })
@TransactionConfiguration()
public class BlockLinkTests extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private IFormDao formDao;

	@Autowired
	private IBlockDao blockDao;

	@Autowired
	private ISimpleFormViewDao simpleFormViewDao;
	
	@Test
	@Rollback(value=false)
	@Transactional
	public void createLinkedBlock() throws NotValidChildException, FieldTooLongException, CharacterNotAllowedException,
			InvalidAnswerFormatException, InvalidAnswerSubformatException, UnexpectedDatabaseException,
			ElementIsReadOnly, ElementCannotBePersistedException {
		int previousBlocks = blockDao.getRowCount();
		Block block = getNewBlock();
		Assert.assertNotNull(block);
		blockDao.makePersistent(block);

		Assert.assertEquals(previousBlocks + 1, blockDao.getRowCount());
	}
	
	public static Block getNewBlock() throws NotValidChildException, FieldTooLongException, CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException, ElementIsReadOnly{
		Block block = FormUtils.createBlock();
		block.setLabel("LinkedBlock");
		return block;
	}

	// Form:
	// - Category1
	// -- SystemField
	// -- InfoText
	// -- Group1
	// --- Question1
	// --- Question2
	// --- Question3
	// --- Question4
	// --- Question5
	// --- Question6
	// --- Question7
	// --- Question8
	// - Category2
	// -- SystemField
	// -- InfoText
	// -- Group1
	// --- Question1
	// --- Question2
	// --- Question3
	// --- Question4
	// --- Question5
	// --- Question6
	// --- Question7
	// --- Question8
	//
	@Test(dependsOnMethods = { "createLinkedBlock" })
	@Rollback(value=false)
	@Transactional
	public void createForm() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
			InvalidAnswerFormatException, InvalidAnswerSubformatException, BadFlowContentException,
			FlowWithoutSourceException, FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException,
			FlowWithoutDestinyException, NotValidTokenType, UnexpectedDatabaseException, ElementIsReadOnly,
			FlowNotAllowedException, ElementCannotBePersistedException {
		int previousForms = formDao.getRowCount();
		Form form = getNewForm();
		formDao.makePersistent(form);
		Assert.assertEquals(previousForms + 1, formDao.getRowCount());
	}
	
	public Form getNewForm() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
		InvalidAnswerFormatException, InvalidAnswerSubformatException, BadFlowContentException,
		FlowWithoutSourceException, FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException,
		FlowWithoutDestinyException, NotValidTokenType, UnexpectedDatabaseException, ElementIsReadOnly,
		FlowNotAllowedException, ElementCannotBePersistedException {
		return FormUtils.createCompleteForm();
	}

	// Form:
	// - Category1
	// -- SystemField
	// -- InfoText
	// -- Group1
	// --- Question1
	// --- Question2
	// --- Question3
	// --- Question4
	// --- Question5
	// --- Question6
	// --- Question7
	// --- Question8
	// - Category2
	// -- SystemField
	// -- InfoText
	// -- Group1
	// --- Question1
	// --- Question2
	// --- Question3
	// --- Question4
	// --- Question5
	// --- Question6
	// --- Question7
	// --- Question8
	// - LinkedBlock (CategoryInBlock1)
	// -- SystemField
	// -- InfoText
	// -- Group1
	// --- Question1
	// --- Question2
	// --- Question3
	// --- Question4
	// --- Question5
	// --- Question6
	// --- Question7
	// --- Question8
	// -- Question1
	// -- Question2
	//
	@Test(dependsOnMethods = { "createLinkedBlock", "createForm" })
	@Rollback(value=false)
	@Transactional
	public void addLinkedBlockToForm() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			BadFlowContentException, FlowWithoutSourceException, FlowSameOriginAndDestinyException,
			FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException, NotValidTokenType,
			UnexpectedDatabaseException, ElementIsReadOnly, ElementCannotBePersistedException, FlowNotAllowedException {
		Form form = getNewForm();
		Block block = getNewBlock();
		BlockReference blockReference = new BlockReference(block);
		formDao.makePersistent(form);
		blockDao.makePersistent(block);
		CompleteFormView completeFormView = new CompleteFormView(form);
		
		int previousChildren = form.getChildren().size();
		int previousStorableObjects = form.getAllInnerStorableObjects().size();
		int previousStorableObjectsCompleteView = completeFormView.getAllInnerStorableObjects().size();
		
		form.addChild(blockReference);
		formDao.makePersistent(form);

		Assert.assertEquals(previousChildren + 1, form.getChildren().size());
		Assert.assertEquals(previousStorableObjects + 1, form.getAllInnerStorableObjects().size());
		Assert.assertEquals(previousStorableObjectsCompleteView
				+ blockReference.getReference().getAllInnerStorableObjects().size(), completeFormView
				.getAllInnerStorableObjects().size());
	}

	@Test(dependsOnMethods = { "addLinkedBlockToForm" })
	@Rollback(value=false)
	@Transactional
	public void moveUpLinkedBlockInForm() throws ElementIsReadOnly, FieldTooLongException, NotValidChildException, CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException, BadFlowContentException, FlowWithoutSourceException, FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException, NotValidTokenType, UnexpectedDatabaseException, FlowNotAllowedException, ElementCannotBePersistedException {
		Form form = getNewForm();
		Block block = getNewBlock();
		BlockReference blockReference = new BlockReference(block);
		formDao.makePersistent(form);
		blockDao.makePersistent(block);
		CompleteFormView completeFormView = new CompleteFormView(form);
		form.addChild(blockReference);
		formDao.makePersistent(form);
		
		int indexOfBlock = form.getIndex(blockReference);
		form.moveChildUp(blockReference);
		Assert.assertEquals(indexOfBlock - 1, (int) form.getIndex(blockReference));

		Assert.assertEquals(BlockReference.class, form.getChildren().get(indexOfBlock - 1).getClass());
		Assert.assertEquals(Category.class, completeFormView.getChildren().get(indexOfBlock - 1).getClass());
		Assert.assertEquals(FormUtils.CATEGORY_IN_BLOCK_1, completeFormView.getChildren().get(indexOfBlock - 1)
				.getLabel());
	}

	@Test(dependsOnMethods = { "moveUpLinkedBlockInForm" })
	@Rollback(value=false)
	@Transactional
	public void moveDownLinkedBlockInForm() throws ElementIsReadOnly, FieldTooLongException, NotValidChildException, CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException, BadFlowContentException, FlowWithoutSourceException, FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException, NotValidTokenType, UnexpectedDatabaseException, FlowNotAllowedException, ElementCannotBePersistedException {
		Form form = getNewForm();
		Block block = getNewBlock();
		BlockReference blockReference = new BlockReference(block);
		formDao.makePersistent(form);
		blockDao.makePersistent(block);
		CompleteFormView completeFormView = new CompleteFormView(form);
		form.addChild(blockReference);
		formDao.makePersistent(form);
		form.moveChildUp(blockReference);
		
		int indexOfBlock = form.getIndex(blockReference);
		form.moveChildDown(blockReference);
		Assert.assertEquals(indexOfBlock + 1, (int) form.getIndex(blockReference));

		Assert.assertEquals(BlockReference.class, form.getChildren().get(indexOfBlock + 1).getClass());
		Assert.assertEquals(Category.class, completeFormView.getChildren().get(indexOfBlock + 1).getClass());
		Assert.assertEquals(FormUtils.CATEGORY_IN_BLOCK_1, completeFormView.getChildren().get(indexOfBlock + 1)
				.getLabel());
	}

	@Test(dependsOnMethods = { "addLinkedBlockToForm" }, expectedExceptions = { ElementIsReadOnly.class })
	@Rollback(value=false)
	@Transactional
	public void moveElementBlockInCompleteFormViewNotAllowed() throws ChildrenNotFoundException, ElementIsReadOnly, FieldTooLongException, NotValidChildException, CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException, BadFlowContentException, FlowWithoutSourceException, FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException, NotValidTokenType, UnexpectedDatabaseException, FlowNotAllowedException, ElementCannotBePersistedException {
		Form form = getNewForm();
		Block block = getNewBlock();
		BlockReference blockReference = new BlockReference(block);
		formDao.makePersistent(form);
		blockDao.makePersistent(block);
		CompleteFormView completeFormView = new CompleteFormView(form);
		form.addChild(blockReference);
		formDao.makePersistent(form);
		
		int indexOfBlock = form.getIndex(blockReference);
		// Get category in position of the block and move first child down.
		completeFormView.getChild(indexOfBlock).moveChildDown(
				completeFormView.getChild(indexOfBlock).getChildren().get(0));
	}

	@Test(dependsOnMethods = { "addLinkedBlockToForm" }, expectedExceptions = { ElementIsReadOnly.class })
	@Rollback(value=false)
	@Transactional
	public void removeElementInLinkedBlockNotAllowed() throws DependencyExistException, ChildrenNotFoundException,
			ElementIsReadOnly, FieldTooLongException, NotValidChildException, CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException, BadFlowContentException, FlowWithoutSourceException, FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException, NotValidTokenType, UnexpectedDatabaseException, FlowNotAllowedException, ElementCannotBePersistedException {
		Form form = getNewForm();
		Block block = getNewBlock();
		BlockReference blockReference = new BlockReference(block);
		formDao.makePersistent(form);
		blockDao.makePersistent(block);
		CompleteFormView completeFormView = new CompleteFormView(form);
		form.addChild(blockReference);
		formDao.makePersistent(form);
		
		int indexOfBlock = form.getIndex(blockReference);
		completeFormView.getChild(indexOfBlock).getChildren().get(0).remove();
	}

	@Test(dependsOnMethods = { "addLinkedBlockToForm" })
	@Rollback(value=false)
	@Transactional
	public void getViewOfFormsThatUseaBlock() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException, BadFlowContentException, FlowWithoutSourceException, FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException, NotValidTokenType, UnexpectedDatabaseException, ElementIsReadOnly, FlowNotAllowedException, ElementCannotBePersistedException {
		Form form = getNewForm();
		Block block = getNewBlock();
		BlockReference blockReference = new BlockReference(block);
		formDao.makePersistent(form);
		blockDao.makePersistent(block);
		form.addChild(blockReference);
		formDao.makePersistent(form);
		
		List<SimpleFormView> forms = simpleFormViewDao.getFormsThatUse(blockReference.getReference());
		Assert.assertTrue(!forms.isEmpty());
	}

	@Test(dependsOnMethods = { "addLinkedBlockToForm" })
	public void blockElementCannotBeRemoveDueToUsedInFormFlow() throws BadFlowContentException,
			FlowWithoutSourceException, FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException,
			FlowWithoutDestinyException, FlowNotAllowedException, UnexpectedDatabaseException,
			ElementCannotBePersistedException, DependencyExistException, ElementIsReadOnly, FieldTooLongException, NotValidChildException, CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException, NotValidTokenType {
		Form form = getNewForm();
		Block block = getNewBlock();
		BlockReference blockReference = new BlockReference(block);
		formDao.makePersistent(form);
		blockDao.makePersistent(block);
		CompleteFormView completeFormView = new CompleteFormView(form);
		form.addChild(blockReference);
		formDao.makePersistent(form);
		
		// Add a flow that uses one element of the block.
		Flow ruleToBlock = FormUtils.createFlow(
				(BaseQuestion) completeFormView.getChildren().get(0).getChildren().get(2).getChildren().get(0),
				(BaseQuestion) completeFormView.getChildren().get(2).getChildren().get(3), false,
				new ArrayList<Token>());
		form.addFlow(ruleToBlock);
		// Save the form.
		formDao.makePersistent(form);
		
		Assert.assertFalse(blockDao.checkIfBlockElementCanBeRemoved(block.getChildren().get(0).getChildren().get(3)));
	}
	
	@Test
	@Rollback(value=false)
	@Transactional
	public void removeFormDoesNotRemoveLinkedBlock() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			ElementIsReadOnly, BadFlowContentException, FlowWithoutSourceException, FlowSameOriginAndDestinyException,
			FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException, NotValidTokenType,
			UnexpectedDatabaseException, FlowNotAllowedException, ElementCannotBeRemovedException,
			ElementCannotBePersistedException {
		
		Block block = FormUtils.createBlock();
		block.setLabel("LinkedBlock4");
		blockDao.makePersistent(block);

		int blockNumber = blockDao.getRowCount();
		int elementsInBlock = block.getAllInnerStorableObjects().size();

		BlockReference blockReference = new BlockReference(block);
		Form form = FormUtils.createCompleteForm(null, "form4");
		form.addChild(blockReference);

		formDao.makePersistent(form);
		formDao.makeTransient(form);
		
		Assert.assertEquals(blockNumber, (int) blockDao.getRowCount());
		Assert.assertEquals(elementsInBlock, blockDao.get(block.getId()).getAllInnerStorableObjects().size());
	}

	@Test(expectedExceptions = FlowNotAllowedException.class)
	@Rollback(value=false)
	@Transactional
	public void flowInLinkedBlockIsNotAllowed() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			ElementIsReadOnly, UnexpectedDatabaseException, BadFlowContentException, FlowWithoutSourceException,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException,
			NotValidTokenType, FlowNotAllowedException, ElementCannotBeRemovedException,
			ElementCannotBePersistedException {
		Block block1 = FormUtils.createBlock();
		block1.setLabel("LinkedBlock41");
		blockDao.makePersistent(block1);

		Block block2 = FormUtils.createBlock();
		block2.setLabel("LinkedBlock42");
		blockDao.makePersistent(block2);

		BlockReference blockReference1 = new BlockReference(block1);
		BlockReference blockReference2 = new BlockReference(block2);

		Form form = FormUtils.createCompleteForm(null, "form6");
		form.addChild(blockReference1);
		form.addChild(blockReference2);
		formDao.makePersistent(form);

		CompleteFormView completeFormView = new CompleteFormView(form);

		// CategoryLinked1->Group1->Question1 to CategoryLinked1->Group1->Question2
		Flow rule1 = FormUtils.createFlow((Question) block1.getChildren().get(0).getChildren().get(2).getChildren()
				.get(0), (Question) block1.getChildren().get(0).getChildren().get(2).getChildren().get(1), false,
				new ArrayList<Token>());
		try {
			completeFormView.addFlow(rule1);
		} finally {
			formDao.makeTransient(form);
			blockDao.makeTransient(block1);
			blockDao.makeTransient(block2);
		}
	}

	@Test
	@Rollback(value=false)
	@Transactional
	public void flowFromElementToLinkedBlockIsAllowed() throws BadFlowContentException, FlowWithoutSourceException,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException,
			UnexpectedDatabaseException, NotValidChildException, ElementIsReadOnly, FieldTooLongException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			NotValidTokenType, FlowNotAllowedException, ElementCannotBeRemovedException,
			ElementCannotBePersistedException {
		Block block1 = FormUtils.createBlock();
		block1.setLabel("LinkedBlock51");
		blockDao.makePersistent(block1);

		Block block2 = FormUtils.createBlock();
		block2.setLabel("LinkedBlock51");
		blockDao.makePersistent(block2);

		BlockReference blockReference1 = new BlockReference(block1);
		BlockReference blockReference2 = new BlockReference(block2);

		Form form = FormUtils.createCompleteForm(null, "form5");
		form.addChild(blockReference1);
		form.addChild(blockReference2);
		formDao.makePersistent(form);

		CompleteFormView completeFormView = new CompleteFormView(form);

		// Category2->Group1->Question1 to CategoryLinked1->Group1->Question2
		Flow rule1 = FormUtils.createFlow((Question) completeFormView.getChildren().get(1).getChildren().get(2)
				.getChildren().get(1), (Question) completeFormView.getChildren().get(2).getChildren().get(2)
				.getChildren().get(1), false, new ArrayList<Token>());
		completeFormView.addFlow(rule1);

		formDao.makeTransient(form);
		blockDao.makeTransient(block1);
		blockDao.makeTransient(block2);
	}

	@Test
	@Rollback(value=false)
	@Transactional
	public void flowBetweenLinkedBlocksIsAllowed() throws BadFlowContentException, FlowWithoutSourceException,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException,
			UnexpectedDatabaseException, NotValidChildException, ElementIsReadOnly, FieldTooLongException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			NotValidTokenType, FlowNotAllowedException, ElementCannotBeRemovedException,
			ElementCannotBePersistedException {
		Block block1 = FormUtils.createBlock();
		block1.setLabel("LinkedBlock61");
		blockDao.makePersistent(block1);

		Block block2 = FormUtils.createBlock();
		block2.setLabel("LinkedBlock62");
		blockDao.makePersistent(block2);

		BlockReference blockReference1 = new BlockReference(block1);
		BlockReference blockReference2 = new BlockReference(block2);

		Form form = FormUtils.createCompleteForm(null, "form7");
		form.addChild(blockReference1);
		form.addChild(blockReference2);
		formDao.makePersistent(form);

		CompleteFormView completeFormView = new CompleteFormView(form);

		// CategoryLinked1->Group1->Question1 to CategoryLinked2->Group1->Question2
		Flow rule1 = FormUtils.createFlow((Question) completeFormView.getChildren().get(2).getChildren().get(2)
				.getChildren().get(1), (Question) completeFormView.getChildren().get(3).getChildren().get(2)
				.getChildren().get(1), false, new ArrayList<Token>());
		completeFormView.addFlow(rule1);

		formDao.makeTransient(form);
		blockDao.makeTransient(block1);
		blockDao.makeTransient(block2);
	}

	@Test(expectedExceptions = ElementCannotBeRemovedException.class)
	@Rollback(value=false)
	@Transactional()
	public void blockCannotBeRemovedIfFormIsLinkingIt() throws BadFlowContentException, FlowWithoutSourceException,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException,
			UnexpectedDatabaseException, NotValidChildException, ElementIsReadOnly, FieldTooLongException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			NotValidTokenType, FlowNotAllowedException, ElementCannotBeRemovedException,
			ElementCannotBePersistedException {
		Block block1 = FormUtils.createBlock();
		block1.setLabel("LinkedBlock61");
		blockDao.makePersistent(block1);
		blockDao.getEntityManager().flush();

		BlockReference blockReference1 = new BlockReference(block1);

		Form form = FormUtils.createCompleteForm(null, "form8");
		form.addChild(blockReference1);
		formDao.makePersistent(form);
		formDao.getEntityManager().flush();

		blockDao.makeTransient(block1);
	}

}
