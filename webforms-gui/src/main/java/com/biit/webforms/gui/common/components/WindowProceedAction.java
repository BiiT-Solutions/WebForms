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
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class WindowProceedAction extends WindowAcceptCancel {
	private static final long serialVersionUID = -2111506182459100300L;
	private static final String width = "450px";
	private static final String height = "200px";

	private Label label;

	public WindowProceedAction(LanguageCodes code, final AcceptActionListener listener) {
		super();
		setContent(generateContent(code.translation()));
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(width);
		setHeight(height);

		addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				listener.acceptAction(window);
				window.close();
			}
		});
		showCentered();
	}

	public WindowProceedAction(LanguageCodes code, final AcceptActionListener acceptListener,
			final CancelActionListener cancelListener) {
		super();
		setContent(generateContent(code.translation()));
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(width);
		setHeight(height);

		addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				acceptListener.acceptAction(window);
				window.close();
			}
		});
		addCancelActionListener(cancelListener);
		showCentered();
	}

	private Component generateContent(String text) {

		label = new Label("<div style=\"text-align: center;\">" + text + "</div>");
		label.setContentMode(ContentMode.HTML);

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();

		rootLayout.addComponent(label);
		rootLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
		return rootLayout;
	}

}
