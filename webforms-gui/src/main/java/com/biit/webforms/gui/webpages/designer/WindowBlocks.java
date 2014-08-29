package com.biit.webforms.gui.webpages.designer;

import com.biit.form.TreeObject;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.language.ILanguageCode;
import com.vaadin.ui.Component;

public class WindowBlocks extends WindowAcceptCancel {
	private static final long serialVersionUID = -359502175714054679L;
	private static final String width = "640px";
	private static final String height = "480px";
	private BlockTreeTable blockTable;

	public WindowBlocks(ILanguageCode code) {
		super();
		setCaption(code.translation());
		setContent(generateContent());
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(width);
		setHeight(height);
	}

	private Component generateContent() {
		blockTable = new BlockTreeTable();
		blockTable.setSizeFull();
		blockTable.setSelectable(true);
		
		return blockTable;
	}
	
	public TreeObject getSelectedBlock(){
		return blockTable.getSelectedRow();
	}

}
