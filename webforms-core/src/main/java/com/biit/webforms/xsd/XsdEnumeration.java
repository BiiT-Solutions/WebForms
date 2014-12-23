package com.biit.webforms.xsd;

public class XsdEnumeration extends XsdNodeWithChilds {

	private static final String NODE_NAME_TAG = "xs:enumeration";
	private static final String PARAMETER_VALUE = "value";
	
	public XsdEnumeration(String value) {
		super();
		putValue(value);
	}
	
	public void putValue(String value) {
		putParameter(PARAMETER_VALUE, value);
	}

	@Override
	protected String getNodeName() {
		return NODE_NAME_TAG;
	}

}
