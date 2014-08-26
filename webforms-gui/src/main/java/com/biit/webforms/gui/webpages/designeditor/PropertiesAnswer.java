package com.biit.webforms.gui.webpages.designeditor;

import com.biit.form.exceptions.FieldTooLongException;
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

		addTab(commonProperties, LanguageCodes.CAPTION_PROPERTIES_ANSWER.translation(), true);

		super.initElement();
	}

	@Override
	protected void initValues() {
		super.initValues();

		value.setValue(instance.getValue());
		//TODO dynamic label
		label.setValue(instance.getLabel());
		description.setValue(instance.getDescription());
	}

	@Override
	public void updateElement() {
		try {
			instance.setValue(value.getValue());
			//TODO dynamic label 
			instance.setLabel(label.getValue());
			instance.setDescription(description.getValue());
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
