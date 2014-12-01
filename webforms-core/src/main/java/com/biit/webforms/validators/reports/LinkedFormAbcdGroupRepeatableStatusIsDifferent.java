package com.biit.webforms.validators.reports;

import com.biit.form.BaseForm;
import com.biit.form.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Form;

public class LinkedFormAbcdGroupRepeatableStatusIsDifferent extends Report {

	public LinkedFormAbcdGroupRepeatableStatusIsDifferent(Form form, BaseForm abcdForm, TreeObject abcdChild) {
		super(ReportLevel.ERROR, generateReport(form,abcdForm,abcdChild));
	}

	private static String generateReport(Form form, BaseForm abcdForm, TreeObject abcdChild) {
		StringBuilder sb = new StringBuilder();
		sb.append("Abcd Form '");
		sb.append(abcdForm.getLabel());
		sb.append("' element '");
		sb.append(abcdChild.getPathName());
		sb.append("' has repeatable flag with a different value than webforms version.");
		return sb.toString();
	}
}
