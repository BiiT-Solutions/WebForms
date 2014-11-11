package com.biit.webforms.xsd;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.biit.webforms.xml.XmlNodeProducer;
import com.biit.webforms.xml.XmlParam;

public abstract class XsdNodeWithChilds extends XmlNodeProducer {

	private static final String PARAMETER_NAME = "name";
	
	private final HashMap<String, String> parameters;
	private final List<XmlNodeProducer> childs;

	public XsdNodeWithChilds(XmlNodeProducer... childs) {
		this.parameters = new HashMap<String, String>();
		this.childs = new ArrayList<XmlNodeProducer>();
		this.childs.addAll(Arrays.asList(childs));
	}
	
	public void putParameter(String name, String value){
		parameters.put(name,value);
	}
	
	public String getParameter(String parameterName){
		return parameters.get(parameterName);
	}

	protected abstract String getNodeName();

	@Override
	public void appendXml(StringBuilder sb) {
		appendXmlTag(sb, getNodeName(), childs, getXmlParams());
	}

	private XmlParam[] getXmlParams() {
		List<XmlParam> xmlParameters = new ArrayList<XmlParam>();
		for (Entry<String, String> entry : parameters.entrySet()) {
			xmlParameters.add(new XmlParam(entry.getKey(), entry.getValue()));
		}
		return xmlParameters.toArray(new XmlParam[]{});
	}
	
	public void putName(String name){
		putParameter(PARAMETER_NAME, name);
	}
	
	public String getName(){
		return getParameter(PARAMETER_NAME);
	}
}
