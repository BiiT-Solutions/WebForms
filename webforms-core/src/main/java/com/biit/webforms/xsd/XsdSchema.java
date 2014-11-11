package com.biit.webforms.xsd;

import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.xml.XmlNodeProducer;
import com.biit.webforms.xml.XmlParam;

public class XsdSchema extends XmlNodeProducer {
	private final static String XML_SCHEMA = "xs:schema";
	private final static String XMLNS_XS = "xmlns:xs";
	private final static String XMLNS_XS_VALUE = "http://www.w3.org/2001/XMLSchema";
	private final static String XMLNS = "xmlns";
	private final String xmlns;
	private final static String ELEMENT_FORM_DEFAULT = "elementFormDefault";
	private final static String ELEMENT_FORM_DEFAULT_VALUE = "qualified";
	private final static String TARGETED_NAMESPACE = "targetedNamespace";
	private final String targetedNamespace;
	private final List<XmlNodeProducer> childs;

	public XsdSchema(String xmlns, String targetedNamespace) {
		this.xmlns = xmlns;
		this.targetedNamespace = targetedNamespace;
		this.childs = new ArrayList<XmlNodeProducer>();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		appendXml(sb);
		return sb.toString();
	}

	@Override
	public void appendXml(StringBuilder sb) {
		appendXmlTag(sb, XML_SCHEMA, childs, new XmlParam(XMLNS_XS, XMLNS_XS_VALUE), new XmlParam(XMLNS, xmlns),
				new XmlParam(ELEMENT_FORM_DEFAULT, ELEMENT_FORM_DEFAULT_VALUE), new XmlParam(TARGETED_NAMESPACE,
						targetedNamespace));
	}
}
