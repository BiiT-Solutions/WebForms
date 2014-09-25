package com.biit.webforms.gui.webpages.designer;

import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.language.AnswerFormatUi;
import com.biit.webforms.language.AnswerSubformatUi;
import com.biit.webforms.language.AnswerTypeUi;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.enumerations.AnswerFormat;
import com.biit.webforms.persistence.entity.enumerations.AnswerSubformat;
import com.biit.webforms.persistence.entity.enumerations.AnswerType;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;
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

	private TextField name;
	private TextArea label;

	private TextArea description;

	private CheckBox mandatory;

	private ComboBox answerType;

	private ComboBox answerFormat;

	private ComboBox answerSubformat;

	private CheckBox horizontal;

	public PropertiesQuestion() {
		super(Question.class);
	}

	@Override
	protected void initElement() {

		name = new TextField(LanguageCodes.CAPTION_NAME.translation());
		name.setWidth(WIDTH);
		name.setRequired(true);

		label = new TextArea(LanguageCodes.CAPTION_LABEL.translation());
		label.setWidth(WIDTH);

		description = new TextArea(LanguageCodes.CAPTION_DESCRIPTION.translation());
		description.setWidth(WIDTH);

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
				AnswerType selectedType = (AnswerType) answerType.getValue();
				answerFormat.setValue(selectedType.getDefaultAnswerFormat());
				answerFormat.setEnabled(selectedType.isAnswerFormatEnabled());
				horizontal.setValue(selectedType.getDefaultHorizontal());
				horizontal.setEnabled(selectedType.isHorizontalEnabled());
				mandatory.setValue(selectedType.getDefaultMandatory());
				mandatory.setEnabled(selectedType.isMandatoryEnabled());
			}
		});

		answerFormat = new ComboBox(LanguageCodes.CAPTION_ANSWER_FORMAT.translation());
		answerFormat.setWidth(WIDTH);
		for (AnswerFormatUi format : AnswerFormatUi.values()) {
			answerFormat.addItem(format.getAnswerFormat());
			answerFormat.setItemCaption(format.getAnswerFormat(), format.getLanguageCode().translation());
		}
		answerFormat.setNullSelectionAllowed(false);
		answerFormat.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -1366771633100053513L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				refreshAnswerSubformatOptions();
			}
		});
		answerFormat.setImmediate(true);

		answerSubformat = new ComboBox(LanguageCodes.CAPTION_ANSWER_SUBFORMAT.translation());
		answerSubformat.setWidth(WIDTH);
		answerSubformat.setNullSelectionAllowed(false);
		refreshAnswerSubformatOptions();

		horizontal = new CheckBox(LanguageCodes.CAPTION_HORIZONTAL.translation());

		mandatory = new CheckBox(LanguageCodes.CAPTION_MANDATORY.translation());

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(name);
		commonProperties.addComponent(label);
		commonProperties.addComponent(description);
		commonProperties.addComponent(answerType);
		commonProperties.addComponent(answerFormat);
		commonProperties.addComponent(answerSubformat);
		commonProperties.addComponent(horizontal);
		commonProperties.addComponent(mandatory);

		boolean canEdit = WebformsAuthorizationService.getInstance().isFormEditable(
				UserSessionHandler.getController().getFormInUse(), UserSessionHandler.getUser());
		commonProperties.setEnabled(canEdit);

		addTab(commonProperties, LanguageCodes.CAPTION_PROPERTIES_QUESTION.translation(), true);

		super.initElement();
	}

	protected void refreshAnswerSubformatOptions() {
		if (answerFormat.getValue() == null) {
			answerSubformat.setValue(null);
			answerSubformat.removeAllItems();
			answerSubformat.setEnabled(false);
		} else {
			answerSubformat.setValue(null);
			answerSubformat.removeAllItems();
			AnswerFormat format = (AnswerFormat) answerFormat.getValue();
			for (AnswerSubformatUi subformat : AnswerSubformatUi.values(format)) {
				answerSubformat.addItem(subformat.getSubformat());
				answerSubformat.setItemCaption(subformat.getSubformat(), subformat.getTranslationCode().translation());
			}
			answerSubformat.setEnabled(true);
			answerSubformat.setValue(format.getDefaultSubformat());
		}
	}

	@Override
	protected void initValues() {
		super.initValues();
		name.addValidator(new TreeObjectNameValidator(instance.getNameAllowedPattern()));
		name.setValue(instance.getName());
		// TODO dynamic label
		label.setValue(instance.getLabel());
		label.addValidator(new LengthValidator(instance.getMaxLabelLength()));
		description.setValue(instance.getDescription());
		mandatory.setValue(instance.isMandatory());
		answerType.setValue(instance.getAnswerType());
		answerFormat.setValue(instance.getAnswerFormat());
		answerSubformat.setValue(instance.getAnswerSubformat());
		horizontal.setValue(instance.isHorizontal());
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateElement() {
		try {
			if (name.isValid()) {
				instance.setName(name.getValue());
			}
			// TODO dynamic label
			if (label.isValid()) {
				instance.setLabel(label.getValue());
			}
			instance.setDescription(description.getValue());
			instance.setMandatory(mandatory.getValue());
			instance.setAnswerType((AnswerType) answerType.getValue());
			instance.setAnswerFormat((AnswerFormat) answerFormat.getValue());
			instance.setAnswerSubformat((AnswerSubformat) answerSubformat.getValue());
			instance.setHorizontal(horizontal.getValue());

		} catch (FieldTooLongException | InvalidAnswerFormatException | CharacterNotAllowedException
				| InvalidAnswerSubformatException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

		super.updateElement();
	}

}
