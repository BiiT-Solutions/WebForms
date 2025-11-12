package com.biit.webforms.gui.webpages.designer;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
