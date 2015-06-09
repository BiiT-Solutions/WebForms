package com.biit.webforms.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.form.entity.BaseQuestion;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "webservice_call_link")
public class WebserviceCallLink extends StorableObject{
	private static final long serialVersionUID = -4009426512262917423L;

	private static final boolean EDITABLE_DEFAULT_VALUE = true;

	@ManyToOne(optional = false)
	private WebserviceCall call;
	
	@ManyToOne(optional = false, fetch=FetchType.EAGER)
	private WebservicePort webservicePort;
	
	@ManyToOne(optional = true, fetch=FetchType.EAGER)
	private BaseQuestion formElement;
	
	private String validationMessage;
	
	private boolean isEditable;

	protected WebserviceCallLink() {
		super();
		setValidationMessage("");
		setEditable(EDITABLE_DEFAULT_VALUE);
	}
	
	public WebserviceCallLink(WebservicePort port){
		super();
		setWebservicePort(port);
		setValidationMessage("");
		setEditable(EDITABLE_DEFAULT_VALUE);
	}
	
	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		return new HashSet<StorableObject>();
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if(object instanceof WebserviceCallLink){
			copyBasicInfo(object);
			WebserviceCallLink link = (WebserviceCallLink) object;
			setWebservicePort(link.getWebservicePort());
			setFormElement(link.getFormElement());
			if(link.getValidationMessage()!=null){
				setValidationMessage(new String(link.getValidationMessage()));
			}else{
				setValidationMessage(new String());
			}
			setEditable(link.isEditable());
		}else{
			throw new NotValidStorableObjectException("Element of class '"+object.getClass().getName()+"' is not compatible with '"+WebserviceCallLink.class.getName()+"'");
		}
	}

	public WebservicePort getWebservicePort() {
		return webservicePort;
	}

	public void setWebservicePort(WebservicePort webservicePort) {
		this.webservicePort = webservicePort;
	}

	public BaseQuestion getFormElement() {
		return formElement;
	}

	public void setFormElement(BaseQuestion formElement) {
		this.formElement = formElement;
	}

	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	
	public void clear(){
		setFormElement(null);
		setValidationMessage("");
		setEditable(EDITABLE_DEFAULT_VALUE);
	}
}
