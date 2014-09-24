package com.biit.webforms.gui.webpages.designer;

import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Answer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class PropertiesAnswer extends StorableObjectProperties<Answer> {
	private static final long serialVersionUID = 8035711998129559199L;
	private static final String WIDTH = "200px";

	private TextField value, label;

	private TextArea description;

	public PropertiesAnswer() {
		super(Answer.class);
	}

	@Override
	protected void initElement() {

		value = new TextField(LanguageCodes.CAPTION_VALUE.translation());
		value.setWidth(WIDTH);
		value.setRequired(true);

		label = new TextField(LanguageCodes.CAPTION_LABEL.translation());
		label.setWidth(WIDTH);

		description = new TextArea(LanguageCodes.CAPTION_DESCRIPTION.translation());
		description.setWidth(WIDTH);

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(value);
		commonProperties.addComponent(label);
		commonProperties.addComponent(description);

		boolean canEdit = WebformsAuthorizationService.getInstance().isFormEditable(
				UserSessionHandler.getController().getFormInUse(), UserSessionHandler.getUser());
		commonProperties.setEnabled(canEdit);

		addTab(commonProperties, LanguageCodes.CAPTION_PROPERTIES_ANSWER.translation(), true);

		super.initElement();
	}

	@Override
	protected void initValues() {
		super.initValues();

		value.addValidator(new TreeObjectNameValidator(instance.getNameAllowedPattern()));
		value.setValue(instance.getValue());
		// TODO dynamic label
		label.setValue(instance.getLabel());
		label.addValidator(new LengthValidator(instance.getMaxLabelLength()));
		description.setValue(instance.getDescription());
	}

	@Override
	public void updateElement() {
		try {
			if (value.isValid()) {
				instance.setValue(value.getValue());
			}
			// TODO dynamic label
			instance.setLabel(label.getValue());
			instance.setDescription(description.getValue());
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
