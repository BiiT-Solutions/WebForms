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
