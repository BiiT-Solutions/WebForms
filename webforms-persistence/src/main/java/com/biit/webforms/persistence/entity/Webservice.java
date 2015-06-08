package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

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
	
	private String serviceName;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "webservice")
	private List<WebservicePort> ports;
	
	public Webservice() {
		super();
		ports = new ArrayList<WebservicePort>();
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> storableObjects = new HashSet<StorableObject>();
		storableObjects.addAll(ports);
		return storableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		
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

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public List<WebservicePort> getPorts() {
		return ports;
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
	
}
