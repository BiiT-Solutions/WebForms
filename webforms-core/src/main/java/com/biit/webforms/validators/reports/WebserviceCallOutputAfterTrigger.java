package com.biit.webforms.validators.reports;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.WebserviceCall;
import com.biit.webforms.persistence.entity.WebserviceCallOutputLink;

public class WebserviceCallOutputAfterTrigger extends Report {

	private final WebserviceCall call;
	private final WebserviceCallOutputLink link;

	public WebserviceCallOutputAfterTrigger(WebserviceCall call,WebserviceCallOutputLink link) {
		super(ReportLevel.ERROR, generateMessage(call,link));
		this.call = call;
		this.link = link;
	}

	private static String generateMessage(WebserviceCall call, WebserviceCallOutputLink link) {
		return "Output '"+link.getWebservicePort()+"' of Webservice call '"+call.getName()+"' is before webservice call trigger.";
	}

	public WebserviceCall getCall() {
		return call;
	}

	public WebserviceCallOutputLink getLink() {
		return link;
	}

}