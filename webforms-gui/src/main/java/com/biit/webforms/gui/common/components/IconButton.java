package com.biit.webforms.gui.common.components;

import com.biit.webforms.gui.common.language.ILanguageCode;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.theme.IThemeIcon;
import com.vaadin.ui.Button;

public class IconButton extends Button {
	private static final long serialVersionUID = -8287465276670542699L;
	private final static IconSize defaultIconSize = IconSize.SMALL;

	public IconButton(IThemeIcon icon, IconSize size, ILanguageCode tooltip) {
		super("");
		createButton(icon, size, tooltip);
		addStyleName("link");
	}

	public IconButton(IThemeIcon icon, IconSize size, String tooltip) {
		super("");
		createButton(icon, size, tooltip);
		addStyleName("link");
	}

	public IconButton(IThemeIcon icon, ILanguageCode tooltip, IconSize size, ClickListener clickListener) {
		super("", clickListener);
		createButton(icon, size, tooltip);
		addStyleName("link");
	}

	public IconButton(IThemeIcon icon, ILanguageCode tooltip, ClickListener clickListener) {
		super("", clickListener);
		createButton(icon, defaultIconSize, tooltip);
		addStyleName("link");
	}

	public IconButton(ILanguageCode caption, IThemeIcon icon, ILanguageCode tooltip, IconSize size,
			ClickListener clickListener) {
		super(ServerTranslate.translate(caption), clickListener);
		createButton(icon, size, tooltip);
	}

	public IconButton(ILanguageCode caption, IThemeIcon icon, ILanguageCode tooltip, ClickListener clickListener) {
		super(ServerTranslate.translate(caption), clickListener);
		createButton(icon, defaultIconSize, tooltip);
	}

	public IconButton(ILanguageCode caption, IThemeIcon icon, ILanguageCode tooltip, IconSize size) {
		super(ServerTranslate.translate(caption));
		createButton(icon, size, tooltip);
	}

	public IconButton(ILanguageCode caption, IThemeIcon icon, ILanguageCode tooltip) {
		super(ServerTranslate.translate(caption));
		createButton(icon, defaultIconSize, tooltip);
	}

	public IconButton(ILanguageCode caption, IThemeIcon icon, String tooltip) {
		super(ServerTranslate.translate(caption));
		createButton(icon, defaultIconSize, tooltip);
	}

	public void setIcon(IThemeIcon icon) {
		setIcon(icon, defaultIconSize);
	}

	public void setIcon(IThemeIcon icon, IconSize size) {
		if (icon != null && (!size.equals(IconSize.NULL))) {
			addStyleName(size.getSyle());
			setIcon(icon.getThemeResource());

		}
	}

	private void createButton(IThemeIcon icon, IconSize size, ILanguageCode tooltip) {
		setIcon(icon, size);
		setDescription(tooltip);
		setImmediate(true);
	}

	private void createButton(IThemeIcon icon, IconSize size, String tooltip) {
		setIcon(icon, size);
		setDescription(tooltip);
		setImmediate(true);
	}

	public void setDescription(ILanguageCode tooltip) {
		setDescription(ServerTranslate.translate(tooltip));
	}
}
