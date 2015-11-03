package com.biit.webforms.gui.webpages.designer;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.gui.common.components.IconProvider;
import com.biit.webforms.gui.common.theme.IThemeIcon;
import com.biit.webforms.persistence.entity.ElementWithImage;
import com.biit.webforms.theme.ThemeIcons;

public class IconProviderTreeObjectExtraInformation extends IconProvider<TreeObject> {

	@Override
	public IThemeIcon getIcon(TreeObject object) {
		if (object.isHiddenElement()) {
			return ThemeIcons.ELEMENT_HIDE;
			// Add image icon.
		} else if (object instanceof ElementWithImage) {
			if (((ElementWithImage) object).getImage() != null) {
				return ThemeIcons.IMAGE;
			}
		}
		return null;
	}

}
