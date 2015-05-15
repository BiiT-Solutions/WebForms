package com.biit.webforms.gui.tests.webpage;

import org.testng.Assert;

import com.biit.gui.tester.VaadinGuiWebpage;
import com.biit.webforms.gui.tests.exceptions.FieldNotEditableException;
import com.biit.webforms.gui.tests.webpage.designer.AnswerPropertiesView;
import com.biit.webforms.gui.tests.webpage.designer.CategoryPropertiesView;
import com.biit.webforms.gui.tests.webpage.designer.FieldPropertiesView;
import com.biit.webforms.gui.tests.webpage.designer.FormPropertiesView;
import com.biit.webforms.gui.tests.webpage.designer.GroupPropertiesView;
import com.biit.webforms.gui.tests.webpage.designer.QuestionPropertiesView;
import com.biit.webforms.gui.tests.webpage.designer.SubanswerPropertiesView;
import com.biit.webforms.gui.tests.webpage.designer.TextPropertiesView;
import com.biit.webforms.gui.tests.window.LinkBlockWindow;
import com.biit.webforms.gui.tests.window.NewBlockWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.HorizontalLayoutElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class Designer extends VaadinGuiWebpage {

	private static final String SAVE_BUTTON_CAPTION = "Save";
	private static final String BLOCKS_BUTTON_CAPTION = "Blocks";
	private static final String SAVE_BLOCK_BUTTON_CAPTION = "Save Block";
	private static final String INSERT_BLOCK_BUTTON_CAPTION = "Insert Block";
	private static final String LINK_BLOCK_BUTTON_CAPTION = "Link Block";
	private static final String CATEGORY_BUTTON_CAPTION = "Category";
	private static final String GROUP_BUTTON_CAPTION = "Group";
	private static final String QUESTION_BUTTON_CAPTION = "Question";
	private static final String ANSWER_BUTTON_CAPTION = "Answer";
	private static final String OTHER_ELEMENTS_BUTTON_CAPTION = "Others";
	private static final String SUBANSWER_BUTTON_CAPTION = "Subanswer";
	private static final String TEXT_BUTTON_CAPTION = "Text";
	private static final String FIELD_BUTTON_CAPTION = "Field";
	private static final String MOVE_BUTTON_CAPTION = "Move";
	private static final String UP_BUTTON_CAPTION = "Up";
	private static final String DOWN_BUTTON_CAPTION = "Down";
	private static final String DELETE_BUTTON_CAPTION = "Delete";
	private static final String HIDE_BUTTON_CAPTION = "Hide";
	private static final String SHOW_BUTTON_CAPTION = "Show";
	private static final String FINISH_BUTTON_CAPTION = "Finish";
	private static final Integer RIGHT_SCROLL_PIXELS = 500;

	private static final String FORM_NAME_EDITED = "new_form_test";
	private static final String FORM_VERSION = "1";
	private static final String FORM_DESCRIPTION = "Form created for gui testing";

	private static final String ANSWER_TYPE_RADIO_BUTTON_COMBOBOX_CAPTION = "Radio Button";
	private static final String ANSWER_TYPE_COMBOBOX_LIST_CAPTION = "List";
	private static final String ANSWER_TYPE_COMBOBOX_MULTI_CHECKBOX_CAPTION = "Multi Checkbox";
	private static final String ANSWER_TYPE_COMBOBOX_INPUT_FIELD_CAPTION = "Input Field";
	private static final String ANSWER_TYPE_COMBOBOX_TEXT_AREA_CAPTION = "Text Area";

	private static final String ANSWER_FORMAT_TEXT_COMBOBOX_CAPTION = "Text";
	private static final String ANSWER_FORMAT_NUMBER_COMBOBOX_CAPTION = "Number";
	private static final String ANSWER_FORMAT_DATE_COMBOBOX_CAPTION = "Date";
	private static final String ANSWER_FORMAT_POSTAL_CODE_COMBOBOX_CAPTION = "Postal Code";

	private static final String ANSWER_SUBFORMAT_TEXT_COMBOBOX_CAPTION = "Text";
	private static final String ANSWER_SUBFORMAT_EMAIL_COMBOBOX_CAPTION = "Email";
	private static final String ANSWER_SUBFORMAT_PHONE_COMBOBOX_CAPTION = "Phone";
	private static final String ANSWER_SUBFORMAT_IBAN_COMBOBOX_CAPTION = "IBAN";
	private static final String ANSWER_SUBFORMAT_BSN_COMBOBOX_CAPTION = "BSN";

	private static final String ANSWER_SUBFORMAT_NUMBER_COMBOBOX_CAPTION = "Number";
	private static final String ANSWER_SUBFORMAT_FLOAT_COMBOBOX_CAPTION = "Float";

	private static final String ANSWER_SUBFORMAT_DATE_COMBOBOX_CAPTION = "Date";
	private static final String ANSWER_SUBFORMAT_PAST_COMBOBOX_CAPTION = "Past";
	private static final String ANSWER_SUBFORMAT_FUTURE_COMBOBOX_CAPTION = "Future";

	private static final String ANSWER_SUBFORMAT_POSTAL_CODE_COMBOBOX_CAPTION = "Postcal Code";

	private static final String CHECKBOX_RETURN_UNCHECKED = "unchecked";
	private static final String CHECKBOX_RETURN_CHECKED = "checked";

	private static final Integer ANSWERS_TO_ADD = 4;
	private static final Integer SUBANSWERS_TO_ADD = 2;

	private static final String QUESTION1_NAME = "Question1";
	private static final String QUESTION2_NAME = "Question2";
	private static final String QUESTION3_NAME = "Question3";
	private static final String ANSWER1_NAME = "Q1Answer1";
	private static final String ANSWER2_NAME = "Q1Answer2";

	private static final Integer TREE_TABLE_INIT_ROW = 0;

	private final FormPropertiesView formPropertiesView;
	private final CategoryPropertiesView categoryPropertiesView;
	private final GroupPropertiesView groupPropertiesView;
	private final QuestionPropertiesView questionPropertiesView;
	private final AnswerPropertiesView answerPropertiesView;
	private final SubanswerPropertiesView subanswerPropertiesView;
	private final TextPropertiesView textPropertiesView;
	private final FieldPropertiesView fieldPropertiesView;
	private final NewBlockWindow newBlockWindow;
	private final LinkBlockWindow linkBlockWindow;

	public Designer() {
		super();
		formPropertiesView = new FormPropertiesView();
		addView(formPropertiesView);
		categoryPropertiesView = new CategoryPropertiesView();
		addView(categoryPropertiesView);
		groupPropertiesView = new GroupPropertiesView();
		addView(groupPropertiesView);
		questionPropertiesView = new QuestionPropertiesView();
		addView(questionPropertiesView);
		answerPropertiesView = new AnswerPropertiesView();
		addView(answerPropertiesView);
		subanswerPropertiesView = new SubanswerPropertiesView();
		addView(subanswerPropertiesView);
		textPropertiesView = new TextPropertiesView();
		addView(textPropertiesView);
		fieldPropertiesView = new FieldPropertiesView();
		addView(fieldPropertiesView);
		newBlockWindow = new NewBlockWindow();
		addWindow(newBlockWindow);
		linkBlockWindow = new LinkBlockWindow();
		addWindow(linkBlockWindow);
	}

	public void addAnswers(int numberOfAnswers) {
		for (int i = 0; i < numberOfAnswers; i++) {
			addNewAnswer();
		}
	}

	public void addAnswersWithSubanswers(int numberOfAnswers, int numberOfSubanswers) {
		for (int i = 0; i < numberOfAnswers; i++) {
			addNewAnswer();
			for (int j = 0; j < numberOfSubanswers; j++) {
				addNewSubanswer();
			}
		}
	}

	public void addNewAnswer() {
		getAnswerButton().click();
	}

	public void addNewCategory() {
		getCategoryButton().click();
	}

	public void addNewField() {
		getFieldButton().click();
	}

	public void addNewGroup() {
		getGroupButton().click();
	}

	public void hideElement() {
		getCategoryButton().click();
	}

	public void addNewInputDateQuestion() throws FieldNotEditableException {
		addNewInputQuestion();
		getQuestionPropertiesView().setAnswerFormatComboBoxValue(ANSWER_FORMAT_DATE_COMBOBOX_CAPTION);
	}

	public void addNewInputDateSubformatDateQuestion() throws FieldNotEditableException {
		addNewInputDateQuestion();
		// Although the type is already selected as default value, we select it
		// again to check that is working properly
		getQuestionPropertiesView().setAnswerSubformatComboBoxValue(ANSWER_SUBFORMAT_DATE_COMBOBOX_CAPTION);
	}

	public void addNewInputDateSubformatFutureQuestion() throws FieldNotEditableException {
		addNewInputDateQuestion();
		getQuestionPropertiesView().setAnswerSubformatComboBoxValue(ANSWER_SUBFORMAT_FUTURE_COMBOBOX_CAPTION);
	}

	public void addNewInputDateSubformatPastQuestion() throws FieldNotEditableException {
		addNewInputDateQuestion();
		getQuestionPropertiesView().setAnswerSubformatComboBoxValue(ANSWER_SUBFORMAT_PAST_COMBOBOX_CAPTION);
	}

	public void addNewInputNumberQuestion() throws FieldNotEditableException {
		addNewInputQuestion();
		getQuestionPropertiesView().setAnswerFormatComboBoxValue(ANSWER_FORMAT_NUMBER_COMBOBOX_CAPTION);
	}

	public void addNewInputNumberSubformatFloatQuestion() throws FieldNotEditableException {
		addNewInputNumberQuestion();
		getQuestionPropertiesView().setAnswerSubformatComboBoxValue(ANSWER_SUBFORMAT_FLOAT_COMBOBOX_CAPTION);
	}

	public void addNewInputNumberSubformatNumberQuestion() throws FieldNotEditableException {
		addNewInputNumberQuestion();
		// Although the type is already selected as default value, we select it
		// again to check that is working properly
		getQuestionPropertiesView().setAnswerSubformatComboBoxValue(ANSWER_SUBFORMAT_NUMBER_COMBOBOX_CAPTION);
	}

	public void addNewInputPostalCodeQuestion() throws FieldNotEditableException {
		addNewInputQuestion();
		getQuestionPropertiesView().setAnswerFormatComboBoxValue(ANSWER_FORMAT_POSTAL_CODE_COMBOBOX_CAPTION);
	}

	public void addNewInputPostalcodeSubformatPostalcodeQuestion() throws FieldNotEditableException {
		addNewInputPostalCodeQuestion();
		// Although the type is already selected as default value, we select it
		// again to check that is working properly
		getQuestionPropertiesView().setAnswerSubformatComboBoxValue(ANSWER_SUBFORMAT_POSTAL_CODE_COMBOBOX_CAPTION);
	}

	public void addNewInputQuestion() throws FieldNotEditableException {
		addNewQuestion();
		// Although the type is already selected as default value, we select it
		// again to check that is working properly
		getQuestionPropertiesView().setAnswerTypeComboBoxValue(ANSWER_TYPE_COMBOBOX_INPUT_FIELD_CAPTION);
	}

	public void addNewInputTextQuestion() throws FieldNotEditableException {
		addNewInputQuestion();
		// Although the type is already selected as default value, we select it
		// again to check that is working properly
		getQuestionPropertiesView().setAnswerFormatComboBoxValue(ANSWER_FORMAT_TEXT_COMBOBOX_CAPTION);
	}

	public void addNewInputTextSubformatBsnQuestion() throws FieldNotEditableException {
		addNewInputTextQuestion();
		getQuestionPropertiesView().setAnswerSubformatComboBoxValue(ANSWER_SUBFORMAT_BSN_COMBOBOX_CAPTION);
	}

	public void addNewInputTextSubformatEmailQuestion() throws FieldNotEditableException {
		addNewInputTextQuestion();
		getQuestionPropertiesView().setAnswerSubformatComboBoxValue(ANSWER_SUBFORMAT_EMAIL_COMBOBOX_CAPTION);
	}

	public void addNewInputTextSubformatIbanQuestion() throws FieldNotEditableException {
		addNewInputTextQuestion();
		getQuestionPropertiesView().setAnswerSubformatComboBoxValue(ANSWER_SUBFORMAT_IBAN_COMBOBOX_CAPTION);
	}

	public void addNewInputTextSubformatPhoneQuestion() throws FieldNotEditableException {
		addNewInputTextQuestion();
		getQuestionPropertiesView().setAnswerSubformatComboBoxValue(ANSWER_SUBFORMAT_PHONE_COMBOBOX_CAPTION);
	}

	public void addNewInputTextSubformatTextQuestion() throws FieldNotEditableException {
		addNewInputTextQuestion();
		// Although the type is already selected as default value, we select it
		// again to check that is working properly
		getQuestionPropertiesView().setAnswerSubformatComboBoxValue(ANSWER_SUBFORMAT_TEXT_COMBOBOX_CAPTION);
	}

	public void addNewListQuestion() throws FieldNotEditableException {
		addNewQuestion();
		getQuestionPropertiesView().setAnswerTypeComboBoxValue(ANSWER_TYPE_COMBOBOX_LIST_CAPTION);
	}

	public void addNewMultiCheckboxQuestion() throws FieldNotEditableException {
		addNewQuestion();
		getQuestionPropertiesView().setAnswerTypeComboBoxValue(ANSWER_TYPE_COMBOBOX_MULTI_CHECKBOX_CAPTION);
	}

	public void addNewQuestion() {
		getQuestionButton().click();
	}

	public void addNewRadioButtonQuestion() throws FieldNotEditableException {
		addNewQuestion();
		getQuestionPropertiesView().setAnswerTypeComboBoxValue(ANSWER_TYPE_RADIO_BUTTON_COMBOBOX_CAPTION);
	}

	public void addNewRepeatableGroup() throws FieldNotEditableException {
		addNewGroup();
		getGroupPropertiesView().clickRepeatableCheckBox();
	}

	public void addNewSubanswer() {
		getSubanswerButton().click();
	}

	public void addNewText() {
		getTextButton().click();
	}

	public void addNewTextAreaQuestion() throws FieldNotEditableException {
		addNewQuestion();
		getQuestionPropertiesView().setAnswerTypeComboBoxValue(ANSWER_TYPE_COMBOBOX_TEXT_AREA_CAPTION);
	}

	public void addSubanswers(int numberOfSubanswers) {
		for (int i = 0; i < numberOfSubanswers; i++) {
			addNewSubanswer();
		}
	}

	public void clickBlocksButton() {
		getBlocksButton().click();
	}

	public void clickDeleteButton() {
		getDeleteButton().click();
	}

	public void clickDownButton() {
		getDownButton().click();
	}

	public void clickInsertBlockButton() {
		getInsertBlockButton().click();
	}

	public void clickInTreeTableRow(int row) {
		getTreeTable().getRow(row).click();
	}

	public void clickLinkBlockButton() {
		getLinkBlockButton().click();
	}

	public void clickSaveBlockButton() {
		getSaveBlockButton().click();
	}

	public void clickUpButton() {
		getUpButton().click();
	}

	private void createAllTypesOfInputQuestions() throws FieldNotEditableException {
		addNewInputTextSubformatTextQuestion();
		addNewInputTextSubformatEmailQuestion();
		addNewInputTextSubformatPhoneQuestion();
		addNewInputTextSubformatIbanQuestion();
		addNewInputTextSubformatBsnQuestion();
		addNewInputNumberSubformatNumberQuestion();
		addNewInputNumberSubformatFloatQuestion();
		addNewInputDateSubformatDateQuestion();
		addNewInputDateSubformatPastQuestion();
		addNewInputDateSubformatFutureQuestion();
		addNewInputPostalcodeSubformatPostalcodeQuestion();

		addNewInputTextSubformatTextQuestion();
		setQuestionNotMandatory();
		addNewInputTextSubformatEmailQuestion();
		setQuestionNotMandatory();
		addNewInputTextSubformatPhoneQuestion();
		setQuestionNotMandatory();
		addNewInputTextSubformatIbanQuestion();
		setQuestionNotMandatory();
		addNewInputTextSubformatBsnQuestion();
		setQuestionNotMandatory();
		addNewInputNumberSubformatNumberQuestion();
		setQuestionNotMandatory();
		addNewInputNumberSubformatFloatQuestion();
		setQuestionNotMandatory();
		addNewInputDateSubformatDateQuestion();
		setQuestionNotMandatory();
		addNewInputDateSubformatPastQuestion();
		setQuestionNotMandatory();
		addNewInputDateSubformatFutureQuestion();
		setQuestionNotMandatory();
		addNewInputPostalcodeSubformatPostalcodeQuestion();
		setQuestionNotMandatory();
	}

	private void createAllTypesOfListQuestions() throws FieldNotEditableException {
		addNewListQuestion();
		addAnswers(ANSWERS_TO_ADD);

		addNewListQuestion();
		setQuestionNotMandatory();
		addAnswers(ANSWERS_TO_ADD);
	}

	private void createAllTypesOfMultiCheckboxQuestions() throws FieldNotEditableException {
		addNewMultiCheckboxQuestion();
		addAnswersWithSubanswers(ANSWERS_TO_ADD, SUBANSWERS_TO_ADD);

		addNewMultiCheckboxQuestion();
		setQuestionNotMandatory();
		addAnswersWithSubanswers(ANSWERS_TO_ADD, SUBANSWERS_TO_ADD);

	}

	private void createAllTypesOfQuestions() throws FieldNotEditableException {
		createAllTypesOfRadioButtonQuestions();
		createAllTypesOfListQuestions();
		createAllTypesOfMultiCheckboxQuestions();
		createAllTypesOfTextAreaQuestions();
		createAllTypesOfInputQuestions();
	}

	private void createAllTypesOfRadioButtonQuestions() throws FieldNotEditableException {
		addNewRadioButtonQuestion();
		addAnswersWithSubanswers(ANSWERS_TO_ADD, SUBANSWERS_TO_ADD);

		addNewRadioButtonQuestion();
		setQuestionNotMandatory();
		addAnswersWithSubanswers(ANSWERS_TO_ADD, SUBANSWERS_TO_ADD);

		addNewRadioButtonQuestion();
		setQuestionHorizontal();
		addAnswersWithSubanswers(ANSWERS_TO_ADD, SUBANSWERS_TO_ADD);

		addNewRadioButtonQuestion();
		setQuestionHorizontal();
		setQuestionNotMandatory();
		addAnswersWithSubanswers(ANSWERS_TO_ADD, SUBANSWERS_TO_ADD);

	}

	private void createAllTypesOfTextAreaQuestions() throws FieldNotEditableException {
		addNewTextAreaQuestion();

		addNewTextAreaQuestion();
		setQuestionNotMandatory();
	}

	public void createAndSaveSimpleBlockDesign() throws FieldNotEditableException {
		// Category 1
		addNewCategory();
		addNewGroup();
		getGroupPropertiesView().clickRepeatableCheckBox();
		addNewRadioButtonQuestion();
		getQuestionPropertiesView().setTechnicalName(QUESTION1_NAME);
		addNewAnswer();
		getAnswerPropertiesView().setValue(ANSWER1_NAME);
		addNewAnswer();
		getAnswerPropertiesView().setValue(ANSWER2_NAME);
		// Save
		saveDesign();
	}

	/**
	 * Creates a form with three categories and one question per category.<br>
	 * The question of the first category also has two answers.
	 * 
	 * @throws FieldNotEditableException
	 */
	public void createAndSaveSimpleFormDesign() throws FieldNotEditableException {
		// Category 1
		addNewCategory();
		addNewRadioButtonQuestion();
		getQuestionPropertiesView().setTechnicalName(QUESTION1_NAME);
		addNewAnswer();
		getAnswerPropertiesView().setValue(ANSWER1_NAME);
		addNewAnswer();
		getAnswerPropertiesView().setValue(ANSWER2_NAME);
		// Category 2
		addNewCategory();
		addNewGroup();
		getGroupPropertiesView().clickRepeatableCheckBox();
		addNewQuestion();
		getQuestionPropertiesView().setTechnicalName(QUESTION2_NAME);
		// Category 3
		addNewCategory();
		addNewGroup();
		addNewQuestion();
		getQuestionPropertiesView().setTechnicalName(QUESTION3_NAME);
		getQuestionPropertiesView().clickMandatoryCheckBox();
		// Save
		saveDesign();
	}

	public void createCompleteFormAndSave() throws FieldNotEditableException {
		// Edit some form properties
		getFormPropertiesView().setName(FORM_NAME_EDITED);
		getFormPropertiesView().setDescription(FORM_DESCRIPTION);
		Assert.assertEquals(FORM_NAME_EDITED, getFormPropertiesView().getName());
		Assert.assertEquals(FORM_VERSION, getFormPropertiesView().getVersionValue());
		Assert.assertEquals(FORM_DESCRIPTION, getFormPropertiesView().getDescription());

		// Category 1
		addNewCategory();
		addNewGroup();
		createAllTypesOfQuestions();
		addNewText();
		addNewField();

		// Category 2
		addNewCategory();
		createAllTypesOfQuestions();
		addNewText();
		addNewField();

		// Category 2
		addNewCategory();
		addNewRepeatableGroup();
		addNewGroup();
		addNewQuestion();
		addNewQuestion();
		addNewText();
		addNewField();

		saveDesign();
	}

	public void finishForm() {
		getFinishButton().click();
	}

	public ButtonElement getAnswerButton() {
		return getButtonElement(ANSWER_BUTTON_CAPTION);
	}

	public AnswerPropertiesView getAnswerPropertiesView() {
		return answerPropertiesView;
	}

	public ButtonElement getBlocksButton() {
		return getButtonElement(BLOCKS_BUTTON_CAPTION);
	}

	@Override
	public ButtonElement getButtonElement(String buttonCaption) {
		if (!existsButton(buttonCaption)) {
			scrollRightUpperButtonMenu();
		}
		return super.getButtonElement(buttonCaption);
	}

	public ButtonElement getCategoryButton() {
		return getButtonElement(CATEGORY_BUTTON_CAPTION);
	}

	public CategoryPropertiesView getCategoryPropertiesView() {
		return categoryPropertiesView;
	}

	public ButtonElement getDeleteButton() {
		return getButtonElement(DELETE_BUTTON_CAPTION);
	}

	public ButtonElement getHideButton() {
		return getButtonElement(HIDE_BUTTON_CAPTION);
	}

	public ButtonElement getShowButton() {
		return getButtonElement(SHOW_BUTTON_CAPTION);
	}

	public ButtonElement getDownButton() {
		return getButtonElement(DOWN_BUTTON_CAPTION);
	}

	public FieldPropertiesView getFieldPropertiesView() {
		return fieldPropertiesView;
	}

	public ButtonElement getFinishButton() {
		return getButtonElement(FINISH_BUTTON_CAPTION);
	}

	public FormPropertiesView getFormPropertiesView() {
		// The form is always going to be the first element of the table
		getTreeTable().getCell(0, 0);
		return formPropertiesView;
	}

	public ButtonElement getGroupButton() {
		return getButtonElement(GROUP_BUTTON_CAPTION);
	}

	public GroupPropertiesView getGroupPropertiesView() {
		return groupPropertiesView;
	}

	public ButtonElement getInsertBlockButton() {
		return getButtonElement(INSERT_BLOCK_BUTTON_CAPTION);
	}

	public ButtonElement getLinkBlockButton() {
		return getButtonElement(LINK_BLOCK_BUTTON_CAPTION);
	}

	public LinkBlockWindow getLinkBlockWindow() {
		return linkBlockWindow;
	}

	public ButtonElement getMoveButton() {
		return getButtonElement(MOVE_BUTTON_CAPTION);
	}

	public NewBlockWindow getNewBlockWindow() {
		return newBlockWindow;
	}

	public ButtonElement getQuestionButton() {
		return getButtonElement(QUESTION_BUTTON_CAPTION);
	}

	public QuestionPropertiesView getQuestionPropertiesView() {
		return questionPropertiesView;
	}

	public ButtonElement getSaveBlockButton() {
		return getButtonElement(SAVE_BLOCK_BUTTON_CAPTION);
	}

	public ButtonElement getSaveButton() {
		return getButtonElement(SAVE_BUTTON_CAPTION);
	}

	public ButtonElement getOthersButton() {
		return getButtonElement(OTHER_ELEMENTS_BUTTON_CAPTION);
	}

	public ButtonElement getFieldButton() {
		if (getButtonElement(FIELD_BUTTON_CAPTION) != null) {
			return getButtonElement(FIELD_BUTTON_CAPTION);
		} else {
			getOthersButton().click();
			return getButtonElement(FIELD_BUTTON_CAPTION);
		}
	}

	public ButtonElement getSubanswerButton() {
		if (getButtonElement(SUBANSWER_BUTTON_CAPTION) != null) {
			return getButtonElement(SUBANSWER_BUTTON_CAPTION);
		} else {
			getOthersButton().click();
			return getButtonElement(SUBANSWER_BUTTON_CAPTION);
		}
	}

	public ButtonElement getTextButton() {
		if (getButtonElement(TEXT_BUTTON_CAPTION) != null) {
			return getButtonElement(TEXT_BUTTON_CAPTION);
		} else {
			getOthersButton().click();
			return getButtonElement(TEXT_BUTTON_CAPTION);
		}
	}

	public SubanswerPropertiesView getSubanswerPropertiesView() {
		return subanswerPropertiesView;
	}

	public TextPropertiesView getTextPropertiesView() {
		return textPropertiesView;
	}

	public TreeTableElement getTreeTable() {
		return $(TreeTableElement.class).first();
	}

	public ButtonElement getUpButton() {
		return getButtonElement(UP_BUTTON_CAPTION);
	}

	@Override
	public String getWebpageUrl() {
		return null;
	}

	public void goToBeginningOfTreeTable() {
		clickInTreeTableRow(TREE_TABLE_INIT_ROW);
	}

	public void saveDesign() {
		getSaveButton().click();
	}

	private void scrollRightUpperButtonMenu() {
		$(HorizontalLayoutElement.class).$$(HorizontalLayoutElement.class).first().scrollLeft(RIGHT_SCROLL_PIXELS);
	}

	public void setQuestionHorizontal() throws FieldNotEditableException {
		if (getQuestionPropertiesView().getHorizontalCheckBoxValue().equals(CHECKBOX_RETURN_UNCHECKED)) {
			getQuestionPropertiesView().clickHorizontalCheckBox();
		}
	}

	public void setQuestionMandatory() throws FieldNotEditableException {
		if (getQuestionPropertiesView().getMandatoryCheckBoxValue().equals(CHECKBOX_RETURN_UNCHECKED)) {
			getQuestionPropertiesView().clickMandatoryCheckBox();
		}
	}

	public void setQuestionNotHorizontal() throws FieldNotEditableException {
		if (getQuestionPropertiesView().getHorizontalCheckBoxValue().equals(CHECKBOX_RETURN_CHECKED)) {
			getQuestionPropertiesView().clickHorizontalCheckBox();
		}
	}

	public void setQuestionNotMandatory() throws FieldNotEditableException {
		if (getQuestionPropertiesView().getMandatoryCheckBoxValue().equals(CHECKBOX_RETURN_CHECKED)) {
			getQuestionPropertiesView().clickMandatoryCheckBox();
		}
	}

	public void toggleExpandedTableRow(int treeTableRow) {
		getTreeTable().getRow(treeTableRow).toggleExpanded();
		getTreeTable().getRow(treeTableRow).waitForVaadin();
	}
}