package com.biit.webforms.persistence.entity;

import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;


public class WebservicePort {
	
	private String name;
	
	private String xpath;
	
	private AnswerType type;
	private AnswerFormat format;
	private AnswerSubformat subformat;
	
	protected WebservicePort(){
		
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

	public AnswerType getType() {
		return type;
	}

	public void setType(AnswerType type) {
		this.type = type;
	}

	public AnswerFormat getFormat() {
		return format;
	}

	public void setFormat(AnswerFormat format) {
		this.format = format;
	}

	public AnswerSubformat getSubformat() {
		return subformat;
	}

	public void setSubformat(AnswerSubformat subformat) {
		this.subformat = subformat;
	}
}
