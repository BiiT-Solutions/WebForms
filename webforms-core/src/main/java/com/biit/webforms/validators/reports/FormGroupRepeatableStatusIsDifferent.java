package com.biit.webforms.validators.reports;

import com.biit.form.BaseForm;
import com.biit.form.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class FormGroupRepeatableStatusIsDifferent extends Report {

	private BaseForm webform;
	private BaseForm abcdForm;
	private TreeObject abcdChild;

	public FormGroupRepeatableStatusIsDifferent(BaseForm form, BaseForm abcdForm, TreeObject abcdChild) {
		super(ReportLevel.ERROR, generateReport(form, abcdForm, abcdChild));
		this.webform = form;
		this.abcdChild = abcdChild;
		this.abcdForm = abcdForm;
	}

	private static String generateReport(BaseForm form, BaseForm abcdForm, TreeObject abcdChild) {
		StringBuilder sb = new StringBuilder();
		sb.append("Form '");
		sb.append(abcdForm.getLabel());
		sb.append("' element '");
		sb.append(abcdChild.getPathName());
		sb.append("' has repeatable flag with a different value that the compared version.");
		return sb.toString();
	}

	public BaseForm getAbcdForm() {
		return abcdForm;
	}

	public TreeObject getAbcdChild() {
		return abcdChild;
	}

	public BaseForm getWebform() {
		return webform;
	}
}
