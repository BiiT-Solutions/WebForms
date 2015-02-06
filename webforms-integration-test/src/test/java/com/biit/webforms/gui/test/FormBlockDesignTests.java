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
		createNewForm(NEW_FORM_NAME);
		goToDesigner();
		getDesigner().addNewCategory();
	}

	/**
	 * Method to avoid repeating all the code that creates a block and a
	 * category.<br>
	 */
	private void createNewBlockAndCategory() {
		createNewBlock(NEW_BLOCK_NAME);
		goToDesigner();
		getDesigner().addNewCategory();
	}

	/**
	 * Method to avoid repeating all the code that block a form design and
	 * removes it afterwards.<br>
	 */
	private void saveDesignAndRemoveForm() {
		getDesigner().saveDesign();
		logOut();
		deleteForm();
	}

	/**
	 * Method to avoid repeating all the code that saves a block design and
	 * removes it afterwards.<br>
	 */
	private void saveDesignAndRemoveBlock() {
		getDesigner().saveDesign();
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
		getDesigner().goToBeginningOfTreeTable();
		getDesigner().clickInTreeTableRow(tableRow);
	}

	@Test(groups = { "formBlockDesign" })
	public void createEmptyForm() {
		createNewForm(NEW_FORM_NAME);
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
		createNewForm(NEW_FORM_NAME);
		goToDesigner();
		Assert.assertFalse(getDesigner().getGroupButton().isEnabled());
		Assert.assertFalse(getDesigner().getQuestionButton().isEnabled());
		Assert.assertFalse(getDesigner().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesigner().getSubanswerButton().isEnabled());
		Assert.assertFalse(getDesigner().getTextButton().isEnabled());
		Assert.assertFalse(getDesigner().getFieldButton().isEnabled());
		logOut();
		deleteForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkGroupQuestionAnswerSubanswerAtBlockLevel() {
		createNewBlock(NEW_BLOCK_NAME);
		goToDesigner();
		Assert.assertFalse(getDesigner().getGroupButton().isEnabled());
		Assert.assertFalse(getDesigner().getQuestionButton().isEnabled());
		Assert.assertFalse(getDesigner().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesigner().getSubanswerButton().isEnabled());
		Assert.assertFalse(getDesigner().getTextButton().isEnabled());
		Assert.assertFalse(getDesigner().getFieldButton().isEnabled());
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
		Assert.assertFalse(getDesigner().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesigner().getSubanswerButton().isEnabled());
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtBlockCategoryLevel() {
		createNewBlockAndCategory();
		Assert.assertFalse(getDesigner().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesigner().getSubanswerButton().isEnabled());
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createTextAtFormCategoryLevel() {
		createNewFormAndCategory();
		getDesigner().addNewText();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void createTextAtBlockCategoryLevel() {
		createNewBlockAndCategory();
		getDesigner().addNewText();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createFieldAtFormCategoryLevel() {
		createNewFormAndCategory();
		getDesigner().addNewField();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void createFieldAtBlockCategoryLevel() {
		createNewBlockAndCategory();
		getDesigner().addNewField();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionAtFormCategoryLevel() {
		createNewFormAndCategory();
		getDesigner().addNewQuestion();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionAtBlockCategoryLevel() {
		createNewBlockAndCategory();
		getDesigner().addNewQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createGroupAtFormCategoryLevel() {
		createNewFormAndCategory();
		getDesigner().addNewGroup();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void createGroupAtBlockCategoryLevel() {
		createNewBlockAndCategory();
		getDesigner().addNewGroup();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtFormGroupLevel() {
		createNewFormAndCategory();
		getDesigner().addNewGroup();
		Assert.assertFalse(getDesigner().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesigner().getSubanswerButton().isEnabled());
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtBlockGroupLevel() {
		createNewBlockAndCategory();
		getDesigner().addNewGroup();
		Assert.assertFalse(getDesigner().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesigner().getSubanswerButton().isEnabled());
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextTextAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewInputTextSubformatTextQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextTextAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewInputTextSubformatTextQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextEmailAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewInputTextSubformatEmailQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextEmailAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewInputTextSubformatEmailQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextPhoneAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewInputTextSubformatPhoneQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextPhoneAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewInputTextSubformatPhoneQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextIbanAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewInputTextSubformatIbanQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextIbanAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewInputTextSubformatIbanQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextBsnAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewInputTextSubformatBsnQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextBsnAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewInputTextSubformatBsnQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberFloatAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewInputNumberSubformatFloatQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberFloatAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewInputNumberSubformatFloatQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberNumberAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewInputNumberSubformatNumberQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberNumberAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewInputNumberSubformatNumberQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateDateAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewInputDateSubformatDateQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateDaterAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewInputDateSubformatDateQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDatePastAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewInputDateSubformatPastQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDatePastAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewInputDateSubformatPastQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateFutureAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewInputDateSubformatFutureQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateFutureAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewInputDateSubformatFutureQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputPostalCodePostalCodeAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewInputPostalcodeSubformatPostalcodeQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputPostalCodePostalCodeAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewInputPostalcodeSubformatPostalcodeQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionTextAreaAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewTextAreaQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionTextAreaAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewTextAreaQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionRadioButtonAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewRadioButtonQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionRadioButtonAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewRadioButtonQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionMultiCheckBoxAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewMultiCheckboxQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionMultiCheckBoxAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewMultiCheckboxQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionListAtFormCategoryLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewListQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionListAtBlockCategoryLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewListQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextTextAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputTextSubformatTextQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextTextAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputTextSubformatTextQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextEmailAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputTextSubformatEmailQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextEmailAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputTextSubformatEmailQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextPhoneAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputTextSubformatPhoneQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextPhoneAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputTextSubformatPhoneQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextIbanAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputTextSubformatIbanQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextIbanAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputTextSubformatIbanQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextBsnAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputTextSubformatBsnQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextBsnAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputTextSubformatBsnQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberFloatAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputNumberSubformatFloatQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberFloatAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputNumberSubformatFloatQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberNumberAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputNumberSubformatNumberQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberNumberAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputNumberSubformatNumberQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateDateAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputDateSubformatDateQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateDaterAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputDateSubformatDateQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDatePastAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputDateSubformatPastQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDatePastAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputDateSubformatPastQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateFutureAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputDateSubformatFutureQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateFutureAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputDateSubformatFutureQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputPostalCodePostalCodeAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputPostalcodeSubformatPostalcodeQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputPostalCodePostalCodeAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputPostalcodeSubformatPostalcodeQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionTextAreaAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewTextAreaQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionTextAreaAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewTextAreaQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionRadioButtonAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewRadioButtonQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionRadioButtonAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewRadioButtonQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionMultiCheckBoxAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewMultiCheckboxQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionMultiCheckBoxAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewMultiCheckboxQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionListAtFormGroupLevel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewListQuestion();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionListAtBlockGroupLevel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewListQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtFormCategoryQuestionInput() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewInputQuestion();
			Assert.assertFalse(getDesigner().getAnswerButton().isEnabled());
			Assert.assertFalse(getDesigner().getSubanswerButton().isEnabled());
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtBlockCategoryQuestionInput() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewInputQuestion();
			Assert.assertFalse(getDesigner().getAnswerButton().isEnabled());
			Assert.assertFalse(getDesigner().getSubanswerButton().isEnabled());
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtFormCategoryGroupQuestionInput() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputQuestion();
			Assert.assertFalse(getDesigner().getAnswerButton().isEnabled());
			Assert.assertFalse(getDesigner().getSubanswerButton().isEnabled());
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtBlockCategoryGroupQuestionInput() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewInputQuestion();
			Assert.assertFalse(getDesigner().getAnswerButton().isEnabled());
			Assert.assertFalse(getDesigner().getSubanswerButton().isEnabled());
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createAnswerSubanswerAtFormCategoryQuestionRadioButton() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewRadioButtonQuestion();
			getDesigner().addNewAnswer();
			getDesigner().addNewSubanswer();
			getDesigner().addNewSubanswer();
			getDesigner().addNewAnswer();
			getDesigner().addNewSubanswer();
			getDesigner().addNewSubanswer();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createAnswerSubanswerAtBlockCategoryQuestionRadioButton() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewRadioButtonQuestion();
			getDesigner().addNewAnswer();
			getDesigner().addNewSubanswer();
			getDesigner().addNewSubanswer();
			getDesigner().addNewAnswer();
			getDesigner().addNewSubanswer();
			getDesigner().addNewSubanswer();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createAnswerSubanswerAtFormCategoryGroupQuestionRadioButton() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewRadioButtonQuestion();
			getDesigner().addNewAnswer();
			getDesigner().addNewSubanswer();
			getDesigner().addNewSubanswer();
			getDesigner().addNewAnswer();
			getDesigner().addNewSubanswer();
			getDesigner().addNewSubanswer();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createAnswerSubanswerAtBlockCategoryGroupQuestionRadioButton() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewRadioButtonQuestion();
			getDesigner().addNewAnswer();
			getDesigner().addNewSubanswer();
			getDesigner().addNewSubanswer();
			getDesigner().addNewAnswer();
			getDesigner().addNewSubanswer();
			getDesigner().addNewSubanswer();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteSubanswerAtFormCategoryQuestionRadioButton() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewRadioButtonQuestion();
			getDesigner().addNewAnswer();
			getDesigner().addNewSubanswer();
			getDesigner().clickDeleteButton();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteSubanswerAtBlockCategoryQuestionRadioButton() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewRadioButtonQuestion();
			getDesigner().addNewAnswer();
			getDesigner().addNewSubanswer();
			getDesigner().clickDeleteButton();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteSubanswerAtFormCategoryGroupQuestionRadioButton() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewRadioButtonQuestion();
			getDesigner().addNewAnswer();
			getDesigner().addNewSubanswer();
			getDesigner().clickDeleteButton();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteSubanswerAtBlockCategoryGroupQuestionRadioButton() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewRadioButtonQuestion();
			getDesigner().addNewAnswer();
			getDesigner().addNewSubanswer();
			getDesigner().clickDeleteButton();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteAnswerAtFormCategoryQuestionRadioButton() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewRadioButtonQuestion();
			getDesigner().addNewAnswer();
			getDesigner().clickDeleteButton();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteAnswerAtBlockCategoryQuestionRadioButton() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewRadioButtonQuestion();
			getDesigner().addNewAnswer();
			getDesigner().clickDeleteButton();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteAnswerAtFormCategoryGroupQuestionRadioButton() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewRadioButtonQuestion();
			getDesigner().addNewAnswer();
			getDesigner().clickDeleteButton();
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteAnswerAtBlockCategoryGroupQuestionRadioButton() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().addNewRadioButtonQuestion();
			getDesigner().addNewAnswer();
			getDesigner().clickDeleteButton();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteQuestionAtFormCategory() {
		createNewFormAndCategory();
		getDesigner().addNewQuestion();
		getDesigner().clickDeleteButton();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteQuestionAtBlockCategory() {
		createNewBlockAndCategory();
		getDesigner().addNewQuestion();
		getDesigner().clickDeleteButton();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteQuestionAtFormCategoryGroup() {
		createNewFormAndCategory();
		getDesigner().addNewGroup();
		getDesigner().addNewQuestion();
		getDesigner().clickDeleteButton();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteQuestionAtBlockCategoryGroup() {
		createNewBlockAndCategory();
		getDesigner().addNewGroup();
		getDesigner().addNewQuestion();
		getDesigner().clickDeleteButton();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteGroupAtFormCategory() {
		createNewFormAndCategory();
		getDesigner().addNewGroup();
		getDesigner().clickDeleteButton();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteGroupAtBlockCategory() {
		createNewBlockAndCategory();
		getDesigner().addNewGroup();
		getDesigner().clickDeleteButton();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteFormCategory() {
		createNewFormAndCategory();
		getDesigner().clickDeleteButton();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteBlockCategory() {
		createNewBlockAndCategory();
		getDesigner().clickDeleteButton();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFormCategoryTechnicalName() {
		try {
			createNewFormAndCategory();
			getDesigner().getCategoryPropertiesView().setTechnicalName(MODIFIED_NAME);
			getDesigner().saveDesign();
			deselectSelecTableRow(TREE_TABLE_CATEGORY_ROW_INDEX);
			Assert.assertEquals(getDesigner().getCategoryPropertiesView().getTechnicalName(), MODIFIED_NAME);
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyBlockCategoryTechnicalName() {
		try {
			createNewBlockAndCategory();
			getDesigner().getCategoryPropertiesView().setTechnicalName(MODIFIED_NAME);
			getDesigner().saveDesign();
			deselectSelecTableRow(TREE_TABLE_CATEGORY_ROW_INDEX);
			Assert.assertEquals(getDesigner().getCategoryPropertiesView().getTechnicalName(), MODIFIED_NAME);
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFormGroupTechnicalName() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().getGroupPropertiesView().setTechnicalName(MODIFIED_NAME);
			getDesigner().saveDesign();
			deselectSelecTableRow(TREE_TABLE_CATEGORY_GROUP_ROW_INDEX);
			Assert.assertEquals(getDesigner().getGroupPropertiesView().getTechnicalName(), MODIFIED_NAME);
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyBlockGroupTechnicalName() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().getGroupPropertiesView().setTechnicalName(MODIFIED_NAME);
			getDesigner().saveDesign();
			deselectSelecTableRow(TREE_TABLE_CATEGORY_GROUP_ROW_INDEX);
			Assert.assertEquals(getDesigner().getGroupPropertiesView().getTechnicalName(), MODIFIED_NAME);
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	private void addQuestionModifyAndCheckTechnicalName(String modifiedName, Integer rowToCheck) {
		try {
			getDesigner().addNewQuestion();
			getDesigner().getQuestionPropertiesView().setTechnicalName(modifiedName);
			getDesigner().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertEquals(getDesigner().getQuestionPropertiesView().getTechnicalName(), modifiedName);
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
		getDesigner().addNewGroup();
		addQuestionModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionTechnicalNameAtBlockGroupLevel() {
		createNewBlockAndCategory();
		getDesigner().addNewGroup();
		addQuestionModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	private void addTextModifyAndCheckTechnicalName(String modifiedName, Integer rowToCheck) {
		try {
			getDesigner().addNewText();
			getDesigner().getTextPropertiesView().setTechnicalName(modifiedName);
			getDesigner().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertEquals(getDesigner().getTextPropertiesView().getTechnicalName(), modifiedName);
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
		getDesigner().addNewGroup();
		addTextModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyTextTechnicalNameAtBlockGroupLevel() {
		createNewBlockAndCategory();
		getDesigner().addNewGroup();
		addTextModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	private void addFieldModifyAndCheckTechnicalName(String modifiedName, Integer rowToCheck) {
		try {
			getDesigner().addNewField();
			getDesigner().getFieldPropertiesView().setTechnicalName(modifiedName);
			getDesigner().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertEquals(getDesigner().getFieldPropertiesView().getTechnicalName(), modifiedName);
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
		getDesigner().addNewGroup();
		addFieldModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFieldTechnicalNameAtBlockGroupLevel() {
		createNewBlockAndCategory();
		getDesigner().addNewGroup();
		addFieldModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFormCategoryLabel() {
		try {
			createNewFormAndCategory();
			getDesigner().getCategoryPropertiesView().setLabel(MODIFIED_LABEL);
			getDesigner().saveDesign();
			deselectSelecTableRow(TREE_TABLE_CATEGORY_ROW_INDEX);
			Assert.assertEquals(getDesigner().getCategoryPropertiesView().getLabel(), MODIFIED_LABEL);
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyBlockCategoryLabel() {
		try {
			createNewBlockAndCategory();
			getDesigner().getCategoryPropertiesView().setLabel(MODIFIED_LABEL);
			getDesigner().saveDesign();
			deselectSelecTableRow(TREE_TABLE_CATEGORY_ROW_INDEX);
			Assert.assertEquals(getDesigner().getCategoryPropertiesView().getLabel(), MODIFIED_LABEL);
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFormGroupLabel() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewGroup();
			getDesigner().getGroupPropertiesView().setLabel(MODIFIED_LABEL);
			getDesigner().saveDesign();
			deselectSelecTableRow(TREE_TABLE_CATEGORY_GROUP_ROW_INDEX);
			Assert.assertEquals(getDesigner().getGroupPropertiesView().getLabel(), MODIFIED_LABEL);
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyBlockGroupLabel() {
		try {
			createNewBlockAndCategory();
			getDesigner().addNewGroup();
			getDesigner().getGroupPropertiesView().setLabel(MODIFIED_LABEL);
			getDesigner().saveDesign();
			deselectSelecTableRow(TREE_TABLE_CATEGORY_GROUP_ROW_INDEX);
			Assert.assertEquals(getDesigner().getGroupPropertiesView().getLabel(), MODIFIED_LABEL);
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	private void addQuestionModifyAndCheckLabel(String modifiedLabel, Integer rowToCheck) {
		try {
			getDesigner().addNewQuestion();
			getDesigner().getQuestionPropertiesView().setLabel(modifiedLabel);
			getDesigner().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertEquals(getDesigner().getQuestionPropertiesView().getLabel(), modifiedLabel);
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
		getDesigner().addNewGroup();
		addQuestionModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionLabelAtBlockGroupLevel() {
		createNewBlockAndCategory();
		getDesigner().addNewGroup();
		addQuestionModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	private void addQuestionAnswerModifyAndCheckLabel(String modifiedLabel, Integer rowToCheck) {
		try {
			getDesigner().addNewRadioButtonQuestion();
			getDesigner().addNewAnswer();
			getDesigner().getAnswerPropertiesView().setLabel(modifiedLabel);
			getDesigner().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertEquals(getDesigner().getAnswerPropertiesView().getLabel(), modifiedLabel);
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
		getDesigner().addNewGroup();
		addQuestionAnswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerLabelAtBlockGroupQuestionLevel() {
		createNewBlockAndCategory();
		getDesigner().addNewGroup();
		addQuestionAnswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	private void addQuestionAnswerSubanswerModifyAndCheckLabel(String modifiedLabel, Integer rowToCheck) {
		try {
			getDesigner().addNewRadioButtonQuestion();
			getDesigner().addNewAnswer();
			getDesigner().addNewSubanswer();
			getDesigner().getSubanswerPropertiesView().setLabel(modifiedLabel);
			getDesigner().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertEquals(getDesigner().getSubanswerPropertiesView().getLabel(), modifiedLabel);
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
		getDesigner().addNewGroup();
		addQuestionAnswerSubanswerModifyAndCheckLabel(MODIFIED_LABEL,
				TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerLabelAtBlockGroupQuestionAnswerLevel() {
		createNewBlockAndCategory();
		getDesigner().addNewGroup();
		addQuestionAnswerSubanswerModifyAndCheckLabel(MODIFIED_LABEL,
				TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFormDescription() {
		try {
			createNewForm(NEW_FORM_NAME);
			goToDesigner();
			getDesigner().getFormPropertiesView().setDescription(MODIFIED_DESCRIPTION);
			getDesigner().saveDesign();
			goToFormManager();
			goToDesigner();
			Assert.assertEquals(getDesigner().getFormPropertiesView().getDescription(), MODIFIED_DESCRIPTION);
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	private void addQuestionModifyAndCheckDescription(String modifiedDescription, Integer rowToCheck) {
		try {
			getDesigner().addNewQuestion();
			getDesigner().getQuestionPropertiesView().setDescription(modifiedDescription);
			getDesigner().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertEquals(getDesigner().getQuestionPropertiesView().getDescription(), modifiedDescription);
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
		getDesigner().addNewGroup();
		addQuestionModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionDescriptionAtBlockGroupLevel() {
		createNewBlockAndCategory();
		getDesigner().addNewGroup();
		addQuestionModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	private void addAnswerModifyAndCheckDescription(String modifiedDescription, Integer rowToCheck) {
		try {
			getDesigner().addNewRadioButtonQuestion();
			getDesigner().addNewAnswer();
			getDesigner().getAnswerPropertiesView().setDescription(modifiedDescription);
			getDesigner().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertEquals(getDesigner().getAnswerPropertiesView().getDescription(), modifiedDescription);
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
		getDesigner().addNewGroup();

		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerDescriptionAtBlockGroupQuestionLevel() {
		createNewBlockAndCategory();
		getDesigner().addNewGroup();
		addAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	private void addSubAnswerModifyAndCheckDescription(String modifiedDescription, Integer rowToCheck) {
		try {
			getDesigner().addNewRadioButtonQuestion();
			getDesigner().addNewAnswer();
			getDesigner().addNewSubanswer();
			getDesigner().getSubanswerPropertiesView().setDescription(modifiedDescription);
			getDesigner().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertEquals(getDesigner().getSubanswerPropertiesView().getDescription(), modifiedDescription);
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
		getDesigner().addNewGroup();
		addSubAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION,
				TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerDescriptionAtBlockGroupQuestionAnswerLevel() {
		createNewBlockAndCategory();
		getDesigner().addNewGroup();
		addSubAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION,
				TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	private void addQuestionModifyAndCheckMandatory(Integer rowToCheck) {
		try {
			getDesigner().addNewQuestion();
			String mandatoryValue = getDesigner().getQuestionPropertiesView().getMandatoryCheckBoxValue();
			getDesigner().getQuestionPropertiesView().clickMandatoryCheckBox();
			getDesigner().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertNotEquals(getDesigner().getQuestionPropertiesView().getMandatoryCheckBoxValue(),
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
		getDesigner().addNewGroup();
		addQuestionModifyAndCheckMandatory(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionMandatoryAtBlockGroupLevel() {
		createNewBlockAndCategory();
		getDesigner().addNewGroup();
		addQuestionModifyAndCheckMandatory(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	private void addQuestionModifyAndCheckHorizontal(Integer rowToCheck) {
		try {
			getDesigner().addNewRadioButtonQuestion();
			String horizontalValue = getDesigner().getQuestionPropertiesView().getHorizontalCheckBoxValue();
			getDesigner().getQuestionPropertiesView().clickHorizontalCheckBox();
			getDesigner().saveDesign();
			deselectSelecTableRow(rowToCheck);
			Assert.assertNotEquals(getDesigner().getQuestionPropertiesView().getHorizontalCheckBoxValue(),
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
		getDesigner().addNewGroup();
		addQuestionModifyAndCheckHorizontal(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionHorizontalAtBlockGroupLevel() {
		createNewBlockAndCategory();
		getDesigner().addNewGroup();
		addQuestionModifyAndCheckHorizontal(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkQuestionHorizontalIsOnlyActivatedForRadioButtonQuestions() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewRadioButtonQuestion();
			Assert.assertEquals(getDesigner().getQuestionPropertiesView().isHorizontalEnabled(), true);
			getDesigner().addNewListQuestion();
			Assert.assertEquals(getDesigner().getQuestionPropertiesView().isHorizontalEnabled(), false);
			getDesigner().addNewMultiCheckboxQuestion();
			Assert.assertEquals(getDesigner().getQuestionPropertiesView().isHorizontalEnabled(), false);
			getDesigner().addNewInputQuestion();
			Assert.assertEquals(getDesigner().getQuestionPropertiesView().isHorizontalEnabled(), false);
			getDesigner().addNewTextAreaQuestion();
			Assert.assertEquals(getDesigner().getQuestionPropertiesView().isHorizontalEnabled(), false);
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void checkQuestionMandatoryIsActivatedForAllQuestions() {
		try {
			createNewFormAndCategory();
			getDesigner().addNewRadioButtonQuestion();
			Assert.assertEquals(getDesigner().getQuestionPropertiesView().ismandatoryEnabled(), true);
			getDesigner().addNewListQuestion();
			Assert.assertEquals(getDesigner().getQuestionPropertiesView().ismandatoryEnabled(), true);
			getDesigner().addNewMultiCheckboxQuestion();
			Assert.assertEquals(getDesigner().getQuestionPropertiesView().ismandatoryEnabled(), true);
			getDesigner().addNewInputQuestion();
			Assert.assertEquals(getDesigner().getQuestionPropertiesView().ismandatoryEnabled(), true);
			getDesigner().addNewTextAreaQuestion();
			Assert.assertEquals(getDesigner().getQuestionPropertiesView().ismandatoryEnabled(), true);
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFormName() {
		try {
			createNewForm(NEW_FORM_NAME);
			goToDesigner();
			getDesigner().getFormPropertiesView().setName(MODIFIED_NAME);
			getDesigner().saveDesign();
			goToFormManager();
			goToDesigner();
			Assert.assertEquals(getDesigner().getFormPropertiesView().getName(), MODIFIED_NAME);
			saveDesignAndRemoveForm();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

}
