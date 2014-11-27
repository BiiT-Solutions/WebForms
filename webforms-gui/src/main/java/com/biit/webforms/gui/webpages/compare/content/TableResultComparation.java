package com.biit.webforms.gui.webpages.compare.content;

import com.biit.webforms.gui.common.theme.CommonThemeIcon;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Table;

public class TableResultComparation extends Table {
	private static final long serialVersionUID = -6481950780301383292L;

	enum Properties {
		STATUS_ICON, RESULT_MESSAGE,
	};

	public TableResultComparation() {
		super();
		initContainerProperties();
	}

	protected void initContainerProperties() {
		addContainerProperty(Properties.STATUS_ICON, Component.class, null, LanguageCodes.CAPTION_COMPARATION.translation(),
				null, Align.CENTER);
		addContainerProperty(Properties.RESULT_MESSAGE, String.class, null, LanguageCodes.CAPTION_LABEL.translation(),
				null, Align.CENTER);

		setVisibleColumns(new Object[] { Properties.STATUS_ICON });
	}

	@SuppressWarnings("unchecked")
	public void addOk(String message) {
		Item item = addItem(getItemIds().size());
		item.getItemProperty(Properties.STATUS_ICON).setValue(getOkIcon());
		item.getItemProperty(Properties.RESULT_MESSAGE).setValue(message);
	}

	@SuppressWarnings("unchecked")
	public void addError(String message) {
		Item item = addItem(getItemIds().size());
		item.getItemProperty(Properties.STATUS_ICON).setValue(getErrorIcon());
		item.getItemProperty(Properties.RESULT_MESSAGE).setValue(message);
	}

	private Object getOkIcon() {
		Image ok = new Image(null, CommonThemeIcon.ACCEPT.getThemeResource());
		ok.setHeight("14px");
		ok.setWidth("14px");
		return ok;
	}

	private Object getErrorIcon() {
		Image error = new Image(null, ThemeIcons.ALERT.getThemeResource());
		error.setHeight("14px");
		error.setWidth("14px");
		return error;
	}

	public String getSelectedResult() {
		if (getValue() != null) {
			Object value = getItem(getValue()).getItemProperty(Properties.RESULT_MESSAGE).getValue();
			if (value != null) {
				return (String) value;
			}
		}
		return "";
	}

}
