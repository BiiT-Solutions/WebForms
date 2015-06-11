package com.biit.webforms.validators;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Flow;

public class EmptyFlowIsNotAlone extends Report {
	private final Flow flow;

	public EmptyFlowIsNotAlone(Flow flow) {
		super(ReportLevel.ERROR, "Element '" + flow.getOrigin().getPathName()
				+ "' has a flow without condition and also other flows are defined.");
		this.flow = flow;
	}

	public Flow getFlow() {
		return flow;
	}

}
