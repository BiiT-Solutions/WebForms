package com.biit.webforms.validators.reports;

import java.util.List;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Flow;

public class NotValidCondition extends Report {

	public NotValidCondition(List<Flow> badFormedExpression) {
		super(ReportLevel.ERROR, generateReport(badFormedExpression));
	}

	private static String generateReport(List<Flow> badFormedExpression) {
		return "Flows " + badFormedExpression + " from question '"
				+ badFormedExpression.get(0).getOrigin().getPathName() + "' have not valid conditions";
	}

}
