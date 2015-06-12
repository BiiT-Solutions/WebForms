package com.biit.webforms.validators;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.condition.Token;

public class FlowConditionIsUseless extends Report {
	private final Flow flow;
	private final Token condition;

	public FlowConditionIsUseless(Flow flow, Token condition) {
		super(ReportLevel.ERROR, "Flow '" + flow + "' has the condition '" + condition
				+ "' which makes itself or other flows unnecessary/impossible.");
		this.flow = flow;
		this.condition = condition;
	}

	public Flow getFlow() {
		return flow;
	}

	public Token getCondition() {
		return condition;
	}
}
