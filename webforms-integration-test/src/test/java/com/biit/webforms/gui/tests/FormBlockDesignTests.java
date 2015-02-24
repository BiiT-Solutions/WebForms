package com.biit.webforms.gui.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.webforms.gui.tests.exceptions.FieldNotEditableException;

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
	public void checkAnswerSubanswerAtBlockCategoryGroupQuestionInput() {
		printTestNameInDebugTrace("checkAnswerSubanswerAtBlockCategoryGroupQuestionInput");
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
	public void checkAnswerSubanswerAtBlockCategoryLevel() {
		printTestNameInDebugTrace("checkAnswerSubanswerAtBlockCategoryLevel");
		createNewBlockAndCategory();
		Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtBlockCategoryQuestionInput() {
		printTestNameInDebugTrace("checkAnswerSubanswerAtBlockCategoryQuestionInput");
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
	public void checkAnswerSubanswerAtBlockGroupLevel() {
		printTestNameInDebugTrace("checkAnswerSubanswerAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtFormCategoryGroupQuestionInput() {
		printTestNameInDebugTrace("checkAnswerSubanswerAtFormCategoryGroupQuestionInput");
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
	public void checkAnswerSubanswerAtFormCategoryLevel() {
		printTestNameInDebugTrace("checkAnswerSubanswerAtFormCategoryLevel");
		createNewFormAndCategory();
		Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtFormCategoryQuestionInput() {
		printTestNameInDebugTrace("checkAnswerSubanswerAtFormCategoryQuestionInput");
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
	public void checkAnswerSubanswerAtFormGroupLevel() {
		printTestNameInDebugTrace("checkAnswerSubanswerAtFormGroupLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkGroupQuestionAnswerSubanswerAtBlockLevel() {
		printTestNameInDebugTrace("checkGroupQuestionAnswerSubanswerAtBlockLevel");
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
	public void checkGroupQuestionAnswerSubanswerAtFormLevel() {
		printTestNameInDebugTrace("checkGroupQuestionAnswerSubanswerAtFormLevel");
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
	public void checkQuestionHorizontalIsOnlyActivatedForRadioButtonQuestions() {
		printTestNameInDebugTrace("checkQuestionHorizontalIsOnlyActivatedForRadioButtonQuestions");
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
		printTestNameInDebugTrace("checkQuestionMandatoryIsActivatedForAllQuestions");
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
	public void createAnswerSubanswerAtBlockCategoryGroupQuestionRadioButton() {
		printTestNameInDebugTrace("createAnswerSubanswerAtBlockCategoryGroupQuestionRadioButton");
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
	public void createAnswerSubanswerAtBlockCategoryQuestionRadioButton() {
		printTestNameInDebugTrace("createAnswerSubanswerAtBlockCategoryQuestionRadioButton");
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
	public void createFieldAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createFieldAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewField();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createGroupAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createGroupAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createGroupAtFormCategoryLevel() {
		printTestNameInDebugTrace("createGroupAtFormCategoryLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		saveDesignAndRemoveForm();
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

	@Test(groups = { "formBlockDesign" })
	public void createQuestionAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createQuestionAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateDateAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createQuestionInputDateDateAtBlockCategoryLevel");
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputDateSubformatDateQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateDateAtBlockGroupLevel() {
		printTestNameInDebugTrace("createQuestionInputDateDateAtBlockGroupLevel");
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
	public void createQuestionInputDateFutureAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createQuestionInputDateFutureAtBlockCategoryLevel");
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputDateSubformatFutureQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateFutureAtBlockGroupLevel() {
		printTestNameInDebugTrace("createQuestionInputDateFutureAtBlockGroupLevel");
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
	public void createQuestionInputDatePastAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createQuestionInputDatePastAtBlockCategoryLevel");
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputDateSubformatPastQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDatePastAtBlockGroupLevel() {
		printTestNameInDebugTrace("createQuestionInputDatePastAtBlockGroupLevel");
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
	public void createQuestionInputNumberFloatAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createQuestionInputNumberFloatAtBlockCategoryLevel");
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputNumberSubformatFloatQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberFloatAtBlockGroupLevel() {
		printTestNameInDebugTrace("createQuestionInputNumberFloatAtBlockGroupLevel");
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
	public void createQuestionInputNumberNumberAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createQuestionInputNumberNumberAtBlockCategoryLevel");
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputNumberSubformatNumberQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberNumberAtBlockGroupLevel() {
		printTestNameInDebugTrace("createQuestionInputNumberNumberAtBlockGroupLevel");
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
	public void createQuestionInputPostalCodePostalCodeAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createQuestionInputPostalCodePostalCodeAtBlockCategoryLevel");
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputPostalcodeSubformatPostalcodeQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputPostalCodePostalCodeAtBlockGroupLevel() {
		printTestNameInDebugTrace("createQuestionInputPostalCodePostalCodeAtBlockGroupLevel");
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
	public void createQuestionInputTextBsnAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createQuestionInputTextBsnAtBlockCategoryLevel");
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputTextSubformatBsnQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextBsnAtBlockGroupLevel() {
		printTestNameInDebugTrace("createQuestionInputTextBsnAtBlockGroupLevel");
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
	public void createQuestionInputTextEmailAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createQuestionInputTextEmailAtBlockCategoryLevel");
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputTextSubformatEmailQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextEmailAtBlockGroupLevel() {
		printTestNameInDebugTrace("createQuestionInputTextEmailAtBlockGroupLevel");
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
	public void createQuestionInputTextIbanAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createQuestionInputTextIbanAtBlockCategoryLevel");
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputTextSubformatIbanQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextIbanAtBlockGroupLevel() {
		printTestNameInDebugTrace("createQuestionInputTextIbanAtBlockGroupLevel");
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
	public void createQuestionInputTextPhoneAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createQuestionInputTextPhoneAtBlockCategoryLevel");
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputTextSubformatPhoneQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextPhoneAtBlockGroupLevel() {
		printTestNameInDebugTrace("createQuestionInputTextPhoneAtBlockGroupLevel");
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
	public void createQuestionInputTextTextAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createQuestionInputTextTextAtBlockCategoryLevel");
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewInputTextSubformatTextQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextTextAtBlockGroupLevel() {
		printTestNameInDebugTrace("createQuestionInputTextTextAtBlockGroupLevel");
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
	public void createQuestionListAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createQuestionListAtBlockCategoryLevel");
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewListQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionListAtBlockGroupLevel() {
		printTestNameInDebugTrace("createQuestionListAtBlockGroupLevel");
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
	public void createQuestionListAtFormGroupLevel() {
		printTestNameInDebugTrace("createQuestionListAtFormGroupLevel");
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
	public void createQuestionMultiCheckBoxAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createQuestionMultiCheckBoxAtBlockCategoryLevel");
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewMultiCheckboxQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionMultiCheckBoxAtBlockGroupLevel() {
		printTestNameInDebugTrace("createQuestionMultiCheckBoxAtBlockGroupLevel");
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
	public void createQuestionRadioButtonAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createQuestionRadioButtonAtBlockCategoryLevel");
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewRadioButtonQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionRadioButtonAtBlockGroupLevel() {
		printTestNameInDebugTrace("createQuestionRadioButtonAtBlockGroupLevel");
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
	public void createQuestionTextAreaAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createQuestionTextAreaAtBlockCategoryLevel");
		try {
			createNewBlockAndCategory();
			getDesignerPage().addNewTextAreaQuestion();
			saveDesignAndRemoveBlock();
		} catch (FieldNotEditableException e) {
			Assert.fail();
		}
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionTextAreaAtBlockGroupLevel() {
		printTestNameInDebugTrace("createQuestionTextAreaAtBlockGroupLevel");
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
	public void createTextAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createTextAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewText();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteAnswerAtBlockCategoryGroupQuestionRadioButton() {
		printTestNameInDebugTrace("deleteAnswerAtBlockCategoryGroupQuestionRadioButton");
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
	public void deleteAnswerAtBlockCategoryQuestionRadioButton() {
		printTestNameInDebugTrace("deleteAnswerAtBlockCategoryQuestionRadioButton");
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
		printTestNameInDebugTrace("deleteAnswerAtFormCategoryGroupQuestionRadioButton");
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
	public void deleteAnswerAtFormCategoryQuestionRadioButton() {
		printTestNameInDebugTrace("deleteAnswerAtFormCategoryQuestionRadioButton");
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
	public void deleteBlockCategory() {
		printTestNameInDebugTrace("deleteBlockCategory");
		createNewBlockAndCategory();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteFormCategory() {
		printTestNameInDebugTrace("deleteFormCategory");
		createNewFormAndCategory();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteGroupAtBlockCategory() {
		printTestNameInDebugTrace("deleteGroupAtBlockCategory");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteGroupAtFormCategory() {
		printTestNameInDebugTrace("deleteGroupAtFormCategory");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteQuestionAtBlockCategory() {
		printTestNameInDebugTrace("deleteQuestionAtBlockCategory");
		createNewBlockAndCategory();
		getDesignerPage().addNewQuestion();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteQuestionAtBlockCategoryGroup() {
		printTestNameInDebugTrace("deleteQuestionAtBlockCategoryGroup");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewQuestion();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteQuestionAtFormCategory() {
		printTestNameInDebugTrace("deleteQuestionAtFormCategory");
		createNewFormAndCategory();
		getDesignerPage().addNewQuestion();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteQuestionAtFormCategoryGroup() {
		printTestNameInDebugTrace("deleteQuestionAtFormCategoryGroup");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewQuestion();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteSubanswerAtBlockCategoryGroupQuestionRadioButton() {
		printTestNameInDebugTrace("deleteSubanswerAtBlockCategoryGroupQuestionRadioButton");
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
	public void deleteSubanswerAtBlockCategoryQuestionRadioButton() {
		printTestNameInDebugTrace("deleteSubanswerAtBlockCategoryQuestionRadioButton");
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
		printTestNameInDebugTrace("deleteSubanswerAtFormCategoryGroupQuestionRadioButton");
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
	public void deleteSubanswerAtFormCategoryQuestionRadioButton() {
		printTestNameInDebugTrace("deleteSubanswerAtFormCategoryQuestionRadioButton");
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
	public void importBlock() {
		printTestNameInDebugTrace("importBlock");
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

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerDescriptionAtBlockCategoryQuestionLevel() {
		printTestNameInDebugTrace("modifyAnswerDescriptionAtBlockCategoryQuestionLevel");
		createNewBlockAndCategory();
		addAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerDescriptionAtBlockGroupQuestionLevel() {
		printTestNameInDebugTrace("modifyAnswerDescriptionAtBlockGroupQuestionLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerDescriptionAtFormCategoryQuestionLevel() {
		printTestNameInDebugTrace("modifyAnswerDescriptionAtFormCategoryQuestionLevel");
		createNewFormAndCategory();
		addAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerDescriptionAtFormGroupQuestionLevel() {
		printTestNameInDebugTrace("modifyAnswerDescriptionAtFormGroupQuestionLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerLabelAtBlockCategoryQuestionLevel() {
		printTestNameInDebugTrace("modifyAnswerLabelAtBlockCategoryQuestionLevel");
		createNewBlockAndCategory();
		addQuestionAnswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerLabelAtBlockGroupQuestionLevel() {
		printTestNameInDebugTrace("modifyAnswerLabelAtBlockGroupQuestionLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionAnswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerLabelAtFormCategoryQuestionLevel() {
		printTestNameInDebugTrace("modifyAnswerLabelAtFormCategoryQuestionLevel");
		createNewFormAndCategory();
		addQuestionAnswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerLabelAtFormGroupQuestionLevel() {
		printTestNameInDebugTrace("modifyAnswerLabelAtFormGroupQuestionLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionAnswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyBlockCategoryLabel() {
		printTestNameInDebugTrace("modifyBlockCategoryLabel");
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
	public void modifyBlockCategoryTechnicalName() {
		printTestNameInDebugTrace("modifyBlockCategoryTechnicalName");
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
	public void modifyBlockGroupLabel() {
		printTestNameInDebugTrace("modifyBlockGroupLabel");
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

	@Test(groups = { "formBlockDesign" })
	public void modifyBlockGroupTechnicalName() {
		printTestNameInDebugTrace("modifyBlockGroupTechnicalName");
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

	@Test(groups = { "formBlockDesign" })
	public void modifyFieldTechnicalNameAtBlockCategoryLevel() {
		printTestNameInDebugTrace("modifyFieldTechnicalNameAtBlockCategoryLevel");
		createNewBlockAndCategory();
		addFieldModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFieldTechnicalNameAtBlockGroupLevel() {
		printTestNameInDebugTrace("modifyFieldTechnicalNameAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addFieldModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFieldTechnicalNameAtFormCategoryLevel() {
		printTestNameInDebugTrace("modifyFieldTechnicalNameAtFormCategoryLevel");
		createNewFormAndCategory();
		addFieldModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFieldTechnicalNameAtFormGroupLevel() {
		printTestNameInDebugTrace("modifyFieldTechnicalNameAtFormGroupLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addFieldModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFormCategoryLabel() {
		printTestNameInDebugTrace("modifyFormCategoryLabel");
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
	public void modifyFormCategoryTechnicalName() {
		printTestNameInDebugTrace("modifyFormCategoryTechnicalName");
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
	public void modifyFormDescription() {
		printTestNameInDebugTrace("modifyFormDescription");
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

	@Test(groups = { "formBlockDesign" })
	public void modifyFormGroupLabel() {
		printTestNameInDebugTrace("modifyFormGroupLabel");
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
	public void modifyFormGroupTechnicalName() {
		printTestNameInDebugTrace("modifyFormGroupTechnicalName");
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
	public void modifyFormName() {
		printTestNameInDebugTrace("modifyFormName");
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
	public void modifyQuestionDescriptionAtBlockCategoryLevel() {
		printTestNameInDebugTrace("modifyQuestionDescriptionAtBlockCategoryLevel");
		createNewBlockAndCategory();
		addQuestionModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionDescriptionAtBlockGroupLevel() {
		printTestNameInDebugTrace("modifyQuestionDescriptionAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionDescriptionAtFormCategoryLevel() {
		printTestNameInDebugTrace("modifyQuestionDescriptionAtFormCategoryLevel");
		createNewFormAndCategory();
		addQuestionModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionDescriptionAtFormGroupLevel() {
		printTestNameInDebugTrace("modifyQuestionDescriptionAtFormGroupLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionHorizontalAtBlockCategoryLevel() {
		printTestNameInDebugTrace("modifyQuestionHorizontalAtBlockCategoryLevel");
		createNewBlockAndCategory();
		addQuestionModifyAndCheckHorizontal(TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionHorizontalAtBlockGroupLevel() {
		printTestNameInDebugTrace("modifyQuestionHorizontalAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckHorizontal(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionHorizontalAtFormCategoryLevel() {
		printTestNameInDebugTrace("modifyQuestionHorizontalAtFormCategoryLevel");
		createNewFormAndCategory();
		addQuestionModifyAndCheckHorizontal(TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionHorizontalAtFormGroupLevel() {
		printTestNameInDebugTrace("modifyQuestionHorizontalAtFormGroupLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckHorizontal(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionLabelAtBlockCategoryLevel() {
		printTestNameInDebugTrace("modifyQuestionLabelAtBlockCategoryLevel");
		createNewBlockAndCategory();
		addQuestionModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionLabelAtBlockGroupLevel() {
		printTestNameInDebugTrace("modifyQuestionLabelAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionLabelAtFormCategoryLevel() {
		printTestNameInDebugTrace("modifyQuestionLabelAtFormCategoryLevel");
		createNewFormAndCategory();
		addQuestionModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionLabelAtFormGroupLevel() {
		printTestNameInDebugTrace("modifyQuestionLabelAtFormGroupLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionMandatoryAtBlockCategoryLevel() {
		printTestNameInDebugTrace("modifyQuestionMandatoryAtBlockCategoryLevel");
		createNewBlockAndCategory();
		addQuestionModifyAndCheckMandatory(TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionMandatoryAtBlockGroupLevel() {
		printTestNameInDebugTrace("modifyQuestionMandatoryAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckMandatory(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionMandatoryAtFormCategoryLevel() {
		printTestNameInDebugTrace("modifyQuestionMandatoryAtFormCategoryLevel");
		createNewFormAndCategory();
		addQuestionModifyAndCheckMandatory(TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionMandatoryAtFormGroupLevel() {
		printTestNameInDebugTrace("modifyQuestionMandatoryAtFormGroupLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckMandatory(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionTechnicalNameAtBlockCategoryLevel() {
		printTestNameInDebugTrace("modifyQuestionTechnicalNameAtBlockCategoryLevel");
		createNewBlockAndCategory();
		addQuestionModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionTechnicalNameAtBlockGroupLevel() {
		printTestNameInDebugTrace("modifyQuestionTechnicalNameAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionTechnicalNameAtFormCategoryLevel() {
		printTestNameInDebugTrace("modifyQuestionTechnicalNameAtFormCategoryLevel");
		createNewFormAndCategory();
		addQuestionModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionTechnicalNameAtFormGroupLevel() {
		printTestNameInDebugTrace("modifyQuestionTechnicalNameAtFormGroupLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerDescriptionAtBlockCategoryQuestionAnswerLevel() {
		printTestNameInDebugTrace("modifySubanswerDescriptionAtBlockCategoryQuestionAnswerLevel");
		createNewBlockAndCategory();
		addSubAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION,
				TREE_TABLE_CATEGORY_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerDescriptionAtBlockGroupQuestionAnswerLevel() {
		printTestNameInDebugTrace("modifySubanswerDescriptionAtBlockGroupQuestionAnswerLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addSubAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION,
				TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerDescriptionAtFormCategoryQuestionAnswerLevel() {
		printTestNameInDebugTrace("modifySubanswerDescriptionAtFormCategoryQuestionAnswerLevel");
		createNewFormAndCategory();
		addSubAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION,
				TREE_TABLE_CATEGORY_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerDescriptionAtFormGroupQuestionAnswerLevel() {
		printTestNameInDebugTrace("modifySubanswerDescriptionAtFormGroupQuestionAnswerLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addSubAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION,
				TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerLabelAtBlockCategoryQuestionAnswerLevel() {
		printTestNameInDebugTrace("modifySubanswerLabelAtBlockCategoryQuestionAnswerLevel");
		createNewBlockAndCategory();
		addQuestionAnswerSubanswerModifyAndCheckLabel(MODIFIED_LABEL,
				TREE_TABLE_CATEGORY_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerLabelAtBlockGroupQuestionAnswerLevel() {
		printTestNameInDebugTrace("modifySubanswerLabelAtBlockGroupQuestionAnswerLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionAnswerSubanswerModifyAndCheckLabel(MODIFIED_LABEL,
				TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerLabelAtFormCategoryQuestionAnswerLevel() {
		printTestNameInDebugTrace("modifySubanswerLabelAtFormCategoryQuestionAnswerLevel");
		createNewFormAndCategory();
		addQuestionAnswerSubanswerModifyAndCheckLabel(MODIFIED_LABEL,
				TREE_TABLE_CATEGORY_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerLabelAtFormGroupQuestionAnswerLevel() {
		printTestNameInDebugTrace("modifySubanswerLabelAtFormGroupQuestionAnswerLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionAnswerSubanswerModifyAndCheckLabel(MODIFIED_LABEL,
				TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyTextTechnicalNameAtBlockCategoryLevel() {
		printTestNameInDebugTrace("modifyTextTechnicalNameAtBlockCategoryLevel");
		createNewBlockAndCategory();
		addTextModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyTextTechnicalNameAtBlockGroupLevel() {
		printTestNameInDebugTrace("modifyTextTechnicalNameAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addTextModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyTextTechnicalNameAtFormCategoryLevel() {
		printTestNameInDebugTrace("modifyTextTechnicalNameAtFormCategoryLevel");
		createNewFormAndCategory();
		addTextModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyTextTechnicalNameAtFormGroupLevel() {
		printTestNameInDebugTrace("modifyTextTechnicalNameAtFormGroupLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addTextModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void saveCategoryAsBlock() {
		printTestNameInDebugTrace("saveCategoryAsBlock");
		createNewFormAndCategoryWithAdminRights();
		getDesignerPage().clickBlocksButton();
		getDesignerPage().clickSaveBlockButton();
		getDesignerPage().getNewBlockWindow().createNewBlock(NEW_BLOCK_NAME);
		checkNotificationIsHumanized(getNotification());
		saveDesignAndRemoveForm();
		deleteBlock();
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
	 * Method to avoid repeating all the code that block a form design and
	 * removes it afterwards.<br>
	 */
	private void saveDesignAndRemoveForm() {
		getDesignerPage().saveDesign();
		logOut();
		deleteForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void saveGroupAsBlock() {
		printTestNameInDebugTrace("saveGroupAsBlock");
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
	public void saveQuestionAsBlock() {
		printTestNameInDebugTrace("saveQuestionAsBlock");
		createNewFormAndCategoryWithAdminRights();
		getDesignerPage().addNewQuestion();
		getDesignerPage().clickBlocksButton();
		getDesignerPage().clickSaveBlockButton();
		getDesignerPage().getNewBlockWindow().createNewBlock(NEW_BLOCK_NAME);
		checkNotificationIsHumanized(getNotification());
		saveDesignAndRemoveForm();
		deleteBlock();
	}
	
	@Test(groups = "formBlockDesign")
	public void validFormWithEverything() {
		try {
			printTestNameInDebugTrace("validFormWithEverything");
			loginFormAdmin1();
			getFormManagerPage().createNewForm(NEW_FORM_NAME);
			goToDesignerPage();
			getDesignerPage().createCompleteFormAndSave();
			goToFormManagerPage();
			getFormManagerPage().deleteForm(1);
			logOut();
		} catch (FieldNotEditableException e) {
			e.printStackTrace();
		}
	}
}
