package com.biit.webforms.gui.webpages.formmanager;

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

import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.webforms.gui.common.components.TreeTableProvider;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class WindowSimpleViewAbcdForms extends WindowAcceptCancel {
	private static final long serialVersionUID = 4894722469159293545L;
	private static final String WINDOW_WIDTH = "60%";
	private static final String WINDOW_HEIGHT = "80%";

	private final TreeTableSimpleViewAbcdForm table;

	protected TreeTableSimpleViewAbcdForm getTable() {
		return table;
	}

	public WindowSimpleViewAbcdForms(TreeTableProvider<SimpleFormView> treeTableProvider) {
		super();

		table = new TreeTableSimpleViewAbcdForm(treeTableProvider);

		configure();
		setContent(generateContent());
		init();
	}

	private void init() {
		updateAcceptButton();
	}

	/**
	 * Update enabled state of accept button
	 */
	private void updateAcceptButton() {
		getAcceptButton().setEnabled(table.getValue() != null);
	}

	private void configure() {
		setWidth(WINDOW_WIDTH);
		setHeight(WINDOW_HEIGHT);
		setResizable(false);
	}

	protected Component generateContent() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();

		table.setSizeFull();
		table.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -7965120368151082421L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateAcceptButton();
			}
		});

		rootLayout.addComponent(table);
		return rootLayout;
	}

	public SimpleFormView getForm() {
		return (SimpleFormView) table.getForm();
	}

	public void setValue(SimpleFormView linkedSimpleAbcdForm) {
		table.setValue(linkedSimpleAbcdForm);
	}
}
