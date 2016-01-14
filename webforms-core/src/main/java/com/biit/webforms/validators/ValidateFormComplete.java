package com.biit.webforms.validators;

import java.util.Set;

import com.biit.form.validators.ValidateBaseForm;
import com.biit.utils.validation.CompositeValidator;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.webservices.Webservice;

public class ValidateFormComplete extends CompositeValidator<Form> {

	public ValidateFormComplete() {
		super(Form.class);
		configure();
	}

	public ValidateFormComplete(Set<Webservice> webservices) {
		super(Form.class);
		configure();
		setExtraData(webservices);
	}

	private void configure() {
		add(ValidateBaseForm.class);
		add(ValidateFormStructure.class);
		add(ValidateFormFlows.class);
		add(ValidateLogic.class);
		add(ValidateWebserviceCalls.class);
		setStopOnFail(true);
	}

}
