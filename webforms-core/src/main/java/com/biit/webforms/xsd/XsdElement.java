package com.biit.webforms.xsd;

public class XsdElement extends XsdNodeWithChilds {

	private static final String PARAMETER_TYPE = "type";
	private static final String PARAMETER_MIN_OCCURS = "minOccurs";
	private static final String PARAMETER_MAX_OCCURS = "maxOccurs";

	private static final String XSD_ELEMENT_TAG = "xs:element";

	public XsdElement(String name) {
		super();
		putName(name);
	}

	public XsdElement(String name, XsdElementType type) {
		super();
		putName(name);
		putType(type);
	}

	public void putType(String type) {
		putParameter(PARAMETER_TYPE, type);
	}

	public void putType(XsdElementType type) {
		putParameter(PARAMETER_TYPE, type.toString());
	}

	public void putType(XsdComplexType type) {
		String name = type.getName();
		if (name != null) {
			putParameter(PARAMETER_TYPE, name);
		}
	}

	public void putMinOccurs(Integer value) {
		putParameter(PARAMETER_MIN_OCCURS, value.toString());
	}

	public void putMaxOccurs(Integer value) {
		putParameter(PARAMETER_MAX_OCCURS, value.toString());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		appendXml(sb);
		return sb.toString();
	}

	@Override
	protected String getNodeName() {
		return XSD_ELEMENT_TAG;
	}
}
