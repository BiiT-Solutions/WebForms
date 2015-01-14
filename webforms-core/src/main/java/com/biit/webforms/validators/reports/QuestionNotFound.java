package com.biit.webforms.validators.reports;

import com.biit.abcd.persistence.entity.Question;
import com.biit.form.BaseForm;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class QuestionNotFound extends Report {

	private BaseForm webform;
	private Question question;

	public QuestionNotFound(BaseForm form, Question abcdQuestion) {
		super(ReportLevel.ERROR, generateReport(form, abcdQuestion));
		this.webform = form;
		this.question = abcdQuestion;
	}

	private static String generateReport(BaseForm form, Question abcdQuestion) {
		StringBuilder sb = new StringBuilder();
		sb.append("Form '");
		sb.append(form);
		sb.append("' doesn't contain question '");
		sb.append(abcdQuestion.getPathName());
		sb.append("'.");
		return sb.toString();
	}

	public Question getQuestion() {
		return question;
	}

	public BaseForm getWebform() {
		return webform;
	}

}
