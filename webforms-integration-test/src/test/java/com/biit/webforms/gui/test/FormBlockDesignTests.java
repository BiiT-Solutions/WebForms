package com.biit.webforms.gui.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.webforms.gui.test.exceptions.FieldNotEditableException;

public class FormBlockDesignTests extends WebFormsTester {

	private static final String NEW_FORM_NAME = "new_form_1";
	private static final String NEW_BLOCK_NAME = "new_block_1";
	private static final String MODIFIED_NAME = "ModifiedName";
	private static final String MODIFIED_LABEL = "ModifiedLabel";
	private static final String MODIFIED_DESCRIPTION = "ModifiedDescription";

	private static final Integer TREE_TABLE_CATEGORY_ROW_INDEX = 1;
	private static final Integer TREE_TABLE_CATEGORY_GROUP_ROW_INDEX = 2;
	private static final Integer TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX = 2;
	private static final Integer TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX = 3;
	private static final Integer TREE_TABLE_CATEGORY_QUESTION_ANSWER_ROW_INDEX = 3;
	private static final Integer TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_ROW_INDEX = 4;
	private static final Integer TREE_TABLE_CATEGORY_QUESTION_ANSWER_SUBANSWER_ROW_INDEX = 4;
	private static final Integer TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX = 5;

	/**
	 * Method to avoid repeating all the code that creates a form and a
	 * category.<br>
	 */
	private void createNewFormAndCategory() {
		createNewEmptyForm(NEW_FORM_NAME);
		goToDesignerPage();
		getDesignerPage().addNewCategory();
	}

	private void createNewFormAndCategoryWithAdminRights() {
		createNewFormWithAdminRights(NEW_FORM_NAME);
		goToDesignerPage();
		getDesignerPage().addNewCategory();
	}

	/**
	 * Method to avoid repeating all the code that creates a block and a
	 * category.<br>
	 */
	private void createNewBlockAndCategory() {
		createNewBlock(NEW_BLOCK_NAME);
		goToDesignerPage();
		getDesignerPage().addNewCategory();
	}

	/**
	 * Method to avoid repeating all the code that block a form design and
	 * removes it afterwards.<br>
	 */
	private void saveDesignAndRemoveForm() {
		getDesignerPage().saveDesign();
		logOut();
		deleteForm();
	}

	/**
	 * Method to avoid repeating all the code that saves a block design and
	 * removes it afterwards.<br>
	 */
	private void saveDesignAndRemoveBlock() {
		getDesignerPage().saveDesign();
		logOut();
		deleteBlock();
	}

	/**
	 * Used to check that the tree table element modifications are stored
	 * correctly
	 * 
	 * @param tableRow
	 */
	private void deselectSelecTableRow(int tableRow) {
		getDesignerPage().goToBeginningOfTreeTable();
		getDesignerPage().clickInTreeTableRow(tableRow);
	}

