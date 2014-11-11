package com.biit.webforms.xml;

public enum XmlEncoding {

	UTF_8("UTF-8"),
	;
	
	private String encoding;
	
	private XmlEncoding(String codification) {
		this.encoding = codification;
	}
	
	public String getEncoding(){
		return encoding;
	}
	
}
