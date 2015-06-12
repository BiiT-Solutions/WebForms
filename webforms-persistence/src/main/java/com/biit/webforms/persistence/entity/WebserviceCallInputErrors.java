package com.biit.webforms.persistence.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class WebserviceCallInputErrors implements Serializable{
	private static final long serialVersionUID = -939386770345155248L;

	private String errorCode;

	private String errorMessage;
	
	protected WebserviceCallInputErrors(){
		super();
	}

	public WebserviceCallInputErrors(String errorCode , String errorMessage) {
		super();
		setErrorCode(errorCode);
		setErrorMessage(errorMessage);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isValid() {
		return (errorCode != null && errorMessage != null && !errorCode.isEmpty() && !errorMessage.isEmpty());
	}
}
