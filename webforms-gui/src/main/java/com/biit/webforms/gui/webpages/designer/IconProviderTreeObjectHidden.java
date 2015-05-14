package com.biit.webforms.gui.webpages.designer;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.gui.common.components.IconProvider;
import com.biit.webforms.gui.common.theme.IThemeIcon;
import com.biit.webforms.theme.ThemeIcons;

public class IconProviderTreeObjectHidden extends IconProvider<TreeObject> {

	@Override
	public IThemeIcon getIcon(TreeObject object) {
		if (object.isHiddenElement()) {
			return ThemeIcons.ELEMENT_HIDE;
		}
		return null;
	}

}
