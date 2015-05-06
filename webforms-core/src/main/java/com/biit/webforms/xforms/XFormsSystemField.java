package com.biit.webforms.xforms;

import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

/**
 * Is a question but with less features.
 * 
 */
public class XFormsSystemField extends XFormsQuestion {

	private static final String CSS_CLASS_SYSTEMFIELD = "webforms-systemfield";

	public XFormsSystemField(XFormsHelper xFormsHelper, SystemField systemField) throws NotValidTreeObjectException,
			NotValidChildException {
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
		binding.append(getPrefill());
		getRelevantStructure(binding);

		binding.append(" />");
	}

	private String getPrefill() {
		return " xxf:default=\"xxf:get-request-parameter('" + getSource().getName() + "')\" ";
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
	protected String getHint() {
		return "";
	}

	/**
	 * System fields always are hidden.
	 */
	@Override
	protected String getAllFlowsVisibility() throws InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		// Visibility of SystemFields is always false, but next elements must not inherit this false value or the flow
		// will be broken.
		getXFormsHelper().addVisibilityOfElement(getSource(), super.getAllFlowsVisibility());
		return "false";
	}

	@Override
	protected String getCssClass() {
		return super.getCssClass() + " " + CSS_CLASS_SYSTEMFIELD;
	}

}