	@Test(groups = { "formBlockDesign" })
	public void createEmptyForm() {
		createNewEmptyForm(NEW_FORM_NAME);
		logOut();
		deleteForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void createEmptyBlock() {
		createNewBlock(NEW_BLOCK_NAME);
		logOut();
		deleteBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkGroupQuestionAnswerSubanswerAtFormLevel() {
		createNewEmptyForm(NEW_FORM_NAME);
		goToDesignerPage();
		Assert.assertFalse(getDesignerPage().getGroupButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getQuestionButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getTextButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getFieldButton().isEnabled());
		logOut();
		deleteForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkGroupQuestionAnswerSubanswerAtBlockLevel() {
		createNewBlock(NEW_BLOCK_NAME);
		goToDesignerPage();
		Assert.assertFalse(getDesignerPage().getGroupButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getQuestionButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getTextButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getFieldButton().isEnabled());
		logOut();
		deleteBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createFormCategory() {
		createNewFormAndCategory();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void createBlockCategory() {
		createNewBlockAndCategory();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtFormCategoryLevel() {
		createNewFormAndCategory();
		Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtBlockCategoryLevel() {
		createNewBlockAndCategory();
		Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createTextAtFormCategoryLevel() {
		createNewFormAndCategory();
		getDesignerPage().addNewText();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void createTextAtBlockCategoryLevel() {
		createNewBlockAndCategory();
		getDesignerPage().addNewText();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createFieldAtFormCategoryLevel() {
		createNewFormAndCategory();
		getDesignerPage().addNewField();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void createFieldAtBlockCategoryLevel() {
		createNewBlockAndCategory();
		getDesignerPage().addNewField();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionAtFormCategoryLevel() {
		createNewFormAndCategory();
		getDesignerPage().addNewQuestion();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionAtBlockCategoryLevel() {
		createNewBlockAndCategory();
		getDesignerPage().addNewQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createGroupAtFormCategoryLevel() {
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void createGroupAtBlockCategoryLevel() {
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtFormGroupLevel() {
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtBlockGroupLevel() {
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextTextAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewInputTextSubformatTextQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextTextAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputTextSubformatTextQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextEmailAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewInputTextSubformatEmailQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextEmailAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputTextSubformatEmailQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextPhoneAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewInputTextSubformatPhoneQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextPhoneAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputTextSubformatPhoneQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextIbanAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewInputTextSubformatIbanQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextIbanAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputTextSubformatIbanQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextBsnAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewInputTextSubformatBsnQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextBsnAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputTextSubformatBsnQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberFloatAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewInputNumberSubformatFloatQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberFloatAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputNumberSubformatFloatQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberNumberAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewInputNumberSubformatNumberQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberNumberAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputNumberSubformatNumberQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateDateAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewInputDateSubformatDateQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateDaterAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputDateSubformatDateQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDatePastAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewInputDateSubformatPastQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDatePastAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputDateSubformatPastQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateFutureAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewInputDateSubformatFutureQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateFutureAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputDateSubformatFutureQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputPostalCodePostalCodeAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewInputPostalcodeSubformatPostalcodeQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputPostalCodePostalCodeAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputPostalcodeSubformatPostalcodeQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionTextAreaAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewTextAreaQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionTextAreaAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewTextAreaQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionRadioButtonAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewRadioButtonQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionRadioButtonAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewRadioButtonQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionMultiCheckBoxAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewMultiCheckboxQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionMultiCheckBoxAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewMultiCheckboxQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionListAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewListQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionListAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewListQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextTextAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputTextSubformatTextQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextTextAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputTextSubformatTextQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextEmailAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputTextSubformatEmailQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextEmailAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputTextSubformatEmailQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextPhoneAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputTextSubformatPhoneQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextPhoneAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputTextSubformatPhoneQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextIbanAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputTextSubformatIbanQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextIbanAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputTextSubformatIbanQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextBsnAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputTextSubformatBsnQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextBsnAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputTextSubformatBsnQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberFloatAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputNumberSubformatFloatQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberFloatAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputNumberSubformatFloatQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberNumberAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputNumberSubformatNumberQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberNumberAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputNumberSubformatNumberQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateDateAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputDateSubformatDateQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateDaterAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputDateSubformatDateQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDatePastAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputDateSubformatPastQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDatePastAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputDateSubformatPastQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateFutureAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputDateSubformatFutureQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateFutureAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputDateSubformatFutureQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputPostalCodePostalCodeAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputPostalcodeSubformatPostalcodeQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputPostalCodePostalCodeAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputPostalcodeSubformatPostalcodeQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionTextAreaAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewTextAreaQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionTextAreaAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewTextAreaQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionRadioButtonAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewRadioButtonQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionRadioButtonAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewRadioButtonQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionMultiCheckBoxAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewMultiCheckboxQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionMultiCheckBoxAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewMultiCheckboxQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionListAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewListQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionListAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewListQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtFormCategoryQuestionInput() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewInputQuestion();
			Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
			Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtBlockCategoryQuestionInput() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputQuestion();
			Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
			Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtFormCategoryGroupQuestionInput() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputQuestion();
			Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
			Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtBlockCategoryGroupQuestionInput() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewInputQuestion();
			Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
			Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createAnswerSubanswerAtFormCategoryQuestionRadioButton() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewRadioButtonQuestion();
			getDesignerPage().addNewAnswer();
			getDesignerPage().addNewSubanswer();
			getDesignerPage().addNewSubanswer();
			getDesignerPage().addNewAnswer();
			getDesignerPage().addNewSubanswer();
			getDesignerPage().addNewSubanswer();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createAnswerSubanswerAtBlockCategoryQuestionRadioButton() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewRadioButtonQuestion();
			getDesignerPage().addNewAnswer();
			getDesignerPage().addNewSubanswer();
			getDesignerPage().addNewSubanswer();
			getDesignerPage().addNewAnswer();
			getDesignerPage().addNewSubanswer();
			getDesignerPage().addNewSubanswer();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createAnswerSubanswerAtFormCategoryGroupQuestionRadioButton() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewRadioButtonQuestion();
			getDesignerPage().addNewAnswer();
			getDesignerPage().addNewSubanswer();
			getDesignerPage().addNewSubanswer();
			getDesignerPage().addNewAnswer();
			getDesignerPage().addNewSubanswer();
			getDesignerPage().addNewSubanswer();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createAnswerSubanswerAtBlockCategoryGroupQuestionRadioButton() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewRadioButtonQuestion();
			getDesignerPage().addNewAnswer();
			getDesignerPage().addNewSubanswer();
			getDesignerPage().addNewSubanswer();
			getDesignerPage().addNewAnswer();
			getDesignerPage().addNewSubanswer();
			getDesignerPage().addNewSubanswer();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteSubanswerAtFormCategoryQuestionRadioButton() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewRadioButtonQuestion();
			getDesignerPage().addNewAnswer();
			getDesignerPage().addNewSubanswer();
			getDesignerPage().clickDeleteButton();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteSubanswerAtBlockCategoryQuestionRadioButton() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewRadioButtonQuestion();
			getDesignerPage().addNewAnswer();
			getDesignerPage().addNewSubanswer();
			getDesignerPage().clickDeleteButton();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteSubanswerAtFormCategoryGroupQuestionRadioButton() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewRadioButtonQuestion();
			getDesignerPage().addNewAnswer();
			getDesignerPage().addNewSubanswer();
			getDesignerPage().clickDeleteButton();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteSubanswerAtBlockCategoryGroupQuestionRadioButton() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewRadioButtonQuestion();
			getDesignerPage().addNewAnswer();
			getDesignerPage().addNewSubanswer();
			getDesignerPage().clickDeleteButton();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteAnswerAtFormCategoryQuestionRadioButton() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewRadioButtonQuestion();
			getDesignerPage().addNewAnswer();
			getDesignerPage().clickDeleteButton();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteAnswerAtBlockCategoryQuestionRadioButton() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewRadioButtonQuestion();
			getDesignerPage().addNewAnswer();
			getDesignerPage().clickDeleteButton();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteAnswerAtFormCategoryGroupQuestionRadioButton() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewRadioButtonQuestion();
			getDesignerPage().addNewAnswer();
			getDesignerPage().clickDeleteButton();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteAnswerAtBlockCategoryGroupQuestionRadioButton() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().addNewRadioButtonQuestion();
			getDesignerPage().addNewAnswer();
			getDesignerPage().clickDeleteButton();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteQuestionAtFormCategory() {
		createNewFormAndCategory();
		getDesignerPage().addNewQuestion();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteQuestionAtBlockCategory() {
		createNewBlockAndCategory();
		getDesignerPage().addNewQuestion();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteQuestionAtFormCategoryGroup() {
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewQuestion();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteQuestionAtBlockCategoryGroup() {
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewQuestion();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteGroupAtFormCategory() {
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteGroupAtBlockCategory() {
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteFormCategory() {
		createNewFormAndCategory();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteBlockCategory() {
		createNewBlockAndCategory();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFormCategoryTechnicalName() {
		try {
			createNewFormAndCategory();
			getDesignerPage().getCategoryPropertiesView().setTechnicalName(MODIFIED_NAME);
			getDesignerPage().saveDesign();
			deselectSelecTableRow(TREE_TABLE_CATEGORY_ROW_INDEX);
			Assert.assertEquals(getDesignerPage().getCategoryPropertiesView().getTechnicalName(), MODIFIED_NAME);
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyBlockCategoryTechnicalName() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().getCategoryPropertiesView().setTechnicalName(MODIFIED_NAME);
			getDesignerPage().saveDesign();
			deselectSelecTableRow(TREE_TABLE_CATEGORY_ROW_INDEX);
			Assert.assertEquals(getDesignerPage().getCategoryPropertiesView().getTechnicalName(), MODIFIED_NAME);
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFormGroupTechnicalName() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().getGroupPropertiesView().setTechnicalName(MODIFIED_NAME);
			getDesignerPage().saveDesign();
			deselectSelecTableRow(TREE_TABLE_CATEGORY_GROUP_ROW_INDEX);
			Assert.assertEquals(getDesignerPage().getGroupPropertiesView().getTechnicalName(), MODIFIED_NAME);
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyBlockGroupTechnicalName() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().getGroupPropertiesView().setTechnicalName(MODIFIED_NAME);
			getDesignerPage().saveDesign();
			deselectSelecTableRow(TREE_TABLE_CATEGORY_GROUP_ROW_INDEX);
			Assert.assertEquals(getDesignerPage().getGroupPropertiesView().getTechnicalName(), MODIFIED_NAME);
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	private void addQuestionModifyAndCheckTechnicalName(String modifiedName, Integer rowToCheck) {
		try {
			getDesignerPage().addNewQuestion();
			getDesignerPage().getQuestionPropertiesView().setTechnicalName(modifiedName);
			getDesignerPage().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertEquals(getDesignerPage().getQuestionPropertiesView().getTechnicalName(), modifiedName);
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionTechnicalNameAtFormCategoryLevel() {
		createNewFormAndCategory();
		addQuestionModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionTechnicalNameAtBlockCategoryLevel() {
		createNewBlockAndCategory();
		addQuestionModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionTechnicalNameAtFormGroupLevel() {
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionTechnicalNameAtBlockGroupLevel() {
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	private void addTextModifyAndCheckTechnicalName(String modifiedName, Integer rowToCheck) {
		try {
			getDesignerPage().addNewText();
			getDesignerPage().getTextPropertiesView().setTechnicalName(modifiedName);
			getDesignerPage().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertEquals(getDesignerPage().getTextPropertiesView().getTechnicalName(), modifiedName);
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyTextTechnicalNameAtFormCategoryLevel() {
		createNewFormAndCategory();
		addTextModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyTextTechnicalNameAtBlockCategoryLevel() {
		createNewBlockAndCategory();
		addTextModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyTextTechnicalNameAtFormGroupLevel() {
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addTextModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyTextTechnicalNameAtBlockGroupLevel() {
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addTextModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	private void addFieldModifyAndCheckTechnicalName(String modifiedName, Integer rowToCheck) {
		try {
			getDesignerPage().addNewField();
			getDesignerPage().getFieldPropertiesView().setTechnicalName(modifiedName);
			getDesignerPage().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertEquals(getDesignerPage().getFieldPropertiesView().getTechnicalName(), modifiedName);
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFieldTechnicalNameAtFormCategoryLevel() {
		createNewFormAndCategory();
		addFieldModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFieldTechnicalNameAtBlockCategoryLevel() {
		createNewBlockAndCategory();
		addFieldModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFieldTechnicalNameAtFormGroupLevel() {
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addFieldModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFieldTechnicalNameAtBlockGroupLevel() {
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addFieldModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFormCategoryLabel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().getCategoryPropertiesView().setLabel(MODIFIED_LABEL);
			getDesignerPage().saveDesign();
			deselectSelecTableRow(TREE_TABLE_CATEGORY_ROW_INDEX);
			Assert.assertEquals(getDesignerPage().getCategoryPropertiesView().getLabel(), MODIFIED_LABEL);
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyBlockCategoryLabel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().getCategoryPropertiesView().setLabel(MODIFIED_LABEL);
			getDesignerPage().saveDesign();
			deselectSelecTableRow(TREE_TABLE_CATEGORY_ROW_INDEX);
			Assert.assertEquals(getDesignerPage().getCategoryPropertiesView().getLabel(), MODIFIED_LABEL);
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFormGroupLabel() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().getGroupPropertiesView().setLabel(MODIFIED_LABEL);
			getDesignerPage().saveDesign();
			deselectSelecTableRow(TREE_TABLE_CATEGORY_GROUP_ROW_INDEX);
			Assert.assertEquals(getDesignerPage().getGroupPropertiesView().getLabel(), MODIFIED_LABEL);
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyBlockGroupLabel() {
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewGroup();
			getDesignerPage().getGroupPropertiesView().setLabel(MODIFIED_LABEL);
			getDesignerPage().saveDesign();
			deselectSelecTableRow(TREE_TABLE_CATEGORY_GROUP_ROW_INDEX);
			Assert.assertEquals(getDesignerPage().getGroupPropertiesView().getLabel(), MODIFIED_LABEL);
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	private void addQuestionModifyAndCheckLabel(String modifiedLabel, Integer rowToCheck) {
		try {
			getDesignerPage().addNewQuestion();
			getDesignerPage().getQuestionPropertiesView().setLabel(modifiedLabel);
			getDesignerPage().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertEquals(getDesignerPage().getQuestionPropertiesView().getLabel(), modifiedLabel);
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionLabelAtFormCategoryLevel() {
		createNewFormAndCategory();
		addQuestionModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionLabelAtBlockCategoryLevel() {
		createNewBlockAndCategory();
		addQuestionModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionLabelAtFormGroupLevel() {
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionLabelAtBlockGroupLevel() {
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	private void addQuestionAnswerModifyAndCheckLabel(String modifiedLabel, Integer rowToCheck) {
		try {
			getDesignerPage().addNewRadioButtonQuestion();
			getDesignerPage().addNewAnswer();
			getDesignerPage().getAnswerPropertiesView().setLabel(modifiedLabel);
			getDesignerPage().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertEquals(getDesignerPage().getAnswerPropertiesView().getLabel(), modifiedLabel);
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerLabelAtFormCategoryQuestionLevel() {
		createNewFormAndCategory();
		addQuestionAnswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerLabelAtBlockCategoryQuestionLevel() {
		createNewBlockAndCategory();
		addQuestionAnswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerLabelAtFormGroupQuestionLevel() {
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionAnswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerLabelAtBlockGroupQuestionLevel() {
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionAnswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	private void addQuestionAnswerSubanswerModifyAndCheckLabel(String modifiedLabel, Integer rowToCheck) {
		try {
			getDesignerPage().addNewRadioButtonQuestion();
			getDesignerPage().addNewAnswer();
			getDesignerPage().addNewSubanswer();
			getDesignerPage().getSubanswerPropertiesView().setLabel(modifiedLabel);
			getDesignerPage().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertEquals(getDesignerPage().getSubanswerPropertiesView().getLabel(), modifiedLabel);
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerLabelAtFormCategoryQuestionAnswerLevel() {
		createNewFormAndCategory();
		addQuestionAnswerSubanswerModifyAndCheckLabel(MODIFIED_LABEL,
				TREE_TABLE_CATEGORY_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerLabelAtBlockCategoryQuestionAnswerLevel() {
		createNewBlockAndCategory();
		addQuestionAnswerSubanswerModifyAndCheckLabel(MODIFIED_LABEL,
				TREE_TABLE_CATEGORY_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerLabelAtFormGroupQuestionAnswerLevel() {
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionAnswerSubanswerModifyAndCheckLabel(MODIFIED_LABEL,
				TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerLabelAtBlockGroupQuestionAnswerLevel() {
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionAnswerSubanswerModifyAndCheckLabel(MODIFIED_LABEL,
				TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFormDescription() {
		try {
			createNewEmptyForm(NEW_FORM_NAME);
			goToDesignerPage();
			getDesignerPage().getFormPropertiesView().setDescription(MODIFIED_DESCRIPTION);
			getDesignerPage().saveDesign();
			goToFormManagerPage();
			goToDesignerPage();
			Assert.assertEquals(getDesignerPage().getFormPropertiesView().getDescription(), MODIFIED_DESCRIPTION);
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	private void addQuestionModifyAndCheckDescription(String modifiedDescription, Integer rowToCheck) {
		try {
			getDesignerPage().addNewQuestion();
			getDesignerPage().getQuestionPropertiesView().setDescription(modifiedDescription);
			getDesignerPage().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertEquals(getDesignerPage().getQuestionPropertiesView().getDescription(), modifiedDescription);
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionDescriptionAtFormCategoryLevel() {
		createNewFormAndCategory();
		addQuestionModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionDescriptionAtBlockCategoryLevel() {
		createNewBlockAndCategory();
		addQuestionModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionDescriptionAtFormGroupLevel() {
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionDescriptionAtBlockGroupLevel() {
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	private void addAnswerModifyAndCheckDescription(String modifiedDescription, Integer rowToCheck) {
		try {
			getDesignerPage().addNewRadioButtonQuestion();
			getDesignerPage().addNewAnswer();
			getDesignerPage().getAnswerPropertiesView().setDescription(modifiedDescription);
			getDesignerPage().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertEquals(getDesignerPage().getAnswerPropertiesView().getDescription(), modifiedDescription);
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerDescriptionAtFormCategoryQuestionLevel() {
		createNewFormAndCategory();
		addAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerDescriptionAtBlockCategoryQuestionLevel() {
		createNewBlockAndCategory();
		addAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerDescriptionAtFormGroupQuestionLevel() {
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();

		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerDescriptionAtBlockGroupQuestionLevel() {
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	private void addSubAnswerModifyAndCheckDescription(String modifiedDescription, Integer rowToCheck) {
		try {
			getDesignerPage().addNewRadioButtonQuestion();
			getDesignerPage().addNewAnswer();
			getDesignerPage().addNewSubanswer();
			getDesignerPage().getSubanswerPropertiesView().setDescription(modifiedDescription);
			getDesignerPage().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertEquals(getDesignerPage().getSubanswerPropertiesView().getDescription(), modifiedDescription);
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerDescriptionAtFormCategoryQuestionAnswerLevel() {
		createNewFormAndCategory();
		addSubAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION,
				TREE_TABLE_CATEGORY_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerDescriptionAtBlockCategoryQuestionAnswerLevel() {
		createNewBlockAndCategory();
		addSubAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION,
				TREE_TABLE_CATEGORY_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerDescriptionAtFormGroupQuestionAnswerLevel() {
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addSubAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION,
				TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerDescriptionAtBlockGroupQuestionAnswerLevel() {
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addSubAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION,
				TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	private void addQuestionModifyAndCheckMandatory(Integer rowToCheck) {
		try {
			getDesignerPage().addNewQuestion();
			String mandatoryValue = getDesignerPage().getQuestionPropertiesView().getMandatoryCheckBoxValue();
			getDesignerPage().getQuestionPropertiesView().clickMandatoryCheckBox();
			getDesignerPage().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertNotEquals(getDesignerPage().getQuestionPropertiesView().getMandatoryCheckBoxValue(),
					mandatoryValue);
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionMandatoryAtFormCategoryLevel() {
		createNewFormAndCategory();
		addQuestionModifyAndCheckMandatory(TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionMandatoryAtBlockCategoryLevel() {
		createNewBlockAndCategory();
		addQuestionModifyAndCheckMandatory(TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionMandatoryAtFormGroupLevel() {
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckMandatory(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionMandatoryAtBlockGroupLevel() {
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckMandatory(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	private void addQuestionModifyAndCheckHorizontal(Integer rowToCheck) {
		try {
			getDesignerPage().addNewRadioButtonQuestion();
			String horizontalValue = getDesignerPage().getQuestionPropertiesView().getHorizontalCheckBoxValue();
			getDesignerPage().getQuestionPropertiesView().clickHorizontalCheckBox();
			getDesignerPage().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertNotEquals(getDesignerPage().getQuestionPropertiesView().getHorizontalCheckBoxValue(),
					horizontalValue);
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionHorizontalAtFormCategoryLevel() {
		createNewFormAndCategory();
		addQuestionModifyAndCheckHorizontal(TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionHorizontalAtBlockCategoryLevel() {
		createNewBlockAndCategory();
		addQuestionModifyAndCheckHorizontal(TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionHorizontalAtFormGroupLevel() {
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckHorizontal(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionHorizontalAtBlockGroupLevel() {
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckHorizontal(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkQuestionHorizontalIsOnlyActivatedForRadioButtonQuestions() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewRadioButtonQuestion();
			Assert.assertEquals(getDesignerPage().getQuestionPropertiesView().isHorizontalEnabled(), true);
			getDesignerPage().addNewListQuestion();
			Assert.assertEquals(getDesignerPage().getQuestionPropertiesView().isHorizontalEnabled(), false);
			getDesignerPage().addNewMultiCheckboxQuestion();
			Assert.assertEquals(getDesignerPage().getQuestionPropertiesView().isHorizontalEnabled(), false);
			getDesignerPage().addNewInputQuestion();
			Assert.assertEquals(getDesignerPage().getQuestionPropertiesView().isHorizontalEnabled(), false);
			getDesignerPage().addNewTextAreaQuestion();
			Assert.assertEquals(getDesignerPage().getQuestionPropertiesView().isHorizontalEnabled(), false);
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void checkQuestionMandatoryIsActivatedForAllQuestions() {
		try {
			createNewFormAndCategory();
			getDesignerPage().addNewRadioButtonQuestion();
			Assert.assertEquals(getDesignerPage().getQuestionPropertiesView().ismandatoryEnabled(), true);
			getDesignerPage().addNewListQuestion();
			Assert.assertEquals(getDesignerPage().getQuestionPropertiesView().ismandatoryEnabled(), true);
			getDesignerPage().addNewMultiCheckboxQuestion();
			Assert.assertEquals(getDesignerPage().getQuestionPropertiesView().ismandatoryEnabled(), true);
			getDesignerPage().addNewInputQuestion();
			Assert.assertEquals(getDesignerPage().getQuestionPropertiesView().ismandatoryEnabled(), true);
			getDesignerPage().addNewTextAreaQuestion();
			Assert.assertEquals(getDesignerPage().getQuestionPropertiesView().ismandatoryEnabled(), true);
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFormName() {
		try {
			createNewEmptyForm(NEW_FORM_NAME);
			goToDesignerPage();
			getDesignerPage().getFormPropertiesView().setName(MODIFIED_NAME);
			getDesignerPage().saveDesign();
			goToFormManagerPage();
			goToDesignerPage();
			Assert.assertEquals(getDesignerPage().getFormPropertiesView().getName(), MODIFIED_NAME);
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void saveQuestionAsBlock() {
		createNewFormAndCategoryWithAdminRights();
		getDesignerPage().addNewQuestion();
		getDesignerPage().clickBlocksButton();
		getDesignerPage().clickSaveBlockButton();
		getDesignerPage().getNewBlockWindow().createNewBlock(NEW_BLOCK_NAME);
		checkNotificationIsHumanized(getNotification());
		saveDesignAndRemoveForm();
		deleteBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void saveGroupAsBlock() {
		createNewFormAndCategoryWithAdminRights();
		getDesignerPage().addNewGroup();
		getDesignerPage().clickBlocksButton();
		getDesignerPage().clickSaveBlockButton();
		getDesignerPage().getNewBlockWindow().createNewBlock(NEW_BLOCK_NAME);
		checkNotificationIsHumanized(getNotification());
		saveDesignAndRemoveForm();
		deleteBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void saveCategoryAsBlock() {
		createNewFormAndCategoryWithAdminRights();
		getDesignerPage().clickBlocksButton();
		getDesignerPage().clickSaveBlockButton();
		getDesignerPage().getNewBlockWindow().createNewBlock(NEW_BLOCK_NAME);
		checkNotificationIsHumanized(getNotification());
		saveDesignAndRemoveForm();
		deleteBlock();
	}

	private void createEmptyFormAndSimpleBlockDesign() {
		try {
			// Create empty form
			createNewFormWithAdminRights(NEW_FORM_NAME);
			goToBlockManagerPage();
			// Create block with design
			getBlockManagerPage().createNewBlock(NEW_BLOCK_NAME);
			goToDesignerPage();
			getDesignerPage().createAndSaveSimpleBlockDesign();
			// Import the block design in the form
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void importBlock() {
		createEmptyFormAndSimpleBlockDesign();
		goToFormManagerPage();
		goToDesignerPage();
		getDesignerPage().clickBlocksButton();
		getDesignerPage().clickInsertBlockButton();
		clickAcceptButtonIfExists();
		getDesignerPage().saveDesign();
		logOut();
		deleteForm();
		deleteBlock();
	}
}
