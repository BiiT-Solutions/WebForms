package com.biit.webforms.gui.webpages.floweditor;

import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.components.UpperMenuWebforms;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.Button.ClickListener;

public class UpperMenuFlowEditor extends UpperMenuWebforms {
	private static final long serialVersionUID = 4521719649478606926L;

	private IconButton saveButton, newFlowButton, editFlowButton, cloneFlowButton, removeFlowButton;

	public UpperMenuFlowEditor() {

		saveButton = new IconButton(LanguageCodes.COMMON_CAPTION_SAVE, ThemeIcons.FORM_SAVE,
				LanguageCodes.COMMON_TOOLTIP_SAVE, IconSize.BIG);

		newFlowButton = new IconButton(LanguageCodes.CAPTION_NEW_RULE, ThemeIcons.RULE_ADD,
				LanguageCodes.TOOLTIP_NEW_RULE, IconSize.BIG);

		editFlowButton = new IconButton(LanguageCodes.CAPTION_EDIT_RULE, ThemeIcons.RULE_EDIT,
				LanguageCodes.TOOLTIP_EDIT_RULE, IconSize.BIG);

		cloneFlowButton = new IconButton(LanguageCodes.CAPTION_CLONE_RULE, ThemeIcons.RULE_CLONE,
				LanguageCodes.TOOLTIP_CLONE_RULE, IconSize.BIG);

		removeFlowButton = new IconButton(LanguageCodes.CAPTION_REMOVE_RULE, ThemeIcons.RULE_REMOVE,
				LanguageCodes.TOOLTIP_REMOVE_RULE, IconSize.BIG);

		addIconButton(saveButton);
		addIconButton(newFlowButton);
		addIconButton(editFlowButton);
		addIconButton(cloneFlowButton);
		addIconButton(removeFlowButton);
		
		setConfirmationNeeded(true);
	}

	public void addSaveButtonListener(ClickListener listener) {
		saveButton.addClickListener(listener);
	}

	public void addNewFlowButtonListener(ClickListener listener) {
		newFlowButton.addClickListener(listener);
	}

	public void addEditFlowButtonListener(ClickListener listener) {
		editFlowButton.addClickListener(listener);
	}

	public void addCloneFlowButtonListener(ClickListener listener) {
		cloneFlowButton.addClickListener(listener);
	}

	public void addRemoveFlowButtonListener(ClickListener listener) {
		removeFlowButton.addClickListener(listener);
	}

	public IconButton getSaveButton() {
		return saveButton;
	}

	public IconButton getNewFlowButton() {
		return newFlowButton;
	}

	public IconButton getEditFlowButton() {
		return editFlowButton;
	}

	public IconButton getCloneFlowButton() {
		return cloneFlowButton;
	}

	public IconButton getRemoveFlowButton() {
		return removeFlowButton;
	}
}
