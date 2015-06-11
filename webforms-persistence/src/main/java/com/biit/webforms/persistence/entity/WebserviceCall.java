package com.biit.webforms.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "webservice_call")
public class WebserviceCall extends StorableObject {
	private static final long serialVersionUID = -8130775804790464077L;
	
	@ManyToOne(optional = false)
	private Form form;

	@Column(nullable = false)
	private String name;

	private String webserviceName;

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval = true)
	private Set<WebserviceCallInputLink> inputLinks;
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval = true)
	private Set<WebserviceCallOutputLink> outputLinks;
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval = true)
	private Set<WebserviceCallValidationLink> validateLinks;

	protected WebserviceCall() {
		super();
		inputLinks = new HashSet<>();
		outputLinks = new HashSet<>();
		validateLinks = new HashSet<>();
	}
	
	public WebserviceCall(String name, Webservice webservice){
		super();
		setName(name);
		setWebserviceName(webservice.getName());
		inputLinks = new HashSet<>();
		outputLinks = new HashSet<>();
		validateLinks = new HashSet<>();
	}
	
	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		return new HashSet<StorableObject>();
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if(object instanceof WebserviceCall){
			copyBasicInfo(object);
			WebserviceCall call = (WebserviceCall) object;
			setName(call.getName());
			setWebserviceName(call.getWebserviceName());
			copyLinkData(inputLinks,call.getInputLinks());
			copyLinkData(outputLinks,call.getOutputLinks());
			copyLinkData(validateLinks,call.getValidateLinks());
		}else{
			throw new NotValidStorableObjectException("Element of class '"+object.getClass().getName()+"' is not compatible with '"+WebserviceCall.class.getName()+"'");
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T extends WebserviceCallLink> void copyLinkData(Set<T> destiny, Set<T> source) throws NotValidStorableObjectException{
		destiny.clear();
		for(WebserviceCallLink link: source){
			destiny.add((T)link.generateCopy());
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWebserviceName() {
		return webserviceName;
	}

	public void setWebserviceName(String name) {
		this.webserviceName = name;
	}

	public Set<WebserviceCallInputLink> getInputLinks() {
		return inputLinks;
	}

	public void setInputLinks(Set<WebserviceCallInputLink> inputLinks) {
		this.inputLinks = inputLinks;
	}

	public Set<WebserviceCallOutputLink> getOutputLinks() {
		return outputLinks;
	}

	public void setOutputLinks(Set<WebserviceCallOutputLink> outputLinks) {
		this.outputLinks = outputLinks;
	}

	public Set<WebserviceCallValidationLink> getValidateLinks() {
		return validateLinks;
	}

	public void setValidateLinks(Set<WebserviceCallValidationLink> validateLinks) {
		this.validateLinks = validateLinks;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}
}
