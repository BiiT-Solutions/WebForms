package com.biit.webforms.gui.webpages.designer;

import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.components.UpperMenuWebforms;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.Button.ClickListener;

public class UpperMenuDesigner extends UpperMenuWebforms{

	private static final long serialVersionUID = -368255188051163986L;
	
	private IconButton saveButton, flowButton, validateButton, finishButton;

	public UpperMenuDesigner() {
		
		saveButton = new IconButton(LanguageCodes.COMMON_CAPTION_SAVE, ThemeIcons.SAVE,
				LanguageCodes.COMMON_TOOLTIP_SAVE, IconSize.BIG);
		
		flowButton = new IconButton(LanguageCodes.COMMON_CAPTION_FLOW, ThemeIcons.EDIT_FLOW,
				LanguageCodes.COMMON_TOOLTIP_FLOW, IconSize.BIG);
		
		validateButton = new IconButton(LanguageCodes.COMMON_CAPTION_VALIDATE, ThemeIcons.VALIDATE,
				LanguageCodes.COMMON_TOOLTIP_VALIDATE, IconSize.BIG);
		
		finishButton = new IconButton(LanguageCodes.COMMON_CAPTION_FINISH, ThemeIcons.VALIDATE,
				LanguageCodes.COMMON_TOOLTIP_FINISH, IconSize.BIG);
		
		addIconButton(saveButton);
		addIconButton(flowButton);
		addIconButton(validateButton);
		addIconButton(finishButton);
	}
	
	public void addSaveButtonListener(ClickListener listener){
		saveButton.addClickListener(listener);
	}
	
	public void addFlowButtonListener(ClickListener listener){
		flowButton.addClickListener(listener);
	}
	
	public void addValidateButtonListener(ClickListener listener){
		validateButton.addClickListener(listener);
	}
	
	public void addFinishButtonListener(ClickListener listener){
		finishButton.addClickListener(listener);
	}
}
