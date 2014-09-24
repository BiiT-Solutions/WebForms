package com.biit.webforms.gui.webpages.designer;

import com.biit.form.TreeObject;
import com.biit.webforms.gui.common.components.TreeObjectTable;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.data.Item;

public class TreeObjectTableDesigner extends TreeObjectTable {
	private static final long serialVersionUID = 2882643672843469056L;
	private static final int LABEL_MAX_LENGTH = 50;

	protected enum TreeObjectTableDesignerProperties {
		ELEMENT_LABEL
	};

	@Override
	protected void initContainerProperties() {
		super.initContainerProperties();
		addContainerProperty(TreeObjectTableDesignerProperties.ELEMENT_LABEL, String.class, null,
				LanguageCodes.CAPTION_LABEL.translation(), null, Align.LEFT);
	}

	@Override
	protected void setValuesToItem(Item item, TreeObject element) {
		super.setValuesToItem(item, element);
		String label = element.getLabel();
		if (label == null) {
			label = new String();
		}
		setLabelToItem(item,label);
	}
	
	/**
	 * Sets label to item and cuts label to max length.
	 * @param item
	 * @param label
	 */
	@SuppressWarnings("unchecked")
	protected void setLabelToItem(Item item, String label){
		if(label.length()>LABEL_MAX_LENGTH){
			label = label.substring(0, LABEL_MAX_LENGTH-1);
			label +="...";
		}
		item.getItemProperty(TreeObjectTableDesignerProperties.ELEMENT_LABEL).setValue(label);
	}
}
