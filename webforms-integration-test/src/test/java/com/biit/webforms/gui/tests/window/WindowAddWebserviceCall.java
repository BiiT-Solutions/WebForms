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

import com.vaadin.testbench.elements.TableElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.WindowElement;

public class WindowAddWebserviceCall extends GenericAcceptCancelWindow {

	private static final String WINDOW_ID = "com.biit.webforms.gui.webpages.webservice.call.WindowWebservices";

	private static final String NAME_FIELD_CAPTION = "Name";

	private TextFieldElement getNameTextField() {
		return getWindow().$(TextFieldElement.class).caption(NAME_FIELD_CAPTION).first();
	}

	private TableElement getWebserviceTable() {
		return $$(WindowElement.class).id("com.biit.webforms.gui.webpages.webservice.call.WindowWebservices")
				.$(TableElement.class).first();
	}

	public void createNewWebserviceCall(String name, int row) {
		getNameTextField().setValue(name);
		getWebserviceTable().getCell(row, 0).click();
		clickAccept();
	}

	@Override
	protected String getWindowId() {
		return WINDOW_ID;
	}

}
