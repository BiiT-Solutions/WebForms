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
import com.biit.webforms.gui.common.components.PropertiesForStorableObjectWithMedia;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Text;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class PropertiesText extends PropertiesForStorableObjectWithMedia<Text> {
	private static final long serialVersionUID = 3545367878977339439L;
	private static final String WIDTH = "200px";

	private TextField name;

	private TextArea description;

	public PropertiesText() {
		super(Text.class);
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		updateElement();
	}

	@Override
	protected void initElement() {

		name = new TextField(LanguageCodes.CAPTION_TECHNICAL_NAME.translation());
		name.setWidth(WIDTH);
		name.setRequired(true);

		description = new TextArea(LanguageCodes.CAPTION_TEXT.translation());
		description.setWidth(WIDTH);

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(name);
		commonProperties.addComponent(description);

		boolean canEdit = getWebformsSecurityService().isElementEditable(ApplicationUi.getController().getFormInUse(),
				UserSession.getUser());
		commonProperties.setEnabled(canEdit);

		addTab(commonProperties, LanguageCodes.CAPTION_PROPERTIES_TEXT.translation(), true);

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

		description.setValue(getInstance().getDescription());
		description.setEnabled(!getInstance().isReadOnly());
	}

	@Override
	public void updateElement() {
		try {
			if (name.isValid()) {
				getInstance().setName(name.getValue());
			}
			getInstance().setDescription(description.getValue());
		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		}

		super.updateElement();
	}
}
