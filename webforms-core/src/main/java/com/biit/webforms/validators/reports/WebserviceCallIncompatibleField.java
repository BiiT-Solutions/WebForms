package com.biit.webforms.validators.reports;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.WebserviceCall;
import com.biit.webforms.persistence.entity.WebserviceCallLink;

public class WebserviceCallIncompatibleField extends Report {

	private final WebserviceCall call;
	private final WebserviceCallLink link;

	public WebserviceCallIncompatibleField(WebserviceCall call,WebserviceCallLink link) {
		super(ReportLevel.ERROR, generateMessage(call,link));
		this.call = call;
		this.link = link;
	}

	private static String generateMessage(WebserviceCall call, WebserviceCallLink link) {
		return "Selected form question for port '"+link.getWebservicePort()+"' of Webservice call '"+call.getName()+"' is incompatible.";
	}

	public WebserviceCall getCall() {
		return call;
	}

	public WebserviceCallLink getLink() {
		return link;
	}

}