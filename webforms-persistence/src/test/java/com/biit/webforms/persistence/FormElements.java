package com.biit.webforms.persistence;

import javax.transaction.Transactional;


import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.persistence.dao.IBlockDao;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOriginException;
import com.biit.webforms.persistence.entity.exceptions.FlowNotAllowedException;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSourceException;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContextTest.xml"})
@Test(groups = {"formElements"})
public class FormElements extends AbstractTransactionalTestNGSpringContextTests {

    private static final Long USER_ID = 0L;
    @Autowired
    private IFormDao formDao;

    @Autowired
    private IBlockDao blockDao;

    private Block block;

    @Test
    @Rollback(value = false)
    @Transactional
    public void createBuildingBlock() throws NotValidChildException, FieldTooLongException,
            CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
            UnexpectedDatabaseException, ElementIsReadOnly, ElementCannotBePersistedException, ChildrenNotFoundException {
        block = FormUtils.createBlock();
        Assert.assertNotNull(block);
        blockDao.makePersistent(block);
        Assert.assertEquals(1, blockDao.getRowCount());
    }

    @Test(dependsOnMethods = {"createBuildingBlock"})
    @Rollback(value = false)
    @Transactional
    public void testPersistCompleteForm() throws FieldTooLongException, NotValidChildException,
            CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
            UnexpectedDatabaseException, ChildrenNotFoundException, BadFlowContentException,
            FlowWithoutSourceException, FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException,
            FlowWithoutDestinyException, NotValidTokenType, ElementIsReadOnly, FlowNotAllowedException,
            ElementCannotBeRemovedException, ElementCannotBePersistedException {
        int prevForms = formDao.getRowCount();
        Form form = FormUtils.createCompleteForm(block);
        formDao.makePersistent(form);

        Assert.assertEquals(prevForms + 1, formDao.getRowCount());
        Assert.assertNotNull(form.getId());

        Form dbForm = formDao.get(form.getId());

        checkFormStructure(dbForm);
        checkEquals(form, dbForm);
        Assert.assertTrue(form.isContentEqual(dbForm));

        formDao.makeTransient(dbForm);
        Assert.assertEquals(prevForms, formDao.getRowCount());
    }

    @Test(dependsOnMethods = {"createBuildingBlock"})
    @Rollback(value = true)
    @Transactional
    public void testNewVersion() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
            InvalidAnswerFormatException, InvalidAnswerSubformatException, UnexpectedDatabaseException,
            ChildrenNotFoundException, BadFlowContentException, FlowWithoutSourceException,
            FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException,
            NotValidStorableObjectException, NotValidTokenType, ElementIsReadOnly, FlowNotAllowedException,
            ElementCannotBePersistedException, ElementCannotBeRemovedException {
        Form form = FormUtils.createCompleteForm(block);
        Form formV2 = form.createNewVersion(USER_ID);

        checkFormStructure(formV2);
        checkEquals(form, formV2);
        // Different version content is not equal
        Assert.assertFalse(form.isContentEqual(formV2));
        //Put the same version number and must be the same!
        formV2.setVersion(form.getVersion());
        Assert.assertTrue(form.isContentEqual(formV2));
    }

    private void checkEquals(TreeObject obj1, TreeObject obj2) throws ChildrenNotFoundException {
        Assert.assertEquals(obj1.getChildren().size(), obj2.getChildren().size());

        for (int i = 0; i < obj1.getChildren().size(); i++) {
            Assert.assertTrue(obj1.getChild(i).isContentEqual(obj2.getChild(i)));
            checkEquals(obj1.getChild(i), obj2.getChild(i));
        }
    }

    private void checkFormStructure(Form form) {
        // Check the structure is present
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_1));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_1, FormUtils.SYSTEM_FIELD_1));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_1, FormUtils.INFO_TEXT_1));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_1, FormUtils.GROUP_1));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_1, FormUtils.GROUP_1, FormUtils.INPUT_TEXT_TEXT));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_1, FormUtils.GROUP_1, FormUtils.INPUT_TEXT_NUMBER_FLOAT));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_1, FormUtils.GROUP_1, FormUtils.INPUT_TEXT_POSTAL_CODE));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_1, FormUtils.GROUP_1, FormUtils.INPUT_TEXT_TEXT_BSN));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_1, FormUtils.GROUP_1, FormUtils.INPUT_TEXT_DATE_BIRTHDAY));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_1, FormUtils.GROUP_1, FormUtils.RADIO_BUTTON));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_1, FormUtils.GROUP_1, FormUtils.MULTI_CHECKBOX));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_1, FormUtils.GROUP_1, FormUtils.LIST));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_2));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_2, FormUtils.SYSTEM_FIELD_1));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_2, FormUtils.INFO_TEXT_1));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_2, FormUtils.GROUP_1));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_2, FormUtils.GROUP_1, FormUtils.INPUT_TEXT_TEXT));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_2, FormUtils.GROUP_1, FormUtils.INPUT_TEXT_NUMBER_FLOAT));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_2, FormUtils.GROUP_1, FormUtils.INPUT_TEXT_POSTAL_CODE));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_2, FormUtils.GROUP_1, FormUtils.INPUT_TEXT_TEXT_BSN));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_2, FormUtils.GROUP_1, FormUtils.INPUT_TEXT_DATE_BIRTHDAY));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_2, FormUtils.GROUP_1, FormUtils.RADIO_BUTTON));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_2, FormUtils.GROUP_1, FormUtils.MULTI_CHECKBOX));
        Assert.assertNotNull(form.getChild(FormUtils.CATEGORY_2, FormUtils.GROUP_1, FormUtils.LIST));
    }
}
