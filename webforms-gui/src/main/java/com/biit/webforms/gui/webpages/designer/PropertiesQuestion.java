package com.biit.webforms.gui.webpages.designer;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.common.components.PropertiesForStorableObjectWithImages;
import com.biit.webforms.language.AnswerFormatUi;
import com.biit.webforms.language.AnswerSubformatUi;
import com.biit.webforms.language.AnswerTypeUi;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Question;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

import java.util.Date;
import java.util.Objects;

public class PropertiesQuestion extends PropertiesForStorableObjectWithImages<Question> {
    private static final long serialVersionUID = 7572463216386081265L;
    private static final String WIDTH = "200px";
    private static final int MAX_ANSWERS_SELECTED = 2;

    private TextField name;
    private TextArea label;
    private TextField alias;
    private TextField abbreviature;

    private TextArea description;

    private TextArea defaultValueString;
    private DateField defaultValueDate;
    private ComboBox defaultValueAnswer;

    private CheckBox mandatory;
    private TextField maxAnswersSelected;

    private ComboBox answerType;

    private ComboBox answerFormat;

    private ComboBox answerSubformat;

    private CheckBox horizontal;

    // Disable the field if orbeon is in edition mode.
    private CheckBox disableEdition;

    public PropertiesQuestion() {
        super(Question.class);
    }

