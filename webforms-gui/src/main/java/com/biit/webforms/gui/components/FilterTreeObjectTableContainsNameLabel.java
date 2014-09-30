package com.biit.webforms.gui.components;

import com.biit.webforms.gui.common.components.FilterTreeObjectTableContainsName;
import com.vaadin.data.Item;

/**
 * Filter for TreeObjectTable. Does a contains of the Name (super) and the label
 * properties.
 * 
 */
public class FilterTreeObjectTableContainsNameLabel extends FilterTreeObjectTableContainsName {
	private static final long serialVersionUID = 3867634034792955643L;

	@Override
	public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
		if (super.passesFilter(itemId, item)) {
			return true;
		}

		String name = ((String) item.getItemProperty(
				TableTreeObjectLabel.TreeObjectTableDesignerProperties.ELEMENT_LABEL).getValue()).toLowerCase();
		if (name.contains(getFilterText())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean appliesToProperty(Object propertyId) {
		if (TableTreeObjectLabel.TreeObjectTableDesignerProperties.ELEMENT_LABEL.equals(propertyId)) {
			return true;
		}
		return super.appliesToProperty(propertyId);
	}
}
