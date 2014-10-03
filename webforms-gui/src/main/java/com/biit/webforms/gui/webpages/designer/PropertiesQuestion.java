package com.biit.webforms.gui.webpages.designer;

import com.biit.form.TreeObject;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.language.AnswerFormatUi;
import com.biit.webforms.language.AnswerSubformatUi;
import com.biit.webforms.language.AnswerTypeUi;
import com.biit.webforms.language.LanguageCodes;
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
		name.setMaxLength(TreeObject.MAX_UNIQUE_COLUMN_LENGTH);

		label = new TextArea(LanguageCodes.CAPTION_LABEL.translation());
		label.setWidth(WIDTH);
		label.setMaxLength(TreeObject.MAX_LABEL_LENGTH);
		label.setImmediate(true);

		description = new TextArea(LanguageCodes.CAPTION_DESCRIPTION.translation());
		description.setWidth(WIDTH);
		description.setMaxLength(Question.MAX_DESCRIPTION_LENGTH);

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
		name.addValidator(new ValidatorTreeObjectName(instance.getNameAllowedPattern()));
		name.addValidator(new ValidatorDuplicateNameOnSameTreeObjectLevel(instance));
		name.addValidator(new ValidatorTreeObjectNameLength());
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
		String tempName = instance.getName();
		String tempLabel = instance.getLabel();
		if (name.isValid()) {
			tempName = name.getValue();
		}
		if (label.isValid()) {
			tempLabel = label.getValue();
		}
		UserSessionHandler.getController().updateQuestion(instance, tempName, tempLabel, description.getValue(),
				mandatory.getValue(), (AnswerType) answerType.getValue(), (AnswerFormat) answerFormat.getValue(),
				(AnswerSubformat) answerSubformat.getValue(), horizontal.getValue());

		super.updateElement();
	}

}