    @Override
    protected void initElement() {

        name = new TextField(LanguageCodes.CAPTION_TECHNICAL_NAME.translation());
        name.setWidth(WIDTH);
        name.setRequired(true);
        name.setMaxLength(TreeObject.MAX_UNIQUE_COLUMN_LENGTH);

        alias = new TextField(LanguageCodes.CAPTION_ALIAS_NAME.translation());
        alias.setWidth(WIDTH);
        alias.setMaxLength(Question.MAX_ALIAS_LENGTH);

        abbreviature = new TextField(LanguageCodes.CAPTION_ABBREVIATURE_NAME.translation());
        abbreviature.setWidth(WIDTH);
        abbreviature.setMaxLength(Question.MAX_ABBREVIATURE_LENGTH);

        label = new TextArea(LanguageCodes.CAPTION_LABEL.translation());
        label.setWidth(WIDTH);
        label.setMaxLength(TreeObject.MAX_LABEL_LENGTH);
        label.setImmediate(true);

        description = new TextArea(LanguageCodes.CAPTION_DESCRIPTION.translation());
        description.setWidth(WIDTH);
        description.setMaxLength(Question.MAX_DESCRIPTION_LENGTH);

        defaultValueString = new TextArea(LanguageCodes.CAPTION_DEFAULT_VALUE.translation());
        defaultValueString.setWidth(WIDTH);
        defaultValueString.setMaxLength(Question.MAX_DEFAULT_VALUE);

        defaultValueDate = new DateField(LanguageCodes.CAPTION_DEFAULT_VALUE.translation());
        defaultValueDate.setWidth(WIDTH);

        defaultValueAnswer = new ComboBox(LanguageCodes.CAPTION_DEFAULT_VALUE.translation());
        defaultValueAnswer.setWidth(WIDTH);

        answerType = new ComboBox(LanguageCodes.CAPTION_ANSWER_TYPE.translation());
        answerType.setWidth(WIDTH);
        for (AnswerTypeUi type : AnswerTypeUi.values()) {
            answerType.addItem(type.getAnswerType());
            answerType.setItemCaption(type.getAnswerType(), type.getLanguageCode().translation());
        }
        answerType.setNullSelectionAllowed(false);
        answerType.addValueChangeListener((ValueChangeListener) event -> {
            AnswerType selectedType = (AnswerType) answerType.getValue();
            // No Input fields must put the format to null or input fields
            // that has not any format already selected
            if (selectedType.getDefaultAnswerFormat() == null || getInstance().getAnswerFormat() == null) {
                answerFormat.setValue(selectedType.getDefaultAnswerFormat());
                answerFormat.setEnabled(selectedType.isAnswerFormatEnabled() && !getInstance().isReadOnly());
            }
            if (!selectedType.getDefaultHorizontal()) {
                horizontal.setValue(selectedType.getDefaultHorizontal());
                horizontal.setEnabled(selectedType.isHorizontalEnabled());
            }
            if (!selectedType.isMandatoryEnabled()) {
                mandatory.setValue(selectedType.getDefaultMandatory());
                mandatory.setEnabled(selectedType.isMandatoryEnabled());
            }
            maxAnswersSelected.setVisible(selectedType.isMaxAnswersSelectedEnabled());

            switch (selectedType) {
                case INPUT:
                    defaultValueAnswer.setVisible(false);
                    defaultValueAnswer.setValue(null);
                    break;
                case TEXT_AREA:
                    defaultValueString.setVisible(true);
                    defaultValueDate.setVisible(false);
                    defaultValueDate.setValue(null);
                    defaultValueAnswer.setVisible(false);
                    defaultValueAnswer.setValue(null);
                    break;
                case SINGLE_SELECTION_LIST:
                case SINGLE_SELECTION_RADIO:
                case SINGLE_SELECTION_SLIDER:
                case MULTIPLE_SELECTION:
                    defaultValueString.setVisible(false);
                    defaultValueString.setValue("");
                    defaultValueDate.setVisible(false);
                    defaultValueDate.setValue(null);
                    refreshDefaultValueAnswerValues();
                    defaultValueAnswer.setVisible(true);
                    break;
            }
        });

        answerFormat = new ComboBox(LanguageCodes.CAPTION_ANSWER_FORMAT.translation());
        answerFormat.setWidth(WIDTH);
        for (AnswerFormatUi format : AnswerFormatUi.values()) {
            answerFormat.addItem(format.getAnswerFormat());
            answerFormat.setItemCaption(format.getAnswerFormat(), format.getLanguageCode().translation());
        }
        answerFormat.setNullSelectionAllowed(false);
        answerFormat.addValueChangeListener((ValueChangeListener) event -> {
            if (answerFormat.getValue() != null) {
                AnswerFormat selectedFormat = (AnswerFormat) answerFormat.getValue();
                if (Objects.requireNonNull(selectedFormat) == AnswerFormat.DATE) {
                    defaultValueString.setVisible(false);
                    defaultValueString.setValue("");
                    defaultValueDate.setVisible(true);
                } else {
                    defaultValueString.setVisible(true);
                    defaultValueDate.setVisible(false);
                    defaultValueDate.setValue(null);
                }
            }
            refreshAnswerSubformatOptions();
        });
        answerFormat.setImmediate(true);

        answerSubformat = new ComboBox(LanguageCodes.CAPTION_ANSWER_SUBFORMAT.translation());
        answerSubformat.setWidth(WIDTH);
        answerSubformat.setNullSelectionAllowed(false);
        refreshAnswerSubformatOptions();

        horizontal = new CheckBox(LanguageCodes.CAPTION_HORIZONTAL.translation());

        mandatory = new CheckBox(LanguageCodes.CAPTION_MANDATORY.translation());

        maxAnswersSelected = new TextField(LanguageCodes.CAPTION_MAX_ANSWERS_SELECTED.translation());
        maxAnswersSelected.setWidth(WIDTH);
        maxAnswersSelected.addValidator(new RegexpValidator("[-]?[0-9]*\\.?,?[0-9]+"
                , "This is not a number!"));
        maxAnswersSelected.setMaxLength(MAX_ANSWERS_SELECTED);

        disableEdition = new CheckBox(LanguageCodes.CAPTION_DISABLE_EDITION.translation());
        disableEdition.setDescription(LanguageCodes.CAPTION_DISABLE_EDITION_TOOLTIP.translation());

        FormLayout commonProperties = new FormLayout();
        commonProperties.setWidth(null);
        commonProperties.setHeight(null);
        commonProperties.addComponent(name);
        commonProperties.addComponent(label);
        commonProperties.addComponent(abbreviature);
        commonProperties.addComponent(alias);
        commonProperties.addComponent(description);
        commonProperties.addComponent(answerType);
        commonProperties.addComponent(answerFormat);
        commonProperties.addComponent(answerSubformat);
        commonProperties.addComponent(defaultValueString);
        commonProperties.addComponent(defaultValueDate);
        commonProperties.addComponent(defaultValueAnswer);
        commonProperties.addComponent(horizontal);
        commonProperties.addComponent(mandatory);
        commonProperties.addComponent(maxAnswersSelected);
        commonProperties.addComponent(disableEdition);

        boolean canEdit = getWebformsSecurityService().isElementEditable(ApplicationUi.getController().getFormInUse(), UserSession.getUser());
        commonProperties.setEnabled(canEdit);

        addTab(commonProperties, LanguageCodes.CAPTION_PROPERTIES_QUESTION.translation(), true);

        super.initElement();
    }

