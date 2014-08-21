package com.biit.webforms.gui.common.components;

import com.biit.form.TreeObject;
import com.biit.webforms.gui.common.theme.IThemeIcon;

public class ComponentCellTreeObject extends ComponentCell {
	private static final long serialVersionUID = 76595396879136434L;

	public void update(TreeObject treeObject) {
		clear();
		addLabel(treeObject.getName());
		addIcon(getIcon(treeObject));
	}

	/**
	 * This function needs to be overriden to set the icons for each case.
	 * 
	 * @param element
	 * @return
	 */
	protected IThemeIcon getIcon(TreeObject element) {
		return null;
	}
}
