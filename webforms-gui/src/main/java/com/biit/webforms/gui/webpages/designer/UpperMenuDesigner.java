package com.biit.webforms.gui.webpages.designer;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.common.components.WindowTextArea;
import com.biit.webforms.gui.components.UpperMenuWebforms;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class UpperMenuDesigner extends UpperMenuWebforms {

	private static final long serialVersionUID = -368255188051163986L;

	private final IconButton saveButton;
	private final IconButton blockMenu, saveAsBlockButton, insertBlockButton, linkBlockButton;
	private final IconButton otherElementsMenu, newSubanswerButton, newTextButton, newSystemFieldButton, answerRangeButton;
	private final IconButton newCategoryButton, newGroupButton, newQuestionButton, newAnswerButton, moveButton, deleteButton, upButton, downButton, finish,
			exportToJavaCode, hideButton, newDynamicAnswer, newAttachFilesButton;

	public UpperMenuDesigner() {
		saveButton = new IconButton(LanguageCodes.COMMON_CAPTION_SAVE, ThemeIcons.FORM_SAVE, LanguageCodes.COMMON_TOOLTIP_SAVE, IconSize.BIG);

		saveAsBlockButton = new IconButton(LanguageCodes.CAPTION_SAVE_AS_BLOCK, ThemeIcons.BUILDING_BLOCK_SAVE, LanguageCodes.TOOLTIP_SAVE_AS_BLOCK,
				IconSize.BIG);

		insertBlockButton = new IconButton(LanguageCodes.COMMON_CAPTION_INSERT_BLOCK, ThemeIcons.BUILDING_BLOCK_ADD, LanguageCodes.COMMON_TOOLTIP_INSERT_BLOCK,
				IconSize.BIG);
		linkBlockButton = new IconButton(LanguageCodes.COMMON_CAPTION_LINK_BLOCK, ThemeIcons.BUILDING_BLOCK_LINK, LanguageCodes.COMMON_TOOLTIP_LINK_BLOCK,
				IconSize.BIG);

		newCategoryButton = new IconButton(LanguageCodes.CAPTION_NEW_CATEGORY, ThemeIcons.DESIGNER_NEW_CATEGORY, LanguageCodes.TOOLTIP_NEW_CATEGORY);
		newGroupButton = new IconButton(LanguageCodes.CAPTION_NEW_GROUP, ThemeIcons.DESIGNER_NEW_GROUP, LanguageCodes.TOOLTIP_NEW_GROUP);
		newQuestionButton = new IconButton(LanguageCodes.CAPTION_NEW_QUESTION, ThemeIcons.DESIGNER_NEW_QUESTION, LanguageCodes.TOOLTIP_NEW_QUESTION);
		newAnswerButton = new IconButton(LanguageCodes.CAPTION_NEW_ANSWER, ThemeIcons.DESIGNER_NEW_ANSWER, LanguageCodes.TOOLTIP_NEW_ANSWER);
		newSubanswerButton = new IconButton(LanguageCodes.CAPTION_NEW_SUBANSWER, ThemeIcons.DESIGNER_NEW_SUBANSWER, LanguageCodes.TOOLTIP_NEW_SUBANSWER);
		newTextButton = new IconButton(LanguageCodes.CAPTION_NEW_TEXT, ThemeIcons.DESIGNER_NEW_INFOTEXT, LanguageCodes.TOOLTIP_NEW_TEXT);
		newAttachFilesButton = new IconButton(LanguageCodes.CAPTION_NEW_ATTACHED_FILES, ThemeIcons.DESIGNER_NEW_ATTACHED_FILES,
				LanguageCodes.TOOLTIP_NEW_ATTACHED_FILES);
		newSystemFieldButton = new IconButton(LanguageCodes.CAPTION_NEW_SYSTEM_FIELD, ThemeIcons.DESIGNER_NEW_SYSTEM_FIELD,
				LanguageCodes.TOOLTIP_NEW_SYSTEM_FIELD);
		newDynamicAnswer = new IconButton(LanguageCodes.CAPTION_NEW_DYNAMIC_ANSWER, ThemeIcons.DESIGNER_NEW_DYNAMIC_ANSWER,
				LanguageCodes.TOOLTIP_NEW_DYNAMIC_ANSWER);

		answerRangeButton = new IconButton(LanguageCodes.CAPTION_NEW_RANGED_ANSWER, ThemeIcons.DESIGNER_NEW_RANGED_ANSWERS,
				LanguageCodes.TOOLTIP_NEW_RANGED_ANSWER);

		moveButton = new IconButton(LanguageCodes.COMMON_CAPTION_MOVE, ThemeIcons.DESIGNER_MOVE, LanguageCodes.COMMON_TOOLTIP_MOVE);
		upButton = new IconButton(LanguageCodes.COMMON_CAPTION_UP, ThemeIcons.ELEMENT_MOVE_UP, LanguageCodes.COMMON_TOOLTIP_UP);
		downButton = new IconButton(LanguageCodes.COMMON_CAPTION_DOWN, ThemeIcons.ELEMENT_MOVE_DOWN, LanguageCodes.COMMON_TOOLTIP_DOWN);
		deleteButton = new IconButton(LanguageCodes.COMMON_CAPTION_DELETE, ThemeIcons.ELEMENT_DELETE, LanguageCodes.COMMON_TOOLTIP_DELETE);
		hideButton = new IconButton(LanguageCodes.COMMON_CAPTION_HIDE, ThemeIcons.ELEMENT_HIDE, LanguageCodes.COMMON_TOOLTIP_HIDE);
		finish = new IconButton(LanguageCodes.COMMON_CAPTION_FINISH, ThemeIcons.FORM_FINISH, LanguageCodes.COMMON_TOOLTIP_FINISH, IconSize.BIG);

		exportToJavaCode = new IconButton(LanguageCodes.APPLICATION_NAME, ThemeIcons.ALERT, LanguageCodes.APPLICATION_NAME);
		exportToJavaCode.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 5531403001527645029L;

			@Override
			public void buttonClick(ClickEvent event) {
				WindowTextArea window = new WindowTextArea("Java code");
				window.setValue(ApplicationUi.getController().getCompleteFormView().exportToJavaCode(new StringBuilder()));
				window.setResizable(true);
				window.showCentered();
			}
		});
		exportToJavaCode.setVisible(false);

		addIconButton(saveButton);

		blockMenu = addSubMenu(ThemeIcons.BUILDING_BLOCK_MENU, LanguageCodes.COMMON_CAPTION_BUILDING_BLOCK_MANAGER,
				LanguageCodes.COMMON_TOOLTIP_BUILDING_BLOCK_MANAGER, saveAsBlockButton, insertBlockButton, linkBlockButton);

		addIconButton(newCategoryButton);
		addIconButton(newGroupButton);
		addIconButton(newQuestionButton);
		addIconButton(newAnswerButton);

		otherElementsMenu = addSubMenu(ThemeIcons.OTHER_ELEMENTS_MENU, LanguageCodes.COMMON_CAPTION_OTHER_ELEMENTS_SUBMENU,
				LanguageCodes.COMMON_TOOLTIP_BUILDING_BLOCK_MANAGER, newSubanswerButton, newDynamicAnswer, newTextButton, newAttachFilesButton,
				newSystemFieldButton, answerRangeButton);

		addIconButton(moveButton);
		addIconButton(upButton);
		addIconButton(downButton);
		addIconButton(deleteButton);
		addIconButton(hideButton);
		addIconButton(finish);
		addIconButton(exportToJavaCode);
		setConfirmationNeeded(true);

		linkBlockButton.setVisible(WebformsConfigurationReader.getInstance().isLinkBloksEnabled());
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

	public void addLinkBlockButtonListener(ClickListener listener) {
		linkBlockButton.addClickListener(listener);
	}

	public void addNewCategoryButtonListener(ClickListener listener) {
		newCategoryButton.addClickListener(listener);
	}

	public void addNewGroupButtonListener(ClickListener listener) {
		newGroupButton.addClickListener(listener);
	}

	public void addNewQuestionButtonListener(ClickListener listener) {
		newQuestionButton.addClickListener(listener);
	}

	public void addNewSystemFieldButtonListener(ClickListener listener) {
		newSystemFieldButton.addClickListener(listener);
	}

	public void addNewTextButtonListener(ClickListener listener) {
		newTextButton.addClickListener(listener);
	}

	public void addNewAttachFileButtonListener(ClickListener listener) {
		newAttachFilesButton.addClickListener(listener);
	}

	public void addNewAnswerButtonListener(ClickListener listener) {
		newAnswerButton.addClickListener(listener);
	}

	public void addNewSubanswerButtonListener(ClickListener listener) {
		newSubanswerButton.addClickListener(listener);
	}

	public void addMoveButtonListener(ClickListener listener) {
		moveButton.addClickListener(listener);
	}

	public void addDeleteButtonListener(ClickListener listener) {
		deleteButton.addClickListener(listener);
	}

	public void addUpButtonListener(ClickListener listener) {
		upButton.addClickListener(listener);
	}

	public void addDownButtonListener(ClickListener listener) {
		downButton.addClickListener(listener);
	}

	public void addHideButtonListener(ClickListener listener) {
		hideButton.addClickListener(listener);
	}

	public void addNewDynamicAnswerListener(ClickListener listener) {
		newDynamicAnswer.addClickListener(listener);
	}

	public void addNewAnswerRangeListener(ClickListener listener) {
		answerRangeButton.addClickListener(listener);
	}

	public IconButton getSaveButton() {
		return saveButton;
	}

	public IconButton getSaveAsBlockButton() {
		return saveAsBlockButton;
	}

	public IconButton getInsertBlockButton() {
		return insertBlockButton;
	}

	public IconButton getLinkBlockButton() {
		return linkBlockButton;
	}

	public IconButton getNewCategoryButton() {
		return newCategoryButton;
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

	public IconButton getNewSubanswerButton() {
		return newSubanswerButton;
	}

	public IconButton getNewDynamicQuestionButton() {
		return newDynamicAnswer;
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

	public IconButton getNewDynamicAnswer() {
		return newDynamicAnswer;
	}

	public void addFinishListener(ClickListener clickListener) {
		finish.addClickListener(clickListener);
	}

	public AbstractComponent getFinish() {
		return finish;
	}

	public AbstractComponent getBlockMenu() {
		return blockMenu;
	}

	public AbstractComponent getOtherElementsMenu() {
		return otherElementsMenu;
	}

	public void updateHideButton(boolean elementIsHidden) {
		if (elementIsHidden) {
			hideButton.setCaption(LanguageCodes.COMMON_CAPTION_SHOW.translation());
			hideButton.setIcon(ThemeIcons.ELEMENT_SHOW);
			hideButton.setDescription(LanguageCodes.COMMON_CAPTION_SHOW);
		} else {
			hideButton.setCaption(LanguageCodes.COMMON_CAPTION_HIDE.translation());
			hideButton.setIcon(ThemeIcons.ELEMENT_HIDE);
			hideButton.setDescription(LanguageCodes.COMMON_CAPTION_HIDE);
		}
	}

	public IconButton getHideButton() {
		return hideButton;
	}

	public IconButton getAnswerRangeButton() {
		return answerRangeButton;
	}

	public IconButton getNewAttachFilesButton() {
		return newAttachFilesButton;
	}
}
