package com.biit.webforms.xsd;

import com.biit.webforms.xml.XmlNodeProducer;

public class XsdRestriction extends XsdNodeWithChilds{

	private static final String NODE_NAME_TAG = "xs:restriction";
	private static final String PARAMETER_BASE = "base";
	
	public XsdRestriction(XmlNodeProducer... childs) {
		super(childs);
	}

	public void putBase(XsdElementType type) {
		putParameter(PARAMETER_BASE, type.toString());
	}
	
	@Override
	protected String getNodeName() {
		return NODE_NAME_TAG;
	}

}
