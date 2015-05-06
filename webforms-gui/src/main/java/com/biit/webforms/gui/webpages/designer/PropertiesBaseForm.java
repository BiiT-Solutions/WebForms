package com.biit.webforms.gui.webpages.designer;

import com.biit.form.entity.BaseForm;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public abstract class PropertiesBaseForm<T extends BaseForm> extends StorableObjectProperties<T> {
	private static final long serialVersionUID = -7053263006728113569L;
	private static final String WIDTH = "200px";

	private TextField label, version;
	private TextArea description;

	public PropertiesBaseForm(Class<? extends T> type) {
		super(type);
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// TODO Auto-generated method stub
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

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(label);
		commonProperties.addComponent(version);
		commonProperties.addComponent(description);

		boolean canEdit = WebformsAuthorizationService.getInstance().isFormEditable(
				UserSessionHandler.getController().getFormInUse(), UserSessionHandler.getUser());
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
}
