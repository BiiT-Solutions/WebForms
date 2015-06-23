package com.biit.webforms.gui.webpages.formmanager;

import com.biit.form.entity.IBaseFormView;
import com.biit.webforms.gui.common.components.IconProvider;
import com.biit.webforms.gui.common.theme.IThemeIcon;
import com.biit.webforms.persistence.entity.SimpleFormView;
import com.biit.webforms.theme.ThemeIcons;

public class IconProviderFormLinked extends IconProvider<IBaseFormView> {

	@Override
	public IThemeIcon getIcon(IBaseFormView object) {
		if (object instanceof SimpleFormView) {
			if (((SimpleFormView) object).getFormReferenceId() != null) {
				return ThemeIcons.FORM_MANAGER_FORM_LINKED;
			}
		}
		return null;
	}

}
