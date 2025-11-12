package com.biit.webforms.gui.common.components;

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

import com.biit.form.entity.BaseForm;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.persistence.entity.DynamicAnswer;

public class ComponentCellTreeObject extends ComponentCell {
	private static final long serialVersionUID = 76595396879136434L;

	private IconProvider<TreeObject> iconProvider;
	private IconProvider<TreeObject> statusIconProvider;
	private IconProvider<TreeObject> imageIconProvider;

	public ComponentCellTreeObject(IconProvider<TreeObject> iconProvider, IconProvider<TreeObject> statusIconProvider,
			IconProvider<TreeObject> imageIconProvider) {
		this.iconProvider = iconProvider;
		this.statusIconProvider = statusIconProvider;
		this.imageIconProvider = imageIconProvider;
	}

	public void update(TreeObject treeObject) {
		unregisterTouchCallback();
		clear();
		if (treeObject instanceof BaseForm) {
			addLabel(treeObject.getLabel());
		} else if (treeObject instanceof DynamicAnswer) {
			addLabel(((DynamicAnswer) treeObject).getReferenceName());
		} else {
			addLabel(treeObject.getName());
		}
		if (iconProvider.getIcon(treeObject) != null) {
			addIcon(iconProvider.getIcon(treeObject));
		}
		if (statusIconProvider.getIcon(treeObject) != null) {
			addIcon(statusIconProvider.getIcon(treeObject));
		}
		if (imageIconProvider.getIcon(treeObject) != null) {
			addIcon(imageIconProvider.getIcon(treeObject));
		}
		registerTouchCallback();
	}

}
