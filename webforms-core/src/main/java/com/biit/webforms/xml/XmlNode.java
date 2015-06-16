package com.biit.webforms.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class XmlNode {
	
	private String name;
	protected List<XmlNode> children;
	
	public XmlNode(String name) {
		this.setName(name);
		children = new ArrayList<XmlNode>();
	}
	
	public void addChild(String xpath){
		String[] xpathElements = xpath.split("/");
		addChild(Arrays.asList(xpathElements).subList(1, xpathElements.length));
	}
	
	private void addChild(List<String> asList) {
		if(!asList.isEmpty()){
			XmlNode child = getOrCreateChild(asList.get(0));
			child.addChild(asList.subList(1, asList.size()));
		}
	}

	private XmlNode getOrCreateChild(String string) {
		for(XmlNode child: children){
			if(Objects.equals(child.getName(),string)){
				return child;
			}
		}
		XmlNode child = new XmlNode(string);
		children.add(child);
		return child;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getXml(){
		StringBuilder sb = new StringBuilder();
		getXml(sb);
		return sb.toString();
	}
	
	public void getXml(StringBuilder sb){
		if(children.isEmpty()){
			sb.append("<"+getName()+"/>");
		}else{
			sb.append("<"+getName()+">");
			for(XmlNode child: children){
				child.getXml(sb);
			}
			sb.append("</"+getName()+">");
		}
	}
	
	public String getCodifiedXml(){
		String xml = getXml();
		xml = xml.replace("&", "&amp;");
		xml = xml.replace("<", "&lt;");
		xml = xml.replace(">", "&gt;");
		xml = xml.replace("\"", "&quot;");
		xml = xml.replace("'", "&apos;");
		return xml;
	}
}
