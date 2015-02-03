package com.biit.webforms.gui.common.components;

import com.biit.webforms.language.LanguageCodes;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class WindowTextArea extends WindowAcceptCancel {
	private static final long serialVersionUID = 4740162877392137594L;

	private static final String WIDTH = "300px";
	private static final String HEIGHT = "300px";

	private TextArea textArea;

	public WindowTextArea(String inputFieldCaption) {
		super();
		setContent(generateContent(inputFieldCaption));
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(WIDTH);
		setHeight(HEIGHT);
		getAcceptButton().setVisible(false);
		getCancelButton().setCaption(LanguageCodes.CAPTION_CLOSE.translation());
		getCancelButton().setDescription(LanguageCodes.CAPTION_CLOSE.translation());
	}

	public String getValue() {
		return textArea.getValue();
	}

	private Component generateContent(String inputFieldCaption) {
		textArea = new TextArea(inputFieldCaption);
		textArea.focus();
		textArea.setSizeFull();

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();

		rootLayout.addComponent(textArea);
		rootLayout.setComponentAlignment(textArea, Alignment.MIDDLE_CENTER);
		return rootLayout;
	}

	public void setValue(String value) {
		textArea.setValue(value);
	}

	
	public void setTextReadOnly(boolean readOnly) {
		textArea.setReadOnly(readOnly);
	}
}
