package com.biit.webforms.validators.reports;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Question;

public class OthersConditionWithNotMandatoryQuestion extends Report {

	public OthersConditionWithNotMandatoryQuestion(Question question) {
		super(ReportLevel.ERROR, "Question '" + question.getPathName()
				+ "' is not mandatory and has an 'others' flow.");
	}

}
