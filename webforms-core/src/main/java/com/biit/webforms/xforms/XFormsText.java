package com.biit.webforms.xforms;

import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.persistence.entity.Text;

/**
 * Is a question but with less features.
 * 
 */
public class XFormsText extends XFormsQuestion {

	public XFormsText(XFormsHelper xFormsHelper, Text text) throws NotValidTreeObjectException, NotValidChildException {
		super(xFormsHelper, text);
	}

	@Override
	public String getBodyHelp() {
		if (((Text) getSource()).getDescription().length() > 0) {
			return getBodyStructure("help", true);
		}
		return "";
	}

	@Override
	public String getHelp() {
		// Avoid empty help windows.
		if (((Text) getSource()).getDescription().length() > 0) {
			return "<help><![CDATA[" + ((Text) getSource()).getDescription() + "]]></help>";
		}
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
		if (((Text) getSource()).getDescription() != null && ((Text) getSource()).getDescription().length() > 0) {
			return "<hint><![CDATA[" + ((Text) getSource()).getDescription() + "]]></hint>";
		}
		return "<hint/>";
	}

}
