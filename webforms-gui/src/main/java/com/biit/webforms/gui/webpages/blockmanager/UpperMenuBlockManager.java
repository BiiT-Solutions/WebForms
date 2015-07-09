package com.biit.webforms.gui.webpages.blockmanager;

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
