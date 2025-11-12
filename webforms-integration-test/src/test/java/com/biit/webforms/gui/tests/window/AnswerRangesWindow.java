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

import com.vaadin.testbench.elements.TextFieldElement;

public class AnswerRangesWindow extends GenericAcceptCancelWindow {

	
	private static final String CLASS_NAME = "com.biit.webforms.gui.webpages.designer.WindowCreateAnswerRanges";
	private static final String LOWER_VALUE_CAPTION = "Lower value:";
	private static final String UPPER_VALUE_CAPTION = "Upper value:";
	private static final String INCREASE_VALUE_CAPTION = "Increase value:";
	
	
	
	public TextFieldElement getLowerValueField() {
		return getWindow().$(TextFieldElement.class).caption(LOWER_VALUE_CAPTION).first();
	}
	
	public TextFieldElement getUpperValueField() {
		return getWindow().$(TextFieldElement.class).caption(UPPER_VALUE_CAPTION).first();
	}
	
	public TextFieldElement getIncreaseValueField() {
		return getWindow().$(TextFieldElement.class).caption(INCREASE_VALUE_CAPTION).first();
	}
	
	public void fillRangeWindow(String lower, String upper, String steap) {
		getLowerValueField().setValue(lower);
		getUpperValueField().setValue(upper);
		getIncreaseValueField().setValue(steap);
	}
	
	@Override
	protected String getWindowId() {
		return CLASS_NAME;
	}

}
