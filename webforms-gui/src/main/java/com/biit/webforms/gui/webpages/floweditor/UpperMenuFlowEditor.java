package com.biit.webforms.gui.webpages.floweditor;

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

public class UpperMenuFlowEditor extends UpperMenuWebforms {
	private static final long serialVersionUID = 4521719649478606926L;

	private IconButton saveButton, newFlowButton, editFlowButton, cloneFlowButton, removeFlowButton, cleanFlowButton;

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

		cleanFlowButton = new IconButton(LanguageCodes.CAPTION_CLEAN_FLOW, ThemeIcons.RULE_CLEAN_FLOW,
				LanguageCodes.TOOLTIP_CLEAN_FLOW, IconSize.BIG);

		addIconButton(saveButton);
		addIconButton(newFlowButton);
		addIconButton(editFlowButton);
		addIconButton(cloneFlowButton);
		addIconButton(removeFlowButton);
		addIconButton(cleanFlowButton);

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

	public void addCleanFlowButtonListener(ClickListener listener) {
		cleanFlowButton.addClickListener(listener);
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

	public IconButton getCleanFlowButton() {
		return cleanFlowButton;
	}
}
