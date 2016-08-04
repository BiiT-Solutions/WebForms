package com.biit.webforms.gui.webpages;

import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;

public class NotFoundPage extends ErrorPage {
	private static final long serialVersionUID = -5449790417580691336L;

	public NotFoundPage() {
		super();

		setLabelContent(LanguageCodes.PAGE_NOT_FOUND.translation());
		setImageSource(ThemeIcons.PAGE_NOT_FOUND.getThemeResource());
	}
}
