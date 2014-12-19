package com.biit.webforms.validators;

import com.biit.form.validators.ValidateBaseForm;
import com.biit.utils.validation.CompositeValidator;
import com.biit.webforms.persistence.entity.Form;

public class ValidateFormComplete extends CompositeValidator<Form>{

	public ValidateFormComplete() {
		super(Form.class);
		add(ValidateBaseForm.class);
		add(ValidateFormFlows.class);
		add(ValidateLogic.class);
		setStopOnFail(true);
	}
	
}
