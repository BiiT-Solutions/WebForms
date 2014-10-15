package com.biit.webforms.gui.webpages.formmanager;

import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class WindowImportAbcdForm extends WindowAcceptCancel{
	private static final long serialVersionUID = 4894722469159293545L;
	private static final String WINDOW_WIDTH = "60%";
	private static final String WINDOW_HEIGHT = "80%";
	
	private final TreeTableAbcdForm table;

	public WindowImportAbcdForm(IFormDao formDao) {
		super();
		
		table = new TreeTableAbcdForm(formDao);
		
		configure();
		
		setContent(generateContent());
	}

	private void configure() {
		setWidth(WINDOW_WIDTH);
		setHeight(WINDOW_HEIGHT);
		setResizable(false);
	}

	private Component generateContent() {
		VerticalLayout rootLayout= new VerticalLayout();
		rootLayout.setSizeFull();
		
		table.setSizeFull();
		
		rootLayout.addComponent(table);
		return rootLayout;
	}
}
