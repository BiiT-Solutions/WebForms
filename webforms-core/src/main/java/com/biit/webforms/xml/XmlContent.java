package com.biit.webforms.xml;

public class XmlContent extends XmlNode{

	public XmlContent() {
		super("");
	}

	@Override
	public void getXml(StringBuilder sb){
		for(XmlNode child: children){
			child.getXml(sb);
		}
	}
}
