package com.biit.webforms.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "webservice_call_validation_link")
public class WebserviceCallValidationLink extends WebserviceCallLink {
	private static final long serialVersionUID = -166737129984518873L;

	private String errorCode;

	private String errorMessage;

	protected WebserviceCallValidationLink() {
		super();
		setErrorCode("");
		setErrorMessage("");
	}

	public WebserviceCallValidationLink(WebserviceValidationPort port, String errorCode) {
		super(port);
		setErrorCode(errorCode);
		setErrorMessage("");
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

	@Override
	public void clear() {
		super.clear();
		setErrorCode("");
		setErrorMessage("");
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		super.copyData(object);
		if (object instanceof WebserviceCallValidationLink) {
			copyBasicInfo(object);
			WebserviceCallValidationLink link = (WebserviceCallValidationLink) object;
			if (link.getErrorCode() != null) {
				setErrorCode(new String(link.getErrorCode()));
			}
			if (link.getErrorMessage() != null) {
				setErrorMessage(new String(link.getErrorMessage()));
			}
		} else {
			throw new NotValidStorableObjectException("Element of class '" + object.getClass().getName() + "' is not compatible with '"
					+ this.getClass().getName() + "'");
		}
	}

	@Override
	public WebserviceCallLink generateCopy() throws NotValidStorableObjectException {
		WebserviceCallValidationLink link = new WebserviceCallValidationLink();
		link.copyData(this);
		return link;
	}

	public static Set<WebserviceCallValidationLink> generateWebserviceValidationLinks(Set<WebserviceValidationPort> validationPorts) {
		Set<WebserviceCallValidationLink> links = new HashSet<>();
		for(WebserviceValidationPort port: validationPorts){
			for(String errorCode: port.getErrorCodes()){
				WebserviceCallValidationLink link = new WebserviceCallValidationLink(port,errorCode);				
				links.add(link);
			}
		}
		return links;
	}
}
