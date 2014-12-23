package com.biit.webforms.xsd;

import com.biit.webforms.xml.XmlNodeProducer;

public class XsdSimpleType extends XsdNodeWithChilds {

	private static final String XSD_SIMPLE_TYPE_TAG = "xs:simpleType";
	

	public XsdSimpleType(XmlNodeProducer... childs) {
		super(childs);
	}

	@Override
	protected String getNodeName() {
		return XSD_SIMPLE_TYPE_TAG;
	}

}