package com.biit.webforms.xml;

import java.util.List;

public abstract class XmlNodeProducer {

	public abstract void appendXml(StringBuilder sb);
	
	protected void appendXmlTag(StringBuilder sb, String tagName, List<XmlNodeProducer> childs, XmlParam ... params){
		sb.append("<");
		sb.append("tagName");
		
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
			sb.append("tagName");
			sb.append(">");
		}
	}
	
	protected void appendXmlTag(StringBuilder sb,String tagName, XmlParam ... params){
		appendXmlTag(sb, tagName, null, params);		
	}

}
