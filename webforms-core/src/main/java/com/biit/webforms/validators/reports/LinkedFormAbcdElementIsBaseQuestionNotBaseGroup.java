package com.biit.webforms.validators.reports;

import com.biit.form.BaseForm;
import com.biit.form.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class LinkedFormAbcdElementIsBaseQuestionNotBaseGroup extends Report {

	private BaseForm webform;
	private BaseForm abcdform;
	private TreeObject abcdChild;

	public LinkedFormAbcdElementIsBaseQuestionNotBaseGroup(BaseForm form, BaseForm abcdForm, TreeObject abcdChild) {
		super(ReportLevel.ERROR, generateReport(form, abcdForm, abcdChild));
		this.webform = form;
		this.abcdform = abcdForm;
		this.abcdChild = abcdChild;
	}

	private static String generateReport(BaseForm form, BaseForm abcdForm, TreeObject abcdChild) {
		StringBuilder sb = new StringBuilder();
		sb.append("Form '");
		sb.append(abcdForm.getLabel());
		sb.append("' element '");
		sb.append(abcdChild.getPathName());
		sb.append("' is a question and in webforms is found as a group.");
		return sb.toString();
	}

	public BaseForm getWebform() {
		return webform;
	}

	public BaseForm getAbcdform() {
		return abcdform;
	}

	public TreeObject getAbcdChild() {
		return abcdChild;
	}
}
