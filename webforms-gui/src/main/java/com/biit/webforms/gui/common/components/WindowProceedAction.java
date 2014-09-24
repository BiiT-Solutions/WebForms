package com.biit.webforms.gui.common.components;

import com.biit.webforms.language.LanguageCodes;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class WindowProceedAction extends WindowAcceptCancel{
	private static final long serialVersionUID = -2111506182459100300L;
	private static final String width = "450px";
	private static final String height = "200px";
	
	private Label label;
	
	public WindowProceedAction(LanguageCodes code, final AcceptActionListener listener){
		super();
		setContent(generateContent(code.translation()));
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(width);
		setHeight(height);
		
		addAcceptActionListener(new AcceptActionListener() {
			
			@Override
			public void acceptAction(WindowAcceptCancel window) {
				listener.acceptAction(window);
				window.close();
			}
		});
		showCentered();
	}

	private Component generateContent(String text) {
		
		label = new Label("<div style=\"text-align: center;\">"+text+"</div>");
		label.setContentMode(ContentMode.HTML);
		
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();

		rootLayout.addComponent(label);
		rootLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
		return rootLayout;
	}
	

}
