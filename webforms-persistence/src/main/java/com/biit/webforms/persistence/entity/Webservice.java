package com.biit.webforms.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.PortType;

@Entity
@Table(name = "webservices")
public class Webservice extends StorableObject {
	private static final long serialVersionUID = -8611580709413698997L;
	public static final int MAX_NAME_LENGTH = 100;
	public static final int MAX_DESCRIPTION_LENGTH = 250;
	
	@Column(length = MAX_NAME_LENGTH, unique=true)
	private String name;
	
	@Column(length = MAX_DESCRIPTION_LENGTH)
	private String description;
	
	private String inputXml;
	
	private String outputXml;
	
	private String protocol;
	
	private String host;
	
	private String port;
	
	private String path;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "webservice", fetch=FetchType.EAGER)
	private Set<WebservicePort> webservicePorts;
	
	public Webservice() {
		super();
		webservicePorts = new HashSet<WebservicePort>();
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> storableObjects = new HashSet<StorableObject>();
		storableObjects.addAll(getWebservicePorts());
		for(WebservicePort port: getWebservicePorts()){
			storableObjects.addAll(port.getAllInnerStorableObjects());
		}			
		return storableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if(object instanceof Webservice){
			copyBasicInfo(object);
			Webservice webservice = (Webservice) object;
			setName(webservice.getName());
			setDescription(webservice.getDescription());
			setInputXml(webservice.getInputXml());
			setOutputXml(webservice.getOutputXml());
			setProtocol(webservice.getProtocol());
			setHost(webservice.getHost());
			setPort(webservice.getPort());
			setPath(webservice.getPath());
			for(WebservicePort port: webservice.getWebservicePorts()){
				WebservicePort copy = new WebservicePort();
				copy.copyData(port);
				getWebservicePorts().add(copy);
			}
		}else{
			throw new NotValidStorableObjectException("Element of class '"+object.getClass().getName()+"' is not compatible with '"+Webservice.class.getName()+"'");
		}
	}

	public String getInputXml() {
		return inputXml;
	}

	public void setInputXml(String inputXml) {
		this.inputXml = inputXml;
	}

	public String getOutputXml() {
		return outputXml;
	}

	public void setOutputXml(String outputXml) {
		this.outputXml = outputXml;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Set<WebservicePort> getWebservicePorts() {
		return webservicePorts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return getProtocol()+"://"+getHost()+":"+getPort()+"/"+getPath();
	}
	
	public Set<WebservicePort> getXmlPorts(PortType type){
		Set<WebservicePort> filteredPorts = new HashSet<>();
		for(WebservicePort port: getWebservicePorts()){
			if(port.getType().equals(type)){
				filteredPorts.add(port);
			}
		}
		return filteredPorts;
	}

	public Set<WebservicePort> getInputPorts() {
		return getXmlPorts(PortType.INPUT);
	}
	
	public Set<WebservicePort> getOutputPorts() {
		return getXmlPorts(PortType.OUTPUT);
	}
	
	public Set<WebservicePort> getValidatePorts() {
		return getXmlPorts(PortType.VALIDATION);
	}
	
}
