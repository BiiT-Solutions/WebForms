package com.biit.webforms.gui.webpages.validation;

import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.components.UpperMenuWebforms;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.Button.ClickListener;

public class ValidationUpperMenu extends UpperMenuWebforms {

	private static final long serialVersionUID = 8072439945816031352L;

	private final IconButton completeValidation, validateStructure, validateFlow, validateAbcdLink, abcdCompare;

	public ValidationUpperMenu() {

		completeValidation = new IconButton(LanguageCodes.CAPTION_COMPLETE_VALIDATION, ThemeIcons.COMPLETE_VALIDATION,
				LanguageCodes.TOOLTIP_COMPLETE_VALIDATION, IconSize.BIG);

		validateStructure = new IconButton(LanguageCodes.CAPTION_VALIDATE_STRUCTURE, ThemeIcons.VALIDATE_STRUCTURE,
				LanguageCodes.TOOLTIP_VALIDATE_STRUCTURE, IconSize.BIG);

		validateFlow = new IconButton(LanguageCodes.CAPTION_VALIDATE_FLOW, ThemeIcons.VALIDATE_FLOW,
				LanguageCodes.TOOLTIP_VALIDATE_FLOW, IconSize.BIG);

		validateAbcdLink = new IconButton(LanguageCodes.CAPTION_VALIDATE_ABCD_LINK, ThemeIcons.VALIDATE_ABCD_LINK,
				LanguageCodes.TOOLTIP_VALIDATE_ABCD_LINK, IconSize.BIG);
		
		abcdCompare = new IconButton(LanguageCodes.CAPTION_COMPARE_ABCD_LINK, ThemeIcons.COMPARE_ABCD_LINK,
				LanguageCodes.TOOLTIP_COMPARE_ABCD_LINK, IconSize.BIG);

		addIconButton(completeValidation);
		addIconButton(validateStructure);
		addIconButton(validateFlow);
		addIconButton(validateAbcdLink);
		addIconButton(abcdCompare);

		setConfirmationNeeded(true);
	}

	public void addCompleteValidationListener(ClickListener listener) {
		completeValidation.addClickListener(listener);
	}

	public void addValidateStructureValidationListener(ClickListener listener) {
		validateStructure.addClickListener(listener);
	}

	public void addValidateFlowListener(ClickListener listener) {
		validateFlow.addClickListener(listener);
	}

	public void addValidateAbcdLinkListener(ClickListener listener) {
		validateAbcdLink.addClickListener(listener);
	}
	
	public void addAbcdCompareListener(ClickListener listener){
		abcdCompare.addClickListener(listener);
	}

	public IconButton getCompleteValidation() {
		return completeValidation;
	}

	public IconButton getValidateStructure() {
		return validateStructure;
	}

	public IconButton getValidateFlow() {
		return validateFlow;
	}

	public IconButton getValidateAbcdLink() {
		return validateAbcdLink;
	}

	public IconButton getAbcdCompare() {
		return abcdCompare;
	}
}
