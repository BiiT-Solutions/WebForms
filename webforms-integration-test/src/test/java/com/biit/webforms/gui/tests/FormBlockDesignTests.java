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

	private void addAnswerModifyAndCheckDescription(String modifiedDescription, Integer rowToCheck) throws FieldNotEditableException {
		getDesignerPage().addNewRadioButtonQuestion();
		getDesignerPage().addNewAnswer();
		getDesignerPage().getAnswerPropertiesView().setDescription(modifiedDescription);
		getDesignerPage().saveDesign();
		deselectSelecTableRow(rowToCheck);
		Assert.assertEquals(getDesignerPage().getAnswerPropertiesView().getDescription(), modifiedDescription);
	}

	private void addFieldModifyAndCheckTechnicalName(String modifiedName, Integer rowToCheck) throws FieldNotEditableException {
		getDesignerPage().addNewField();
		getDesignerPage().getFieldPropertiesView().setTechnicalName(modifiedName);
		getDesignerPage().saveDesign();
		deselectSelecTableRow(rowToCheck);
		Assert.assertEquals(getDesignerPage().getFieldPropertiesView().getTechnicalName(), modifiedName);
	}

	private void addQuestionAnswerModifyAndCheckLabel(String modifiedLabel, Integer rowToCheck) throws FieldNotEditableException {
		getDesignerPage().addNewRadioButtonQuestion();
		getDesignerPage().addNewAnswer();
		getDesignerPage().getAnswerPropertiesView().setLabel(modifiedLabel);
		getDesignerPage().saveDesign();
		deselectSelecTableRow(rowToCheck);
		Assert.assertEquals(getDesignerPage().getAnswerPropertiesView().getLabel(), modifiedLabel);
	}

	private void addQuestionAnswerSubanswerModifyAndCheckLabel(String modifiedLabel, Integer rowToCheck) throws FieldNotEditableException {
		getDesignerPage().addNewRadioButtonQuestion();
		getDesignerPage().addNewAnswer();
		getDesignerPage().addNewSubanswer();
		getDesignerPage().getSubanswerPropertiesView().setLabel(modifiedLabel);
		getDesignerPage().saveDesign();
		deselectSelecTableRow(rowToCheck);
		Assert.assertEquals(getDesignerPage().getSubanswerPropertiesView().getLabel(), modifiedLabel);
	}

	private void addQuestionModifyAndCheckDescription(String modifiedDescription, Integer rowToCheck) throws FieldNotEditableException {
		getDesignerPage().addNewQuestion();
		getDesignerPage().getQuestionPropertiesView().setDescription(modifiedDescription);
		getDesignerPage().saveDesign();
		deselectSelecTableRow(rowToCheck);
		Assert.assertEquals(getDesignerPage().getQuestionPropertiesView().getDescription(), modifiedDescription);
	}

	private void addQuestionModifyAndCheckHorizontal(Integer rowToCheck) throws FieldNotEditableException {
		getDesignerPage().addNewRadioButtonQuestion();
		String horizontalValue = getDesignerPage().getQuestionPropertiesView().getHorizontalCheckBoxValue();
		getDesignerPage().getQuestionPropertiesView().clickHorizontalCheckBox();
		getDesignerPage().saveDesign();
		deselectSelecTableRow(rowToCheck);
		Assert.assertNotEquals(getDesignerPage().getQuestionPropertiesView().getHorizontalCheckBoxValue(), horizontalValue);
	}

	private void addQuestionModifyAndCheckLabel(String modifiedLabel, Integer rowToCheck) throws FieldNotEditableException {
		getDesignerPage().addNewQuestion();
		getDesignerPage().getQuestionPropertiesView().setLabel(modifiedLabel);
		getDesignerPage().saveDesign();
		deselectSelecTableRow(rowToCheck);
		Assert.assertEquals(getDesignerPage().getQuestionPropertiesView().getLabel(), modifiedLabel);
	}

	private void addQuestionModifyAndCheckMandatory(Integer rowToCheck) throws FieldNotEditableException {
		getDesignerPage().addNewQuestion();
		String mandatoryValue = getDesignerPage().getQuestionPropertiesView().getMandatoryCheckBoxValue();
		getDesignerPage().getQuestionPropertiesView().clickMandatoryCheckBox();
		getDesignerPage().saveDesign();
		deselectSelecTableRow(rowToCheck);
		Assert.assertNotEquals(getDesignerPage().getQuestionPropertiesView().getMandatoryCheckBoxValue(), mandatoryValue);
	}

	private void addQuestionModifyAndCheckTechnicalName(String modifiedName, Integer rowToCheck) throws FieldNotEditableException {
		getDesignerPage().addNewQuestion();
		getDesignerPage().getQuestionPropertiesView().setTechnicalName(modifiedName);
		getDesignerPage().saveDesign();
		deselectSelecTableRow(rowToCheck);
		Assert.assertEquals(getDesignerPage().getQuestionPropertiesView().getTechnicalName(), modifiedName);
	}

	private void addSubAnswerModifyAndCheckDescription(String modifiedDescription, Integer rowToCheck) throws FieldNotEditableException {
		getDesignerPage().addNewRadioButtonQuestion();
		getDesignerPage().addNewAnswer();
		getDesignerPage().addNewSubanswer();
		getDesignerPage().getSubanswerPropertiesView().setDescription(modifiedDescription);
		getDesignerPage().saveDesign();
		deselectSelecTableRow(rowToCheck);
		Assert.assertEquals(getDesignerPage().getSubanswerPropertiesView().getDescription(), modifiedDescription);
	}

	private void addTextModifyAndCheckTechnicalName(String modifiedName, Integer rowToCheck) throws FieldNotEditableException {
		getDesignerPage().addNewText();
		getDesignerPage().getTextPropertiesView().setTechnicalName(modifiedName);
		getDesignerPage().saveDesign();
		deselectSelecTableRow(rowToCheck);
		Assert.assertEquals(getDesignerPage().getTextPropertiesView().getTechnicalName(), modifiedName);
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
		takeScreenshot("screnshotBeforeLogOut");
		logOut();
		deleteBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtBlockCategoryGroupQuestionInput() throws FieldNotEditableException {
		printTestNameInDebugTrace("checkAnswerSubanswerAtBlockCategoryGroupQuestionInput");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewInputQuestion();
		Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
		getDesignerPage().closeOthersButton();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtBlockCategoryLevel() {
		printTestNameInDebugTrace("checkAnswerSubanswerAtBlockCategoryLevel");
		createNewBlockAndCategory();
		Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
		getDesignerPage().closeOthersButton();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtBlockCategoryQuestionInput() throws FieldNotEditableException {
		printTestNameInDebugTrace("checkAnswerSubanswerAtBlockCategoryQuestionInput");
		createNewBlockAndCategory();
		getDesignerPage().addNewInputQuestion();
		Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
		getDesignerPage().closeOthersButton();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtBlockGroupLevel() {
		printTestNameInDebugTrace("checkAnswerSubanswerAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
		getDesignerPage().closeOthersButton();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtFormCategoryGroupQuestionInput() throws FieldNotEditableException {
		printTestNameInDebugTrace("checkAnswerSubanswerAtFormCategoryGroupQuestionInput");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewInputQuestion();
		Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
		getDesignerPage().closeOthersButton();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtFormCategoryLevel() {
		printTestNameInDebugTrace("checkAnswerSubanswerAtFormCategoryLevel");
		createNewFormAndCategory();
		Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
		getDesignerPage().closeOthersButton();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtFormCategoryQuestionInput() throws FieldNotEditableException {
		printTestNameInDebugTrace("checkAnswerSubanswerAtFormCategoryQuestionInput");
		createNewFormAndCategory();
		getDesignerPage().addNewInputQuestion();
		Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
		getDesignerPage().closeOthersButton();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkAnswerSubanswerAtFormGroupLevel() {
		printTestNameInDebugTrace("checkAnswerSubanswerAtFormGroupLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
		Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
		getDesignerPage().closeOthersButton();
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
		Assert.assertFalse(getDesignerPage().getOthersButton().isEnabled());

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
		Assert.assertFalse(getDesignerPage().getOthersButton().isEnabled());

		logOut();
		deleteForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void checkQuestionHorizontalIsOnlyActivatedForRadioButtonQuestions() throws FieldNotEditableException {
		printTestNameInDebugTrace("checkQuestionHorizontalIsOnlyActivatedForRadioButtonQuestions");
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
	}

	@Test(groups = { "formBlockDesign" })
	public void checkQuestionMandatoryIsActivatedForAllQuestions() throws FieldNotEditableException {
		printTestNameInDebugTrace("checkQuestionMandatoryIsActivatedForAllQuestions");
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
	}

	@Test(groups = { "formBlockDesign" })
	public void createAnswerSubanswerAtBlockCategoryGroupQuestionRadioButton() throws FieldNotEditableException {
		printTestNameInDebugTrace("createAnswerSubanswerAtBlockCategoryGroupQuestionRadioButton");
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
	}

	@Test(groups = { "formBlockDesign" })
	public void createAnswerSubanswerAtBlockCategoryQuestionRadioButton() throws FieldNotEditableException {
		printTestNameInDebugTrace("createAnswerSubanswerAtBlockCategoryQuestionRadioButton");
		createNewBlockAndCategory();
		getDesignerPage().addNewRadioButtonQuestion();
		getDesignerPage().addNewAnswer();
		getDesignerPage().addNewSubanswer();
		getDesignerPage().addNewSubanswer();
		getDesignerPage().addNewAnswer();
		getDesignerPage().addNewSubanswer();
		getDesignerPage().addNewSubanswer();
		saveDesignAndRemoveBlock();
	}

	private void createEmptyFormAndSimpleBlockDesign() throws FieldNotEditableException {
		// Create empty form
		createNewFormWithAdminRights(NEW_FORM_NAME);
		goToBlockManagerPage();
		// Create block with design
		getBlockManagerPage().createNewBlock(NEW_BLOCK_NAME);
		goToDesignerPage();
		getDesignerPage().createAndSaveSimpleBlockDesign();
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

	@Test(groups = { "formBlockDesign" })
	public void createQuestionAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createQuestionAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateDateAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputDateDateAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewInputDateSubformatDateQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateDateAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputDateDateAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewInputDateSubformatDateQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateFutureAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputDateFutureAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewInputDateSubformatFutureQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDateFutureAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputDateFutureAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewInputDateSubformatFutureQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDatePastAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputDatePastAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewInputDateSubformatPastQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputDatePastAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputDatePastAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewInputDateSubformatPastQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberFloatAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputNumberFloatAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewInputNumberSubformatFloatQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberFloatAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputNumberFloatAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewInputNumberSubformatFloatQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberNumberAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputNumberNumberAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewInputNumberSubformatNumberQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputNumberNumberAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputNumberNumberAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewInputNumberSubformatNumberQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputPostalCodePostalCodeAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputPostalCodePostalCodeAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewInputPostalcodeSubformatPostalcodeQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputPostalCodePostalCodeAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputPostalCodePostalCodeAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewInputPostalcodeSubformatPostalcodeQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextBsnAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputTextBsnAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewInputTextSubformatBsnQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextBsnAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputTextBsnAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewInputTextSubformatBsnQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextEmailAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputTextEmailAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewInputTextSubformatEmailQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextEmailAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputTextEmailAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewInputTextSubformatEmailQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextIbanAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputTextIbanAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewInputTextSubformatIbanQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextIbanAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputTextIbanAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewInputTextSubformatIbanQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextPhoneAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputTextPhoneAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewInputTextSubformatPhoneQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextPhoneAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputTextPhoneAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewInputTextSubformatPhoneQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextTextAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputTextTextAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewInputTextSubformatTextQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionInputTextTextAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionInputTextTextAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewInputTextSubformatTextQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionListAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionListAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewListQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionListAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionListAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewListQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionListAtFormGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionListAtFormGroupLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewListQuestion();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionMultiCheckBoxAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionMultiCheckBoxAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewMultiCheckboxQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionMultiCheckBoxAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionMultiCheckBoxAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewMultiCheckboxQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionRadioButtonAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionRadioButtonAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewRadioButtonQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionRadioButtonAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionRadioButtonAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewRadioButtonQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionTextAreaAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionTextAreaAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewTextAreaQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createQuestionTextAreaAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("createQuestionTextAreaAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewTextAreaQuestion();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void createTextAtBlockCategoryLevel() {
		printTestNameInDebugTrace("createTextAtBlockCategoryLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewText();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteAnswerAtBlockCategoryGroupQuestionRadioButton() throws FieldNotEditableException {
		printTestNameInDebugTrace("deleteAnswerAtBlockCategoryGroupQuestionRadioButton");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewRadioButtonQuestion();
		getDesignerPage().addNewAnswer();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteAnswerAtBlockCategoryQuestionRadioButton() throws FieldNotEditableException {
		printTestNameInDebugTrace("deleteAnswerAtBlockCategoryQuestionRadioButton");
		createNewBlockAndCategory();
		getDesignerPage().addNewRadioButtonQuestion();
		getDesignerPage().addNewAnswer();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteAnswerAtFormCategoryGroupQuestionRadioButton() throws FieldNotEditableException {
		printTestNameInDebugTrace("deleteAnswerAtFormCategoryGroupQuestionRadioButton");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewRadioButtonQuestion();
		getDesignerPage().addNewAnswer();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteAnswerAtFormCategoryQuestionRadioButton() throws FieldNotEditableException {
		printTestNameInDebugTrace("deleteAnswerAtFormCategoryQuestionRadioButton");
		createNewFormAndCategory();
		getDesignerPage().addNewRadioButtonQuestion();
		getDesignerPage().addNewAnswer();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveForm();
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
	public void deleteSubanswerAtBlockCategoryGroupQuestionRadioButton() throws FieldNotEditableException {
		printTestNameInDebugTrace("deleteSubanswerAtBlockCategoryGroupQuestionRadioButton");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewRadioButtonQuestion();
		getDesignerPage().addNewAnswer();
		getDesignerPage().addNewSubanswer();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteSubanswerAtBlockCategoryQuestionRadioButton() throws FieldNotEditableException {
		printTestNameInDebugTrace("deleteSubanswerAtBlockCategoryQuestionRadioButton");
		createNewBlockAndCategory();
		getDesignerPage().addNewRadioButtonQuestion();
		getDesignerPage().addNewAnswer();
		getDesignerPage().addNewSubanswer();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteSubanswerAtFormCategoryGroupQuestionRadioButton() throws FieldNotEditableException {
		printTestNameInDebugTrace("deleteSubanswerAtFormCategoryGroupQuestionRadioButton");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().addNewRadioButtonQuestion();
		getDesignerPage().addNewAnswer();
		getDesignerPage().addNewSubanswer();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void deleteSubanswerAtFormCategoryQuestionRadioButton() throws FieldNotEditableException {
		printTestNameInDebugTrace("deleteSubanswerAtFormCategoryQuestionRadioButton");
		createNewFormAndCategory();
		getDesignerPage().addNewRadioButtonQuestion();
		getDesignerPage().addNewAnswer();
		getDesignerPage().addNewSubanswer();
		getDesignerPage().clickDeleteButton();
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void importBlock() throws FieldNotEditableException {
		printTestNameInDebugTrace("importBlock");
		createEmptyFormAndSimpleBlockDesign();
		goToFormManagerPage();
		goToDesignerPage();
		getDesignerPage().clickBlocksButton();
		getDesignerPage().clickInsertBlockButton();
		clickAcceptButtonIfExists();

		takeScreenshot("screenshotBeforewtf");

		getDesignerPage().saveDesign();

		logOut();
		deleteForm();
		deleteBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerDescriptionAtBlockCategoryQuestionLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyAnswerDescriptionAtBlockCategoryQuestionLevel");
		createNewBlockAndCategory();
		addAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerDescriptionAtBlockGroupQuestionLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyAnswerDescriptionAtBlockGroupQuestionLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerDescriptionAtFormCategoryQuestionLevel() throws FieldNotEditableException {
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
	public void modifyAnswerLabelAtBlockCategoryQuestionLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyAnswerLabelAtBlockCategoryQuestionLevel");
		createNewBlockAndCategory();
		addQuestionAnswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerLabelAtBlockGroupQuestionLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyAnswerLabelAtBlockGroupQuestionLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionAnswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerLabelAtFormCategoryQuestionLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyAnswerLabelAtFormCategoryQuestionLevel");
		createNewFormAndCategory();
		addQuestionAnswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyAnswerLabelAtFormGroupQuestionLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyAnswerLabelAtFormGroupQuestionLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionAnswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyBlockCategoryLabel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyBlockCategoryLabel");
		createNewBlockAndCategory();
		getDesignerPage().getCategoryPropertiesView().setLabel(MODIFIED_LABEL);
		getDesignerPage().saveDesign();
		deselectSelecTableRow(TREE_TABLE_CATEGORY_ROW_INDEX);
		Assert.assertEquals(getDesignerPage().getCategoryPropertiesView().getLabel(), MODIFIED_LABEL);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyBlockCategoryTechnicalName() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyBlockCategoryTechnicalName");
		createNewBlockAndCategory();
		getDesignerPage().getCategoryPropertiesView().setTechnicalName(MODIFIED_NAME);
		getDesignerPage().saveDesign();
		deselectSelecTableRow(TREE_TABLE_CATEGORY_ROW_INDEX);
		Assert.assertEquals(getDesignerPage().getCategoryPropertiesView().getTechnicalName(), MODIFIED_NAME);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyBlockGroupLabel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyBlockGroupLabel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().getGroupPropertiesView().setLabel(MODIFIED_LABEL);
		getDesignerPage().saveDesign();
		deselectSelecTableRow(TREE_TABLE_CATEGORY_GROUP_ROW_INDEX);
		Assert.assertEquals(getDesignerPage().getGroupPropertiesView().getLabel(), MODIFIED_LABEL);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyBlockGroupTechnicalName() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyBlockGroupTechnicalName");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().getGroupPropertiesView().setTechnicalName(MODIFIED_NAME);
		getDesignerPage().saveDesign();
		deselectSelecTableRow(TREE_TABLE_CATEGORY_GROUP_ROW_INDEX);
		Assert.assertEquals(getDesignerPage().getGroupPropertiesView().getTechnicalName(), MODIFIED_NAME);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFieldTechnicalNameAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyFieldTechnicalNameAtBlockCategoryLevel");
		createNewBlockAndCategory();
		addFieldModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFieldTechnicalNameAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyFieldTechnicalNameAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addFieldModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFieldTechnicalNameAtFormCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyFieldTechnicalNameAtFormCategoryLevel");
		createNewFormAndCategory();
		addFieldModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFieldTechnicalNameAtFormGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyFieldTechnicalNameAtFormGroupLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addFieldModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFormCategoryLabel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyFormCategoryLabel");
		createNewFormAndCategory();
		getDesignerPage().getCategoryPropertiesView().setLabel(MODIFIED_LABEL);
		getDesignerPage().saveDesign();
		deselectSelecTableRow(TREE_TABLE_CATEGORY_ROW_INDEX);
		Assert.assertEquals(getDesignerPage().getCategoryPropertiesView().getLabel(), MODIFIED_LABEL);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFormCategoryTechnicalName() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyFormCategoryTechnicalName");
		createNewFormAndCategory();
		getDesignerPage().getCategoryPropertiesView().setTechnicalName(MODIFIED_NAME);
		getDesignerPage().saveDesign();
		deselectSelecTableRow(TREE_TABLE_CATEGORY_ROW_INDEX);
		Assert.assertEquals(getDesignerPage().getCategoryPropertiesView().getTechnicalName(), MODIFIED_NAME);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFormDescription() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyFormDescription");
		createNewEmptyForm(NEW_FORM_NAME);
		goToDesignerPage();
		getDesignerPage().getFormPropertiesView().setDescription(MODIFIED_DESCRIPTION);
		getDesignerPage().saveDesign();
		goToFormManagerPage();
		goToDesignerPage();
		Assert.assertEquals(getDesignerPage().getFormPropertiesView().getDescription(), MODIFIED_DESCRIPTION);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFormGroupLabel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyFormGroupLabel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().getGroupPropertiesView().setLabel(MODIFIED_LABEL);
		getDesignerPage().saveDesign();
		deselectSelecTableRow(TREE_TABLE_CATEGORY_GROUP_ROW_INDEX);
		Assert.assertEquals(getDesignerPage().getGroupPropertiesView().getLabel(), MODIFIED_LABEL);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFormGroupTechnicalName() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyFormGroupTechnicalName");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		getDesignerPage().getGroupPropertiesView().setTechnicalName(MODIFIED_NAME);
		getDesignerPage().saveDesign();
		deselectSelecTableRow(TREE_TABLE_CATEGORY_GROUP_ROW_INDEX);
		Assert.assertEquals(getDesignerPage().getGroupPropertiesView().getTechnicalName(), MODIFIED_NAME);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyFormName() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyFormName");
		createNewEmptyForm(NEW_FORM_NAME);
		goToDesignerPage();
		getDesignerPage().getFormPropertiesView().setName(MODIFIED_NAME);
		getDesignerPage().saveDesign();
		goToFormManagerPage();
		goToDesignerPage();
		Assert.assertEquals(getDesignerPage().getFormPropertiesView().getName(), MODIFIED_NAME);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionDescriptionAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyQuestionDescriptionAtBlockCategoryLevel");
		createNewBlockAndCategory();
		addQuestionModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionDescriptionAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyQuestionDescriptionAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionDescriptionAtFormCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyQuestionDescriptionAtFormCategoryLevel");
		createNewFormAndCategory();
		addQuestionModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionDescriptionAtFormGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyQuestionDescriptionAtFormGroupLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionHorizontalAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyQuestionHorizontalAtBlockCategoryLevel");
		createNewBlockAndCategory();
		addQuestionModifyAndCheckHorizontal(TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionHorizontalAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyQuestionHorizontalAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckHorizontal(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionHorizontalAtFormCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyQuestionHorizontalAtFormCategoryLevel");
		createNewFormAndCategory();
		addQuestionModifyAndCheckHorizontal(TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionHorizontalAtFormGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyQuestionHorizontalAtFormGroupLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckHorizontal(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionLabelAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyQuestionLabelAtBlockCategoryLevel");
		createNewBlockAndCategory();
		addQuestionModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionLabelAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyQuestionLabelAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionLabelAtFormCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyQuestionLabelAtFormCategoryLevel");
		createNewFormAndCategory();
		addQuestionModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionLabelAtFormGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyQuestionLabelAtFormGroupLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionMandatoryAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyQuestionMandatoryAtBlockCategoryLevel");
		createNewBlockAndCategory();
		addQuestionModifyAndCheckMandatory(TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionMandatoryAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyQuestionMandatoryAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckMandatory(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionMandatoryAtFormCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyQuestionMandatoryAtFormCategoryLevel");
		createNewFormAndCategory();
		addQuestionModifyAndCheckMandatory(TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionMandatoryAtFormGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyQuestionMandatoryAtFormGroupLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckMandatory(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionTechnicalNameAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyQuestionTechnicalNameAtBlockCategoryLevel");
		createNewBlockAndCategory();
		addQuestionModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionTechnicalNameAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyQuestionTechnicalNameAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionTechnicalNameAtFormCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyQuestionTechnicalNameAtFormCategoryLevel");
		createNewFormAndCategory();
		addQuestionModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyQuestionTechnicalNameAtFormGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyQuestionTechnicalNameAtFormGroupLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerDescriptionAtBlockCategoryQuestionAnswerLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifySubanswerDescriptionAtBlockCategoryQuestionAnswerLevel");
		createNewBlockAndCategory();
		addSubAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerDescriptionAtBlockGroupQuestionAnswerLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifySubanswerDescriptionAtBlockGroupQuestionAnswerLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addSubAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerDescriptionAtFormCategoryQuestionAnswerLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifySubanswerDescriptionAtFormCategoryQuestionAnswerLevel");
		createNewFormAndCategory();
		addSubAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerDescriptionAtFormGroupQuestionAnswerLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifySubanswerDescriptionAtFormGroupQuestionAnswerLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addSubAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerLabelAtBlockCategoryQuestionAnswerLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifySubanswerLabelAtBlockCategoryQuestionAnswerLevel");
		createNewBlockAndCategory();
		addQuestionAnswerSubanswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerLabelAtBlockGroupQuestionAnswerLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifySubanswerLabelAtBlockGroupQuestionAnswerLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionAnswerSubanswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerLabelAtFormCategoryQuestionAnswerLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifySubanswerLabelAtFormCategoryQuestionAnswerLevel");
		createNewFormAndCategory();
		addQuestionAnswerSubanswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifySubanswerLabelAtFormGroupQuestionAnswerLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifySubanswerLabelAtFormGroupQuestionAnswerLevel");
		createNewFormAndCategory();
		getDesignerPage().addNewGroup();
		addQuestionAnswerSubanswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyTextTechnicalNameAtBlockCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyTextTechnicalNameAtBlockCategoryLevel");
		createNewBlockAndCategory();
		addTextModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyTextTechnicalNameAtBlockGroupLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyTextTechnicalNameAtBlockGroupLevel");
		createNewBlockAndCategory();
		getDesignerPage().addNewGroup();
		addTextModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
		saveDesignAndRemoveBlock();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyTextTechnicalNameAtFormCategoryLevel() throws FieldNotEditableException {
		printTestNameInDebugTrace("modifyTextTechnicalNameAtFormCategoryLevel");
		createNewFormAndCategory();
		addTextModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
		saveDesignAndRemoveForm();
	}

	@Test(groups = { "formBlockDesign" })
	public void modifyTextTechnicalNameAtFormGroupLevel() throws FieldNotEditableException {
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
	public void validFormWithEverything() throws FieldNotEditableException {
		printTestNameInDebugTrace("validFormWithEverything");
		loginFormAdmin1();
		getFormManagerPage().createNewForm(NEW_FORM_NAME);
		goToDesignerPage();
		getDesignerPage().createCompleteFormAndSave();
		goToFormManagerPage();
		getFormManagerPage().deleteForm(1);
		logOut();
	}
}
