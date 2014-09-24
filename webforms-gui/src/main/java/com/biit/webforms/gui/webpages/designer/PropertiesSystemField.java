package com.biit.webforms.gui.webpages.designer;

import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
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

		name = new TextField(LanguageCodes.CAPTION_NAME.translation());
		name.setWidth(WIDTH);
		name.setRequired(true);

		field = new TextField(LanguageCodes.CAPTION_FIELD.translation());
		field.setWidth(WIDTH);

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(name);
		commonProperties.addComponent(field);
		
		boolean canEdit = WebformsAuthorizationService.getInstance().isFormEditable(
				UserSessionHandler.getController().getFormInUse(), UserSessionHandler.getUser());
		commonProperties.setEnabled(canEdit);

		addTab(commonProperties, LanguageCodes.CAPTION_PROPERTIES_SYSTEM_FIELD.translation(), true);

		super.initElement();
	}

	@Override
	protected void initValues() {
		super.initValues();
		
		name.addValidator(new TreeObjectNameValidator(instance.getNameAllowedPattern()));
		name.setValue(instance.getName());
		field.setValue(instance.getFieldName());
		field.addValidator(new LengthValidator(instance.getMaxLabelLength()));
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateElement() {
		try {
			if(name.isValid()){
				instance.setName(name.getValue());
			}
			instance.setFieldName(field.getValue());

		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
		super.updateElement();
	}

}
