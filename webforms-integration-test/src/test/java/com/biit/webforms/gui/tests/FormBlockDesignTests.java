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

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputSliderAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewSliderQuestion(TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputSliderAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewSliderQuestion(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void checkAnswerSubanswerAtBlockCategoryGroupQuestionInput() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewInputQuestion();
        Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
        Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
        getDesignerPage().closeOthersButton();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void checkAnswerSubanswerAtBlockCategoryLevel() {
        createNewBlockAndCategory();
        Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
        Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
        getDesignerPage().closeOthersButton();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void checkAnswerSubanswerAtBlockCategoryQuestionInput() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewInputQuestion();
        Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
        Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
        getDesignerPage().closeOthersButton();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void checkAnswerSubanswerAtBlockGroupLevel() {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
        Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
        getDesignerPage().closeOthersButton();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void checkAnswerSubanswerAtFormCategoryGroupQuestionInput() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewInputQuestion();
        Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
        Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
        getDesignerPage().closeOthersButton();
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void checkAnswerSubanswerAtFormCategoryLevel() {
        createNewFormAndCategory();
        Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
        Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
        getDesignerPage().closeOthersButton();
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void checkAnswerSubanswerAtFormCategoryQuestionInput() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().addNewInputQuestion();
        Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
        Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
        getDesignerPage().closeOthersButton();
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void checkAnswerSubanswerAtFormGroupLevel() {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
        Assert.assertFalse(getDesignerPage().getSubanswerButton().isEnabled());
        getDesignerPage().closeOthersButton();
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void checkGroupQuestionAnswerSubanswerAtBlockLevel() {
        createNewBlock(NEW_BLOCK_NAME);
        goToDesignerPage();
        Assert.assertFalse(getDesignerPage().getGroupButton().isEnabled());
        Assert.assertFalse(getDesignerPage().getQuestionButton().isEnabled());
        Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
        Assert.assertFalse(getDesignerPage().getOthersButton().isEnabled());

        logOut();
        deleteBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void checkGroupQuestionAnswerSubanswerAtFormLevel() {
        createNewEmptyForm(NEW_FORM_NAME);
        goToDesignerPage();
        Assert.assertFalse(getDesignerPage().getGroupButton().isEnabled());
        Assert.assertFalse(getDesignerPage().getQuestionButton().isEnabled());
        Assert.assertFalse(getDesignerPage().getAnswerButton().isEnabled());
        Assert.assertFalse(getDesignerPage().getOthersButton().isEnabled());

        logOut();
        deleteForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void checkQuestionHorizontalIsOnlyActivatedForRadioButtonQuestions() throws FieldNotEditableException {
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

    @Test(groups = {"formBlockDesign"})
    public void checkQuestionMandatoryIsActivatedForAllQuestions() throws FieldNotEditableException {
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

    @Test(groups = {"formBlockDesign"})
    public void createAnswerSubanswerAtBlockCategoryGroupQuestionRadioButton() throws FieldNotEditableException {
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

    @Test(groups = {"formBlockDesign"})
    public void createAnswerSubanswerAtBlockCategoryQuestionRadioButton() throws FieldNotEditableException {
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

    @Test(groups = {"formBlockDesign"})
    public void createFieldAtBlockCategoryLevel() {
        createNewBlockAndCategory();
        getDesignerPage().addNewField();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createGroupAtBlockCategoryLevel() {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createGroupAtFormCategoryLevel() {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionAtBlockCategoryLevel() {
        createNewBlockAndCategory();
        getDesignerPage().addNewQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputDateDateAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewInputDateSubformatDateQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputDateDateAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewInputDateSubformatDateQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputDateFutureAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewInputDateSubformatFutureQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputDateFutureAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewInputDateSubformatFutureQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputDatePastAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewInputDateSubformatPastQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputDatePastAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewInputDateSubformatPastQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputNumberFloatAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewInputNumberSubformatFloatQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputNumberFloatAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewInputNumberSubformatFloatQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputNumberNumberAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewInputNumberSubformatNumberQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputNumberNumberAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewInputNumberSubformatNumberQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputPostalCodePostalCodeAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewInputPostalcodeSubformatPostalcodeQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputPostalCodePostalCodeAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewInputPostalcodeSubformatPostalcodeQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputTextBsnAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewInputTextSubformatBsnQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputTextBsnAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewInputTextSubformatBsnQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputTextEmailAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewInputTextSubformatEmailQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputTextEmailAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewInputTextSubformatEmailQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputTextIbanAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewInputTextSubformatIbanQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputTextIbanAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewInputTextSubformatIbanQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputTextPhoneAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewInputTextSubformatPhoneQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputTextPhoneAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewInputTextSubformatPhoneQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputTextTextAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewInputTextSubformatTextQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionInputTextTextAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewInputTextSubformatTextQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionListAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewListQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionListAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewListQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionListAtFormGroupLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewListQuestion();
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionMultiCheckBoxAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewMultiCheckboxQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionMultiCheckBoxAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewMultiCheckboxQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionRadioButtonAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewRadioButtonQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionRadioButtonAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewRadioButtonQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionTextAreaAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewTextAreaQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createQuestionTextAreaAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewTextAreaQuestion();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void createTextAtBlockCategoryLevel() {
        createNewBlockAndCategory();
        getDesignerPage().addNewText();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void deleteAnswerAtBlockCategoryGroupQuestionRadioButton() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewRadioButtonQuestion();
        getDesignerPage().addNewAnswer();
        getDesignerPage().clickDeleteButton();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void deleteAnswerAtBlockCategoryQuestionRadioButton() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewRadioButtonQuestion();
        getDesignerPage().addNewAnswer();
        getDesignerPage().clickDeleteButton();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void deleteAnswerAtFormCategoryGroupQuestionRadioButton() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewRadioButtonQuestion();
        getDesignerPage().addNewAnswer();
        getDesignerPage().clickDeleteButton();
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void deleteAnswerAtFormCategoryQuestionRadioButton() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().addNewRadioButtonQuestion();
        getDesignerPage().addNewAnswer();
        getDesignerPage().clickDeleteButton();
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void deleteBlockCategory() {
        createNewBlockAndCategory();
        getDesignerPage().clickDeleteButton();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void deleteFormCategory() {
        createNewFormAndCategory();
        getDesignerPage().clickDeleteButton();
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void deleteGroupAtBlockCategory() {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().clickDeleteButton();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void deleteGroupAtFormCategory() {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().clickDeleteButton();
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void deleteQuestionAtBlockCategory() {
        createNewBlockAndCategory();
        getDesignerPage().addNewQuestion();
        getDesignerPage().clickDeleteButton();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void deleteQuestionAtBlockCategoryGroup() {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewQuestion();
        getDesignerPage().clickDeleteButton();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void deleteQuestionAtFormCategory() {
        createNewFormAndCategory();
        getDesignerPage().addNewQuestion();
        getDesignerPage().clickDeleteButton();
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void deleteQuestionAtFormCategoryGroup() {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewQuestion();
        getDesignerPage().clickDeleteButton();
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void deleteSubanswerAtBlockCategoryGroupQuestionRadioButton() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewRadioButtonQuestion();
        getDesignerPage().addNewAnswer();
        getDesignerPage().addNewSubanswer();
        getDesignerPage().clickDeleteButton();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void deleteSubanswerAtBlockCategoryQuestionRadioButton() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewRadioButtonQuestion();
        getDesignerPage().addNewAnswer();
        getDesignerPage().addNewSubanswer();
        getDesignerPage().clickDeleteButton();
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void deleteSubanswerAtFormCategoryGroupQuestionRadioButton() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().addNewRadioButtonQuestion();
        getDesignerPage().addNewAnswer();
        getDesignerPage().addNewSubanswer();
        getDesignerPage().clickDeleteButton();
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void deleteSubanswerAtFormCategoryQuestionRadioButton() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().addNewRadioButtonQuestion();
        getDesignerPage().addNewAnswer();
        getDesignerPage().addNewSubanswer();
        getDesignerPage().clickDeleteButton();
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void importBlock() throws FieldNotEditableException {
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

    @Test(groups = {"formBlockDesign"})
    public void modifyAnswerDescriptionAtBlockCategoryQuestionLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        addAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_QUESTION_ANSWER_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyAnswerDescriptionAtBlockGroupQuestionLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        addAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyAnswerDescriptionAtFormCategoryQuestionLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        addAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_QUESTION_ANSWER_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyAnswerDescriptionAtFormGroupQuestionLevel() {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyAnswerLabelAtBlockCategoryQuestionLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        addQuestionAnswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_QUESTION_ANSWER_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyAnswerLabelAtBlockGroupQuestionLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        addQuestionAnswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyAnswerLabelAtFormCategoryQuestionLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        addQuestionAnswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_QUESTION_ANSWER_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyAnswerLabelAtFormGroupQuestionLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        addQuestionAnswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyBlockCategoryLabel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().getCategoryPropertiesView().setLabel(MODIFIED_LABEL);
        getDesignerPage().saveDesign();
        deselectSelecTableRow(TREE_TABLE_CATEGORY_ROW_INDEX);
        Assert.assertEquals(getDesignerPage().getCategoryPropertiesView().getLabel(), MODIFIED_LABEL);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyBlockCategoryTechnicalName() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().getCategoryPropertiesView().setTechnicalName(MODIFIED_NAME);
        getDesignerPage().saveDesign();
        deselectSelecTableRow(TREE_TABLE_CATEGORY_ROW_INDEX);
        Assert.assertEquals(getDesignerPage().getCategoryPropertiesView().getTechnicalName(), MODIFIED_NAME);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyBlockGroupLabel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().getGroupPropertiesView().setLabel(MODIFIED_LABEL);
        getDesignerPage().saveDesign();
        deselectSelecTableRow(TREE_TABLE_CATEGORY_GROUP_ROW_INDEX);
        Assert.assertEquals(getDesignerPage().getGroupPropertiesView().getLabel(), MODIFIED_LABEL);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyBlockGroupTechnicalName() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().getGroupPropertiesView().setTechnicalName(MODIFIED_NAME);
        getDesignerPage().saveDesign();
        deselectSelecTableRow(TREE_TABLE_CATEGORY_GROUP_ROW_INDEX);
        Assert.assertEquals(getDesignerPage().getGroupPropertiesView().getTechnicalName(), MODIFIED_NAME);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyFieldTechnicalNameAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        addFieldModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyFieldTechnicalNameAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        addFieldModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyFieldTechnicalNameAtFormCategoryLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        addFieldModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyFieldTechnicalNameAtFormGroupLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        addFieldModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyFormCategoryLabel() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().getCategoryPropertiesView().setLabel(MODIFIED_LABEL);
        getDesignerPage().saveDesign();
        deselectSelecTableRow(TREE_TABLE_CATEGORY_ROW_INDEX);
        Assert.assertEquals(getDesignerPage().getCategoryPropertiesView().getLabel(), MODIFIED_LABEL);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyFormCategoryTechnicalName() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().getCategoryPropertiesView().setTechnicalName(MODIFIED_NAME);
        getDesignerPage().saveDesign();
        deselectSelecTableRow(TREE_TABLE_CATEGORY_ROW_INDEX);
        Assert.assertEquals(getDesignerPage().getCategoryPropertiesView().getTechnicalName(), MODIFIED_NAME);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyFormDescription() throws FieldNotEditableException {
        createNewEmptyForm(NEW_FORM_NAME);
        goToDesignerPage();
        getDesignerPage().getFormPropertiesView().setDescription(MODIFIED_DESCRIPTION);
        getDesignerPage().saveDesign();
        goToFormManagerPage();
        goToDesignerPage();
        Assert.assertEquals(getDesignerPage().getFormPropertiesView().getDescription(), MODIFIED_DESCRIPTION);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyFormGroupLabel() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().getGroupPropertiesView().setLabel(MODIFIED_LABEL);
        getDesignerPage().saveDesign();
        deselectSelecTableRow(TREE_TABLE_CATEGORY_GROUP_ROW_INDEX);
        Assert.assertEquals(getDesignerPage().getGroupPropertiesView().getLabel(), MODIFIED_LABEL);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyFormGroupTechnicalName() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        getDesignerPage().getGroupPropertiesView().setTechnicalName(MODIFIED_NAME);
        getDesignerPage().saveDesign();
        deselectSelecTableRow(TREE_TABLE_CATEGORY_GROUP_ROW_INDEX);
        Assert.assertEquals(getDesignerPage().getGroupPropertiesView().getTechnicalName(), MODIFIED_NAME);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyFormName() throws FieldNotEditableException {
        createNewEmptyForm(NEW_FORM_NAME);
        goToDesignerPage();
        getDesignerPage().getFormPropertiesView().setName(MODIFIED_NAME);
        getDesignerPage().saveDesign();
        goToFormManagerPage();
        goToDesignerPage();
        Assert.assertEquals(getDesignerPage().getFormPropertiesView().getName(), MODIFIED_NAME);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyQuestionDescriptionAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        addQuestionModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyQuestionDescriptionAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        addQuestionModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyQuestionDescriptionAtFormCategoryLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        addQuestionModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyQuestionDescriptionAtFormGroupLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        addQuestionModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyQuestionHorizontalAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        addQuestionModifyAndCheckHorizontal(TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyQuestionHorizontalAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        addQuestionModifyAndCheckHorizontal(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyQuestionHorizontalAtFormCategoryLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        addQuestionModifyAndCheckHorizontal(TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyQuestionHorizontalAtFormGroupLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        addQuestionModifyAndCheckHorizontal(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyQuestionLabelAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        addQuestionModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyQuestionLabelAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        addQuestionModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyQuestionLabelAtFormCategoryLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        addQuestionModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyQuestionLabelAtFormGroupLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        addQuestionModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyQuestionMandatoryAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        addQuestionModifyAndCheckMandatory(TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyQuestionMandatoryAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        addQuestionModifyAndCheckMandatory(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyQuestionMandatoryAtFormCategoryLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        addQuestionModifyAndCheckMandatory(TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyQuestionMandatoryAtFormGroupLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        addQuestionModifyAndCheckMandatory(TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyQuestionTechnicalNameAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        addQuestionModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyQuestionTechnicalNameAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        addQuestionModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyQuestionTechnicalNameAtFormCategoryLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        addQuestionModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyQuestionTechnicalNameAtFormGroupLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        addQuestionModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifySubanswerDescriptionAtBlockCategoryQuestionAnswerLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        addSubAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifySubanswerDescriptionAtBlockGroupQuestionAnswerLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        addSubAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifySubanswerDescriptionAtFormCategoryQuestionAnswerLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        addSubAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifySubanswerDescriptionAtFormGroupQuestionAnswerLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        addSubAnswerModifyAndCheckDescription(MODIFIED_DESCRIPTION, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifySubanswerLabelAtBlockCategoryQuestionAnswerLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        addQuestionAnswerSubanswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifySubanswerLabelAtBlockGroupQuestionAnswerLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        addQuestionAnswerSubanswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifySubanswerLabelAtFormCategoryQuestionAnswerLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        addQuestionAnswerSubanswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifySubanswerLabelAtFormGroupQuestionAnswerLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        addQuestionAnswerSubanswerModifyAndCheckLabel(MODIFIED_LABEL, TREE_TABLE_CATEGORY_GROUP_QUESTION_ANSWER_SUBANSWER_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyTextTechnicalNameAtBlockCategoryLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        addTextModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyTextTechnicalNameAtBlockGroupLevel() throws FieldNotEditableException {
        createNewBlockAndCategory();
        getDesignerPage().addNewGroup();
        addTextModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
        saveDesignAndRemoveBlock();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyTextTechnicalNameAtFormCategoryLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        addTextModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_QUESTION_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void modifyTextTechnicalNameAtFormGroupLevel() throws FieldNotEditableException {
        createNewFormAndCategory();
        getDesignerPage().addNewGroup();
        addTextModifyAndCheckTechnicalName(MODIFIED_NAME, TREE_TABLE_CATEGORY_GROUP_QUESTION_ROW_INDEX);
        saveDesignAndRemoveForm();
    }

    @Test(groups = {"formBlockDesign"})
    public void saveCategoryAsBlock() {
        createNewFormAndCategoryWithAdminRights();
        getDesignerPage().clickBlocksButton();
        getDesignerPage().clickSaveBlockButton();
        getDesignerPage().getNewBlockWindow().createNewBlock(NEW_BLOCK_NAME);
        checkNotificationIsHumanized(getNotification());
        saveDesignAndRemoveForm();
        deleteBlock();
    }


    @Test(groups = {"formBlockDesign"})
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

    @Test(groups = {"formBlockDesign"})
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

    @Test(groups = "formBlockDesign")
    public void validFormWithEverything() throws FieldNotEditableException {
        loginFormAdmin1();
        getFormManagerPage().createNewForm(NEW_FORM_NAME);
        goToDesignerPage();
        getDesignerPage().createCompleteFormAndSave();
        goToFormManagerPage();
        getFormManagerPage().deleteForm(1);
        logOut();
    }

}
