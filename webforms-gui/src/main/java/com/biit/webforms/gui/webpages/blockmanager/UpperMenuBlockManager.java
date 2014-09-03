package com.biit.webforms.gui.webpages.blockmanager;

import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.components.UpperMenuWebforms;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.Button.ClickListener;

public class UpperMenuBlockManager extends UpperMenuWebforms{
	private static final long serialVersionUID = 6951880828390670219L;

	private IconButton newBlock, editDesign, editFlow;
	
	public UpperMenuBlockManager(){
		super();
		
		addButtons();
	}

	private void addButtons() {
		newBlock = new IconButton(LanguageCodes.CAPTION_NEW_BLOCK, ThemeIcons.BUILDING_BLOCK_ADD,
				LanguageCodes.TOOLTIP_NEW_BLOCK, IconSize.BIG);
		editDesign = new IconButton(LanguageCodes.COMMON_CAPTION_DESIGN, ThemeIcons.PAGE_BUILDING_BLOCK_EDITOR,
				LanguageCodes.COMMON_TOOLTIP_DESIGN, IconSize.BIG);
		editFlow = new IconButton(LanguageCodes.COMMON_CAPTION_FLOW, ThemeIcons.PAGE_BUILDING_BLOCK_FLOW_EDITOR,
				LanguageCodes.COMMON_TOOLTIP_FLOW, IconSize.BIG);
		
		addIconButton(newBlock);
		addIconButton(editDesign);
		addIconButton(editFlow);
	}
	
	public void addNewBlockListener(ClickListener listener){
		newBlock.addClickListener(listener);
	}
	
	public void addEditDesignListener(ClickListener listener){
		editDesign.addClickListener(listener);
	}
	
	public void addEditFlowListener(ClickListener listener){
		editFlow.addClickListener(listener);
	}

	public IconButton getNewBlock() {
		return newBlock;
	}

	public IconButton getEditDesign() {
		return editDesign;
	}

	public IconButton getEditFlow() {
		return editFlow;
	}
}
