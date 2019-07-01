package com.biit.webforms.validators.reports;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.Flow;

public class EmptyConditionWithMandatoryQuestion extends Report {

	private Flow flow;

	public EmptyConditionWithMandatoryQuestion(Flow flow) {
		super(ReportLevel.ERROR, "Flow '" + flow + "' origin '" + flow.getOrigin().getPathName()
				+ "' is mandatory and conditions is marked as '" + TokenTypes.EMPTY + "'.");
		this.flow = flow;
	}

	public Flow getFlow() {
		return flow;
	}

}
