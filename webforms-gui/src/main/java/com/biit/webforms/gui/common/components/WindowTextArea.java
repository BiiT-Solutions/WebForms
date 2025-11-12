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

import com.biit.webforms.language.LanguageCodes;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class WindowTextArea extends WindowAcceptCancel {
	private static final long serialVersionUID = 4740162877392137594L;

	private static final String WIDTH = "300px";
	private static final String HEIGHT = "300px";

	private TextArea textArea;

	public WindowTextArea(String inputFieldCaption) {
		super();
		setContent(generateContent(inputFieldCaption));
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(WIDTH);
		setHeight(HEIGHT);
		getAcceptButton().setVisible(false);
		getCancelButton().setCaption(LanguageCodes.CAPTION_CLOSE.translation());
		getCancelButton().setDescription(LanguageCodes.CAPTION_CLOSE.translation());
	}

	public String getValue() {
		return textArea.getValue();
	}

	private Component generateContent(String inputFieldCaption) {
		textArea = new TextArea(inputFieldCaption);
		textArea.focus();
		textArea.setSizeFull();

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();

		rootLayout.addComponent(textArea);
		rootLayout.setComponentAlignment(textArea, Alignment.MIDDLE_CENTER);
		return rootLayout;
	}

	public void setValue(String value) {
		textArea.setValue(value);
	}

	
	public void setTextReadOnly(boolean readOnly) {
		textArea.setReadOnly(readOnly);
	}
}
