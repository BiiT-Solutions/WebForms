package com.biit.webforms.gui.webpages.designer;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.common.components.PropertiesForStorableObjectWithImages;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Answer;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class PropertiesAnswer extends PropertiesForStorableObjectWithImages<Answer> {
    private static final long serialVersionUID = 8035711998129559199L;
    private static final String WIDTH = "200px";

    private TextField value, label;

    private TextArea description;

    private CheckBox descriptionAlwaysVisible;

    public PropertiesAnswer() {
        super(Answer.class);
    }

    @Override
    protected void initElement() {

        value = new TextField(LanguageCodes.CAPTION_VALUE.translation());
        value.setWidth(WIDTH);
        value.setRequired(true);
        value.setMaxLength(TreeObject.MAX_UNIQUE_COLUMN_LENGTH);

        label = new TextField(LanguageCodes.CAPTION_LABEL.translation());
        label.setWidth(WIDTH);
        label.setMaxLength(TreeObject.MAX_LABEL_LENGTH);

        description = new TextArea(LanguageCodes.CAPTION_DESCRIPTION.translation());
        description.setWidth(WIDTH);
        description.setMaxLength(Answer.MAX_DESCRIPTION_LENGTH);

        descriptionAlwaysVisible = new CheckBox(LanguageCodes.CAPTION_DESCRIPTION_ALWAYS_VISIBLE.translation());

        FormLayout commonProperties = new FormLayout();
        commonProperties.setWidth(null);
        commonProperties.setHeight(null);
        commonProperties.addComponent(value);
        commonProperties.addComponent(label);
        commonProperties.addComponent(description);
        commonProperties.addComponent(descriptionAlwaysVisible);

        boolean canEdit = getWebformsSecurityService().isElementEditable(ApplicationUi.getController().getFormInUse(),
                UserSession.getUser());
        commonProperties.setEnabled(canEdit);

        addTab(commonProperties, LanguageCodes.CAPTION_PROPERTIES_ANSWER.translation(), true);

        super.initElement();
    }

    @Override
    protected void initValues() {
        super.initValues();

        value.addValidator(new ValidatorTreeObjectName(getInstance().getNameAllowedPattern()));
        value.addValidator(new ValidatorTreeObjectNameLength());
        value.addValidator(new ValidatorDuplicateValueAnswer(getInstance()));
        value.setValue(getInstance().getValue());
        value.setEnabled(!getInstance().isReadOnly());

        label.setValue(getInstance().getLabel());
        label.addValidator(new LengthValidator(getInstance().getMaxLabelLength()));
        label.setEnabled(!getInstance().isReadOnly());

        description.setValue(getInstance().getDescription());
        description.setEnabled(!getInstance().isReadOnly());

        descriptionAlwaysVisible.setValue(getInstance().isDescriptionAlwaysVisible());
        descriptionAlwaysVisible.setEnabled(!getInstance().isReadOnly());
    }

    @Override
    public void updateElement() {
        String tempValue = getInstance().getValue();
        String tempLabel = getInstance().getLabel();
        if (value.isValid()) {
            tempValue = value.getValue();
        }
        if (label.isValid()) {
            tempLabel = label.getValue();
        }
        ApplicationUi.getController().updateAnswer(getInstance(), tempValue, tempLabel, description.getValue(),
                descriptionAlwaysVisible.getValue(), getImage());

        super.updateElement();
    }

    @Override
    protected void firePropertyUpdateOnExitListener() {
        updateElement();
    }

}
