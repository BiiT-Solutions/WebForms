package com.biit.webforms.xsd;

/**
 * Basic node for the schema definition.
 *
 */
public class XsdSchema extends XsdNodeWithChilds {
	private static final String XML_SCHEMA_TAG = "xs:schema";
	private static final String XMLNS_XS = "xmlns:xs";
	private static final String XMLNS_XS_VALUE = "http://www.w3.org/2001/XMLSchema";
	private static final String XMLNS = "xmlns";
	private static final String ELEMENT_FORM_DEFAULT = "elementFormDefault";
	private static final String ELEMENT_FORM_DEFAULT_VALUE = "qualified";
	private static final String TARGETED_NAMESPACE = "targetNamespace";

	public XsdSchema(String xmlns, String targetedNamespace) {
		putParameter(XMLNS_XS, XMLNS_XS_VALUE);
		putParameter(XMLNS, xmlns);
		putParameter(ELEMENT_FORM_DEFAULT, ELEMENT_FORM_DEFAULT_VALUE);
		putParameter(TARGETED_NAMESPACE, targetedNamespace);
	}

	@Override
	protected String getNodeName() {
		return XML_SCHEMA_TAG;
	}
}
