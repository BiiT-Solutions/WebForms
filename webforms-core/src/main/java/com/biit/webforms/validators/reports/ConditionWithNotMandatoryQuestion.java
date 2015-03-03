package com.biit.webforms.validators.reports;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Question;

public class ConditionWithNotMandatoryQuestion extends Report {

	private final Question question;
	private final Flow flow;
	
	public ConditionWithNotMandatoryQuestion(Question question, Flow flow) {
		super(ReportLevel.ERROR, generateReport(question,flow));
		this.question = question;
		this.flow = flow;
	}

	private static String generateReport(Question question, Flow flow) {
		return null;
	}

	public Question getQuestion() {
		return question;
	}
	
	public Flow getFlow() {
		return flow;
	}

}
