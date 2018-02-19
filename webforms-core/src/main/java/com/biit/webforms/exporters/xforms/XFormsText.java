package com.biit.webforms.exporters.xforms;

import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.persistence.entity.Text;

/**
 * Is a question but with less features.
 * 
 */
public class XFormsText extends XFormsQuestion {

	private static final String CSS_CLASS_INFOTEXT = "webforms-infotext";

	public XFormsText(XFormsHelper xFormsHelper, Text text) throws NotValidTreeObjectException, NotValidChildException {
		super(xFormsHelper, text);
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
	protected String getLabel(OrbeonLanguage language) {
		if (((Text) getSource()).getDescription().length() > 0) {
			return "<label><![CDATA[" + ((Text) getSource()).getDescription() + "]]></label>";
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
	protected String getCssClass() {
		return super.getCssClass() + " " + CSS_CLASS_INFOTEXT;
	}

}
