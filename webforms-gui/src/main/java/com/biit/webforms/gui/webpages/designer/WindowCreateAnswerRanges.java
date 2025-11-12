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

import com.biit.form.result.QuestionWithValueResult;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.exceptions.InvalidValue;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

import java.math.BigDecimal;

public class WindowCreateAnswerRanges extends WindowAcceptCancel {
    private static final long serialVersionUID = 5008247299399153993L;
    private static final String width = "340px";
    private static final String height = "220px";

    private TextField lowerValue;
    private TextField upperValue;
    private TextField stepRange;

    public WindowCreateAnswerRanges() {
        super();
        setContent(generateContent());
        setResizable(false);
        setDraggable(false);
        setClosable(false);
        setModal(true);
        setWidth(width);
        setHeight(height);
        setCaption(LanguageCodes.WINDOW_ANSWER_RANGE_CAPTION.translation());
    }

    private Component generateContent() {
        lowerValue = new TextField(LanguageCodes.ANSWER_RANGE_LOWER_VALUE.translation());
        lowerValue.focus();
        lowerValue.setWidth("100%");
        lowerValue.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 4953347262492851075L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                lowerValue.isValid();
            }
        });

        lowerValue.setMaxLength(QuestionWithValueResult.MAX_LENGTH);
        lowerValue.addValidator(new ValidatorFloat());

        upperValue = new TextField(LanguageCodes.ANSWER_RANGE_UPPER_VALUE.translation());
        upperValue.setWidth("100%");
        upperValue.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 4953347262492851075L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                upperValue.isValid();
            }
        });
        upperValue.addValidator(new ValidatorFloat());

        stepRange = new TextField(LanguageCodes.ANSWER_RANGE_STEAP_VALUE.translation());
        stepRange.setWidth("100%");
        stepRange.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 4953347262492851075L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                stepRange.isValid();
            }
        });
        stepRange.addValidator(new ValidatorFloat());

        FormLayout layout = new FormLayout();
        layout.setSpacing(true);
        layout.setSizeFull();

        layout.addComponent(lowerValue);
        layout.addComponent(upperValue);
        layout.addComponent(stepRange);
        layout.setComponentAlignment(lowerValue, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(upperValue, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(stepRange, Alignment.MIDDLE_CENTER);
        return layout;
    }

    public BigDecimal getLowerValue() throws InvalidValue {
        try {
            return new BigDecimal(lowerValue.getValue());
        } catch (NumberFormatException e) {
            throw new InvalidValue();
        }
    }

    public BigDecimal getUpperValue() throws InvalidValue {
        try {
            return new BigDecimal(upperValue.getValue());
        } catch (NumberFormatException e) {
            throw new InvalidValue();
        }
    }

    public BigDecimal getStepValue() throws InvalidValue {
        try {
            return new BigDecimal(stepRange.getValue());
        } catch (NumberFormatException e) {
            throw new InvalidValue();
        }
    }

}
