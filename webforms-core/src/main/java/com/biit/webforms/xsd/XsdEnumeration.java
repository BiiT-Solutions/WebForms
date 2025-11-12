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

public class XsdEnumeration extends XsdNodeWithChilds {

	private static final String NODE_NAME_TAG = "xs:enumeration";
	private static final String PARAMETER_VALUE = "value";
	
	public XsdEnumeration(String value) {
		super();
		putValue(value);
	}
	
	public void putValue(String value) {
		putParameter(PARAMETER_VALUE, value);
	}

	@Override
	protected String getNodeName() {
		return NODE_NAME_TAG;
	}

}
