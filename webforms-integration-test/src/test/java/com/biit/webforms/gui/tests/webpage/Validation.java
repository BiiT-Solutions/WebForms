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

import com.biit.gui.tester.VaadinGuiWebpage;
import com.biit.webforms.gui.tests.window.CompareAbcdFormWindow;
import com.vaadin.testbench.elements.TextAreaElement;

public class Validation extends VaadinGuiWebpage {

	private static final String ALL_BUTTON_CAPTION = "All";
	private static final String STRUCTURE_BUTTON_CAPTION = "Structure";
	private static final String FLOW_BUTTON_CAPTION = "Flow";
	private static final String ABCD_LINK_BUTTON_CAPTION = "ABCD Link";
	private static final String ABCD_DIFF_BUTTON_CAPTION = "ABCD Diff";
	private final CompareAbcdFormWindow compareAbcdFormWindow;

	public Validation() {
		super();
		compareAbcdFormWindow = new CompareAbcdFormWindow();
		addWindow(compareAbcdFormWindow);
	}
	
	public void clickAbcdDiffButton() {
		getButtonElement(ABCD_DIFF_BUTTON_CAPTION).click();
	}
	
	public void clickAbcdLinkButton() {
		getButtonElement(ABCD_LINK_BUTTON_CAPTION).click();
	}

	public void clickAllButton() {
		getButtonElement(ALL_BUTTON_CAPTION).click();
	}

	public void clickFlowButton() {
		getButtonElement(FLOW_BUTTON_CAPTION).click();
	}

	public void clickStructureButton() {
		getButtonElement(STRUCTURE_BUTTON_CAPTION).click();
	}

	private TextAreaElement getTextArea(){
		return $(TextAreaElement.class).first();
	}
	
	public String getTextAreaValue(){
		return getTextArea().getText();
	}
	
	@Override
	public String getWebpageUrl() {
		return null;
	}

	public CompareAbcdFormWindow getCompareAbcdFormWindow(){
		return compareAbcdFormWindow;
	}
}
