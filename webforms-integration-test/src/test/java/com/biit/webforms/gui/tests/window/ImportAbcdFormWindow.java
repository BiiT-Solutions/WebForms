package com.biit.webforms.gui.tests.window;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Test)
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

import com.vaadin.testbench.elements.TreeTableElement;


public class ImportAbcdFormWindow extends GenericAcceptCancelWindow {

	private static final Integer TREE_TABLE_FORM_ROW = 1;
	private static final Integer TREE_TABLE_FORM_COLUMN = 0;
	private static final String CLASS_NAME = "com.biit.webforms.gui.webpages.formmanager.WindowImportAbcdForms";
	
	@Override
	protected String getWindowId() {
		return CLASS_NAME;
	}
	
	private TreeTableElement getTreeTable(){
		return getWindow().$(TreeTableElement.class).first();
	}
	
	public void selectAbcdForm(){
		getTreeTable().getCell(TREE_TABLE_FORM_ROW, TREE_TABLE_FORM_COLUMN).click();
	}

}
