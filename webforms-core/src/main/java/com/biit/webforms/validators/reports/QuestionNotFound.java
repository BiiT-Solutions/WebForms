package com.biit.webforms.validators.reports;

import com.biit.abcd.persistence.entity.Question;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Form;

public class QuestionNotFound extends Report {

	public QuestionNotFound(Form form, Question abcdQuestion) {
		super(ReportLevel.ERROR, generateReport(form, abcdQuestion));
	}

	private static String generateReport(Form form, Question abcdQuestion) {
		StringBuilder sb = new StringBuilder();
		sb.append("Form '");
		sb.append(form);
		sb.append("' doesn't contain question '");
		sb.append(abcdQuestion.getPathName());
		sb.append("'.");
		return sb.toString();
	}

}
