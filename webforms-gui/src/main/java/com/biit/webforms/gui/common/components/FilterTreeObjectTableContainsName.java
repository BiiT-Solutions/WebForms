package com.biit.webforms.gui.common.components;

import com.biit.form.TreeObject;
import com.vaadin.data.Item;

/**
 * Filter fot TableTreeObject. Does a contans of the ElementName.
 * 
 */
public class FilterTreeObjectTableContainsName implements IFilterContainsText {
	private static final long serialVersionUID = 852831365303680293L;
	private String text;

	@Override
	public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
		TreeObject treeObject = (TreeObject) itemId;
		String name = treeObject.getName().toLowerCase();

		if (name.contains(getFilterText())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean appliesToProperty(Object propertyId) {
		if (text == null || text.isEmpty()) {
			return true;
		}
		if (TableTreeObject.TreeObjectTableProperties.ELEMENT_NAME.equals(propertyId)) {
			return true;
		}
		return false;
	}

	@Override
	public void setFilterText(String text) {
		this.text = text.toLowerCase();
	}

	public String getFilterText() {
		return text;
	}

}
