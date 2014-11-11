package com.biit.webforms.xml;

import java.util.ArrayList;
import java.util.List;

public class XmlDocument {

	private static final String VERSION_PARAMETER = "version";
	private static final String ENCODING_PARAMETER = "encoding";
	private final String version;
	private final XmlEncoding encoding;
	private final List<XmlNodeProducer> nodes;
	
	public XmlDocument(String version,XmlEncoding encoding) {
		this.version= version;
		this.encoding= encoding;
		this.nodes = new ArrayList<XmlNodeProducer>();
	}
	
	public void addXmlNode(XmlNodeProducer node){
		nodes.add(node);
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml ");
		(new XmlParam(VERSION_PARAMETER,version)).appendStringTo(sb);
		(new XmlParam(ENCODING_PARAMETER,encoding.getEncoding())).appendStringTo(sb);
		sb.append("?>");
		
		for(XmlNodeProducer node: nodes){
			node.appendXml(sb);
		}
		
		return sb.toString();
	}
	
}
