package com.biit.webforms.gui.webpages.floweditor;

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
import com.biit.webforms.persistence.entity.Flow;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;

/**
 * Table filter of flows by origin.
 *
 */
public class OriginFilter implements Filter {
	private static final long serialVersionUID = -8541390160081477981L;

	protected TreeObject filter;
	protected Object newFlow;

	public OriginFilter(TreeObject filter, Object newFlow) {
		super();
		this.filter = filter;
		this.newFlow = newFlow;
	}

	public TreeObject getFilter() {
		return filter;
	}

	public void setFilter(TreeObject filter) {
		this.filter = filter;
	}

	@Override
	public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
		if (filter == null || (itemId != null && itemId.equals(newFlow)) || (itemId == null)) {
			return true;
		}
		Flow flow = (Flow) itemId;
		return filter.equals(flow.getOrigin()) || filter.contains(flow.getOrigin());
	}

	@Override
	public boolean appliesToProperty(Object propertyId) {
		// Doesn't apply to any property.
		return false;
	}
};
