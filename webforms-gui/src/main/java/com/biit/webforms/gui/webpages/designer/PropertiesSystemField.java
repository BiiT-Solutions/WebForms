package com.biit.webforms.gui.webpages.designer;

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

import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.SystemField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class PropertiesSystemField extends StorableObjectProperties<SystemField> {
	private static final long serialVersionUID = 7967797300015955172L;

	private TextField name, field;

	private static final String WIDTH = "200px";

	public PropertiesSystemField() {
		super(SystemField.class);
	}

	@Override
	protected void initElement() {

		name = new TextField(LanguageCodes.CAPTION_TECHNICAL_NAME.translation());
		name.setWidth(WIDTH);
		name.setRequired(true);

		field = new TextField(LanguageCodes.CAPTION_FIELD.translation());
		field.setWidth(WIDTH);

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(name);
		commonProperties.addComponent(field);

		boolean canEdit = getWebformsSecurityService().isElementEditable(ApplicationUi.getController().getFormInUse(),
				UserSession.getUser());
		commonProperties.setEnabled(canEdit);

		addTab(commonProperties, LanguageCodes.CAPTION_PROPERTIES_SYSTEM_FIELD.translation(), true);

		super.initElement();
	}

	@Override
	protected void initValues() {
		super.initValues();

		name.addValidator(new ValidatorTreeObjectName(getInstance().getNameAllowedPattern()));
		name.addValidator(new ValidatorDuplicateNameOnSameTreeObjectLevel(getInstance()));
		name.addValidator(new ValidatorTreeObjectNameLength());
		name.setValue(getInstance().getName());
		name.setEnabled(!getInstance().isReadOnly());

		field.setValue(getInstance().getFieldName());
		field.addValidator(new LengthValidator(getInstance().getMaxLabelLength()));
		field.setEnabled(!getInstance().isReadOnly());
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		updateElement();
	}

	@Override
	public void updateElement() {
		try {
			if (name.isValid()) {
				getInstance().setName(name.getValue());
			}
			if (field.isValid()) {
				getInstance().setFieldName(field.getValue());
			}
		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		}
		super.updateElement();
	}

}
