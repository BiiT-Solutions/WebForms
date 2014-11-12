package com.biit.webforms.xml;

import java.util.List;

public abstract class XmlNodeProducer {

	protected abstract String getNodeName();
	
	public abstract void appendXml(StringBuilder sb);
	
	protected void appendXmlTag(StringBuilder sb, String tagName, List<XmlNodeProducer> childs, XmlParam ... params){
		sb.append("<");
		sb.append(getNodeName());
		
		for(int i=0; i<params.length; i++){
			sb.append(" ");
			params[i].appendStringTo(sb);
		}
		
		if(childs==null || childs.isEmpty()){
			sb.append("/>");
		}else{
			sb.append(">");
			
			for(XmlNodeProducer child: childs){
				child.appendXml(sb);
			}
			
			sb.append("</");
			sb.append(getNodeName());
			sb.append(">");
		}
	}
	
	protected void appendXmlTag(StringBuilder sb,String tagName, XmlParam ... params){
		appendXmlTag(sb, tagName, null, params);		
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		appendXml(sb);
		return sb.toString();
	}
}
