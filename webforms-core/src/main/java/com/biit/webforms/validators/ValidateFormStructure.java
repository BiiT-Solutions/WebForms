package com.biit.webforms.validators;

import com.biit.form.validators.ValidateBaseForm;
import com.biit.utils.validation.CompositeValidator;
import com.biit.webforms.persistence.entity.Form;

public class ValidateFormStructure extends CompositeValidator<Form> {

	public ValidateFormStructure() {
		super(Form.class);
		add(ValidateBaseForm.class);
		add(ValidateSubanswers.class);
		add(ValidateDynamicAnswers.class);
		add(ValidateDefaultValues.class);
	}

}
