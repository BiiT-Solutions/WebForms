package com.biit.webforms.validators.reports;

import com.biit.form.entity.BaseForm;
import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class FormElementIsBaseGroupNotBaseQuestion extends Report {

	private BaseForm formWithGroup;
	private BaseForm formWithQuestion;
	private TreeObject elementAsGroup;

	public FormElementIsBaseGroupNotBaseQuestion(BaseForm formWithGroup, BaseForm formWithQuestion, TreeObject elementAsGroup) {
		super(ReportLevel.ERROR, generateReport(formWithGroup, formWithQuestion, elementAsGroup));
		this.formWithGroup = formWithGroup;
		this.formWithQuestion = formWithQuestion;
		this.elementAsGroup = elementAsGroup;
	}

	private static String generateReport(BaseForm formWithGroup, BaseForm formWithQuestion, TreeObject elementAsGroup) {
		StringBuilder sb = new StringBuilder();
		sb.append("Form '");
		sb.append(formWithGroup.getLabel());
		sb.append("' Version '");
		sb.append(formWithGroup.getVersion());
		sb.append("' element '");
		sb.append(elementAsGroup.getPathName());
		sb.append("' is a group and has been found as a question in ");
		sb.append("Form '");
		sb.append(formWithQuestion.getLabel());
		sb.append("' Version '");
		sb.append(formWithQuestion.getVersion());
		sb.append("'.");
		return sb.toString();
	}

	public BaseForm getFormWithGroup() {
		return formWithGroup;
	}

	public BaseForm getFormWithQuestion() {
		return formWithQuestion;
	}

	public TreeObject getElementAsGroup() {
		return elementAsGroup;
	}

}
