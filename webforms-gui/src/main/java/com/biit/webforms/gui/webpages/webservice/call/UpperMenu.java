package com.biit.webforms.gui.webpages.webservice.call;

import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.components.UpperMenuWebforms;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;

public class UpperMenu extends UpperMenuWebforms {
	private static final long serialVersionUID = 6910390631017964877L;

	private final IconButton saveButton;
	
	public UpperMenu(){
		saveButton = new IconButton(LanguageCodes.COMMON_CAPTION_SAVE, ThemeIcons.FORM_SAVE,
				LanguageCodes.COMMON_TOOLTIP_SAVE, IconSize.BIG);
		
		addIconButton(saveButton);
	}

	public IconButton getSaveButton() {
		return saveButton;
	}
}
