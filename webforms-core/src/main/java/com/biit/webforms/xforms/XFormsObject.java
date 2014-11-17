package com.biit.webforms.xforms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

public abstract class XFormsObject<T extends TreeObject> {
	protected static final String RANGE_TOKEN = "between";
	protected static final String XPATH_DATE_FORMAT = "yyyy-MM-dd";

	private T source;
	private XFormsHelper xFormsHelper;

	private List<XFormsObject<? extends TreeObject>> children;

	private XFormsObject<? extends TreeObject> parent;

	public XFormsObject(XFormsHelper xFormsHelper, T treeObject) throws NotValidTreeObjectException,
			NotValidChildException {
		setSource(treeObject);
		this.xFormsHelper = xFormsHelper;
		children = new ArrayList<>();
		for (TreeObject child : treeObject.getChildren()) {
			addChild(child);
		}
	}

	protected void setSource(T treeObject) throws NotValidTreeObjectException {
		source = treeObject;
	}

	protected void addChild(TreeObject child) throws NotValidChildException {
		try {
			XFormsObject<?> newChild;
			if (child instanceof Category) {
				newChild = new XFormsCategory(xFormsHelper, (Category) child);
			} else if (child instanceof Group) {
				if (((Group) child).isRepeatable()) {
					newChild = new XFormsRepeatableGroup(xFormsHelper, (Group) child);
				} else {
					newChild = new XFormsGroup(xFormsHelper, (Group) child);
				}
			} else if (child instanceof BaseQuestion) {
				if (child instanceof Text) {
					newChild = new XFormsText(xFormsHelper, (Text) child);
				} else if (child instanceof SystemField) {
					newChild = new XFormsSystemField(xFormsHelper, (SystemField) child);
				} else {
					newChild = new XFormsQuestion(xFormsHelper, (BaseQuestion) child);
				}
				xFormsHelper.addXFormsQuestion((XFormsQuestion) newChild);
			} else if (child instanceof Answer) {
				newChild = new XFormsAnswer(xFormsHelper, (Answer) child);
			} else {
				// Forms cannot be a valid child.
				throw new NotValidChildException("Inserted child '" + child + "' is not valid. ");
			}
			newChild.setParent(this);
			children.add(newChild);
		} catch (NotValidTreeObjectException e) {
			throw new NotValidChildException("Inserted child '" + child + "' is not valid. ");
		}
	}

	protected T getSource() {
		return source;
	}

	protected String getAlert() {
		return "<alert>" + "</alert>";
	}

	protected String getBindingName() {
		return getControlName() + "-bind";
	}

	protected String getBodyAlert() {
		return getBodyStructure("alert", false);
	}

	protected String getBodyHelp() {
		return "";
	}

	protected String getBodyHint() {
		return getBodyStructure("hint", false);
	}

	protected String getBodyLabel() {
		return getBodyStructure("label", true);
	}

	/**
	 * Defines the structure of the element in the body part of the XForms.
	 * 
	 * @param treeObject
	 * @return
	 * @throws InvalidFlowInForm
	 */
	protected String getBodyStructure(String structure, boolean html) {
		String text = "<xf:" + structure + " ref=\"$form-resources/" + getPath() + "/" + structure + "\"";
		if (html) {
			text += " mediatype=\"text/html\" ";
		}
		text += " />";
		return text;
	}

	/**
	 * Return the complete path of the element.
	 * 
	 * @return
	 */
	protected String getPath() {
		if (getParent() != null) {
			return getParent().getPath() + "/" + getControlName();
		}
		return getControlName();
	}

	protected List<XFormsObject<? extends TreeObject>> getChildren() {
		return children;
	}

	protected String getControlName() {
		return getSource().getName();
	}

	protected String getHelp() {
		// Avoid empty help windows.
		return "";
	}

	protected String getLabel() {
		return "<label><![CDATA[" + getSource().getLabel() + "]]></label>";
	}

	protected String getHint() {
		return "<hint/>";
	}

