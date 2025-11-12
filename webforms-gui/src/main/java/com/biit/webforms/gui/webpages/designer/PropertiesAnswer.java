package com.biit.webforms.gui.webpages.designer;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.form.entity.TreeObject;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.common.components.PropertiesForStorableObjectWithMedia;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Answer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class PropertiesAnswer extends PropertiesForStorableObjectWithMedia<Answer> {
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
        value.setRequired(true);
        value.setMaxLength(TreeObject.MAX_UNIQUE_COLUMN_LENGTH);

        label = new TextField(LanguageCodes.CAPTION_LABEL.translation());
        label.setWidth(WIDTH);
        label.setMaxLength(TreeObject.MAX_LABEL_LENGTH);

        description = new TextArea(LanguageCodes.CAPTION_DESCRIPTION.translation());
        description.setWidth(WIDTH);
        description.setMaxLength(Answer.MAX_DESCRIPTION_LENGTH);

        FormLayout commonProperties = new FormLayout();
        commonProperties.setWidth(null);
        commonProperties.setHeight(null);
        commonProperties.addComponent(value);
        commonProperties.addComponent(label);
        commonProperties.addComponent(description);

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
                getImage(), getVideo(), getAudio());

        super.updateElement();
    }

    @Override
    protected void firePropertyUpdateOnExitListener() {
        updateElement();
    }

}
