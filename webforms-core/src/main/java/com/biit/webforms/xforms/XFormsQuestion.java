package com.biit.webforms.xforms;

import java.util.ArrayList;
import java.util.Set;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.enumerations.FlowType;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOriginException;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSourceException;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

public class XFormsQuestion extends XFormsObject<BaseQuestion> {
	private final static int MAX_YEARS_BIRTHDAY = 120;
	private final static String CSS_CLASS_RADIO_BUTTON_HORIZONTAL = "fr-radio-horizontal";
	private static final String CSS_CLASS_QUESTION = "webforms-question";
	private static final String CSS_CLASS_QUESTION_HELP = "webforms-help";

	public XFormsQuestion(XFormsHelper xFormsHelper, BaseQuestion question) throws NotValidTreeObjectException,
			NotValidChildException {
		super(xFormsHelper, question);
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
	public void getBinding(StringBuilder binding) throws NotExistingDynamicFieldException, InvalidDateException,
			StringRuleSyntaxError, PostCodeRuleSyntaxError {
		binding.append("<xf:bind id=\"").append(getBindingId()).append("\"  name=\"").append(getBindingName())
				.append("\" ");
		// Reference must be always to a name and not to a complete xpath, if
		// the xpath is used, in a loop all repeated
		// questions would always have the same answers selected.
		binding.append("ref=\"").append(getName()).append("\" ");
		getXFormsType(binding);
		isMandatory(binding);
		getConstraints(binding);
		getRelevantStructure(binding);
		binding.append(" />");
	}

	protected void isMandatory(StringBuilder binding) {
		if (((Question) getSource()).isMandatory()) {
			binding.append(" required=\"true()\" ");
		}
	}

	/**
	 * Some dates have constraints.
	 * 
	 * @return
	 */
	protected void getConstraints(StringBuilder contraints) {
		if (((Question) getSource()).getAnswerFormat() != null) {
			// adjust-date-to-timezone is used to remove timestamp
			// "If $timezone is the empty sequence, returns an xs:date without a timezone."
			// So you can write:
			// adjust-date-to-timezone(current-date(), ())"
			if (((Question) getSource()).getAnswerSubformat().equals(AnswerSubformat.DATE_PAST)) {
				contraints.append(" constraint=\"string-length("
						+ getXFormsHelper().getXFormsObject(getSource()).getXPath()
						+ "/text())=0 or . &lt;= adjust-date-to-timezone(current-date(), ())\" ");
			} else if (((Question) getSource()).getAnswerSubformat().equals(AnswerSubformat.DATE_FUTURE)) {
				contraints.append(" constraint=\"string-length("
						+ getXFormsHelper().getXFormsObject(getSource()).getXPath()
						+ "/text())=0 or . &gt;= adjust-date-to-timezone(current-date(), ())\" ");
			} else if (((Question) getSource()).getAnswerSubformat().equals(AnswerSubformat.DATE_BIRTHDAY)) {
				contraints
						.append(" constraint=\"string-length("
								+ getXFormsHelper().getXFormsObject(getSource()).getXPath()
								+ "/text())=0 or . &lt;= adjust-date-to-timezone(current-date(), ()) and (year-from-date(current-date()) - year-from-date(.) &lt;= ")
						.append(MAX_YEARS_BIRTHDAY).append(")\" ");
			}
		}
	}

	@Override
	protected String getAlert() {
		if (getSource() instanceof Question) {
			if (((Question) getSource()).getAnswerFormat() != null) {
				switch (((Question) getSource()).getAnswerFormat()) {
				case DATE:
					switch (((Question) getSource()).getAnswerSubformat()) {
					case DATE_FUTURE:
						return "<alert><![CDATA[Date must be at the future!]]></alert>";
					case DATE_PAST:
					case DATE_BIRTHDAY:
						return "<alert><![CDATA[Date must be at the past!]]></alert>";
					default:
						break;
					}
				default:
					break;
				}
			}
		}
		return "<alert/>";
	}

	/**
	 * Translate the element type of DHszw forms to XForms.
	 * 
	 * @param element
	 * @return
	 */
	protected void getXFormsType(StringBuilder type) {
		if (((Question) getSource()).getAnswerFormat() != null) {
			switch (((Question) getSource()).getAnswerFormat()) {
			case TEXT:
				switch (((Question) getSource()).getAnswerSubformat()) {
				case EMAIL:
					type.append(" type=\"xf:email\" ");
					break;
				case PHONE:
					type.append(" constraint=\". = '' or matches(., '^")
							.append(WebformsConfigurationReader.getInstance().getRegexPhone()).append("')\" ");
					break;
				case IBAN:
					type.append(" constraint=\". = '' or matches(., '^")
							.append(WebformsConfigurationReader.getInstance().getRegexIban()).append("')\" ");
					break;
				case BSN:
					type.append(" constraint=\". = '' or matches(., '")
							.append(WebformsConfigurationReader.getInstance().getRegexBsn()).append("')\" ");
					break;
				case TEXT:
				default:
					break;
				}
				break;
			case DATE:
				switch (((Question) getSource()).getAnswerSubformat()) {
				case DATE:
				case DATE_PAST:
				case DATE_FUTURE:
				case DATE_PERIOD:
				case DATE_BIRTHDAY:
					type.append(" type=\"xf:date\" ");
					break;
				default:
					break;
				}
				break;
			case NUMBER:
				switch (((Question) getSource()).getAnswerSubformat()) {
				case NUMBER:
					type.append(" type=\"xf:decimal\" ");
					break;
				case FLOAT:
					type.append(" type=\"xf:float\" ");
					break;
				default:
					break;
				}
				break;
			case POSTAL_CODE:
				type.append(" constraint=\". = '' or matches(., '^")
						.append(WebformsConfigurationReader.getInstance().getRegexPostalCode()).append("')\" ");
				break;
			}
		}
	}

	@Override
	public void getSectionBody(StringBuilder body) {
		// Add row.
		body.append("<xh:tr>");
		body.append("<xh:td>");

		// Add element info.
		body.append(createElementBody());

		// Close row.
		body.append("</xh:td>");
		body.append("</xh:tr>");
	}

	/**
	 * Creates a static element that does not depends on the value of other element.
	 * 
	 * @return
	 * @throws InvalidFlowInForm
	 */
	private String createElementBody() {
		StringBuilder section = new StringBuilder();
		section.append("<xf:" + getElementFormDefinition() + " " + getApparence() + " id=\"" + getSectionControlName()
				+ "\" class=\"" + getCssClass() + "\" bind=\"" + getBindingId() + "\">");
		section.append(getBodyLabel());
		section.append(getBodyHint());
		section.append(getBodyAlert());
		section.append(getBodyHelp());
		createElementAnswersItems(section);
		section.append("</xf:" + getElementFormDefinition() + " >");
		return section.toString();
	}

	/**
	 * XForms has input, output and selectable elements. This method translates an element to the correct one.
	 * 
	 * @param element
	 * @return
	 */
	protected String getElementFormDefinition() {
		switch (((Question) getSource()).getAnswerType()) {
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
		return "";
	}

	/**
	 * XForms has input, output and selectable elements. This method translates an element to the correct one.
	 * 
	 * @param element
	 * @return
	 */
	protected String getApparence() {
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
	protected void createElementAnswersItems(StringBuilder row) {
		if (!getChildren().isEmpty()) {
			// Combobox has a dummy element selected as default. Is added before
			// the itemset declaration.
			if (((Question) getSource()).getAnswerType().equals(AnswerType.SINGLE_SELECTION_LIST)) {
				row.append("<xf:item><xf:label>[Select...]</xf:label><xf:value/></xf:item>");
			}

			// Only one itemset for elements without subanswers.
			boolean simpleElementsAdded = false;
			for (XFormsObject<? extends TreeObject> answer : getChildren()) {
				if (!answer.getChildren().isEmpty() || !simpleElementsAdded) {
					answer.getSectionBody(row);
					if (answer.getChildren().isEmpty()) {
						simpleElementsAdded = true;
					}
				}

			}
		}
	}

	/**
	 * Some elements needs to insert HTML text. Adds the tags to allow html code in the element.
	 * 
	 * @param element
	 * @return
	 */
	protected String isHtmlText() {
		switch (((Question) getSource()).getAnswerType()) {
		case MULTIPLE_SELECTION:
			return "mediatype=\"text/html\"";
		default:
			return "";
		}
	}

	@Override
	protected String getHint() {
		return "<hint/>";
	}

	@Override
	protected String getDefaultVisibility() throws InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		// First element always visible.
		if (getXFormsHelper().isFirstQuestion(getSource())) {
			return "";
		}
		// Other elements uses the previous element visibility.
		return getXFormsHelper().getVisibilityOfElement(getXFormsHelper().getPreviousBaseQuestion(getSource()));
	}

	/**
	 * Creates a new computed go to next element Flow. Type normal, condition = '' -> true
	 * 
	 * @param origin
	 * @param destiny
	 */
	public static void addNewNextElementFlow(BaseQuestion origin, BaseQuestion destiny) {
		Flow flow = new Flow();
		try {
			flow.setContent(origin, FlowType.NORMAL, destiny, false, new ArrayList<Token>());
		} catch (BadFlowContentException | FlowWithoutSourceException | FlowSameOriginAndDestinyException
				| FlowDestinyIsBeforeOriginException | FlowWithoutDestinyException e) {
			// Impossible
			WebformsLogger.errorMessage(XFormsQuestion.class.getName(), e);
		}
	}

	@Override
	public Set<Flow> getFlowsTo() {
		return getXFormsHelper().getFlowsWithDestiny(getSource());
	}

	@Override
	protected String getCalculateStructure(String flow) {
		String parsedFlow = flow.replace("$", "../$");
		// Calculate set readonly as true by default.
		return " calculate=\"if(" + parsedFlow + ") then . else ''\" readonly=\"false\"";
	}

	/**
	 * Some elements have extra classes to fit some javascripts.
	 * 
	 * @param element
	 * @return
	 */
	@Override
	protected String getCssClass() {
		String classList = super.getCssClass() + " " + CSS_CLASS_QUESTION;
		if (getSource() instanceof Question && ((Question) getSource()).isHorizontal()) {
			classList += " " + CSS_CLASS_RADIO_BUTTON_HORIZONTAL;
		}
		if (getSource() instanceof Question && ((Question) getSource()).getDescription() != null
				&& ((Question) getSource()).getDescription().length() > 0) {
			classList += " " + CSS_CLASS_QUESTION_HELP;
		}
		return classList;
	}

}
