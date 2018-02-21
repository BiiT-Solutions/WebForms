package com.biit.webforms.gui.webpages.designer;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.gui.common.components.IconProvider;
import com.biit.webforms.gui.common.theme.IThemeIcon;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.theme.ThemeIcons;

public class IconProviderTreeObjectExtraInformation extends IconProvider<TreeObject> {

	@Override
	public IThemeIcon getIcon(TreeObject object) {
		if (object.isHiddenElement()) {
			return ThemeIcons.ELEMENT_HIDE;
			// Add image icon.
		}
		if (object instanceof Group) {
			Group group = (Group) object;
			if (group.isRepeatable()) {
				return ThemeIcons.DESIGNER_GROUP_LOOP;
			}
		}
		if (object instanceof Question) {
			if (object.getParent() != null && object.getParent() instanceof Group) {
				if (object.getParent().getParent() != null && object.getParent().getParent() instanceof Group) {
					if (((Group) object.getParent().getParent()).isShownAsTable()) {
						return ThemeIcons.DESIGNER_GROUP_TABLE_COLUMN;
					}
				}
			}
		}
		return null;
	}
}
