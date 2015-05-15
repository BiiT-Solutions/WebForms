package com.biit.webforms.gui.common.components;

import com.biit.form.entity.BaseForm;
import com.biit.form.entity.TreeObject;

public class ComponentCellTreeObject extends ComponentCell {
	private static final long serialVersionUID = 76595396879136434L;

	private IconProvider<TreeObject> iconProvider;
	private IconProvider<TreeObject> statusIconProvider;

	public ComponentCellTreeObject(IconProvider<TreeObject> iconProvider, IconProvider<TreeObject> statusIconProvider) {
		this.iconProvider = iconProvider;
		this.statusIconProvider = statusIconProvider;
	}

	public void update(TreeObject treeObject) {
		clear();
		if (treeObject instanceof BaseForm) {
			addLabel(treeObject.getLabel());
		} else {
			addLabel(treeObject.getName());
		}
		if (iconProvider.getIcon(treeObject) != null) {
			addIcon(iconProvider.getIcon(treeObject));
		}
		if (statusIconProvider.getIcon(treeObject) != null) {
			addIcon(statusIconProvider.getIcon(treeObject));
		}
	}

}
