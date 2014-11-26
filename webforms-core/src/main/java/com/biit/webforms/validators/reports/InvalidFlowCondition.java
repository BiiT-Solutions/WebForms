package com.biit.webforms.validators.reports;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Flow;

public class InvalidFlowCondition extends Report {

	public InvalidFlowCondition(Flow flow) {
		super(ReportLevel.ERROR, generateReport(flow));
	}

	private static String generateReport(Flow flow) {
		StringBuilder sb = new StringBuilder();
		sb.append("Flow: ");
		sb.append(flow.toString());
		sb.append(" has an invalid condition.");
		return sb.toString();
	}

}
