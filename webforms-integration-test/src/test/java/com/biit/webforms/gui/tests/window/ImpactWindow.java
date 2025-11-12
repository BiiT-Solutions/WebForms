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

import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.ComboBoxElement;

public class ImpactWindow extends GenericAcceptCancelWindow {

	private static final String CLASS_NAME = "com.biit.webforms.gui.webpages.formmanager.WindowImpactAnalysis";
	private static final String COMBOBOX_CAPTION = "Version";
	private static final String COMBOBOX_VALUE = "1";
	private static final String CLOSE_ELEMENT_NAME = "v-window-closebox";

	private void setComboBoxValue() {
		getWindow().$(ComboBoxElement.class).caption(COMBOBOX_CAPTION).first().selectByText(COMBOBOX_VALUE);
	}

	public void clickAcceptButton() {
		setComboBoxValue();
		clickAccept();
	}

	public void closeWindow() {
		getWindow().findElement(By.className(CLOSE_ELEMENT_NAME)).click();
	}

	@Override
	protected String getWindowId() {
		return CLASS_NAME;
	}
}
