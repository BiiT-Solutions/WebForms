package com.biit.webforms.validators.reports;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;

public class WebserviceCallInputAfterTrigger extends Report {

	private final WebserviceCall call;
	private final WebserviceCallInputLink link;

	public WebserviceCallInputAfterTrigger(WebserviceCall call,WebserviceCallInputLink link) {
		super(ReportLevel.ERROR, generateMessage(call,link));
		this.call = call;
		this.link = link;
	}

	private static String generateMessage(WebserviceCall call, WebserviceCallInputLink link) {
		return "Input '"+link.getWebservicePort()+"' of Webservice call '"+call.getName()+"' is after webservice call trigger.";
	}

	public WebserviceCall getCall() {
		return call;
	}

	public WebserviceCallInputLink getLink() {
		return link;
	}

}