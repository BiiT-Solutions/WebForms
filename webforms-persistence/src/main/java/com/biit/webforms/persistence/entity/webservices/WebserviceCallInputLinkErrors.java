package com.biit.webforms.persistence.entity.webservices;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "webservice_call_input_link_errors")
public class WebserviceCallInputLinkErrors implements Serializable, Comparable<WebserviceCallInputLinkErrors> {
	private static final long serialVersionUID = -939386770345155248L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@ManyToOne(optional = false)
	private WebserviceCallInputLink webserviceCallInputLink;

	private String errorCode;

	private String errorMessage;

	protected WebserviceCallInputLinkErrors() {
		super();
	}

	public WebserviceCallInputLinkErrors(String errorCode, String errorMessage) {
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

	public WebserviceCallInputLink getWebserviceCallInputLink() {
		return webserviceCallInputLink;
	}

	public void setWebserviceCallInputLink(WebserviceCallInputLink webserviceCallInput) {
		this.webserviceCallInputLink = webserviceCallInput;
	}

	@Override
	public int compareTo(WebserviceCallInputLinkErrors arg0) {
		if (arg0 == null) {
			return 1;
		} else {
			return this.getErrorCode().compareTo(arg0.getErrorCode());
		}
	}

	public void resetIds() {
		id = null;
	}
}
