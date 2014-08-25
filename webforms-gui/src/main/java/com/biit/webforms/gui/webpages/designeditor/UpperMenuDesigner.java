package com.biit.webforms.gui.webpages.designeditor;

import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.components.UpperMenuWebforms;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.Button.ClickListener;

public class UpperMenuDesigner extends UpperMenuWebforms {

	private static final long serialVersionUID = -368255188051163986L;

	private IconButton saveButton, flowButton, validateButton, finishButton, newCategoryButton, newSubcategoryButton,
			newGroupButton, newQuestionButton, newTextButton, newAnswerButton, moveButton, deleteButton, upButton, downButton;

	public UpperMenuDesigner() {

		saveButton = new IconButton(LanguageCodes.COMMON_CAPTION_SAVE, ThemeIcons.SAVE,
				LanguageCodes.COMMON_TOOLTIP_SAVE, IconSize.BIG);

		flowButton = new IconButton(LanguageCodes.COMMON_CAPTION_FLOW, ThemeIcons.FLOW_EDITOR,
				LanguageCodes.COMMON_TOOLTIP_FLOW, IconSize.BIG);

		validateButton = new IconButton(LanguageCodes.COMMON_CAPTION_VALIDATE, ThemeIcons.VALIDATE,
				LanguageCodes.COMMON_TOOLTIP_VALIDATE, IconSize.BIG);

		finishButton = new IconButton(LanguageCodes.COMMON_CAPTION_FINISH, ThemeIcons.VALIDATE,
				LanguageCodes.COMMON_TOOLTIP_FINISH, IconSize.BIG);

		newCategoryButton = new IconButton(LanguageCodes.CAPTION_NEW_CATEGORY, ThemeIcons.DESIGNER_NEW_CATEGORY,
				LanguageCodes.TOOLTIP_NEW_CATEGORY);
		newSubcategoryButton = new IconButton(LanguageCodes.CAPTION_NEW_SUBCATEGORY,
				ThemeIcons.DESIGNER_NEW_SUBCATEGORY, LanguageCodes.TOOLTIP_NEW_SUBCATEGORY);
		newGroupButton = new IconButton(LanguageCodes.CAPTION_NEW_GROUP, ThemeIcons.DESIGNER_NEW_GROUP,
				LanguageCodes.TOOLTIP_NEW_GROUP);
		newQuestionButton = new IconButton(LanguageCodes.CAPTION_NEW_QUESTION, ThemeIcons.DESIGNER_NEW_QUESTION,
				LanguageCodes.TOOLTIP_NEW_QUESTION);
		newTextButton = new IconButton(LanguageCodes.CAPTION_NEW_TEXT, ThemeIcons.DESIGNER_NEW_TEXT,
				LanguageCodes.TOOLTIP_NEW_TEXT);
		newAnswerButton = new IconButton(LanguageCodes.CAPTION_NEW_ANSWER, ThemeIcons.DESIGNER_NEW_ANSWER,
				LanguageCodes.TOOLTIP_NEW_ANSWER);
		moveButton = new IconButton(LanguageCodes.COMMON_CAPTION_MOVE, ThemeIcons.DESIGNER_MOVE,
				LanguageCodes.COMMON_TOOLTIP_MOVE);
		deleteButton = new IconButton(LanguageCodes.COMMON_CAPTION_DELETE, ThemeIcons.DELETE,
				LanguageCodes.COMMON_TOOLTIP_DELETE);
		upButton = new IconButton(LanguageCodes.COMMON_CAPTION_UP, ThemeIcons.UP,
				LanguageCodes.COMMON_TOOLTIP_UP);
		downButton = new IconButton(LanguageCodes.COMMON_CAPTION_DOWN, ThemeIcons.DOWN,
				LanguageCodes.COMMON_TOOLTIP_DOWN);

		addIconButton(saveButton);
		addIconButton(flowButton);
		addIconButton(newCategoryButton);
		addIconButton(newSubcategoryButton);
		addIconButton(newGroupButton);
		addIconButton(newQuestionButton);
		addIconButton(newTextButton);
		addIconButton(newAnswerButton);
		addIconButton(moveButton);
		addIconButton(deleteButton);
		addIconButton(upButton);
		addIconButton(downButton);
		addIconButton(validateButton);
		addIconButton(finishButton);
	}

	public void addSaveButtonListener(ClickListener listener) {
		saveButton.addClickListener(listener);
	}

	public void addFlowButtonListener(ClickListener listener) {
		flowButton.addClickListener(listener);
	}

	public void addValidateButtonListener(ClickListener listener) {
		validateButton.addClickListener(listener);
	}

	public void addFinishButtonListener(ClickListener listener) {
		finishButton.addClickListener(listener);
	}
	
	public void addNewCategoryButtonListener(ClickListener listener){
		newCategoryButton.addClickListener(listener);
	}
	
	public void addNewSubCategoryButtonListener(ClickListener listener){
		newSubcategoryButton.addClickListener(listener);
	}
	
	public void addNewGroupButtonListener(ClickListener listener){
		newGroupButton.addClickListener(listener);
	}
	
	public void addNewQuestionButtonListener(ClickListener listener){
		newQuestionButton.addClickListener(listener);
	}
	
	public void addNewTextButtonListener(ClickListener listener){
		newTextButton.addClickListener(listener);
	}
	
	public void addNewAnswerButtonListener(ClickListener listener){
		newAnswerButton.addClickListener(listener);
	}
	
	public void addMoveButtonListener(ClickListener listener){
		moveButton.addClickListener(listener);
	}
	
	public void addDeleteButtonListener(ClickListener listener){
		deleteButton.addClickListener(listener);
	}
	
	public void addUpButtonListener(ClickListener listener){
		upButton.addClickListener(listener);
	}

	public void addDownButtonListener(ClickListener listener){
		downButton.addClickListener(listener);
	}

	public IconButton getSaveButton() {
		return saveButton;
	}

	public IconButton getFlowButton() {
		return flowButton;
	}

	public IconButton getValidateButton() {
		return validateButton;
	}

	public IconButton getFinishButton() {
		return finishButton;
	}

	public IconButton getNewCategoryButton() {
		return newCategoryButton;
	}

	public IconButton getNewSubcategoryButton() {
		return newSubcategoryButton;
	}

	public IconButton getNewGroupButton() {
		return newGroupButton;
	}

	public IconButton getNewQuestionButton() {
		return newQuestionButton;
	}

	public IconButton getNewTextButton() {
		return newTextButton;
	}

	public IconButton getNewAnswerButton() {
		return newAnswerButton;
	}

	public IconButton getMoveButton() {
		return moveButton;
	}

	public IconButton getDeleteButton() {
		return deleteButton;
	}

	public IconButton getUpButton() {
		return upButton;
	}

	public IconButton getDownButton() {
		return downButton;
	}
}
