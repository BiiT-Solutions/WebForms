package com.biit.webforms.persistence.entity.webservices;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.webservices.WebserviceValidatedPort;

@Entity
@Table(name = "webservice_call_input_link")
public class WebserviceCallInputLink extends WebserviceCallLink {
	private static final long serialVersionUID = 8534588762014331498L;

	@ManyToOne(optional = false)
	protected WebserviceCall webserviceCall;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "webservice_call_input_link_errors", joinColumns = @JoinColumn(name = "ID"))
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "webserviceCallInput")
	private Set<WebserviceCallInputErrors> errors;

	private String validationXpath;

	protected WebserviceCallInputLink() {
		super();
		errors = new HashSet<>();
	}

	public WebserviceCallInputLink(WebserviceValidatedPort port) {
		super(port);
		errors = new HashSet<>();
		setValidationXpath(port.getValidationXpath());
		for (String errorCode : port.getErrorCodes()) {
			addWebserviceCallInputError(new WebserviceCallInputErrors(errorCode, ""));
		}
	}

	private void addWebserviceCallInputError(WebserviceCallInputErrors webserviceCallInputErrors) {
		errors.add(webserviceCallInputErrors);
		webserviceCallInputErrors.setWebserviceCallInput(this);
	}

	@Override
	public WebserviceCallLink generateCopy() throws NotValidStorableObjectException {
		WebserviceCallInputLink link = new WebserviceCallInputLink();
		link.copyData(this);
		return link;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof WebserviceCallInputLink) {
			super.copyData(object);
			WebserviceCallInputLink link = (WebserviceCallInputLink) object;
			
			setValidationXpath(link.getValidationXpath());
			for(WebserviceCallInputErrors error: getErrors()){
				addWebserviceCallInputError(new WebserviceCallInputErrors(error.getErrorCode(), error.getErrorMessage()));
			}
		} else {
			throw new NotValidStorableObjectException("Element of class '" + object.getClass().getName() + "' is not compatible with '"
					+ this.getClass().getName() + "'");
		}
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
		if (webserviceCall != null) {
			webserviceCall.getInputLinks().remove(this);
			setWebserviceCall(null);
		}
	}

	public String getValidationXpath() {
		return validationXpath;
	}

	public void setValidationXpath(String validationXpath) {
		this.validationXpath = validationXpath;
	}

	public void resetIds() {
		super.resetIds();
		for (WebserviceCallInputErrors error : getErrors()) {
			error.resetIds();
		}
	}
}
