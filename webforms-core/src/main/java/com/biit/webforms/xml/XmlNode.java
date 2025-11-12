package com.biit.webforms.xml;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
