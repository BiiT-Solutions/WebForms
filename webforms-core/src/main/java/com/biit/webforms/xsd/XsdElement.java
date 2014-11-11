package com.biit.webforms.xsd;

import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.xml.XmlParam;

public class XsdElement extends XsdNodeWithChilds {

	private static final String PARAMETER_TYPE = "type";
	private static final String PARAMETER_MIN_OCCURS = "minOccurs";
	private static final String PARAMETER_MAX_OCCURS = "maxOccurs";
	
	private static final String XSD_ELEMENT_TAG = "xs:element";
	private String name;
	private String type;
	private Integer minOccurs;
	private Integer maxOccurs;

	public XsdElement(String name, XsdElementType type) {
		putName(name);
		putType(type);
	}

	public void putType(XsdElementType type) {
		putParameter(PARAMETER_TYPE, type.toString());
	}
	
	public void putType(XsdComplexType type){
		String name = type.getName();
		if(name!=null){
			putParameter(PARAMETER_TYPE, name);
		}
	}
	
	public void putMinOccurs(Integer value){
		putParameter(PARAMETER_MIN_OCCURS, value.toString());
	}
	
	public void putMaxOccurs(Integer value){
		putParameter(PARAMETER_MAX_OCCURS, value.toString());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		appendXml(sb);
		return sb.toString();
	}

	@Override
	public void appendXml(StringBuilder sb) {
		List<XmlParam> params = new ArrayList<>();

		params.add(new XmlParam("name", name));
		params.add(new XmlParam("type", type));
		if (minOccurs != null) {
			params.add(new XmlParam("minOccurs", minOccurs.toString()));
		}
		if (maxOccurs != null) {
			params.add(new XmlParam("maxOccurs", maxOccurs.toString()));
		}

		appendXmlTag(sb, name, params.toArray(new XmlParam[] {}));
	}

	@Override
	protected String getNodeName() {
		return XSD_ELEMENT_TAG;
	}
}
