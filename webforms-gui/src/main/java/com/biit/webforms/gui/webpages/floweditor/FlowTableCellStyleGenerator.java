package com.biit.webforms.gui.webpages.floweditor;

import com.biit.webforms.persistence.entity.Flow;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.CellStyleGenerator;

public class FlowTableCellStyleGenerator implements CellStyleGenerator {
	private static final long serialVersionUID = 5431376183460313567L;

	@Override
	public String getStyle(Table source, Object itemId, Object propertyId) {
		String styles = "";
		if (itemId instanceof Flow) {
			if (((Flow) itemId).isReadOnly()) {
				styles += "tree-cell-disabled ";
			}
		}
		return styles;
	}

}
