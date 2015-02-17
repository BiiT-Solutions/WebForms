package com.biit.webforms.gui.webpages.floweditor;

import com.biit.form.TreeObject;
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
