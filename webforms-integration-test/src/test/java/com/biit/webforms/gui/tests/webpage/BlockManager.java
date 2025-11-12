package com.biit.webforms.gui.tests.webpage;

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

import com.biit.gui.tester.VaadinGuiWebpage;
import com.biit.webforms.gui.tests.window.NewBlockWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TableElement;

public class BlockManager extends VaadinGuiWebpage {

	private static final String NEW_BLOCK_BUTTON_CAPTION = "New Block";
	private static final String REMOVE_BLOCK_BUTTON_CAPTION = "Remove Block";
	private static final Integer BLOCK_ROW = 0;

	private final NewBlockWindow newBlockWindow;

	public BlockManager() {
		super();
		newBlockWindow = new NewBlockWindow();
		addWindow(newBlockWindow);
	}

	public NewBlockWindow getNewBlockWindow() {
		return newBlockWindow;
	}

	@Override
	public String getWebpageUrl() {
		return null;
	}

	public ButtonElement getNewBlockButton() {
		return $(ButtonElement.class).caption(NEW_BLOCK_BUTTON_CAPTION).first();
	}

	public ButtonElement getRemoveBlockButton() {
		return $(ButtonElement.class).caption(REMOVE_BLOCK_BUTTON_CAPTION).first();
	}

	public void createNewBlock(String blockName) {
		deleteAllCreatedBlocks();
		openNewBlockWindow();
		getNewBlockWindow().createNewBlock(blockName);
	}

	private void openNewBlockWindow() {
		getNewBlockButton().click();
	}

	public void deleteBlock() {
		getRemoveBlockButton().click();
		clickAcceptButtonIfExists();
	}

	public TableElement getBlockTable() {
		return $(TableElement.class).first();
	}

	public void selectBlock(int row) {
		getBlockTable().getCell(row, 0).click();
	}

	public void deleteAllCreatedBlocks() {
		try {
			while (true) {
				getBlockTable().getCell(BLOCK_ROW, 0);
				if (!getRemoveBlockButton().isEnabled()) {
					getBlockTable().getCell(BLOCK_ROW, 0).click();
				}
				deleteBlock();
			}
		} catch (NoSuchElementException e) {
			return;
		}
	}
}
