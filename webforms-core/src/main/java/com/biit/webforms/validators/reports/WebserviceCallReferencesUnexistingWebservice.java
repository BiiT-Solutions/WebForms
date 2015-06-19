package com.biit.webforms.validators.reports;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;

public class WebserviceCallReferencesUnexistingWebservice extends Report {

	private final WebserviceCall call;

	public WebserviceCallReferencesUnexistingWebservice(WebserviceCall call) {
		super(ReportLevel.ERROR, generateMessage(call));
		this.call = call;
	}

	private static String generateMessage(WebserviceCall call) {
		return "Webservice call '"+call.getName()+"' references unexisting webservice '"+call.getWebserviceName()+"'.";
	}

	public WebserviceCall getCall() {
		return call;
	}

}
