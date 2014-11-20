package com.biit.webforms.validators.reports;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Flow;

public class FlowOriginIsNotMandatory extends Report {

	public FlowOriginIsNotMandatory(Flow flow) {
		super(ReportLevel.ERROR, "Flow '" + flow + "' origin '" + flow.getOrigin().getPathName()
				+ "' is not mandatory.");
	}

}
