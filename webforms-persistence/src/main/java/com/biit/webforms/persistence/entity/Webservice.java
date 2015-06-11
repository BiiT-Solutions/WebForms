package com.biit.webforms.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Webservice {
	
	private String name;
			
	private String protocol;
	
	private String host;
	
	private String port;
	
	private String path;
	
	private Set<WebserviceIoPort> inputPorts;
	
	private Set<WebserviceIoPort> outputPorts;
	
	private Set<WebserviceValidationPort> validationPorts;
	
	public Webservice() {
		super();
		inputPorts = new HashSet<>();
		outputPorts = new HashSet<>();
		validationPorts = new HashSet<>();
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return getProtocol()+"://"+getHost()+":"+getPort()+"/"+getPath();
	}

	public Set<WebserviceIoPort> getInputPorts() {
		return inputPorts;
	}

	public void setInputPorts(Set<WebserviceIoPort> inputPorts) {
		this.inputPorts = inputPorts;
	}

	public Set<WebserviceIoPort> getOutputPorts() {
		return outputPorts;
	}

	public void setOutputPorts(Set<WebserviceIoPort> outputPorts) {
		this.outputPorts = outputPorts;
	}

	public Set<WebserviceValidationPort> getValidationPorts() {
		return validationPorts;
	}

	public void setValidationPorts(Set<WebserviceValidationPort> validationPorts) {
		this.validationPorts = validationPorts;
	}
	
	public String toJson(){
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();
		
		return gson.toJson(this);
	}
	
	public static Webservice fromJson(String jsonString){
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();

		return (Webservice) gson.fromJson(jsonString, Webservice.class);
	}
}
