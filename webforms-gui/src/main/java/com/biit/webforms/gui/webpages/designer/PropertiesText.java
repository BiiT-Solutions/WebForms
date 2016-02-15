package com.biit.webforms.gui.webpages.designer;

import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.components.PropertiesForStorableObjectWithImages;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Text;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class PropertiesText extends PropertiesForStorableObjectWithImages<Text> {
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

		boolean canEdit = getWebformsSecurityService().isElementEditable(UserSessionHandler.getController().getFormInUse(),
				UserSessionHandler.getUser());
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
