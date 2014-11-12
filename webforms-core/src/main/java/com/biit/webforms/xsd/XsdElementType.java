package com.biit.webforms.xsd;

public enum XsdElementType {

	STRING("xs:string"),
	DECIMAL("xs:decimal"),
	INTEGER("xs:integer"),
	BOOLEAN("xs:boolean"),
	DATE("xs:date"),
	TIME("xs:time"),
	;
	
	private String xsdValue;
	
	private XsdElementType(String xsdValue) {
		this.xsdValue =xsdValue;  
	}
	
	@Override
	public String toString(){
		return xsdValue;
	}
}
