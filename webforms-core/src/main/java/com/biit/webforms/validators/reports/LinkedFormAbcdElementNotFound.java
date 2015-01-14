package com.biit.webforms.validators.reports;

import com.biit.form.BaseCategory;
import com.biit.form.BaseForm;
import com.biit.form.BaseGroup;
import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class LinkedFormAbcdElementNotFound extends Report {

	private BaseForm webform;
	private BaseForm abcdform;
	private TreeObject abcdChild;

	public LinkedFormAbcdElementNotFound(BaseForm form, BaseForm abcdForm, TreeObject abcdChild) {
		super(ReportLevel.ERROR, generateReport(form, abcdForm, abcdChild));
		this.webform = form;
		this.abcdform = abcdForm;
		this.abcdChild = abcdChild;
	}

	private static String generateReport(BaseForm form, BaseForm abcdForm, TreeObject abcdChild) {
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
		sb.append("' and it's not found in webforms form.");
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
