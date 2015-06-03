package com.biit.webforms.validators.reports;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Question;

public class DynamicAnswerNullReference extends Report {

	private Question question;

	public DynamicAnswerNullReference(Question question) {
		super(ReportLevel.ERROR, generateReport(question));
		this.question = question;
	}

	private static String generateReport(Question question) {
		StringBuilder sb = new StringBuilder();
		sb.append("Question '");
		sb.append(question.getPathName());
		sb.append("' has a empty dynamic answer.");
		return sb.toString();
	}

	public Question getQuestion() {
		return question;
	}

}