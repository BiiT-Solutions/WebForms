package com.biit.webforms.gui.common.theme;

import com.vaadin.server.ThemeResource;


public enum CommonThemeIcon implements IThemeIcon{
	
	ACCEPT("appbar.check.svg"),

	CANCEL("appbar.close.svg");
	
	private String value;

	CommonThemeIcon(String value) {
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
