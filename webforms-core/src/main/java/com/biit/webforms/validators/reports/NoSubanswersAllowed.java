package com.biit.webforms.validators.reports;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Question;

public class NoSubanswersAllowed extends Report {

	private Question question;

	public NoSubanswersAllowed(Question question) {
		super(ReportLevel.ERROR, generateReport(question));
		this.question = question;
	}

	private static String generateReport(Question question) {
		StringBuilder sb = new StringBuilder();
		sb.append("Question '");
		sb.append(question.getPathName());
		sb.append("' cannot have subanswers. Only radio buttons are allowed to use subanswers.");
		return sb.toString();
	}

	public Question getQuestion() {
		return question;
	}

}
