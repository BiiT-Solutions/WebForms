package com.biit.webforms.validators.reports;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;

public class TokenUsesNonFinalAnswer extends Report {

	private final Flow flow;
	private final TokenComparationAnswer token;

	public TokenUsesNonFinalAnswer(Flow flow, TokenComparationAnswer token) {
		super(ReportLevel.ERROR, generateError(flow, token));
		this.flow = flow;
		this.token = token;
	}

	private static String generateError(Flow flow, TokenComparationAnswer token) {
		StringBuilder sb = new StringBuilder();
		sb.append("Flow: ");
		sb.append(flow);
		sb.append(" has a reference to '");
		sb.append(token.getAnswer());
		sb.append("' which is not a final answer.");
		return sb.toString();
	}

	public Flow getFlow() {
		return flow;
	}

	public TokenComparationAnswer getToken() {
		return token;
	}

}
