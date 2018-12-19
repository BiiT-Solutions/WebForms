package com.biit.webforms.gui.webpages.designer;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.AttachedFiles;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class PropertiesAttachedFiles extends StorableObjectProperties<AttachedFiles> {
	private static final long serialVersionUID = 3545367878977339439L;
	private static final String WIDTH = "200px";

	private TextField name;
	private TextArea label;

	private CheckBox mandatory;

	// Disable the field if orbeon is in edition mode.
	private CheckBox disableEdition;

	public PropertiesAttachedFiles() {
		super(AttachedFiles.class);
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

		label = new TextArea(LanguageCodes.CAPTION_LABEL.translation());
		label.setWidth(WIDTH);
		label.setMaxLength(TreeObject.MAX_LABEL_LENGTH);
		label.setImmediate(true);

		mandatory = new CheckBox(LanguageCodes.CAPTION_MANDATORY.translation());

		disableEdition = new CheckBox(LanguageCodes.CAPTION_DISABLE_EDITION.translation());
		disableEdition.setDescription(LanguageCodes.CAPTION_DISABLE_EDITION_TOOLTIP.translation());

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(name);
		commonProperties.addComponent(label);
		commonProperties.addComponent(mandatory);
		commonProperties.addComponent(disableEdition);

		boolean canEdit = getWebformsSecurityService().isElementEditable(ApplicationUi.getController().getFormInUse(), UserSession.getUser());
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

		label.setValue(getInstance().getLabel());
		label.addValidator(new LengthValidator(getInstance().getMaxLabelLength()));
		label.setEnabled(!getInstance().isReadOnly());

		mandatory.setValue(getInstance().isMandatory());
		mandatory.setEnabled(!getInstance().isReadOnly());

		disableEdition.setValue(getInstance().isEditionDisabled());
		disableEdition.setEnabled(!getInstance().isReadOnly());
	}

	@Override
	public void updateElement() {
		String tempName = getInstance().getName();
		String tempLabel = getInstance().getLabel();
		if (name.isValid()) {
			tempName = name.getValue();
		}
		if (label.isValid()) {
			tempLabel = label.getValue();
		}

		ApplicationUi.getController().updateAttachedFiles(getInstance(), tempName, tempLabel, mandatory.getValue(), disableEdition.getValue());

		super.updateElement();
	}
}
