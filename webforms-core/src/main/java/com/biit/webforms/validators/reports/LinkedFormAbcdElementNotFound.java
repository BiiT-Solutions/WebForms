package com.biit.webforms.validators.reports;

import com.biit.form.BaseCategory;
import com.biit.form.BaseForm;
import com.biit.form.BaseGroup;
import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Form;

public class LinkedFormAbcdElementNotFound extends Report {

	public LinkedFormAbcdElementNotFound(Form form, BaseForm abcdForm, TreeObject abcdChild) {
		super(ReportLevel.ERROR, generateReport(form, abcdForm, abcdChild));
	}

	private static String generateReport(Form form, BaseForm abcdForm, TreeObject abcdChild) {
		StringBuilder sb = new StringBuilder();
		sb.append("Form '");
		sb.append(abcdForm.getLabel());
		sb.append("' Version '");
		sb.append(abcdForm.getVersion());
		if (abcdChild instanceof BaseCategory) {
			sb.append("' has category '");
		} else {
			if (abcdChild instanceof BaseGroup) {
				sb.append("' has group '");
			} else {
				if (abcdChild instanceof BaseQuestion) {
					sb.append("' has group '");
				} else {
					sb.append("' has element '");
				}
			}
		}
		sb.append(abcdChild.getPathName());
		sb.append("' and it's not found in webforms form");
		return sb.toString();
	}

}
