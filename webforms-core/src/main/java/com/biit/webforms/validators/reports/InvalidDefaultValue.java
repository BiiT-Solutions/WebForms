package com.biit.webforms.validators.reports;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Question;

public class InvalidDefaultValue extends Report {

	private Question question;

	public InvalidDefaultValue(Question question) {
		super(ReportLevel.ERROR, generateReport(question));
		this.question = question;
	}

	private static String generateReport(Question question) {
		StringBuilder sb = new StringBuilder();
		sb.append("Question '");
		sb.append(question.toString());
		sb.append("' has an invalid default value '");
		sb.append(question.getDefaultValueString());
		sb.append("' for the format '");
		sb.append(question.getAnswerSubformat().toString());
		sb.append("'.");
		return sb.toString();
	}

	public Question getQuestion() {
		return question;
	}
}
