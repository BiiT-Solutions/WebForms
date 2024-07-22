package com.biit.webforms.validators;

import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.validators.reports.OnlyNumbersInSliderAllowed;

import java.util.Objects;

/**
 * Sliders only can have answers with numbers in names.
 */
public class ValidateSliders extends SimpleValidator<TreeObject> {

    public ValidateSliders() {
        super(TreeObject.class);
    }

    @Override
    protected void validateImplementation(TreeObject element) {
        if (element instanceof WebformsBaseQuestion) {
            if (Objects.equals(((WebformsBaseQuestion) element).getAnswerType(), AnswerType.SINGLE_SELECTION_SLIDER)) {
                for (TreeObject child : element.getChildren()) {
                    try {
                        Integer.parseInt(child.getName());
                    } catch (NumberFormatException e) {
                        assertTrue(false, new OnlyNumbersInSliderAllowed((WebformsBaseQuestion) element, child));
                    }
                }
            }
        } else {
            for (TreeObject child : element.getChildren()) {
                validateImplementation(child);
            }
        }
    }
}
