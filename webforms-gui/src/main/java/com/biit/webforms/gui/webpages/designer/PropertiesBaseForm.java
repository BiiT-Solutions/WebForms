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

import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.common.components.PropertiesForStorableObjectWithMedia;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Form;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public abstract class PropertiesBaseForm<T extends Form> extends PropertiesForStorableObjectWithMedia<T> {
	private static final long serialVersionUID = -7053263006728113569L;
	private static final String WIDTH = "200px";

	private TextField label, version, referenceLabel, referenceVersion;
	private TextArea description;

	// Disable the field if orbeon is in edition mode. Disables all questions of
	// the form.
	private CheckBox disableEdition;

	public PropertiesBaseForm(Class<? extends T> type) {
		super(type);
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// Form updateTime is not updated when detached.
	}

	@Override
	protected void initElement() {
		label = new TextField(LanguageCodes.CAPTION_NAME.translation());
		label.setWidth(WIDTH);
		label.setEnabled(true);

		version = new TextField(LanguageCodes.CAPTION_VERSION.translation());
		version.setWidth(WIDTH);
		version.setEnabled(false);

		description = new TextArea(LanguageCodes.CAPTION_DESCRIPTION.translation());
		description.setWidth(WIDTH);
		description.setMaxLength(Form.MAX_DESCRIPTION_LENGTH);
		description.setImmediate(true);

		referenceLabel = new TextField(LanguageCodes.REFERENCE_CAPTION_NAME.translation());
		referenceLabel.setWidth(WIDTH);
		referenceLabel.setEnabled(false);
		referenceLabel.setVisible(false);

		referenceVersion = new TextField(LanguageCodes.CAPTION_VERSION.translation());
		referenceVersion.setWidth(WIDTH);
		referenceVersion.setEnabled(false);
		referenceVersion.setVisible(false);

		disableEdition = new CheckBox(LanguageCodes.CAPTION_DISABLE_EDITION.translation());
		disableEdition.setDescription(LanguageCodes.CAPTION_DISABLE_EDITION_TOOLTIP.translation());

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(label);
		commonProperties.addComponent(version);
		commonProperties.addComponent(description);
		commonProperties.addComponent(referenceLabel);
		commonProperties.addComponent(referenceVersion);
		commonProperties.addComponent(disableEdition);

		boolean canEdit = getWebformsSecurityService().isFormEditable(ApplicationUi.getController().getFormInUse(), UserSession.getUser());
		commonProperties.setEnabled(canEdit);

		addTab(commonProperties, LanguageCodes.CAPTION_PROPERTIES_FORM.translation(), true);

		super.initElement();
	}

	@Override
	protected void initValues() {
		super.initValues();
		getLabelTextField().setValue(( getInstance()).getLabel());
		getLabelTextField().setEnabled(!(getInstance()).isReadOnly());

		getVersionTextField().setValue("" + (getInstance()).getVersion());

		getDescriptionTextArea().setValue((getInstance()).getDescription());
		getDescriptionTextArea().setEnabled(!(getInstance()).isReadOnly());

		if (getInstance() instanceof Form && (getInstance()).getFormReference() != null) {
			referenceLabel.setVisible(true);
			referenceVersion.setVisible(true);
			getReferenceLabel().setValue(
					getInstance() instanceof Form && (getInstance()).getFormReference() != null ? (getInstance())
							.getFormReference().getLabel() : "");
			getReferenceVersion().setValue(
					getInstance() instanceof Form && (getInstance()).getFormReference() != null ? (getInstance())
							.getFormReference().getVersion() + "" : "");
		}

		if (getInstance() instanceof Form) {
			disableEdition.setValue((getInstance()).isEditionDisabled());
		}
	}

	public CheckBox getDisableEdition() {
		return disableEdition;
	}

	protected TextField getLabelTextField() {
		return label;
	}

	protected TextField getVersionTextField() {
		return version;
	}

	protected TextArea getDescriptionTextArea() {
		return description;
	}

	public TextField getReferenceLabel() {
		return referenceLabel;
	}

	public TextField getReferenceVersion() {
		return referenceVersion;
	}
}
