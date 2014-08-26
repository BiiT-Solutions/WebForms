package com.biit.webforms.gui.webpages.designeditor;

import com.biit.form.exceptions.FieldTooLongException;
import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Subcategory;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class PropertiesSubcategory extends StorableObjectProperties<Subcategory> {
	private static final long serialVersionUID = -859153918941072265L;
	private static final String WIDTH = "200px";

	private TextField name, label;

	public PropertiesSubcategory() {
		super(Subcategory.class);
	}

	@Override
	protected void initElement() {

		name = new TextField(LanguageCodes.CAPTION_NAME.translation());
		name.setWidth(WIDTH);

		label = new TextField(LanguageCodes.CAPTION_LABEL.translation());
		label.setWidth(WIDTH);

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(name);
		commonProperties.addComponent(label);

		addTab(commonProperties, LanguageCodes.CAPTION_PROPERTIES_SUBCATEGORY.translation(), true);

		super.initElement();
	}

	@Override
	protected void initValues() {
		super.initValues();

		name.setValue(instance.getName());
		label.setValue(instance.getLabel());
	}

	@Override
	public void updateElement() {
		try {
			instance.setName(name.getValue());
			instance.setLabel(label.getValue());
		} catch (FieldTooLongException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

		super.updateElement();
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// TODO Auto-generated method stub

	}

}
