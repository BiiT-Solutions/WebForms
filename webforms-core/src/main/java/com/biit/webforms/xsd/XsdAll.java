package com.biit.webforms.xsd;

import com.biit.webforms.xml.XmlNodeProducer;

public class XsdAll extends XsdNodeWithChilds{

	private static final String XS_ALL = "xs:all";
	
	public XsdAll(XmlNodeProducer ... childs){
		super(childs);
	}
	
	@Override
	protected String getNodeName() {
		return XS_ALL;
	}

}
