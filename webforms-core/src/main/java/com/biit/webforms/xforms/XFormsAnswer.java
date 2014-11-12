package com.biit.webforms.xforms;

import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

public class XFormsAnswer extends XFormsObject<Answer> {

	public XFormsAnswer(XFormsHelper xFormsHelper, Answer answer) throws NotValidTreeObjectException,
			NotValidChildException {
		super(xFormsHelper, answer);
	}

	@Override
	public String getBinding() throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		return null;
	}

	@Override
	public String getSectionBody() {
		return null;
	}

	/**
	 * Answer has no definitions in the model.
	 */
	@Override
	public String getDefinition() {
		return "";
	}

	protected String getValue() {
		return "<value><![CDATA[" + getSource().getName() + "]]></value>";
	}

	@Override
	protected String getHint() {
		if (((Answer) getSource()).getDescription() != null && ((Answer) getSource()).getDescription().length() > 0) {
			return "<hint><![CDATA[" + ((Answer) getSource()).getDescription() + "]]></hint>";
		}
		return "<hint/>";
	}

	/**
	 * Answers has a special definition inside a question.
	 */
	@Override
	protected String getResources() throws NotExistingDynamicFieldException {
		String resource = "<item>";

		resource += getLabel();
		resource += getHint();
		resource += getValue();

		resource += "</item>";
		return resource;
	}

	@Override
	protected String getAllFlowsVisibility() throws InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDefaultVisibility() throws InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		// TODO Auto-generated method stub
		return null;
	}
}
