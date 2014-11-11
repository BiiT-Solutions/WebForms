package com.biit.webforms.xml;

public class XmlParam {

	private final String paramName;
	private final String paramValue;

	protected XmlParam() {
		paramName = null;
		paramValue = null;
	}

	public XmlParam(String paramName, String paramValue) {
		this.paramName = paramName;
		this.paramValue = paramValue;
	}

	public void appendStringTo(StringBuilder sb) {
		sb.append(paramName);
		sb.append("=\"");
		sb.append(paramValue);
		sb.append("\"");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		appendStringTo(sb);
		return sb.toString();
	}
}
