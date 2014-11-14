package com.biit.webforms.xforms;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
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
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOrigin;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestiny;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSource;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

public class XFormsQuestion extends XFormsObject<BaseQuestion> {
	private final static int MAX_YEARS_BIRTHDAY = 120;

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
	public String getBinding() throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		return "<xf:bind id=\"" + getBindingName() + "\"  name=\"" + getSource().getName() + "\" ref=\""
				+ getControlName() + "\" " + getXFormsType() + " " + isMandatory() + " " + getConstraints() + " "
				+ getRelevantStructure() + " />";
	}

	protected String isMandatory() {
		return ((Question) getSource()).isMandatory() ? " required=\"true()\" " : "";
	}

	/**
	 * Some dates have constraints.
	 * 
	 * @return
	 */
	protected String getConstraints() {
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
	protected String createElementAnswersItems() {
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
		if (((Question) getSource()).getDescription() != null && ((Question) getSource()).getDescription().length() > 0) {
			return "<hint><![CDATA[" + ((Question) getSource()).getDescription() + "]]></hint>";
		}
		return "<hint/>";
	}

	@Override
	protected String getAllFlowsVisibility() throws InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		// Load stored visibility if exists.
		if (getXFormsHelper().getVisibilityOfQuestion(getSource()) != null) {
			return getXFormsHelper().getVisibilityOfQuestion(getSource());
		}

		Set<Flow> flowsTo = getXFormsHelper().getFlowsWithDestiny(getSource());

		// String visibility = getRelevantByFlows(flowsTo);
		String visibility = "";
		// Get all visibility rule as tokens.
		List<Token> visibilityAsToken = getRelevantByFlowsAsTokens(flowsTo);
		if(visibilityAsToken.isEmpty()){
			return "";
		}
		// Simplify the visibility expression
		BooleanExpressionSimplifier simplifier = new BooleanExpressionSimplifier(visibilityAsToken);
		System.out.println("Visibility: "+ visibilityAsToken.toString());
		List<Token> simplifiedVisibility = simplifier.getSimplified();
		System.out.println("Simplified: " + simplifiedVisibility.toString());

		// Store for future reuse.
		getXFormsHelper().addVisibilityOfQuestionAsToken(getSource(), simplifiedVisibility);

		//Convert to String.
		String flowvisibility = "";
		// returns the condition or the 'others' rule.
		for (Token token : simplifiedVisibility) {
			System.out.println(token);
			String conditionVisibility = convertTokenToXForms(token);

			// 'not' rules need that source must select an answer.
			// conditionVisibility += othersSourceMustBeFilledUp(flow);

			flowvisibility += conditionVisibility.trim() + " ";
		}

		flowvisibility = flowvisibility.trim();
		if (flowvisibility.length() > 0) {
			visibility += "(" + (flowvisibility).trim() + ")";
		}

		// Store calculated visibility as string
		getXFormsHelper().addVisibilityOfQuestion(getSource(), visibility);
		return visibility;
	}

	@Override
	protected String getDefaultVisibility() throws InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		// First element always visible.
		if (getXFormsHelper().isFirstQuestion(getSource())) {
			return "";
		}
		// Other elements uses the previous element visibility.
		return getXFormsHelper().getVisibilityOfQuestion(getXFormsHelper().getPreviousQuestion(getSource()));
	}

	/**
	 * Creates a new computed go to next element Flow. Type normal, condition = '' -> true
	 * 
	 * @param origin
	 * @param destiny
	 */
	public static void addNewNextElementFlow(TreeObject origin, TreeObject destiny) {
		Flow flow = new Flow();
		try {
			flow.setContent(origin, FlowType.NORMAL, destiny, false, new ArrayList<Token>());
		} catch (BadFlowContentException | FlowWithoutSource | FlowSameOriginAndDestinyException
				| FlowDestinyIsBeforeOrigin | FlowWithoutDestiny e) {
			// Impossible
			WebformsLogger.errorMessage(XFormsQuestion.class.getName(), e);
		}
	}

}
