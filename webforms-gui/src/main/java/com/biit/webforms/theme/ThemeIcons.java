package com.biit.webforms.theme;

import com.biit.webforms.gui.common.theme.IThemeIcon;
import com.vaadin.server.ThemeResource;

public enum ThemeIcons implements IThemeIcon{
	
	EDIT("appbar.edit.svg"),
	SETTINGS("appbar.lines.horizontal.4.svg"),
	FORM_MANAGER_PAGE("appbar.cabinet.files.svg"),
	
	FLOW_EDITOR("appbar.cabinet.files.svg"), 
	SAVE("appbar.save.svg"), 
	VALIDATE("appbar.cabinet.files.svg"),
	FINISH("appbar.cabinet.files.svg"), 
	DESIGNER_EDITOR("appbar.cabinet.files.svg"),
	
	DESIGNER_QUESTION_CHECKLIST("appbar.checkmark.svg"),
	DESIGNER_QUESTION_RADIOBUTTON("appbar.radiobutton.svg"),
	DESIGNER_QUESTION_DATE("appbar.calendar.31.svg"),
	DESIGNER_QUESTION_NUMBER("appbar.interface.textbox.number.svg"),
	DESIGNER_QUESTION_POSTALCODE("appbar.email.hardedge.svg"),
	DESIGNER_QUESTION_TEXT("appbar.interface.textbox.squared.svg");	
	
	private String value;

	ThemeIcons(String value) {
		this.value = value;
	}

	@Override
	public ThemeResource getThemeResource() {
		return new ThemeResource(value);
	}

	@Override
	public String getFile() {
		return value;
	}

}