    protected void refreshDefaultValueAnswerValues() {
        defaultValueAnswer.removeAllItems();
        for (Answer answer : getInstance().getFinalAnswers()) {
            defaultValueAnswer.addItem(answer);
            defaultValueAnswer.setItemCaption(answer, answer.getLabel());
        }
        defaultValueAnswer.setValue(getInstance().getDefaultValueAnswer());
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
                // Skip the DATE_PERIOD value
                if (format.equals(AnswerFormat.DATE) && subformat.equals(AnswerSubformatUi.DATE_PERIOD)) {
                    continue;
                }
                answerSubformat.addItem(subformat.getSubformat());
                answerSubformat.setItemCaption(subformat.getSubformat(), subformat.getLanguageCode().translation());
            }
            answerSubformat.setEnabled(isAnswerSubformatEnabled());
            answerSubformat.setValue(format.getDefaultSubformat());
        }
    }

    @Override
    protected void initValues() {
        super.initValues();
        name.addValidator(new ValidatorTreeObjectName(getInstance().getNameAllowedPattern()));
        name.addValidator(new ValidatorDuplicateNameOnSameTreeObjectLevel(getInstance()));
        name.addValidator(new ValidatorTreeObjectNameLength());
        name.setValue(getInstance().getName());
        name.setEnabled(!getInstance().isReadOnly());

        alias.setValue(getInstance().getAlias() != null ? getInstance().getAlias() : "");
        alias.setEnabled(!getInstance().isReadOnly());

        abbreviature.setValue(getInstance().getAbbreviation() != null ? getInstance().getAbbreviation() : "");
        abbreviature.setEnabled(!getInstance().isReadOnly());

        label.setValue(getInstance().getLabel());
        label.addValidator(new LengthValidator(getInstance().getMaxLabelLength()));
        label.setEnabled(!getInstance().isReadOnly());

        description.setValue(getInstance().getDescription());
        description.setEnabled(!getInstance().isReadOnly());

        mandatory.setValue(getInstance().isMandatory());
        mandatory.setEnabled(!getInstance().isReadOnly());

        answerType.setValue(getInstance().getAnswerType());
        answerType.setEnabled(!getInstance().isReadOnly());

        // AnswerFormat enabled is controlled in other part of the code.
        answerFormat.setValue(getInstance().getAnswerFormat());
        answerSubformat.setValue(getInstance().getAnswerSubformat());
        answerSubformat.setEnabled(isAnswerSubformatEnabled());

        horizontal.setValue(getInstance().isHorizontal());
        horizontal.setEnabled(getInstance().getAnswerType().isHorizontalEnabled() && !getInstance().isReadOnly());

        if (getInstance().getDefaultValueString() != null) {
            defaultValueString.setValue(getInstance().getDefaultValueString());
        } else {
            defaultValueString.setValue("");
        }
        if (getInstance().getDefaultValueTime() != null) {
            defaultValueDate.setValue(new Date(getInstance().getDefaultValueTime().getTime()));
        } else {
            defaultValueDate.setValue(null);
        }
        defaultValueAnswer.setValue(getInstance().getDefaultValueAnswer());
        disableEdition.setValue(getInstance().isEditionDisabled());

        disableEdition.setEnabled(!getInstance().isReadOnly());

        defaultValueDate.setEnabled(!getInstance().isReadOnly());
        defaultValueString.setEnabled(!getInstance().isReadOnly());
        defaultValueAnswer.setEnabled(!getInstance().isReadOnly());
    }

    @Override
    protected void firePropertyUpdateOnExitListener() {
        updateElement();
    }

    private boolean isAnswerSubformatEnabled() {
        return !getInstance().isReadOnly() && answerType.getValue() == AnswerType.INPUT;
    }

    @Override
    public void updateElement() {
        String tempName = getInstance().getName();
        String tempLabel = getInstance().getLabel();
        Object tempDefaultValue = null;
        if (name.isValid()) {
            tempName = name.getValue();
        }
        if (label.isValid()) {
            tempLabel = label.getValue();
        }
        if (defaultValueString.getValue() != null && !defaultValueString.getValue().isEmpty()) {
            tempDefaultValue = defaultValueString.getValue();
        }
        if (tempDefaultValue == null) {
            tempDefaultValue = defaultValueDate.getValue();
        }
        if (tempDefaultValue == null) {
            tempDefaultValue = defaultValueAnswer.getValue();
        }

        ApplicationUi.getController().updateQuestion(getInstance(), tempName, tempLabel, abbreviature.getValue(), alias.getValue(),
                description.getValue(), mandatory.getValue(), (AnswerType) answerType.getValue(), (AnswerFormat) answerFormat.getValue(),
                (AnswerSubformat) answerSubformat.getValue(), horizontal.getValue(), tempDefaultValue, disableEdition.getValue(), getImage());

        super.updateElement();
    }
}
