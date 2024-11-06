package com.biit.webforms.gui.webpages.designer;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.gui.common.components.IconProvider;
import com.biit.webforms.gui.common.theme.IThemeIcon;
import com.biit.webforms.persistence.entity.ElementWithMedia;
import com.biit.webforms.theme.ThemeIcons;

public class IconProviderTreeObjectImage extends IconProvider<TreeObject> {

	@Override
	public IThemeIcon getIcon(TreeObject object) {
		if (object instanceof ElementWithMedia) {
			if (((ElementWithMedia) object).getImage() != null) {
				return ThemeIcons.IMAGE;
			}
		}
		return null;
	}

}
