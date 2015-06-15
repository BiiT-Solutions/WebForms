package com.biit.webforms.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "webservice_call_input_link")
public class WebserviceCallInputLink extends WebserviceCallLink {
	private static final long serialVersionUID = 8534588762014331498L;

	@ManyToOne(optional = false)
	protected WebserviceCall webserviceCall;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "webservice_call_input_link_errors", joinColumns = @JoinColumn(name = "ID"))
	private Set<WebserviceCallInputErrors> errors;

	protected WebserviceCallInputLink() {
		super();
		errors = new HashSet<>();
	}

	public WebserviceCallInputLink(WebserviceValidatedPort port) {
		super(port);
		errors = new HashSet<>();
		for (String errorCode : port.getErrorCodes()) {
			errors.add(new WebserviceCallInputErrors(errorCode, ""));
		}
	}

	@Override
	public WebserviceCallLink generateCopy() throws NotValidStorableObjectException {
		WebserviceCallInputLink link = new WebserviceCallInputLink();
		link.copyData(this);
		return link;
	}

	public Set<WebserviceCallInputErrors> getErrors() {
		return errors;
	}

	/**
	 * Filters error list and returns only the error codes with a message for
	 * the user.
	 * 
	 * @return
	 */
	public Set<WebserviceCallInputErrors> getValidErrors() {
		Set<WebserviceCallInputErrors> validErrors = new HashSet<>();
		for (WebserviceCallInputErrors error : getErrors()) {
			if (error.isValid()) {
				validErrors.add(error);
			}
		}
		return validErrors;
	}

	public void setErrors(Set<WebserviceCallInputErrors> errors) {
		this.errors = errors;
	}

	@Override
	public void setWebserviceCall(WebserviceCall webserviceCall) {
		this.webserviceCall = webserviceCall;
	}

	@Override
	public WebserviceCall getWebserviceCall() {
		return webserviceCall;
	}

	@Override
	public void remove() {
		if(webserviceCall!=null){
			webserviceCall.getInputLinks().remove(this);
			setWebserviceCall(null);
		}
	}
}
