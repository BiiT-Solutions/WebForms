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
