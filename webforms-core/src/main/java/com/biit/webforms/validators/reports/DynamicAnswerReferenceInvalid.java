package com.biit.webforms.validators.reports;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Question;

public class DynamicAnswerReferenceInvalid extends Report {

	private Question question;
	private Question reference;

	public DynamicAnswerReferenceInvalid(Question question,Question reference) {
		super(ReportLevel.ERROR, generateReport(question,reference));
		this.question = question;
		this.reference = reference;
	}

	private static String generateReport(Question question,Question reference) {
		StringBuilder sb = new StringBuilder();
		sb.append("Question '");
		sb.append(question.getPathName());
		sb.append("' references future question '");
		sb.append(reference.getPathName());
		sb.append(".");
		return sb.toString();
	}

	public Question getQuestion() {
		return question;
	}

	public Question getReference() {
		return reference;
	}

}