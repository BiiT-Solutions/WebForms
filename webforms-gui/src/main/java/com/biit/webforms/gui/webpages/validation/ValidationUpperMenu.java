package com.biit.webforms.gui.webpages.validation;

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
