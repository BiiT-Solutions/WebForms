package com.biit.webforms.validators.reports;

import com.biit.form.BaseForm;
import com.biit.form.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class FormElementIsBaseQuestionNotBaseGroup extends Report {

	private BaseForm formWithQuestion;
	private BaseForm formWithGroup;
	private TreeObject elementAsQuestion;

	public FormElementIsBaseQuestionNotBaseGroup(BaseForm formWithQuestion, BaseForm formWithGroup,
			TreeObject elementAsQuestion) {
		super(ReportLevel.ERROR, generateReport(formWithQuestion, formWithGroup, elementAsQuestion));
		this.formWithQuestion = formWithQuestion;
		this.formWithGroup = formWithGroup;
		this.elementAsQuestion = elementAsQuestion;
	}

	private static String generateReport(BaseForm formWithQuestion, BaseForm formWithGroup, TreeObject elementAsQuestion) {
		StringBuilder sb = new StringBuilder();
		sb.append("Form '");
		sb.append(formWithQuestion.getLabel());
		sb.append("' Version '");
		sb.append(formWithQuestion.getVersion());
		sb.append("' element '");
		sb.append(elementAsQuestion.getPathName());
		sb.append("' is a question and is found as a group in ");
		sb.append("Form '");
		sb.append(formWithGroup.getLabel());
		sb.append("' Version '");
		sb.append(formWithGroup.getVersion());
		sb.append("'.");
		return sb.toString();
	}

	public BaseForm getFormWithQuestion() {
		return formWithQuestion;
	}

	public BaseForm getFormWithGroup() {
		return formWithGroup;
	}

	public TreeObject getElementAsQuestion() {
		return elementAsQuestion;
	}
}
