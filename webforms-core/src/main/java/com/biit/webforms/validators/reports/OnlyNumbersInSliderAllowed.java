package com.biit.webforms.validators.reports;

import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;

public class OnlyNumbersInSliderAllowed extends Report {

	private WebformsBaseQuestion question;

	public OnlyNumbersInSliderAllowed(WebformsBaseQuestion question, TreeObject answer) {
		super(ReportLevel.ERROR, generateReport(question, answer));
		this.question = question;
	}

	private static String generateReport(WebformsBaseQuestion question, TreeObject answer) {
		StringBuilder sb = new StringBuilder();
		sb.append("Slider '");
		sb.append(question.getPathName());
		sb.append("' only can have numerical answer names. Answer '" + answer + "' technical name is a string.");
		return sb.toString();
	}

	public WebformsBaseQuestion getQuestion() {
		return question;
	}

}
