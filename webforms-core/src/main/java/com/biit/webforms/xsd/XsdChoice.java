package com.biit.webforms.xsd;

public class XsdChoice extends XsdNodeWithChilds{
	
	private static final String XSD_CHOICE = "xs:choice";

	@Override
	protected String getNodeName() {
		return XSD_CHOICE;
	}

}
