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
import com.biit.form.entity.IBaseFormView;
import com.biit.webforms.gui.common.components.TreeTableProvider;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Form;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class WindowImportAbcdForms extends WindowSimpleViewAbcdForms {
	private static final long serialVersionUID = -3270709862406747626L;

	private TextField importNameField;

	public WindowImportAbcdForms(TreeTableProvider<SimpleFormView> formProvider) {
		super(formProvider);
		updateName();
	}

	@Override
	protected Component generateContent() {
		// Generate super view and add a textfield and combo box.
		VerticalLayout component = (VerticalLayout) super.generateContent();

		HorizontalLayout nameOrganizationLayout = new HorizontalLayout();
		nameOrganizationLayout.setHeight(null);
		nameOrganizationLayout.setWidth("100%");
		nameOrganizationLayout.setSpacing(true);

		importNameField = new TextField(LanguageCodes.CAPTION_NAME.translation());
		importNameField.setMaxLength(Form.MAX_LABEL_LENGTH);
		importNameField.setWidth("100%");

		nameOrganizationLayout.addComponent(importNameField);
		nameOrganizationLayout.setExpandRatio(importNameField, 1.0f);

		component.addComponent(nameOrganizationLayout);

		component.setExpandRatio(getTable(), 1.0f);

		getTable().addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -3971685162057144466L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateName();
			}
		});

		return component;
	}

	protected void updateName() {
		if (getTable().getForm() != null) {
			IBaseFormView element = getTable().getForm();
			importNameField.setValue(element.getLabel());
		}
	}

	public String getImportName() {
		return importNameField.getValue();
	}

}
