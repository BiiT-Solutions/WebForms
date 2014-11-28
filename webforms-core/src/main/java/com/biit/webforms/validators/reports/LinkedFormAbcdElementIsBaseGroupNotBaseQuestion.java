package com.biit.webforms.validators.reports;

import com.biit.form.BaseForm;
import com.biit.form.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Form;

public class LinkedFormAbcdElementIsBaseGroupNotBaseQuestion extends Report {

	public LinkedFormAbcdElementIsBaseGroupNotBaseQuestion(Form form, BaseForm abcdForm, TreeObject abcdChild) {
		super(ReportLevel.ERROR, generateReport(form,abcdForm,abcdChild));
	}

	private static String generateReport(Form form, BaseForm abcdForm, TreeObject abcdChild) {
		StringBuilder sb = new StringBuilder();
		sb.append("Form '");
		sb.append(abcdForm.getLabel());
		sb.append("' Version '");
		sb.append(abcdForm.getVersion());
		sb.append("' element '");
		sb.append(abcdChild.getPathName());
		sb.append("' is a group in webforms is found as a base question");
		return sb.toString();
	}

}
