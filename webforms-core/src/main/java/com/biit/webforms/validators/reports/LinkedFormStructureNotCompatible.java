package com.biit.webforms.validators.reports;

import com.biit.form.entity.BaseForm;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class LinkedFormStructureNotCompatible extends Report {

	private BaseForm webform;
	private BaseForm abcdForm;

	public LinkedFormStructureNotCompatible(BaseForm form, BaseForm abcdForm) {
		super(ReportLevel.ERROR, generateReport(form, abcdForm));
		this.webform = form;
		this.abcdForm = abcdForm;
	}

	private static String generateReport(BaseForm form, BaseForm abcdForm) {
		StringBuilder sb = new StringBuilder();
		sb.append("Form '");
		sb.append(form.getLabel());
		sb.append("' Version '");
		sb.append(abcdForm.getVersion());
		sb.append("' is not compatible with '");
		sb.append(abcdForm);
		sb.append("' version '");
		sb.append(abcdForm.getVersion());
		sb.append("'.");
		return sb.toString();
	}

	public BaseForm getWebform() {
		return webform;
	}

	public BaseForm getAbcdForm() {
		return abcdForm;
	}

}
