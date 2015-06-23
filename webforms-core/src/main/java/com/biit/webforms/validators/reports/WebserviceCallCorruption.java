package com.biit.webforms.validators.reports;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;

public class WebserviceCallCorruption extends Report {

	private final WebserviceCall call;

	public WebserviceCallCorruption(WebserviceCall call) {
		super(ReportLevel.ERROR, generateMessage(call));
		this.call = call;
	}

	private static String generateMessage(WebserviceCall call) {
		return "Webservice call '"+call.getName()+"' uses an invalid webservice configuration.";
	}

	public WebserviceCall getCall() {
		return call;
	}

}
