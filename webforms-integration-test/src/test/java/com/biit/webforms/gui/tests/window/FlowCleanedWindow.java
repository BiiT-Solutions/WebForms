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

import org.openqa.selenium.NoSuchElementException;

import com.biit.gui.tester.VaadinGuiWindow;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.WindowElement;

public class FlowCleanedWindow extends VaadinGuiWindow {

	public static final String CLOSE_BUTTON_CAPTION = "Close";
	private static final String CLASS_NAME = "com.biit.webforms.gui.common.components.WindowTextArea";
	
	public ButtonElement getCloseButton() {
		if (getWindow() != null) {
			ElementQuery<ButtonElement> close = getWindow().$(ButtonElement.class).caption(CLOSE_BUTTON_CAPTION);
			if (close.exists()) {
				return close.first();
			}
		}
		return null;
	}
	
	public void clickCloseButton(){
		getCloseButton().click();
	}

	private String getWindowId() {
		return CLASS_NAME;
	}
	
	public WindowElement getWindow() {
		try {
			return $$(WindowElement.class).id(getWindowId());
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public void waitToShow() {
		while (true) {
			try {
				if (getWindow() != null) {
					return;
				}
			} catch (Exception e) {
				// ignore
			}
		}
	}
}
