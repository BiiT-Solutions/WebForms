package com.biit.webforms.gui.webpages.designer;

import com.biit.form.entity.BaseForm;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.common.components.PropertiesForStorableObjectWithImages;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Form;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public abstract class PropertiesBaseForm<T extends Form> extends PropertiesForStorableObjectWithImages<T> {
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
		// label.addValidator(new
		// ValidatorTreeObjectName(BaseForm.NAME_ALLOWED));
		// label.addValidator(new ValidatorTreeObjectNameLength());

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
		getLabelTextField().setValue(((BaseForm) getInstance()).getLabel());
		getLabelTextField().setEnabled(!((TreeObject) getInstance()).isReadOnly());

		getVersionTextField().setValue("" + ((BaseForm) getInstance()).getVersion());

		getDescriptionTextArea().setValue(((Form) getInstance()).getDescription());
		getDescriptionTextArea().setEnabled(!((TreeObject) getInstance()).isReadOnly());

		if (getInstance() instanceof Form && ((Form) getInstance()).getFormReference() != null) {
			referenceLabel.setVisible(true);
			referenceVersion.setVisible(true);
			getReferenceLabel().setValue(
					getInstance() instanceof Form && ((Form) getInstance()).getFormReference() != null ? ((Form) getInstance())
							.getFormReference().getLabel() : "");
			getReferenceVersion().setValue(
					getInstance() instanceof Form && ((Form) getInstance()).getFormReference() != null ? ((Form) getInstance())
							.getFormReference().getVersion() + "" : "");
		}

		if (getInstance() instanceof Form) {
			disableEdition.setValue(((Form) getInstance()).isEditionDisabled());
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
