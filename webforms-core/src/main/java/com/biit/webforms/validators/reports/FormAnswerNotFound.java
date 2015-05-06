package com.biit.webforms.validators.reports;

import com.biit.form.entity.BaseForm;
import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class FormAnswerNotFound extends Report {

	private BaseForm formWithElement;
	private BaseForm formWithoutElement;
	private TreeObject elementMissed;

	public FormAnswerNotFound(BaseForm formWithElement, BaseForm formWithoutElement, TreeObject elementMissed) {
		super(ReportLevel.ERROR, generateReport(formWithElement, formWithoutElement, elementMissed));
		this.formWithElement = formWithElement;
		this.formWithoutElement = formWithoutElement;
		this.elementMissed = elementMissed;
	}

	private static String generateReport(BaseForm formWithElement, BaseForm formWithoutElement, TreeObject elementMissed) {
		StringBuilder sb = new StringBuilder();
		sb.append("Form '");
		sb.append(formWithoutElement.getLabel());
		sb.append("' Version '");
		sb.append(formWithoutElement.getVersion());
		sb.append("' has answer '");
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
