package com.biit.webforms.gui.webpages.designer;

import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.components.UpperMenuWebforms;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.Button.ClickListener;

public class UpperMenuDesigner extends UpperMenuWebforms {

	private static final long serialVersionUID = -368255188051163986L;

	private IconButton saveButton, saveAsBlockButton, insertBlockButton, flowButton, validateButton, finishButton, newCategoryButton, newSubcategoryButton,
			newGroupButton, newQuestionButton, newTextButton, newSystemFieldButton, newAnswerButton, moveButton, deleteButton, upButton, downButton;

	public UpperMenuDesigner() {

		saveButton = new IconButton(LanguageCodes.COMMON_CAPTION_SAVE, ThemeIcons.FORM_SAVE,
				LanguageCodes.COMMON_TOOLTIP_SAVE, IconSize.BIG);
		
		saveAsBlockButton = new IconButton(LanguageCodes.CAPTION_SAVE_AS_BLOCK, ThemeIcons.BUILDING_BLOCK_SAVE,
				LanguageCodes.TOOLTIP_SAVE_AS_BLOCK, IconSize.BIG);
		
		insertBlockButton = new IconButton(LanguageCodes.COMMON_CAPTION_INSERT_BLOCK, ThemeIcons.BUILDING_BLOCK_ADD,
				LanguageCodes.COMMON_TOOLTIP_INSERT_BLOCK, IconSize.BIG);

		flowButton = new IconButton(LanguageCodes.COMMON_CAPTION_FLOW, ThemeIcons.PAGE_FORM_FLOW_EDITOR,
				LanguageCodes.COMMON_TOOLTIP_FLOW, IconSize.BIG);

		validateButton = new IconButton(LanguageCodes.COMMON_CAPTION_VALIDATE, ThemeIcons.FORM_VALIDATE,
				LanguageCodes.COMMON_TOOLTIP_VALIDATE, IconSize.BIG);

		finishButton = new IconButton(LanguageCodes.COMMON_CAPTION_FINISH, ThemeIcons.FORM_FINISH,
				LanguageCodes.COMMON_TOOLTIP_FINISH, IconSize.BIG);

		newCategoryButton = new IconButton(LanguageCodes.CAPTION_NEW_CATEGORY, ThemeIcons.DESIGNER_NEW_CATEGORY,
				LanguageCodes.TOOLTIP_NEW_CATEGORY);
		newSubcategoryButton = new IconButton(LanguageCodes.CAPTION_NEW_SUBCATEGORY,
				ThemeIcons.DESIGNER_NEW_SUBCATEGORY, LanguageCodes.TOOLTIP_NEW_SUBCATEGORY);
		newGroupButton = new IconButton(LanguageCodes.CAPTION_NEW_GROUP, ThemeIcons.DESIGNER_NEW_GROUP,
				LanguageCodes.TOOLTIP_NEW_GROUP);
		newQuestionButton = new IconButton(LanguageCodes.CAPTION_NEW_QUESTION, ThemeIcons.DESIGNER_NEW_QUESTION,
				LanguageCodes.TOOLTIP_NEW_QUESTION);
		newAnswerButton = new IconButton(LanguageCodes.CAPTION_NEW_ANSWER, ThemeIcons.DESIGNER_NEW_ANSWER,
				LanguageCodes.TOOLTIP_NEW_ANSWER);
		newTextButton = new IconButton(LanguageCodes.CAPTION_NEW_TEXT, ThemeIcons.DESIGNER_NEW_INFOTEXT,
				LanguageCodes.TOOLTIP_NEW_TEXT);
		newSystemFieldButton = new IconButton(LanguageCodes.CAPTION_NEW_SYSTEM_FIELD, ThemeIcons.DESIGNER_NEW_SYSTEM_FIELD,
				LanguageCodes.TOOLTIP_NEW_SYSTEM_FIELD);		
		moveButton = new IconButton(LanguageCodes.COMMON_CAPTION_MOVE, ThemeIcons.DESIGNER_MOVE,
				LanguageCodes.COMMON_TOOLTIP_MOVE);
		upButton = new IconButton(LanguageCodes.COMMON_CAPTION_UP, ThemeIcons.ELEMENT_MOVE_UP,
				LanguageCodes.COMMON_TOOLTIP_UP);
		downButton = new IconButton(LanguageCodes.COMMON_CAPTION_DOWN, ThemeIcons.ELEMENT_MOVE_DOWN,
				LanguageCodes.COMMON_TOOLTIP_DOWN);
		deleteButton = new IconButton(LanguageCodes.COMMON_CAPTION_DELETE, ThemeIcons.ELEMENT_DELETE,
				LanguageCodes.COMMON_TOOLTIP_DELETE);

		addIconButton(saveButton);
		addIconButton(saveAsBlockButton);
		addIconButton(insertBlockButton);
		addIconButton(flowButton);
		addIconButton(newCategoryButton);
		addIconButton(newSubcategoryButton);
		addIconButton(newGroupButton);
		addIconButton(newQuestionButton);
		addIconButton(newAnswerButton);
		addIconButton(newTextButton);
		addIconButton(newSystemFieldButton);
		addIconButton(moveButton);
		addIconButton(upButton);
		addIconButton(downButton);
		addIconButton(deleteButton);
		addIconButton(validateButton);
		addIconButton(finishButton);
	}

	public void addSaveButtonListener(ClickListener listener) {
		saveButton.addClickListener(listener);
	}
	
	public void addSaveAsBlockButtonListener(ClickListener listener) {
		saveAsBlockButton.addClickListener(listener);
	}
	
	public void addInsertBlockButtonListener(ClickListener listener) {
		insertBlockButton.addClickListener(listener);
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
	
	public void addnewSystemFieldButtonListener(ClickListener listener){
		newSystemFieldButton.addClickListener(listener);
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
	
	public IconButton getSaveAsBlockButton() {
		return saveAsBlockButton;
	}
	
	public IconButton getInsertBlockButton(){
		return insertBlockButton;
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
	
	public IconButton getNewSystemFieldButton() {
		return newSystemFieldButton;
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
