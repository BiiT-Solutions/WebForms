package com.biit.webforms.gui.webpages.designer;

import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.language.AnswerFormatUi;
import com.biit.webforms.language.AnswerTypeUi;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.AnswerFormat;
import com.biit.webforms.persistence.entity.AnswerType;
import com.biit.webforms.persistence.entity.Question;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class PropertiesQuestion extends StorableObjectProperties<Question> {
	private static final long serialVersionUID = 7572463216386081265L;
	private static final String WIDTH = "200px";

	private TextField name, label;

	private TextArea description;

	private CheckBox mandatory;

	private ComboBox answerType;

	private ComboBox answerFormat;

	private CheckBox horizontal;

	public PropertiesQuestion() {
		super(Question.class);
	}

	@Override
	protected void initElement() {

		name = new TextField(LanguageCodes.CAPTION_NAME.translation());
		name.setWidth(WIDTH);

		label = new TextField(LanguageCodes.CAPTION_LABEL.translation());
		label.setWidth(WIDTH);

		description = new TextArea(LanguageCodes.CAPTION_DESCRIPTION.translation());
		description.setWidth(WIDTH);

		mandatory = new CheckBox(LanguageCodes.CAPTION_MANDATORY.translation());

		answerType = new ComboBox(LanguageCodes.CAPTION_ANSWER_TYPE.translation());
		answerType.setWidth(WIDTH);
		for (AnswerTypeUi type : AnswerTypeUi.values()) {
			answerType.addItem(type.getAnswerType());
			answerType.setItemCaption(type.getAnswerType(), type.getLanguageCode().translation());
		}
		answerType.setNullSelectionAllowed(false);
		answerType.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -7743742253650945202L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(answerType.getValue().equals(AnswerType.INPUT)){
					answerFormat.setValue(AnswerFormat.TEXT);
					answerFormat.setEnabled(true);
					horizontal.setEnabled(false);
				}else{
					answerFormat.setValue(null);
					answerFormat.setEnabled(false);
					horizontal.setEnabled(true);
				}
			}
		});

		answerFormat = new ComboBox(LanguageCodes.CAPTION_ANSWER_FORMAT.translation());
		answerFormat.setWidth(WIDTH);
		for (AnswerFormatUi format : AnswerFormatUi.values()) {
			answerFormat.addItem(format.getAnswerFormat());
			answerFormat.setItemCaption(format.getAnswerFormat(), format.getLanguageCode().translation());
		}
		answerFormat.setNullSelectionAllowed(false);

		horizontal = new CheckBox(LanguageCodes.CAPTION_HORIZONTAL.translation());

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(name);
		commonProperties.addComponent(label);
		commonProperties.addComponent(description);
		commonProperties.addComponent(mandatory);
		commonProperties.addComponent(answerType);
		commonProperties.addComponent(answerFormat);
		commonProperties.addComponent(horizontal);

		addTab(commonProperties, LanguageCodes.CAPTION_PROPERTIES_QUESTION.translation(), true);

		super.initElement();
	}

	@Override
	protected void initValues() {
		super.initValues();

		name.setValue(instance.getName());
		//TODO dynamic label
		label.setValue(instance.getLabel());		
		description.setValue(instance.getDescription());
		mandatory.setValue(instance.isMandatory());
		answerType.setValue(instance.getAnswerType());
		answerFormat.setValue(instance.getAnswerFormat());
		horizontal.setValue(instance.isHorizontal());
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateElement() {
		try {
			instance.setName(name.getValue());
			//TODO dynamic label
			instance.setLabel(label.getValue());
			instance.setDescription(description.getValue());
			instance.setMandatory(mandatory.getValue());
			instance.setAnswerType((AnswerType) answerType.getValue());
			instance.setAnswerFormat((AnswerFormat) answerFormat.getValue());
			instance.setHorizontal(horizontal.getValue());

		} catch (FieldTooLongException | InvalidAnswerFormatException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

		super.updateElement();
	}

}
