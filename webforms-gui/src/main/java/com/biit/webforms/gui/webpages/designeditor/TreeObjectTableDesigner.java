package com.biit.webforms.gui.webpages.designeditor;

import com.biit.form.TreeObject;
import com.biit.webforms.gui.common.components.TreeObjectTable;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.data.Item;

public class TreeObjectTableDesigner extends TreeObjectTable {
	private static final long serialVersionUID = 2882643672843469056L;

	protected enum TreeObjectTableDesignerProperties {
		ELEMENT_LABEL
	};

	@Override
	protected void initContainerProperties() {
		super.initContainerProperties();
		addContainerProperty(TreeObjectTableDesignerProperties.ELEMENT_LABEL, String.class, null,
				LanguageCodes.CAPTION_LABEL.translation(), null, Align.LEFT);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void setValuesToItem(Item item, TreeObject element) {
		super.setValuesToItem(item, element);
		String label = element.getLabel();
		if (label == null) {
			label = new String();
		}
		item.getItemProperty(TreeObjectTableDesignerProperties.ELEMENT_LABEL).setValue(label);
	}
}
