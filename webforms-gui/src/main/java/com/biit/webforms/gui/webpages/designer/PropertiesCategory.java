package com.biit.webforms.gui.webpages.designer;

import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Category;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class PropertiesCategory extends StorableObjectProperties<Category> {
	private static final long serialVersionUID = 766903215139261772L;
	private static final String WIDTH = "200px";

	private TextField name;
	private TextArea label;

	public PropertiesCategory() {
		super(Category.class);
	}

	@Override
	protected void initElement() {

		name = new TextField(LanguageCodes.CAPTION_NAME.translation());
		name.setWidth(WIDTH);
		name.setRequired(true);

		label = new TextArea(LanguageCodes.CAPTION_LABEL.translation());
		label.setWidth(WIDTH);

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(name);
		commonProperties.addComponent(label);

		boolean canEdit = WebformsAuthorizationService.getInstance().isFormEditable(
				UserSessionHandler.getController().getFormInUse(), UserSessionHandler.getUser());
		commonProperties.setEnabled(canEdit);

		addTab(commonProperties, LanguageCodes.CAPTION_PROPERTIES_CATEGORY.translation(), true);

		super.initElement();
	}

	@Override
	protected void initValues() {
		super.initValues();

		name.addValidator(new ValidatorTreeObjectName(instance.getNameAllowedPattern()));
		name.addValidator(new ValidatorDuplicateNameOnSameTreeObjectLevel(instance));
		name.setValue(instance.getName());
		label.setValue(instance.getLabel());
		label.addValidator(new LengthValidator(instance.getMaxLabelLength()));
	}

	@Override
	public void updateElement() {
		try {
			if (name.isValid()) {
				instance.setName(name.getValue());
			}
			if(label.isValid()){
				instance.setLabel(label.getValue());
			}
		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

		super.updateElement();
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// TODO Auto-generated method stub

	}

}
