package com.biit.webforms.validators.reports;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Flow;

public class BackwardFlow extends Report {

	private final Flow flow;

	public BackwardFlow(Flow flow) {
		super(ReportLevel.ERROR, "Backward flow '" + flow + "' from '" + flow.getOrigin().getPathName() + "' to '"
				+ flow.getDestiny().getPathName() + "'.");
		this.flow = flow;
	}

	public Flow getFlow() {
		return flow;
	}

}
