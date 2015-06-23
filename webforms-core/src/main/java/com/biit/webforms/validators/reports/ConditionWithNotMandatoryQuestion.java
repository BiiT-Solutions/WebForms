package com.biit.webforms.validators.reports;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;

public class ConditionWithNotMandatoryQuestion extends Report {

	private final WebformsBaseQuestion question;
	private final Flow flow;

	public ConditionWithNotMandatoryQuestion(WebformsBaseQuestion question, Flow flow) {
		super(ReportLevel.ERROR, generateReport(question, flow));
		this.question = question;
		this.flow = flow;
	}

	private static String generateReport(WebformsBaseQuestion question, Flow flow) {
		return null;
	}

	public WebformsBaseQuestion getQuestion() {
		return question;
	}

	public Flow getFlow() {
		return flow;
	}

}