	protected String getRelevantStructure() throws InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		String flow = getAllFlowsVisibility();
		if (flow != null && flow.length() > 0) {
			return " relevant=\"" + flow + "\"";
		}
		return "";
	}

	/**
	 * Gets the visibility of an element depending on the flow rules.
	 * 
	 * @return
	 * @throws InvalidDateException
	 * @throws StringRuleSyntaxError
	 * @throws PostCodeRuleSyntaxError
	 */
	protected abstract String getAllFlowsVisibility() throws InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError;

	/**
	 * Is the default visibility of an element. For Categories is the visibility of the first question, for questions,
	 * is the visibility of the previous question and for the first question is always true.
	 * 
	 * @return
	 * @throws InvalidDateException
	 * @throws StringRuleSyntaxError
	 * @throws PostCodeRuleSyntaxError
	 */
	protected abstract String getDefaultVisibility() throws InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError;

	protected String getResources() throws NotExistingDynamicFieldException {
		String resource = "<" + getControlName() + ">";
		resource += getLabel();
		resource += getHint();
		resource += getAlert();
		resource += getHelp();

		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			resource += child.getResources();
		}

		resource += "</" + getControlName() + ">";
		return resource;
	}

	protected String getSectionControlName() {
		return getControlName() + "-control";
	}

	protected abstract String getBinding() throws NotExistingDynamicFieldException, InvalidDateException,
			StringRuleSyntaxError, PostCodeRuleSyntaxError;

	protected abstract String getSectionBody();

	protected String getDefinition() {
		String section = "<" + getControlName() + ">";
		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			section += child.getDefinition();
		}
		section += "</" + getControlName() + ">";
		return section;
	}

	protected XFormsObject<? extends TreeObject> getParent() {
		return parent;
	}

	protected void setParent(XFormsObject<? extends TreeObject> parent) {
		this.parent = parent;
	}

	/**
	 * Only loops uses templates. Search in childs.
	 * 
	 * @return
	 */
	protected String getTemplates() {
		String templates = "";
		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			templates += child.getTemplates();
		}
		return templates;
	}

	public XFormsHelper getXFormsHelper() {
		return xFormsHelper;
	}

	/**
	 * Returns a linkedHashSet with all the children in the current hierarchy that are instance of the filter type in
	 * appearance order.
	 * 
	 * @param filter
	 * @return
	 */
	public LinkedHashSet<XFormsObject<? extends TreeObject>> getAllChildrenInHierarchy(
			Class<? extends XFormsObject<? extends TreeObject>> filter) {
		LinkedHashSet<XFormsObject<? extends TreeObject>> selectedChildren = new LinkedHashSet<>();
		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			if (filter.isInstance(child)) {
				selectedChildren.add(child);
			}
			selectedChildren.addAll(child.getAllChildrenInHierarchy(filter));
		}
		return selectedChildren;
	}

	/**
	 * Convert a token into a string that can be processed by orbeon.
	 * 
	 * @param token
	 * @return
	 * @throws InvalidDateException
	 */
	protected String convertTokenToXForms(Token token) throws InvalidDateException {
		String visibility = "";
		if (token instanceof TokenComparationAnswer) {
			// $control-name='answer'
			if (((TokenComparationAnswer) token).getQuestion().getAnswerType().equals(AnswerType.MULTIPLE_SELECTION)) {
				visibility += getMultiCheckBoxVisibility((TokenComparationAnswer) token);
			} else {
				visibility += getBasicSelectionVisibility((TokenComparationAnswer) token);
			}
		} else if (token instanceof TokenComparationValue) {
			// $control-name=1
			visibility += getInputFieldVisibility((TokenComparationValue) token);
		} else if (token instanceof TokenAnswerNeeded) {
			visibility += "string-length($"
					+ getXFormsHelper().getXFormsObject(((TokenAnswerNeeded) token).getQuestion()).getControlName()
					+ ")>0";
		} else {
			// An operator 'and', 'or', ...
			visibility += token.getType().getOrbeonRepresentation();
		}
		return visibility;
	}

	/**
	 * Checkbox answer is returned as an unique string with all selected options divided by spaces. We need 'contains'
	 * XForms method to know if the answer is selected or not. But a 'contains' can fail if an element is a substring of
	 * other element, then we add space to delimiter substrings.
	 * 
	 * @param token
	 * @return
	 */
	private String getMultiCheckBoxVisibility(TokenComparationAnswer token) {
		return "contains(concat($" + getXFormsHelper().getXFormsObject(token.getQuestion()).getControlName()
				+ ", ' '), concat('" + token.getAnswer().getLabel() + "', ' '))";
	}

	/**
	 * Visibility for a token that is a component that allows to select an answer to the user. Selectable elements that
	 * are not a multicheckbox use standard $control-name='answer' string.
	 * 
	 * @param token
	 * @return
	 */
	private String getBasicSelectionVisibility(TokenComparationAnswer token) {
		String visibility = "";
		visibility += "$"
				+ getXFormsHelper().getXFormsObject(((TokenComparationAnswer) token).getQuestion()).getControlName();
		visibility += token.getType().getOrbeonRepresentation();
		visibility += "'" + ((TokenComparationAnswer) token).getAnswer() + "'";
		return visibility;
	}

	/**
	 * Translate token of an input field to orbeon relevant rules.
	 * 
	 * @param token
	 * @return
	 * @throws ParseException
	 * @throws PostCodeRuleSyntaxError
	 */
	private String getInputFieldVisibility(TokenComparationValue token) throws InvalidDateException {
		String visibility = "";
		if (token.getQuestion().getAnswerFormat() != null) {
			switch (token.getQuestion().getAnswerFormat()) {
			case NUMBER:
				visibility += "number($" + getXFormsHelper().getXFormsObject(token.getQuestion()).getControlName()
						+ ")";
				visibility += " " + token.getType().getOrbeonRepresentation();
				visibility += " " + token.getValue();
				break;
			case TEXT:
			case POSTAL_CODE:
				visibility += "$" + getXFormsHelper().getXFormsObject(token.getQuestion()).getControlName();
				visibility += " " + token.getType().getOrbeonRepresentation();
				visibility += " '" + token.getValue() + "'";
				break;
			case DATE:
				switch (token.getSubformat()) {
				case DATE:
				case DATE_BIRTHDAY:
				case DATE_FUTURE:
				case DATE_PAST:
					SimpleDateFormat formatter = new SimpleDateFormat();
					// Convert String to date.
					formatter.applyPattern(WebformsConfigurationReader.getInstance().getDatePattern());
					Date date;
					try {
						date = formatter.parse(token.getValue());
						// Convert date to Orbeon string format.
						formatter.applyPattern(XPATH_DATE_FORMAT);
						visibility += "xs:date($"
								+ getXFormsHelper().getXFormsObject(token.getQuestion()).getControlName() + ")";
						visibility += " " + token.getType().getOrbeonRepresentation();
						visibility += " xs:date('" + formatter.format(date) + "')";
					} catch (ParseException e) {
						throw new InvalidDateException(e.getMessage());
					}
					break;
				case DATE_PERIOD:
					visibility += translateDatePeriod(token);
					break;
				default:
					break;
				}
				break;
			}
		}
		return visibility;
	}

	private String translateDatePeriod(TokenComparationValue token) {
		String xPathOperation = "";
		switch (token.getDatePeriodUnit()) {
		case YEAR:
			xPathOperation = "yearMonthDuration";
			break;
		case MONTH:
			xPathOperation = "yearMonthDuration";
			break;
		case DAY:
			xPathOperation = "dayTimeDuration";
			break;
		}
		// Symbols for '>', '<' are the opposite in the orbeon operator.
		if (!token.getQuestion().getAnswerSubformat().equals(AnswerSubformat.DATE_FUTURE)) {
			// adjust-date-to-timezone is used to remove timestamp
			// "If $timezone is the empty sequence, returns an xs:date without a timezone." So you can write:
			// adjust-date-to-timezone(current-date(), ())"
			return "xs:date($" + getXFormsHelper().getXFormsObject(token.getQuestion()).getControlName() + ") "
					+ getOrbeonDatesOpposite(token.getType()).getOrbeonRepresentation()
					+ " adjust-date-to-timezone(current-date(), ()) - xs:" + xPathOperation + "('P" + token.getValue()
					+ token.getDatePeriodUnit().getAbbreviature() + "')";
		}
		return "xs:date($" + getXFormsHelper().getXFormsObject(token.getQuestion()).getControlName() + ") "
				+ token.getType().getOrbeonRepresentation() + " adjust-date-to-timezone(current-date(), ()) + xs:"
				+ xPathOperation + "('P" + token.getValue() + token.getDatePeriodUnit().getAbbreviature() + "')";
	}

	/**
	 * Translate between [string1, string2] to $question >= string1 and $question <= string2;
	 * 
	 * @param rule
	 * @return
	 * @throws StringRuleSyntaxError
	 */
	private String translateRangePostCode(TokenComparationValue token) throws PostCodeRuleSyntaxError {
		// Delete range token.
		String instruction = token.getValue();

		int comma = instruction.indexOf(",");
		if (comma < 0) {
			throw new PostCodeRuleSyntaxError("Postcode interval syntax error in rule '" + token.toString()
					+ "'. Please use correct syntax i.e. '" + RANGE_TOKEN + " ['0000aa', '9999zz']'.");
		}

		Pattern pattern = Pattern.compile("'([0-9]{4}[a-zA-Z]{2})'");
		Matcher matcher = pattern.matcher(instruction);

		if (!matcher.find()) {
			throw new PostCodeRuleSyntaxError(
					"Rule '"
							+ token.toString()
							+ "' isn't correctly defined. Please, check the post code format and that it is defined between quotes.");
		}

		String firstInterval = "";
		String secondInterval = "";
		try {
			firstInterval = matcher.group(1);
			matcher.find();
			secondInterval = matcher.group(1);
			if (firstInterval.length() == 0 || secondInterval.length() == 0) {
				throw new StringRuleSyntaxError(
						"Rule '"
								+ token.toString()
								+ "' isn't correctly defined. Please, check the post code format and that it is defined between quotes.");
			}
		} catch (Exception e) {
			throw new PostCodeRuleSyntaxError(
					"Rule '"
							+ token.toString()
							+ "' isn't correctly defined. Please, check the post code format and that it is defined between quotes.");
		}

		return " $" + getXFormsHelper().getXFormsObject(token.getQuestion()).getControlName() + " &gt;= '"
				+ firstInterval + "' and " + "$"
				+ getXFormsHelper().getXFormsObject(token.getQuestion()).getControlName() + " &lt;= '" + secondInterval
				+ "' ";
	}

	/**
	 * Obtains the relevant rule calculation obtained from a set of flows.
	 * 
	 * @param flows
	 * @return
	 * @throws InvalidDateException
	 */
	protected String getRelevantByFlows(Set<Flow> flows) throws InvalidDateException {
		// String visibility = getRelevantByFlows(flowsTo);
		String visibility = "";
		// Get all visibility rule as tokens.
		List<Token> visibilityAsToken = getRelevantByFlowsAsTokens(flows);
		if (visibilityAsToken.isEmpty()) {
			return "";
		}
		// Simplify the visibility expression
		List<Token> simplifiedVisibility;
		if (WebformsConfigurationReader.getInstance().isBooleanSimplificationEnabled()) {
			BooleanExpressionSimplifier simplifier = new BooleanExpressionSimplifier(visibilityAsToken);
			simplifiedVisibility = simplifier.getSimplified();
		} else {
			simplifiedVisibility = visibilityAsToken;
		}

		// Store for future reuse.
		getXFormsHelper().addVisibilityOfQuestionAsToken(getSource(), simplifiedVisibility);

		// Convert to String.
		String flowvisibility = "";
		// returns the condition or the 'others' rule.
		for (Token token : simplifiedVisibility) {
			String conditionVisibility = convertTokenToXForms(token);

			// 'not' rules need that source must select an answer.
			// conditionVisibility += othersSourceMustBeFilledUp(flow);

			flowvisibility += conditionVisibility.trim() + " ";
		}

		flowvisibility = flowvisibility.trim();
		if (flowvisibility.length() > 0) {
			visibility += "(" + flowvisibility.trim() + ")";
		}

		return visibility;
	}

	/**
	 * Includes previous question visibility.
	 * 
	 * @param flows
	 * @return
	 * @throws InvalidDateException
	 */
	protected List<Token> getRelevantByFlowsAsTokens(Set<Flow> flows) throws InvalidDateException {
		List<Token> visibility = new ArrayList<>();
		for (Flow flow : flows) {
			// Add previous visibility.
			List<Token> previousVisibility = new ArrayList<>();
			previousVisibility = getXFormsHelper().getVisibilityOfQuestionAsToken(flow.getOrigin());
			if (!flow.getCondition().isEmpty() && previousVisibility != null && previousVisibility.size() > 1) {
				previousVisibility.add(0, Token.leftPar());
				previousVisibility.add(Token.rigthPar());
			}

			List<Token> flowvisibility = flow.getCondition();

			if (flow.isOthers()) {
				// Dates does not need this extra instruction (string-length($date)>0 is always false).
				if (!(flow.getOrigin() instanceof Question) || ((Question) flow.getOrigin()).getAnswerFormat() == null
						|| !((Question) flow.getOrigin()).getAnswerFormat().equals(AnswerFormat.DATE)) {
					flowvisibility.add(Token.and());
					flowvisibility.add(new TokenAnswerNeeded((Question) flow.getOrigin()));
				}
			}

			if (!flowvisibility.isEmpty() || (previousVisibility != null && !previousVisibility.isEmpty())) {
				// Connector with previous rule if exists.
				if (!visibility.isEmpty()) {
					visibility.add(Token.or());
				}

				// Add visibility of previous element.
				visibility.add(Token.leftPar());
				if (previousVisibility != null && !previousVisibility.isEmpty()) {
					visibility.addAll(previousVisibility);
					if (!flowvisibility.isEmpty()) {
						visibility.add(Token.and());
					}
				}

				visibility.addAll(flowvisibility);
				visibility.add(Token.rigthPar());
			}
		}
		return visibility;
	}

	/**
	 * Returns the opposite value used in Orbeon. Note: <= the opposite is >=. The equals is mantained.
	 * 
	 * @return
	 */
	public TokenTypes getOrbeonDatesOpposite(TokenTypes type) {
		switch (type) {
		case GT:
			return TokenTypes.LT;
		case LT:
			return TokenTypes.GT;
		case GE:
			return TokenTypes.LE;
		case LE:
			return TokenTypes.GE;
		default:
			return type;
		}
	}
}
