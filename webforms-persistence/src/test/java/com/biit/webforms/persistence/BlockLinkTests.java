package com.biit.webforms.persistence;

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
import com.biit.webforms.persistence.dao.IBlockDao;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.BlockReference;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.CompleteFormView;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOrigin;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestiny;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSource;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "blockTest" })
public class BlockLinkTests extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private IFormDao formDao;

	@Autowired
	private IBlockDao blockDao;

	private BlockReference blockReference;
	private Form form;
	private CompleteFormView completeFormView;

	@Test
	public void createLinkedBlock() throws NotValidChildException, FieldTooLongException, CharacterNotAllowedException,
			InvalidAnswerFormatException, InvalidAnswerSubformatException, UnexpectedDatabaseException {
		int previousBlocks = blockDao.getRowCount();

		Block block = FormUtils.createBlock();
		block.setLabel("LinkedBlock");
		Assert.assertNotNull(block);
		blockDao.makePersistent(block);

		Assert.assertEquals(previousBlocks + 1, blockDao.getRowCount());

		blockReference = new BlockReference(block);
	}

	@Test(dependsOnMethods = { "createLinkedBlock" })
	public void createForm() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
			InvalidAnswerFormatException, InvalidAnswerSubformatException, BadFlowContentException, FlowWithoutSource,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin, FlowWithoutDestiny, NotValidTokenType,
			UnexpectedDatabaseException {
		int previousForms = formDao.getRowCount();
		form = FormUtils.createCompleteForm();
		formDao.makePersistent(form);
		Assert.assertEquals(previousForms + 1, formDao.getRowCount());
		completeFormView = new CompleteFormView(form);
	}

	@Test(dependsOnMethods = { "createLinkedBlock", "createForm" })
	public void addLinkedBlockToForm() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			BadFlowContentException, FlowWithoutSource, FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin,
			FlowWithoutDestiny, NotValidTokenType, UnexpectedDatabaseException {
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
	public void moveUpLinkedBlockInForm() {
		int indexOfBlock = form.getIndex(blockReference);
		form.moveChildUp(blockReference);
		Assert.assertEquals(indexOfBlock - 1, (int) form.getIndex(blockReference));

		Assert.assertEquals(BlockReference.class, form.getChildren().get(indexOfBlock - 1).getClass());
		Assert.assertEquals(Category.class, completeFormView.getChildren().get(indexOfBlock - 1).getClass());
		Assert.assertEquals(FormUtils.CATEGORY_IN_BLOCK_1, completeFormView.getChildren().get(indexOfBlock - 1)
				.getLabel());
	}

	@Test(dependsOnMethods = { "moveUpLinkedBlockInForm" })
	public void moveDownLinkedBlockInForm() {
		int indexOfBlock = form.getIndex(blockReference);
		form.moveChildDown(blockReference);
		Assert.assertEquals(indexOfBlock + 1, (int) form.getIndex(blockReference));

		Assert.assertEquals(BlockReference.class, form.getChildren().get(indexOfBlock + 1).getClass());
		Assert.assertEquals(Category.class, completeFormView.getChildren().get(indexOfBlock + 1).getClass());
		Assert.assertEquals(FormUtils.CATEGORY_IN_BLOCK_1, completeFormView.getChildren().get(indexOfBlock + 1)
				.getLabel());
	}

	@Test
	public void moveElementBlockInFormNotAllowed() {

	}

	@Test
	public void removeElementInLinkedBlockNotAllowed() {

	}

	@Test
	public void removeLinkedBlockFromForm() {

	}

	@Test
	public void removeFormDoesNotRemoveLinkedBlock() {

	}

	@Test
	public void flowInLinkedBlockIsNotAllowed() {

	}

	@Test
	public void flowFromLinkedBlockToElementIsAllowed() {

	}

	@Test
	public void flowbetweenLinkedBlocksIsNotAllowed() {

	}

}
