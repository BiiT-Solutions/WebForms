package com.biit.webforms.gui.common.components;

import com.biit.form.entity.TreeObject;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;

/**
 * This filter uses a treeObject to determine if an element is filtered. If the
 * order of the element is less or equal than the comparison element, then is
 * filtered.
 * 
 * 
 */
public class FilterByTreeObjectOrderGreater implements Filter {

	private static final long serialVersionUID = -7366271561916347424L;

	private TreeObject filterSeed;

	@Override
	public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
		TreeObject treeObject = (TreeObject) itemId;
		if (filterSeed.compareTo(treeObject) >= 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean appliesToProperty(Object propertyId) {
		return true;
	}

	public TreeObject getFilterSeed() {
		return filterSeed;
	}

	public void setFilterSeed(TreeObject filterSeed) {
		this.filterSeed = filterSeed;
	}
}
