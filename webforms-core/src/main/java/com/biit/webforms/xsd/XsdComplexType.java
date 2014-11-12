package com.biit.webforms.xsd;

import com.biit.webforms.xml.XmlNodeProducer;

public class XsdComplexType extends XsdNodeWithChilds {

	private static final String XSD_COMPLEX_TYPE_TAG = "xs:complexType";
	

	public XsdComplexType(String name, XmlNodeProducer... childs) {
		super(childs);
		putName(name);
	}

	@Override
	protected String getNodeName() {
		return XSD_COMPLEX_TYPE_TAG;
	}

}
