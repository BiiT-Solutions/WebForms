package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebserviceValidationPort extends WebservicePort{

	private List<String> errorCodes;
	
	public WebserviceValidationPort() {
		super();
		setErrorCodes(new ArrayList<String>());
	}

	public WebserviceValidationPort(String name, String xpath, String ... values) {
		super(name,xpath);
		setErrorCodes(new ArrayList<String>());
		getErrorCodes().addAll(Arrays.asList(values));		
	}

	public List<String> getErrorCodes() {
		return errorCodes;
	}

	public void setErrorCodes(List<String> errorCodes) {
		this.errorCodes = errorCodes;
	}
	
}
