package com.biit.webforms.gui.webpages.webservice.call;

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

import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.webservices.Webservice;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class WindowWebservices extends WindowAcceptCancel {
	private static final long serialVersionUID = 5243624432249940382L;

	private static final String WINDOW_WIDTH = "40%";
	private static final String WINDOW_HEIGHT = "60%";

	private final TableWebservices table;
	private final TextField name;

	public WindowWebservices() {
		super();
		table = new TableWebservices();
		name = new TextField();
		configure();
		setContent(generateContent());
	}

	private Component generateContent() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSpacing(true);
		rootLayout.setSizeFull();

		name.setCaption(LanguageCodes.CAPTION_WEBSERVICE_CALL_NAME.translation());
		name.setRequired(true);
		name.setWidth("100%");

		table.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -1543187448320885714L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				getAcceptButton().setEnabled(event.getProperty().getValue() != null);
			}
		});
		table.setSizeFull();
		table.addRows(ApplicationUi.getController().getAllWebservices());
		table.setSelectable(true);
		table.setNullSelectionAllowed(false);
		table.sortByName();

		// Select the first element if any.
		if (table.getItemIds().isEmpty()) {
			getAcceptButton().setEnabled(false);
		} else {
			table.setValue(table.getItemIds().iterator().next());
		}

		rootLayout.addComponent(name);
		rootLayout.addComponent(table);
		rootLayout.setExpandRatio(table, 1.0f);

		return rootLayout;
	}

	private void configure() {
		setDraggable(false);
		setModal(true);
		setResizable(false);

		setWidth(WINDOW_WIDTH);
		setHeight(WINDOW_HEIGHT);
	}

	@Override
	protected boolean acceptAction() {
		return name.isValid() && (table.getValue() != null);
	}

	public String getName() {
		return name.getValue();
	}

	public Webservice getWebservice() {
		return (Webservice) table.getValue();
	}
}
