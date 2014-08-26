package com.biit.webforms.gui.common.components;

import com.biit.form.TreeObject;

public class ComponentCellTreeObject extends ComponentCell {
	private static final long serialVersionUID = 76595396879136434L;

	private IconProvider<TreeObject> iconProvider;

	public ComponentCellTreeObject(IconProvider<TreeObject> iconProvider) {
		this.iconProvider = iconProvider;
	}

	public void update(TreeObject treeObject) {
		clear();
		addLabel(treeObject.getName());
		addIcon(iconProvider.getIcon(treeObject));
	}

}
