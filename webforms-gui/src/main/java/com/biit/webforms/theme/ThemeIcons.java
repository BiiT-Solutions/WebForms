package com.biit.webforms.theme;

import com.biit.webforms.gui.common.theme.IThemeIcon;
import com.vaadin.server.ThemeResource;

public enum ThemeIcons implements IThemeIcon{
	
	EDIT("appbar.edit.svg"),
	SETTINGS("appbar.lines.horizontal.4.svg"),
	FORM_MANAGER_PAGE("appbar.cabinet.files.svg"),
	
	EDIT_FLOW("appbar.cabinet.files.svg"), 
	SAVE("appbar.save.svg"), 
	VALIDATE("appbar.cabinet.files.svg"),
	FINISH("appbar.cabinet.files.svg");	
	
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
