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

/**
 * Basic node for the schema definition.
 *
 */
public class XsdSchema extends XsdNodeWithChilds {
	private static final String XML_SCHEMA_TAG = "xs:schema";
	private static final String XMLNS_XS = "xmlns:xs";
	private static final String XMLNS_XS_VALUE = "http://www.w3.org/2001/XMLSchema";
	private static final String XMLNS = "xmlns";
	private static final String ELEMENT_FORM_DEFAULT = "elementFormDefault";
	private static final String ELEMENT_FORM_DEFAULT_VALUE = "qualified";
	private static final String TARGETED_NAMESPACE = "targetNamespace";

	public XsdSchema(String xmlns, String targetedNamespace) {
		putParameter(XMLNS_XS, XMLNS_XS_VALUE);
		putParameter(XMLNS, xmlns);
		putParameter(ELEMENT_FORM_DEFAULT, ELEMENT_FORM_DEFAULT_VALUE);
		putParameter(TARGETED_NAMESPACE, targetedNamespace);
	}

	@Override
	protected String getNodeName() {
		return XML_SCHEMA_TAG;
	}
}
