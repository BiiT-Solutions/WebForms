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
import com.biit.webforms.enumerations.PortType;

@Entity
@Table(name = "webservice_call")
public class WebserviceCall extends StorableObject {
	private static final long serialVersionUID = -8130775804790464077L;
	
	@ManyToOne(optional = false)
	private Form form;

	@Column(nullable = false)
	private String name;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Webservice webservice;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "call")
	private Set<WebserviceCallLink> links;

	protected WebserviceCall() {
		super();
		links = new HashSet<WebserviceCallLink>();
	}
	
	public WebserviceCall(String name, Webservice webservice){
		super();
		setName(name);
		setWebservice(webservice);
		links = new HashSet<WebserviceCallLink>();
		for(WebservicePort port: webservice.getWebservicePorts()){
			links.add(new WebserviceCallLink(port));
		}
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
			setWebservice(call.getWebservice());
			links.clear();
			for(WebserviceCallLink link: call.getLinks()){
				WebserviceCallLink copiedLink = new WebserviceCallLink();
				copiedLink.copyData(link);
				links.add(copiedLink);
			}
		}else{
			throw new NotValidStorableObjectException("Element of class '"+object.getClass().getName()+"' is not compatible with '"+WebserviceCall.class.getName()+"'");
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Webservice getWebservice() {
		return webservice;
	}

	public void setWebservice(Webservice webservice) {
		this.webservice = webservice;
	}

	public Set<WebserviceCallLink> getLinks() {
		return links;
	}
	
	public Set<WebserviceCallLink> getLinks(PortType type) {
		Set<WebserviceCallLink> filteredLinks = new HashSet<WebserviceCallLink>();
		for(WebserviceCallLink link: getLinks()){
			if(link.getWebservicePort().getType().equals(type)){
				filteredLinks.add(link);
			}
		}
		return filteredLinks;
	}
	
	public Set<WebserviceCallLink> getUsedInputLinks(){
		return getLinks(PortType.INPUT);
	}
	
	public Set<WebserviceCallLink> getUsedOutputLinks(){
		return getLinks(PortType.OUTPUT);
	}
	
	public Set<WebserviceCallLink> getUsedValidationLinks(){
		return getLinks(PortType.VALIDATION);
	}
	
	public Set<WebserviceCallLink> getAllInputLinks(){
		return getAllLinks(PortType.INPUT);
	}
	
	public Set<WebserviceCallLink> getAllOutputLinks(){
		return getAllLinks(PortType.OUTPUT);
	}
	
	public Set<WebserviceCallLink> getAllValidationLinks(){
		return getAllLinks(PortType.VALIDATION);
	}
	
	private Set<WebserviceCallLink> getAllLinks(PortType type){
		Set<WebserviceCallLink> links = getLinks(type);
		Set<WebservicePort> ports = getWebservice().getXmlPorts(type);
		
		for(WebservicePort port: ports){
			boolean exists = false;
			for(WebserviceCallLink link: links){
				if(link.getWebservicePort().equals(port)){
					exists = true;
					break;
				}
			}
			if(!exists){
				links.add(new WebserviceCallLink(port));
			}
		}
		return links;
	}
}
