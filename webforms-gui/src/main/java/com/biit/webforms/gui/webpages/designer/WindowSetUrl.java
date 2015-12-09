package com.biit.webforms.gui.webpages.designer;

import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.ui.TextField;

public class WindowSetUrl extends WindowAcceptCancel {
	private static final long serialVersionUID = 2517104955287396556L;
	private static final String width = "640px";
	private static final String height = "180px";
	private final TextField urlField;

	public WindowSetUrl() {
		super();
		setCaption(LanguageCodes.CAPTION_PROPERTIES_IMAGE_URL.translation());
		urlField = new TextField(LanguageCodes.CAPTION_WINDOW_IMAGE_URL.translation());
		urlField.setWidth("80%");
		setContent(urlField);
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(width);
		setHeight(height);
	}

	public String getUrl() {
		return urlField.getValue();
	}

}
