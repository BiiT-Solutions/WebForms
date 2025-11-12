package com.biit.webforms.gui.common.components;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
