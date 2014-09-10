package com.biit.webforms.gui.common.theme;

import com.vaadin.server.ThemeResource;

public enum CommonThemeIcon implements IThemeIcon {

	ACCEPT("button.accept.svg"),

	CANCEL("button.cancel.svg"),

	TREE_OBJECT_GROUP_LOOP("group.loop.svg"),

	ELEMENT_EXPAND("element.expand.svg"),

	ELEMENT_COLLAPSE("element.collapse.svg"), 
	
	SEARCH("search.svg"),
	
	REMOVE("remove.svg");

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
