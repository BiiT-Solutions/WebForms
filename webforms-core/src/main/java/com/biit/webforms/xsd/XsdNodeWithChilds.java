package com.biit.webforms.xsd;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.biit.webforms.xml.XmlNodeProducer;
import com.biit.webforms.xml.XmlParam;

/**
 * Abstract class to define Xsd nodes with children
 *
 */
public abstract class XsdNodeWithChilds extends XmlNodeProducer {

	private static final String PARAMETER_NAME = "name";

	private final HashMap<String, String> parameters;
	private final List<XmlNodeProducer> childs;

	public XsdNodeWithChilds(XmlNodeProducer... childs) {
		this.parameters = new HashMap<String, String>();
		this.childs = new ArrayList<XmlNodeProducer>();
		this.childs.addAll(Arrays.asList(childs));
	}

	public void addChild(XmlNodeProducer child) {
		childs.add(child);
	}

	public void putParameter(String name, String value) {
		parameters.put(name, value);
	}

	public String getParameter(String parameterName) {
		return parameters.get(parameterName);
	}

	@Override
	public void appendXml(StringBuilder sb) {
		appendXmlTag(sb, getNodeName(), childs, getXmlParams());
	}

	private XmlParam[] getXmlParams() {
		List<XmlParam> xmlParameters = new ArrayList<XmlParam>();
		for (Entry<String, String> entry : parameters.entrySet()) {
			xmlParameters.add(new XmlParam(entry.getKey(), entry.getValue()));
		}
		return xmlParameters.toArray(new XmlParam[] {});
	}

	public void putName(String name) {
		putParameter(PARAMETER_NAME, name);
	}

	public String getName() {
		return getParameter(PARAMETER_NAME);
	}
}
