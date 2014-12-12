package com.biit.webforms.gui.webpages.designer;

import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Text;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class PropertiesText extends StorableObjectProperties<Text> {
	private static final long serialVersionUID = 3545367878977339439L;
	private static final String WIDTH = "200px";
	
	private TextField name;

	private TextArea description;

	public PropertiesText() {
		super(Text.class);
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// TODO Auto-generated method stub

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
		
		boolean canEdit = WebformsAuthorizationService.getInstance().isFormEditable(
				UserSessionHandler.getController().getFormInUse(), UserSessionHandler.getUser());
		commonProperties.setEnabled(canEdit);

		addTab(commonProperties, LanguageCodes.CAPTION_PROPERTIES_TEXT.translation(), true);

		super.initElement();
	}

	@Override
	protected void initValues() {
		super.initValues();
		
		name.addValidator(new ValidatorTreeObjectName(instance.getNameAllowedPattern()));
		name.addValidator(new ValidatorDuplicateNameOnSameTreeObjectLevel(instance));
		name.addValidator(new ValidatorTreeObjectNameLength());
		name.setValue(instance.getName());
		description.setValue(instance.getDescription());
	}

	@Override
	public void updateElement() {
		try {
			if(name.isValid()){
				instance.setName(name.getValue());
			}
			instance.setDescription(description.getValue());
		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

		super.updateElement();
	}
}
