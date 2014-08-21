package com.biit.webforms.gui.webpages.floweditor;

import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.components.UpperMenuWebforms;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.Button.ClickListener;

public class UpperMenuFlowEditor extends UpperMenuWebforms {
	private static final long serialVersionUID = 4521719649478606926L;
	
	private IconButton saveButton, designButton, validateButton, finishButton;

	public UpperMenuFlowEditor() {

		saveButton = new IconButton(LanguageCodes.COMMON_CAPTION_SAVE, ThemeIcons.SAVE,
				LanguageCodes.COMMON_TOOLTIP_SAVE, IconSize.BIG);

		designButton = new IconButton(LanguageCodes.COMMON_CAPTION_DESIGN, ThemeIcons.DESIGNER_EDITOR,
				LanguageCodes.COMMON_TOOLTIP_DESIGN, IconSize.BIG);

		validateButton = new IconButton(LanguageCodes.COMMON_CAPTION_VALIDATE, ThemeIcons.VALIDATE,
				LanguageCodes.COMMON_TOOLTIP_VALIDATE, IconSize.BIG);

		finishButton = new IconButton(LanguageCodes.COMMON_CAPTION_FINISH, ThemeIcons.VALIDATE,
				LanguageCodes.COMMON_TOOLTIP_FINISH, IconSize.BIG);

		addIconButton(saveButton);
		addIconButton(designButton);
		addIconButton(validateButton);
		addIconButton(finishButton);
	}

	public void addSaveButtonListener(ClickListener listener) {
		saveButton.addClickListener(listener);
	}

	public void addDesignButtonListener(ClickListener listener) {
		designButton.addClickListener(listener);
	}

	public void addValidateButtonListener(ClickListener listener) {
		validateButton.addClickListener(listener);
	}

	public void addFinishButtonListener(ClickListener listener) {
		finishButton.addClickListener(listener);
	}
}
