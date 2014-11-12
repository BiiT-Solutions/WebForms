package com.biit.webforms.xsd;

import com.biit.webforms.xml.XmlNodeProducer;

public class XsdChoice extends XsdNodeWithChilds {

	private static final String XSD_CHOICE = "xs:choice";

	public XsdChoice(XmlNodeProducer ... childs) {
		super(childs);
	}

	@Override
	protected String getNodeName() {
		return XSD_CHOICE;
	}

}
