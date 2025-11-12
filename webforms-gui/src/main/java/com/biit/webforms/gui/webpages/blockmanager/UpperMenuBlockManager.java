package com.biit.webforms.gui.webpages.blockmanager;

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

/**
 * Upper menu of block Manager webpage.
 *
 */
public class UpperMenuBlockManager extends UpperMenuWebforms {
	private static final long serialVersionUID = 6951880828390670219L;

	private IconButton newBlock, removeBlock;

	public UpperMenuBlockManager() {
		super();

		addButtons();
	}

	private void addButtons() {
		newBlock = new IconButton(LanguageCodes.CAPTION_NEW_BLOCK, ThemeIcons.BUILDING_BLOCK_ADD,
				LanguageCodes.TOOLTIP_NEW_BLOCK, IconSize.BIG);

		addIconButton(newBlock);

		removeBlock = new IconButton(LanguageCodes.CAPTION_REMOVE_BLOCK, ThemeIcons.DELETE_BUILDING_BLOCK,
				LanguageCodes.CAPTION_REMOVE_BLOCK, IconSize.MEDIUM);
		addIconButton(removeBlock);
	}

	public void addNewBlockListener(ClickListener listener) {
		newBlock.addClickListener(listener);
	}

	public IconButton getNewBlock() {
		return newBlock;
	}
	
	public void addRemoveBlock(ClickListener listener){
		removeBlock.addClickListener(listener);
	}

	public IconButton getRemoveBlock() {
		return removeBlock;
	}

}
