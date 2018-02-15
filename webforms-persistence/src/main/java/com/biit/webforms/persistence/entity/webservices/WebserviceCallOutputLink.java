package com.biit.webforms.persistence.entity.webservices;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.webservices.WebservicePort;

@Entity
@Table(name = "webservice_call_output_link")
public class WebserviceCallOutputLink extends WebserviceCallLink {
	private static final long serialVersionUID = -107489603701913849L;

	private static final boolean EDITABLE_DEFAULT_VALUE = false;

	@ManyToOne(optional = false)
	@JoinColumn(name = "webservice_call")
	protected WebserviceCall webserviceCall;

	@Column(name = "editable")
	private boolean isEditable;

	public WebserviceCallOutputLink() {
		super();
		setEditable(EDITABLE_DEFAULT_VALUE);
	}

	public WebserviceCallOutputLink(WebservicePort port) {
		super(port);
		setEditable(EDITABLE_DEFAULT_VALUE);
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		super.copyData(object);
		if (object instanceof WebserviceCallOutputLink) {
			WebserviceCallOutputLink link = (WebserviceCallOutputLink) object;
			setEditable(link.isEditable());
		} else {
			throw new NotValidStorableObjectException("Element of class '" + object.getClass().getName() + "' is not compatible with '"
					+ this.getClass().getName() + "'");
		}
	}

	@Override
	public void clear() {
		super.clear();
		setEditable(EDITABLE_DEFAULT_VALUE);
	}

	@Override
	public WebserviceCallLink generateCopy() throws NotValidStorableObjectException {
		WebserviceCallOutputLink link = new WebserviceCallOutputLink();
		link.copyData(this);
		return link;
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
			webserviceCall.getOutputLinks().remove(this);
			setWebserviceCall(null);
		}
	}
}
