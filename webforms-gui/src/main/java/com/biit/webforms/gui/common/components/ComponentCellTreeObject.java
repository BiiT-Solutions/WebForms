package com.biit.webforms.gui.common.components;

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
