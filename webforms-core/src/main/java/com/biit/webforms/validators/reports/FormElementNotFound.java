package com.biit.webforms.validators.reports;

import com.biit.form.BaseCategory;
import com.biit.form.BaseForm;
import com.biit.form.BaseGroup;
import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class FormElementNotFound extends Report {

	private BaseForm formWithElement;
	private BaseForm formWithoutElement;
	private TreeObject elementMissed;

	public FormElementNotFound(BaseForm formWithElement, BaseForm formWithoutElement, TreeObject elementMissed) {
		super(ReportLevel.ERROR, generateReport(formWithElement, formWithoutElement, elementMissed));
		this.formWithElement = formWithElement;
		this.formWithoutElement = formWithoutElement;
		this.elementMissed = elementMissed;
	}

	private static String generateReport(BaseForm formWithElement, BaseForm formWithoutElement, TreeObject elementMissed) {
		StringBuilder sb = new StringBuilder();
		sb.append("Form '");
		sb.append(formWithElement.getLabel());
		sb.append("' Version '");
		sb.append(formWithElement.getVersion());
		if (elementMissed instanceof BaseCategory) {
			sb.append("' has category '");
		} else {
			if (elementMissed instanceof BaseGroup) {
				sb.append("' has group '");
			} else {
				if (elementMissed instanceof BaseQuestion) {
					sb.append("' has group '");
				} else {
					sb.append("' has element '");
				}
			}
		}
		sb.append(elementMissed.getPathName());
		sb.append("' and it's not found in ");
		sb.append("Form '");
		sb.append(formWithoutElement.getLabel());
		sb.append("' Version '");
		sb.append(formWithoutElement.getVersion());
		sb.append("'.");
		return sb.toString();
	}

	public BaseForm getFormWithElement() {
		return formWithElement;
	}

	public BaseForm getFormWithoutElement() {
		return formWithoutElement;
	}

	public TreeObject getElementMissed() {
		return elementMissed;
	}

}
