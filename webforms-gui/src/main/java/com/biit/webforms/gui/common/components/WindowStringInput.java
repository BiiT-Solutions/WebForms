package com.biit.webforms.gui.common.components;

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

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class WindowStringInput extends WindowAcceptCancel {
	private static final long serialVersionUID = 361486551550136464L;
	private static final String width = "300px";
	private static final String height = "180px";

	private TextField textField;

	public WindowStringInput(String inputFieldCaption) {
		super();
		setContent(generateContent(inputFieldCaption));
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(width);
		setHeight(height);
	}
	
	public void setDefaultValue(String nullValue){
		textField.setValue(nullValue);
	}

	public String getValue() {
		return textField.getValue();
	}

	private Component generateContent(String inputFieldCaption) {
		textField = new TextField(inputFieldCaption);
		textField.focus();

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();

		rootLayout.addComponent(textField);
		rootLayout.setComponentAlignment(textField, Alignment.MIDDLE_CENTER);
		return rootLayout;
	}

	public void setValue(String value) {
		textField.setValue(value);
	}
}
