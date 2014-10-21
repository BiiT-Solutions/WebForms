package com.biit.webforms.gui.webpages.designer;

import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.gui.webpages.floweditor.SearchFormElementField.SearchFormElementChanged;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class PropertiesForm extends StorableObjectProperties<Form> {
	private static final long serialVersionUID = -7053263006728113569L;
	private static final String WIDTH = "200px";

	private TextField name, version;
	private TextArea description;
	private LinkedFormField linkedForm;

	public PropertiesForm() {
		super(Form.class);
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void initElement() {

		name = new TextField(LanguageCodes.CAPTION_NAME.translation());
		name.setWidth(WIDTH);
		name.setEnabled(false);

		version = new TextField(LanguageCodes.CAPTION_VERSION.translation());
		version.setWidth(WIDTH);
		version.setEnabled(false);

		description = new TextArea(LanguageCodes.CAPTION_DESCRIPTION.translation());
		description.setWidth(WIDTH);
		description.setMaxLength(Form.MAX_DESCRIPTION_LENGTH);
		description.setImmediate(true);

		linkedForm = new LinkedFormField(LanguageCodes.CAPTION_LINKED_FORM.translation());
		linkedForm.setWidth(WIDTH);
		linkedForm.addValueChangeListener(new SearchFormElementChanged() {

			@Override
			public void currentElement(Object object) {
				updateElement();
			}
		});

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(name);
		commonProperties.addComponent(version);
		commonProperties.addComponent(description);
		commonProperties.addComponent(linkedForm);

		boolean canEdit = WebformsAuthorizationService.getInstance().isFormEditable(
				UserSessionHandler.getController().getFormInUse(), UserSessionHandler.getUser());
		commonProperties.setEnabled(canEdit);

		addTab(commonProperties, LanguageCodes.CAPTION_PROPERTIES_FORM.translation(), true);

		super.initElement();
	}

	@Override
	protected void initValues() {
		super.initValues();

		name.setValue(instance.getName());
		version.setValue("" + instance.getVersion());
		description.setValue(instance.getDescription());
		linkedForm.setValue(UserSessionHandler.getController().getLinkedSimpleAbcdForm(instance));
	}

	@Override
	public void updateElement() {
		UserSessionHandler.getController().updateForm(instance, description.getValue(), linkedForm.getValue());

		super.updateElement();
	}
}
