package com.biit.webforms.persistence.dao.exceptions;

public class WebserviceNotFoundException extends Exception {
	private static final long serialVersionUID = -3959692414068266959L;
	
	public WebserviceNotFoundException(String name) {
		super("Webservice with name '"+name+"' not found.");
	}

}
