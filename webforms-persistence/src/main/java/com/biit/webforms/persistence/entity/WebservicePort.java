package com.biit.webforms.persistence.entity;


public class WebservicePort {
	
	private String name;
	
	private String xpath;
	
	public WebservicePort(){
		
	}
	
	public WebservicePort(String name, String xpath) {
		setName(name);
		setXpath(xpath);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

}
