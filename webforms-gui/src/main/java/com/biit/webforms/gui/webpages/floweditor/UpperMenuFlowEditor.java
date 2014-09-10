package com.biit.webforms.gui.webpages.floweditor;

import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.components.UpperMenuWebforms;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.Button.ClickListener;

public class UpperMenuFlowEditor extends UpperMenuWebforms {
	private static final long serialVersionUID = 4521719649478606926L;
	
	private IconButton saveButton, newRuleButton, editRuleButton, validateButton, finishButton;

	public UpperMenuFlowEditor() {

		saveButton = new IconButton(LanguageCodes.COMMON_CAPTION_SAVE, ThemeIcons.FORM_SAVE,
				LanguageCodes.COMMON_TOOLTIP_SAVE, IconSize.BIG);
		
		newRuleButton = new IconButton(LanguageCodes.CAPTION_NEW_RULE, ThemeIcons.NEW_RULE,
				LanguageCodes.TOOLTIP_NEW_RULE, IconSize.BIG);
		
		editRuleButton = new IconButton(LanguageCodes.CAPTION_EDIT_RULE, ThemeIcons.EDIT_RULE,
				LanguageCodes.TOOLTIP_EDIT_RULE, IconSize.BIG);

		validateButton = new IconButton(LanguageCodes.COMMON_CAPTION_VALIDATE, ThemeIcons.FORM_VALIDATE,
				LanguageCodes.COMMON_TOOLTIP_VALIDATE, IconSize.BIG);

		finishButton = new IconButton(LanguageCodes.COMMON_CAPTION_FINISH, ThemeIcons.FORM_VALIDATE,
				LanguageCodes.COMMON_TOOLTIP_FINISH, IconSize.BIG);

		addIconButton(saveButton);
		addIconButton(newRuleButton);
		addIconButton(editRuleButton);
		addIconButton(validateButton);
		addIconButton(finishButton);
	}

	public void addSaveButtonListener(ClickListener listener) {
		saveButton.addClickListener(listener);
	}

	public void addValidateButtonListener(ClickListener listener) {
		validateButton.addClickListener(listener);
	}

	public void addFinishButtonListener(ClickListener listener) {
		finishButton.addClickListener(listener);
	}
	
	public void addNewRuleButtonListener(ClickListener listener){
		newRuleButton.addClickListener(listener);
	}
	
	public void addEditRuleButtonListener(ClickListener listener){
		editRuleButton.addClickListener(listener);
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

	public IconButton getValidateButton() {
		return validateButton;
	}

	public IconButton getFinishButton() {
		return finishButton;
	}
}
