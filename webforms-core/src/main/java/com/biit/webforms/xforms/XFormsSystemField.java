package com.biit.webforms.xforms;

import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

/**
 * Is a question but with less features.
 * 
 */
public class XFormsSystemField extends XFormsQuestion {

	public XFormsSystemField(XFormsHelper xFormsHelper, SystemField systemField) throws NotValidTreeObjectException,
			NotValidChildException {
		super(xFormsHelper, systemField);
	}

	@Override
	protected String getLabel() {
		return "<label><![CDATA[" + ((SystemField) getSource()).getFieldName() + "]]></label>";
	}

	@Override
	public String getBodyHelp() {
		return "";
	}

	@Override
	public String getHelp() {
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
	protected String createElementAnswersItems() {
		return "";
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
	protected String getHint() {
		return "";
	}

	/**
	 * System fields always are hidden.S
	 */
	@Override
	protected String getAllFlowsVisibility() throws InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		// Visibility of SystemFields is always false, but next elements must not inherit this false value or the flow
		// will be broken.
		getXFormsHelper().addVisibilityOfElement(getSource(), super.getAllFlowsVisibility());
		return "false";
	}

}
