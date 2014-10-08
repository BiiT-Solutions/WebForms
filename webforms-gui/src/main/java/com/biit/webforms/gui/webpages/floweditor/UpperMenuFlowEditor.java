package com.biit.webforms.gui.webpages.floweditor;

import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.components.UpperMenuWebforms;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.Button.ClickListener;

public class UpperMenuFlowEditor extends UpperMenuWebforms {
	private static final long serialVersionUID = 4521719649478606926L;

	private IconButton saveButton, newRuleButton, editRuleButton, cloneRuleButton, removeRuleButton, printPdfButton,
			finishButton;

	public UpperMenuFlowEditor() {

		saveButton = new IconButton(LanguageCodes.COMMON_CAPTION_SAVE, ThemeIcons.FORM_SAVE,
				LanguageCodes.COMMON_TOOLTIP_SAVE, IconSize.BIG);

		newRuleButton = new IconButton(LanguageCodes.CAPTION_NEW_RULE, ThemeIcons.RULE_ADD,
				LanguageCodes.TOOLTIP_NEW_RULE, IconSize.BIG);

		editRuleButton = new IconButton(LanguageCodes.CAPTION_EDIT_RULE, ThemeIcons.RULE_EDIT,
				LanguageCodes.TOOLTIP_EDIT_RULE, IconSize.BIG);

		cloneRuleButton = new IconButton(LanguageCodes.CAPTION_CLONE_RULE, ThemeIcons.RULE_CLONE,
				LanguageCodes.TOOLTIP_CLONE_RULE, IconSize.BIG);

		removeRuleButton = new IconButton(LanguageCodes.CAPTION_REMOVE_RULE, ThemeIcons.RULE_REMOVE,
				LanguageCodes.TOOLTIP_REMOVE_RULE, IconSize.BIG);

		printPdfButton = new IconButton(LanguageCodes.CAPTION_PRINT_FLOW, ThemeIcons.EXPORT_FORM_TO_PDF,
				LanguageCodes.TOOLTIP_PRINT_FLOW, IconSize.BIG);

		finishButton = new IconButton(LanguageCodes.COMMON_CAPTION_FINISH, ThemeIcons.FORM_FINISH,
				LanguageCodes.COMMON_TOOLTIP_FINISH, IconSize.BIG);

		addIconButton(saveButton);
		addIconButton(newRuleButton);
		addIconButton(editRuleButton);
		addIconButton(cloneRuleButton);
		addIconButton(removeRuleButton);
		addIconButton(printPdfButton);
		addIconButton(finishButton);
		
		setConfirmationNeeded(true);
	}

	public void addSaveButtonListener(ClickListener listener) {
		saveButton.addClickListener(listener);
	}

	public void addFinishButtonListener(ClickListener listener) {
		finishButton.addClickListener(listener);
	}

	public void addNewRuleButtonListener(ClickListener listener) {
		newRuleButton.addClickListener(listener);
	}

	public void addEditRuleButtonListener(ClickListener listener) {
		editRuleButton.addClickListener(listener);
	}

	public void addCloneRuleButtonListener(ClickListener listener) {
		cloneRuleButton.addClickListener(listener);
	}

	public void addRemoveRuleButtonListener(ClickListener listener) {
		removeRuleButton.addClickListener(listener);
	}

	public void addPrintPdfButtonListener(ClickListener listener) {
		printPdfButton.addClickListener(listener);
	}

	public IconButton getSaveButton() {
		return saveButton;
	}

	public IconButton getNewRuleButton() {
		return newRuleButton;
	}

	public IconButton getEditRuleButton() {
		return editRuleButton;
	}

	public IconButton getCloneRuleButton() {
		return cloneRuleButton;
	}

	public IconButton getRemoveRuleButton() {
		return removeRuleButton;
	}

	public IconButton getFinishButton() {
		return finishButton;
	}
}
