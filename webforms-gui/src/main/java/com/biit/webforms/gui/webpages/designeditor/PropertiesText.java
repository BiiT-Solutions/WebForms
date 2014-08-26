package com.biit.webforms.gui.webpages.designeditor;

import com.biit.form.exceptions.FieldTooLongException;
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

		name = new TextField(LanguageCodes.CAPTION_NAME.translation());
		name.setWidth(WIDTH);
		
		description = new TextArea(LanguageCodes.CAPTION_TEXT.translation());
		description.setWidth(WIDTH);

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(name);
		commonProperties.addComponent(description);

		addTab(commonProperties, LanguageCodes.CAPTION_PROPERTIES_TEXT.translation(), true);

		super.initElement();
	}

	@Override
	protected void initValues() {
		super.initValues();

		name.setValue(instance.getName());
		description.setValue(instance.getDescription());
	}

	@Override
	public void updateElement() {
		try {
			instance.setName(name.getValue());
			instance.setDescription(description.getValue());
		} catch (FieldTooLongException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

		super.updateElement();
	}
}
