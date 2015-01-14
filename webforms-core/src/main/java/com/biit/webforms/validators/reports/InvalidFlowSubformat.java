package com.biit.webforms.validators.reports;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;

public class InvalidFlowSubformat extends Report {

	private Flow flow;
	private TokenComparationValue invalidToken;

	public InvalidFlowSubformat(Flow flow, TokenComparationValue invalidToken) {
		super(ReportLevel.ERROR, generateReport(flow, invalidToken));
		this.flow = flow;
		this.invalidToken = invalidToken;
	}

	private static String generateReport(Flow flow, TokenComparationValue invalidToken) {
		if (invalidToken != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("Condition '");
			sb.append(invalidToken);
			sb.append("' in flow '");
			sb.append(flow.toString());
			sb.append("' has as subformat '");
			sb.append(invalidToken.getSubformat());
			sb.append("' but its question has format '");
			sb.append(invalidToken.getQuestion().getAnswerFormat());
			sb.append("'.");
			return sb.toString();
		}
		return "";
	}

	public Flow getFlow() {
		return flow;
	}

	public TokenComparationValue getInvalidToken() {
		return invalidToken;
	}
}
