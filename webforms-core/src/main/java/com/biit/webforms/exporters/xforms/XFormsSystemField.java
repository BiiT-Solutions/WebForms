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

import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.exporters.xforms.exceptions.InvalidDateException;
import com.biit.webforms.exporters.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.exporters.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.exporters.xforms.exceptions.StringRuleSyntaxError;
import com.biit.webforms.persistence.entity.SystemField;

/**
 * Is a question but with less features.
 *
 */
public class XFormsSystemField extends XFormsQuestion {

	private static final String CSS_CLASS_SYSTEMFIELD = "webforms-systemfield";

	public XFormsSystemField(XFormsHelper xFormsHelper, SystemField systemField)
			throws NotValidTreeObjectException, NotValidChildException {
		super(xFormsHelper, systemField);
	}

	/**
	 * System fields can be prefilles using parameters.
	 */
	@Override
	public void getBinding(StringBuilder binding) throws NotExistingDynamicFieldException, InvalidDateException,
			StringRuleSyntaxError, PostCodeRuleSyntaxError {
		binding.append("<xf:bind id=\"").append(getBindingId()).append("\"  name=\"").append(getBindingName())
				.append("\" ");
		// Reference must be always to a name and not to a complete xpath, if
		// the xpath is used, in a loop all repeated
		// questions would always have the same answers selected.
		binding.append("ref=\"").append(getName()).append("\" ");
		binding.append(getDefaultValue());
		getRelevantStructure(binding);

		binding.append(" />");
	}

	/**
	 * System Fields has not any calculated value or flowRule field. And are
	 * always hidden.
	 */
	@Override
	protected void getRelevantStructure(StringBuilder relevant)
			throws InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		relevant.append(" relevant=\"false\"");
	}

	private String getDefaultValue() {
		return " xxf:default=\"xxf:get-request-parameter('" + ((SystemField) getSource()).getName() + "')\" ";
	}

	@Override
	protected String getLabel(OrbeonLanguage language) {
		return "<label><![CDATA[" + ((SystemField) getSource()).getFieldName() + "]]></label>";
	}

	@Override
	public String getBodyHelp() {
		return "";
	}

	@Override
	public String getHelp(OrbeonLanguage language) {
		return "";
	}

	@Override
	protected void isMandatory(StringBuilder builder) {
		// DO nothing
	}

	@Override
	protected void getConstraints(StringBuilder builder) {
		// DO nothing
	}

	@Override
	protected void createElementAnswersItems(StringBuilder stringBuilder) {
		// DO nothing
	}

	@Override
	protected void getXFormsType(StringBuilder builder) {
		// DO nothing
	}

	@Override
	protected String getElementFormDefinition() {
		return "output";
	}

	@Override
	protected String getApparence() {
		return "";
	}

	@Override
	protected String isHtmlText() {
		return "";
	}

	@Override
	protected String getHint(OrbeonLanguage language) {
		return "";
	}

	/**
	 * System fields always are hidden.
	 */
	@Override
	protected String getAllFlowsVisibility()
			throws InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		// Visibility of SystemFields is always false, but next elements must
		// not inherit this false value or the flow
		// will be broken.
		getXFormsHelper().addVisibilityOfElement(getSource(), super.getAllFlowsVisibility());
		return "false";
	}

	@Override
	protected String getCssClass() {
		return super.getCssClass() + " " + CSS_CLASS_SYSTEMFIELD;
	}

}
