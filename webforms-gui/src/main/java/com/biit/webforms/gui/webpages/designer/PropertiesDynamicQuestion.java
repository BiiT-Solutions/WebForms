package com.biit.webforms.gui.webpages.designer;

import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.language.AnswerTypeUi;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.DynamicQuestion;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;

public class PropertiesDynamicQuestion extends StorableObjectProperties<DynamicQuestion> {
	private static final long serialVersionUID = 986400400927849415L;
	private static final String WIDTH = "200px";
	
	private ComboBox answerTypeComboBox;
	private CheckBox mandatory;
	private CheckBox horizontal;

	public PropertiesDynamicQuestion() {
		super(DynamicQuestion.class);
	}
	
	@Override
	protected void initElement() {
		
		answerTypeComboBox = new ComboBox(LanguageCodes.CAPTION_ANSWER_TYPE.translation());
		answerTypeComboBox.setWidth(WIDTH);
		for (AnswerTypeUi type : AnswerTypeUi.values()) {
			answerTypeComboBox.addItem(type.getAnswerType());
			answerTypeComboBox.setItemCaption(type.getAnswerType(), type.getLanguageCode().translation());
		}
		answerTypeComboBox.setNullSelectionAllowed(false);
		answerTypeComboBox.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -7743742253650945202L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				AnswerType selectedType = (AnswerType) answerTypeComboBox.getValue();
				if (!selectedType.getDefaultHorizontal()) {
					horizontal.setValue(selectedType.getDefaultHorizontal());
					horizontal.setEnabled(selectedType.isHorizontalEnabled());
				}
				if (!selectedType.isMandatoryEnabled()) {
					mandatory.setValue(selectedType.getDefaultMandatory());
					mandatory.setEnabled(selectedType.isMandatoryEnabled());
				}
			}
		});
		
		horizontal = new CheckBox(LanguageCodes.CAPTION_HORIZONTAL.translation());

		mandatory = new CheckBox(LanguageCodes.CAPTION_MANDATORY.translation());
		
		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(answerTypeComboBox);
		commonProperties.addComponent(horizontal);
		commonProperties.addComponent(mandatory);
		
		boolean canEdit = WebformsAuthorizationService.getInstance().isFormEditable(
				UserSessionHandler.getController().getFormInUse(), UserSessionHandler.getUser());
		commonProperties.setEnabled(canEdit);

		addTab(commonProperties, LanguageCodes.CAPTION_PROPERTIES_QUESTION.translation(), true);

		super.initElement();
	}

	@Override
	public void updateElement() {
		
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		updateElement();
	}

}
