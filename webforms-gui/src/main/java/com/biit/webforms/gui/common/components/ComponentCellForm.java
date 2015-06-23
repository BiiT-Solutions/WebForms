package com.biit.webforms.gui.common.components;

import com.biit.form.entity.IBaseFormView;

public class ComponentCellForm extends ComponentCell {
	private static final long serialVersionUID = 76595396879136434L;

	private IconProvider<IBaseFormView> iconProvider;

	public ComponentCellForm(IconProvider<IBaseFormView> iconProvider) {
		this.iconProvider = iconProvider;
	}

	public void update(IBaseFormView form) {
		unregisterTouchCallback();
		clear();
		addLabel(form.getLabel());
		if (iconProvider.getIcon(form) != null) {
			addIcon(iconProvider.getIcon(form));
		}
		registerTouchCallback();
	}

}
