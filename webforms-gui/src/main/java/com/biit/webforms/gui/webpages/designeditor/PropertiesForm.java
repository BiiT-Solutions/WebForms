package com.biit.webforms.gui.webpages.designeditor;

import com.biit.form.exceptions.FieldTooLongException;
import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class PropertiesForm extends StorableObjectProperties<Form>{
	private static final long serialVersionUID = -7053263006728113569L;
	private static final String WIDTH = "200px";

	private TextField name, version;
	private TextArea description;

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

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(name);
		commonProperties.addComponent(version);
		commonProperties.addComponent(description);

		addTab(commonProperties, LanguageCodes.CAPTION_PROPERTIES_FORM.translation(), true);

		super.initElement();
	}

	@Override
	protected void initValues() {
		super.initValues();

		name.setValue(instance.getName());
		version.setValue(""+instance.getVersion());
		description.setValue(instance.getDescription());
		
	}
	
	@Override
	public void updateElement() {
		try {
			instance.setDescription(description.getValue());
		} catch (FieldTooLongException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

		super.updateElement();
	}
}
