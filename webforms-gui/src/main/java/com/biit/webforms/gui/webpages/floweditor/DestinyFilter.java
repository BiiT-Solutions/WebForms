package com.biit.webforms.gui.webpages.floweditor;

import com.biit.form.TreeObject;
import com.biit.webforms.persistence.entity.Flow;
import com.vaadin.data.Item;

/**
 * Table filter of flows by destiny.
 *
 */
public class DestinyFilter extends OriginFilter {
	private static final long serialVersionUID = -7194892064324001003L;

	public DestinyFilter(TreeObject filter, Object newFlow) {
		super(filter, newFlow);
	}

	@Override
	public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
		if (filter == null || (itemId != null && itemId.equals(newFlow))) {
			return true;
		}
		Flow flow = (Flow) itemId;
		return filter.equals(flow.getDestiny()) || filter.contains(flow.getDestiny());
	}
}