package com.biit.webforms.xforms;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

/**
 * Generates temporal fake fields to store validation data. It is the only way
 * to make work validation at the same time as repeated elements.
 *
 */
public class XformsWebserviceValidationField extends XFormsQuestion {

	public XformsWebserviceValidationField(XFormsHelper xFormsHelper, BaseQuestion question) throws NotValidTreeObjectException,
			NotValidChildException {
		super(xFormsHelper, new WebserviceValidationField(question));
	}

	/**
	 * A validation field doesn't use alers.
	 */
	@Override
	protected String getAlert() {
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
	protected String getDefaultVisibility() throws InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
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
	public String getHelp() {
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
	protected void getRelevantStructure(StringBuilder relevant) throws InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		relevant.append(" relevant=\"false()\"");
	}
}
