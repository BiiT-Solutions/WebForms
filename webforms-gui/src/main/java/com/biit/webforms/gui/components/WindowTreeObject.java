package com.biit.webforms.gui.components;

import com.biit.form.TreeObject;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.language.ILanguageCode;
import com.biit.webforms.gui.webpages.designer.TreeObjectTableDesigner;
import com.biit.webforms.persistence.entity.Form;
import com.vaadin.ui.Component;

public class WindowTreeObject extends WindowAcceptCancel{
	private static final long serialVersionUID = -1341380408900400222L;
	private static final String width = "640px";
	private static final String height = "480px";
	private TreeObjectTableDesigner formTable;

	public WindowTreeObject(ILanguageCode code, Form form) {
		super();
		setCaption(code.translation());
		setContent(generateContent(form));
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(width);
		setHeight(height);
	}

	private Component generateContent(Form form) {
		formTable = new TreeObjectTableDesigner();
		formTable.setSizeFull();
		formTable.setSelectable(true);
		formTable.loadTreeObject(form,null);
		
		return formTable;
	}
	
	public TreeObject getSelectedTreeObject(){
		return formTable.getSelectedRow();
	}
	
}
