package com.biit.webforms.validators.reports;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.WebserviceCall;

public class WebserviceCallTriggerNull extends Report {

	private final WebserviceCall call;

	public WebserviceCallTriggerNull(WebserviceCall call) {
		super(ReportLevel.ERROR, generateMessage(call));
		this.call = call;
	}

	private static String generateMessage(WebserviceCall call) {
		return "Trigger of Webservice call '"+call.getName()+"' is not configured.";
	}

	public WebserviceCall getCall() {
		return call;
	}

}
