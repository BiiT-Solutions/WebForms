package com.biit.webforms.persistence.entity.webservices;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.biit.form.entity.BaseQuestion;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.webservices.WebservicePort;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class WebserviceCallLink extends StorableObject {
	private static final long serialVersionUID = -4009426512262917423L;

	private static final int WEBSERVICE_PORT_MAX_LENGTH = 250;

	@Column(name = "webservice_port", length = WEBSERVICE_PORT_MAX_LENGTH)
	private String webservicePort;

	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "form_element")
	private BaseQuestion formElement;

	protected WebserviceCallLink() {
		super();
		setWebservicePort("");
	}

	public WebserviceCallLink(WebservicePort port) {
		super();
		setWebservicePort(port.getName());

	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		return new HashSet<StorableObject>();
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof WebserviceCallLink) {
			copyBasicInfo(object);
			WebserviceCallLink link = (WebserviceCallLink) object;
			if (link.getWebservicePort() != null) {
				setWebservicePort(link.getWebservicePort());
			}
			setFormElement(link.getFormElement());
		} else {
			throw new NotValidStorableObjectException("Element of class '" + object.getClass().getName() + "' is not compatible with '"
					+ WebserviceCallLink.class.getName() + "'");
		}
	}

	public BaseQuestion getFormElement() {
		return formElement;
	}

	public void setFormElement(BaseQuestion formElement) {
		this.formElement = formElement;
	}

	public void clear() {
		setWebservicePort(null);
		setFormElement(null);
	}

	public String getWebservicePort() {
		return webservicePort;
	}

	public void setWebservicePort(String webservicePort) {
		this.webservicePort = webservicePort;
	}

	public abstract WebserviceCallLink generateCopy() throws NotValidStorableObjectException;

	public abstract void setWebserviceCall(WebserviceCall webserviceCall);

	public abstract WebserviceCall getWebserviceCall();

	public abstract void remove();

	public void updateReferences(HashMap<String, BaseQuestion> references) {
		if (getFormElement() != null) {
			setFormElement(references.get(getFormElement().getOriginalReference()));
		}
	}

}
