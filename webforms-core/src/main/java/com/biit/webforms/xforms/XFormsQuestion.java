package com.biit.webforms.xforms;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

public class XFormsQuestion extends XFormsObject {
	private final static int MAX_YEARS_BIRTHDAY = 120;

	public XFormsQuestion(BaseQuestion question) throws NotValidTreeObjectException, NotValidChildException {
		super(question);
	}

	@Override
	protected void setSource(TreeObject treeObject) throws NotValidTreeObjectException {
		if (treeObject instanceof Question) {
			super.setSource(treeObject);
		} else {
			throw new NotValidTreeObjectException("Invalid source!");
		}
	}

	@Override
	public String getBodyHelp() {
		if (((Question) getSource()).getDescription().length() > 0) {
			return getBodyStructure("help", true);
		}
		return "";
	}

	@Override
	public String getHelp() {
		// Avoid empty help windows.
		if (((Question) getSource()).getDescription().length() > 0) {
			return "<help><![CDATA[" + ((Question) getSource()).getDescription() + "]]></help>";
		}
		return "";
	}

	@Override
	public String getBinding() throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		return "<xf:bind id=\"" + getBindingName() + "\"  name=\"" + getSource().getName() + "\" ref=\""
				+ getControlName() + "\" " + getXFormsType() + " " + isMandatory() + " " + getConstraints() + " "
				+ getRelevantStructure() + " relevant=\"" + getRelevantRule() + "\" >";
	}

	private String isMandatory() {
		return ((Question) getSource()).isMandatory() ? " required=\"true()\" " : "";
	}

	/**
	 * Some dates have constraints.
	 * 
	 * @return
	 */
	private String getConstraints() {
		if (((Question) getSource()).getAnswerFormat() != null) {
			if (((Question) getSource()).getAnswerFormat().equals(AnswerSubformat.DATE_PAST)) {
				return " constraint=\". &lt;= current-date()\" ";
			} else if (((Question) getSource()).getAnswerSubformat().equals(AnswerSubformat.DATE_FUTURE)) {
				return " constraint=\". &gt;= current-date()\" ";
			} else if (((Question) getSource()).getAnswerSubformat().equals(AnswerSubformat.DATE_BIRTHDAY)) {
				return " constraint=\". &lt;= current-date() and (year-from-date(current-date()) - year-from-date(.) &lt;= "
						+ MAX_YEARS_BIRTHDAY + ")\"";
			}
		}
		return "";
	}

	/**
	 * Translate the element type of DHszw forms to XForms.
	 * 
	 * @param element
	 * @return
	 */
	protected String getXFormsType() {
		if (((Question) getSource()).getAnswerFormat() != null) {
			switch (((Question) getSource()).getAnswerFormat()) {
			case TEXT:
				switch (((Question) getSource()).getAnswerSubformat()) {
				case TEXT:
					return "";
				case EMAIL:
					return " type=\"xf:email\" ";
				case PHONE:
					return " constraint=\". = '' or matches(., '^"
							+ WebformsConfigurationReader.getInstance().getRegexPhone() + "')\" ";
				case IBAN:
					return " constraint=\". = '' or matches(., '^"
							+ WebformsConfigurationReader.getInstance().getRegexIban() + "')\" ";
				case BSN:
					return " constraint=\". = '' or matches(., '"
							+ WebformsConfigurationReader.getInstance().getRegexBsn() + "')\" ";
				default:
					return "";
				}
			case DATE:
				switch (((Question) getSource()).getAnswerSubformat()) {
				case DATE:
				case DATE_PAST:
				case DATE_FUTURE:
				case DATE_PERIOD:
				case DATE_BIRTHDAY:
					return " type=\"xf:date\" ";
				default:
					return "";
				}
			case NUMBER:
				switch (((Question) getSource()).getAnswerSubformat()) {
				case NUMBER:
					return " type=\"xf:decimal\" ";
				case FLOAT:
					return " type=\"xf:float\" ";
				default:
					return "";
				}

			case POSTAL_CODE:
				return " constraint=\". = '' or matches(., '^"
						+ WebformsConfigurationReader.getInstance().getRegexPostalCode() + "')\" ";
			}
		}
		return "";
	}

	@Override
	public String getSectionBody() {
		String section = "";
		// Add row.
		section += "<xh:tr>";
		section += "<xh:td>";

		// Add element info.
		section += createElement();

		// Close row.
		section += "</xh:td>";
		section += "</xh:tr>";
		return section;
	}

	/**
	 * Creates a static element that does not depends on the value of other element.
	 * 
	 * @return
	 * @throws InvalidFlowInForm
	 */
	private String createElement() {
		String section = "";
		section += "<xf:" + getElementFormDefinition() + " " + getApparence() + " id=\"" + getSectionControlName()
				+ "\" " + " bind=\"" + getBindingName() + "\">";
		section += getBodyLabel();
		section += getBodyHint();
		section += getBodyAlert();
		section += getBodyHelp();
		section += createElementAnswersItems();
		section += "</xf:" + getElementFormDefinition() + " >";
		return section;
	}

	/**
	 * XForms has input, output and selectable elements. This method translates an element to the correct one.
	 * 
	 * @param element
	 * @return
	 */
	private String getElementFormDefinition() {
		switch (((Question) getSource()).getAnswerType()) {
		// case NULL:
		// return "output ";
		case INPUT:
			return "input";
		case TEXT_AREA:
			return "textarea";
		case SINGLE_SELECTION_RADIO:
			return "select1";
		case SINGLE_SELECTION_LIST:
			return "select1";
		case MULTIPLE_SELECTION:
			return "select";
		}
		return " ";
	}

	/**
	 * XForms has input, output and selectable elements. This method translates an element to the correct one.
	 * 
	 * @param element
	 * @return
	 */
	private String getApparence() {
		switch (((Question) getSource()).getAnswerType()) {
		// case NULL:
		// return "";
		case INPUT:
			return "";
		case TEXT_AREA:
			return "";
		case SINGLE_SELECTION_RADIO:
			return "appearance=\"full\"";
		case SINGLE_SELECTION_LIST:
			return "appearance=\"minimal\"";
		case MULTIPLE_SELECTION:
			return "appearance=\"full\"";
		}
		return " ";
	}

	/**
	 * Elements with answers need to add information of the answers in the body.
	 * 
	 * @param element
	 * @return
	 */
	private String createElementAnswersItems() {
		String row = "";
		if (!getChildren().isEmpty()) {
			// Combobox has a dummy element selected as default. Is added before
			// the itemset declaration.
			if (((Question) getSource()).getAnswerType().equals(AnswerType.SINGLE_SELECTION_LIST)) {
				row += "<xf:item><xf:label>[Select...]</xf:label><xf:value/></xf:item>";
			}
			row += "<xf:itemset ref=\"$form-resources/";
			row += getPath() + "/item\">";
			row += "<xf:label ref=\"label\" " + isHtmlText() + " />";
			row += "<xf:value ref=\"value\"/>";
			row += "<xf:hint ref=\"hint\"/>";
			row += "</xf:itemset>";
		}
		return row;
	}

	/**
	 * Some elements needs to insert HTML text. Adds the tags to allow html code in the element.
	 * 
	 * @param element
	 * @return
	 */
	private String isHtmlText() {
		switch (((Question) getSource()).getAnswerType()) {
		case MULTIPLE_SELECTION:
			return "mediatype=\"text/html\"";
		default:
			return "";
		}
	}

	@Override
	protected String getHint() {
		if (((Question) getSource()).getDescription() != null && ((Question) getSource()).getDescription().length() > 0) {
			return "<hint><![CDATA[" + ((Question) getSource()).getDescription() + "]]></hint>";
		}
		return "<hint/>";
	}

	@Override
	protected String getFlowVisibility() throws InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		//Set<Flow> flows = ((Form) getSource().getAncestor(Form.class)).getFlowsTo((BaseQuestion) getSource());
		return null;
	}

}
