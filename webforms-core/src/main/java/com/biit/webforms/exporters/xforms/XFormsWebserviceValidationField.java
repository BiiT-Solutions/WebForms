package com.biit.webforms.exporters.xforms;

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

import com.biit.form.entity.BaseQuestion;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.exporters.xforms.exceptions.InvalidDateException;
import com.biit.webforms.exporters.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.exporters.xforms.exceptions.StringRuleSyntaxError;

/**
 * Generates temporal fake fields to store validation data. It is the only way
 * to make work validation at the same time as repeated elements.
 *
 */
public class XFormsWebserviceValidationField extends XFormsQuestion {

	public XFormsWebserviceValidationField(XFormsHelper xFormsHelper, BaseQuestion question)
			throws NotValidTreeObjectException, NotValidChildException {
		super(xFormsHelper, new WebserviceValidationField(question));
	}

	/**
	 * A validation field doesn't use alers.
	 */
	@Override
	protected String getAlert(OrbeonLanguage language) {
		return "";
	}

	/**
	 * A validation field doesn't add any constraint.
	 */
	protected void getConstraints(StringBuilder constraints) {
		// Doesnt have any aditional constraint.
	}

	/**
	 * A validation field type is string
	 */
	@Override
	protected void getXFormsType(StringBuilder type) {
		// String type doesn't need to add any declaration.
	}

	/**
	 * A validation field is always input.
	 */
	@Override
	protected String getElementFormDefinition() {
		return "input";
	}

	/**
	 * A validation field is always input and doesn't need to add anything.
	 */
	protected String getApparence() {
		return " ";
	}

	/**
	 * A validation field doesn't have answer type.
	 */
	@Override
	protected String isHtmlText() {
		return "";
	}

	@Override
	protected String getDefaultVisibility()
			throws InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		// TODO
		return "";
	}

	/**
	 * No extra class
	 */
	@Override
	protected String getCssClass() {
		return "";
	}

	/**
	 * No visibility
	 */
	protected String getVisibilityStructure() {
		return "";
	}

	/**
	 * The validation field is not mandatory
	 */
	@Override
	protected void isMandatory(StringBuilder binding) {
		// Do nothing
	}

	/**
	 * Validation field doesn't require help.
	 */
	@Override
	public String getHelp(OrbeonLanguage language) {
		return "";
	}

	/**
	 * Validation field doesn't require help.
	 */
	@Override
	public String getBodyHelp() {
		return "";
	}

	/**
	 * Relevant structure false
	 */
	@Override
	protected void getRelevantStructure(StringBuilder relevant)
			throws InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		relevant.append(" relevant=\"false()\"");
	}
}
