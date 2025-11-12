package com.biit.gui.tester;

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

import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import com.vaadin.testbench.TestBenchTestCase;

public class VaadinGuiWindow extends TestBenchTestCase {

	public void sendKeyDown(Keys key) {
		Actions builder = new Actions(getDriver());
		builder.keyDown(key).perform();
	}

	public void sendKeyUp(Keys key) {
		Actions builder = new Actions(getDriver());
		builder.keyUp(key).perform();
	}

	public void pressKeys(Keys... keys) {
		Actions builder = new Actions(getDriver());
		builder.sendKeys(keys).perform();
	}
}
