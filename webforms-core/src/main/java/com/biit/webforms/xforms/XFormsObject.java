package com.biit.webforms.xforms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
		return "<alert/>";
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

	/**
	 * Returns a unique name.
	 * 
	 * @return
	 */
	protected String getControlName() {
		return getXFormsHelper().getUniqueName(getSource());
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

	protected void getRelevantStructure(StringBuilder relevant) throws InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		String flow = getAllFlowsVisibility();
		if (flow != null && flow.length() > 0) {
			getFlowRule(relevant);
			relevant.append(" relevant=\"").append(flow).append("\"");
			relevant.append(getCalculateStructure(flow));
		}
	}

	/**
	 * Simple flow rule representation for human reading.S
	 * 
	 * @return
	 */
	private void getFlowRule(StringBuilder flowRule) {
		Iterator<Flow> iterator = getFlowsTo().iterator();
		flowRule.append(" flowrule=\"");
		StringBuilder rule = new StringBuilder();
		while (iterator.hasNext()) {
			Flow flow = iterator.next();
			rule.append(flow.toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
			if (iterator.hasNext() && rule.length() > 0) {
				rule.append(" and ");
			}
		}
		flowRule.append(rule);
		flowRule.append("\"");
	}

	/**
	 * Calculate is used to clean up the value of the element if the previous element value is changed. Then all relvant
	 * rules of next elements are forced to recalculate.
	 * 
	 * @param flow
	 * @return
	 */
	protected abstract String getCalculateStructure(String flow);

	/**
	 * Gets the visibility of an element depending on the flow rules.
	 * 
	 * @return
	 * @throws InvalidDateException
	 * @throws StringRuleSyntaxError
	 * @throws PostCodeRuleSyntaxError
	 */
	protected String getAllFlowsVisibility() throws InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		// Load stored visibility if exists.
		if (getXFormsHelper().getVisibilityOfElement(getSource()) != null) {
			return getXFormsHelper().getVisibilityOfElement(getSource());
		}

		Set<Flow> flowsTo = getFlowsTo();
		String visibility = getRelevantByFlows(flowsTo);

		// Store calculated visibility as string
		getXFormsHelper().addVisibilityOfElement(getSource(), visibility);
		return visibility;
	}

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

	protected abstract void getBinding(StringBuilder binding) throws NotExistingDynamicFieldException,
			InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError;

	protected abstract void getSectionBody(StringBuilder body);

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
	protected void convertTokenToXForms(StringBuilder visibility, Token token) throws InvalidDateException {
		if (token instanceof TokenComparationAnswer) {
			// $control-name='answer'
			if (((TokenComparationAnswer) token).getQuestion().getAnswerType().equals(AnswerType.MULTIPLE_SELECTION)) {
				getMultiCheckBoxVisibility(visibility, (TokenComparationAnswer) token);
			} else {
				getBasicSelectionVisibility(visibility, (TokenComparationAnswer) token);
			}
		} else if (token instanceof TokenComparationValue) {
			// $control-name=1
			getInputFieldVisibility(visibility, (TokenComparationValue) token);
		} else if (token instanceof TokenAnswerNeeded) {
			if (((TokenAnswerNeeded) token).isDateField()) {
				visibility
						.append("string-length(format-date($")
						.append(getXFormsHelper().getXFormsObject(((TokenAnswerNeeded) token).getQuestion())
								.getControlName()).append(", '[MNn,*-3]/[D01]/[Y]')) &gt; 0");
			} else {
				visibility
						.append("string-length($")
						.append(getXFormsHelper().getXFormsObject(((TokenAnswerNeeded) token).getQuestion())
								.getControlName()).append(") &gt; 0");
			}
		} else {
			// An operator 'and', 'or', ...
			visibility.append(token.getType().getOrbeonRepresentation());
		}
	}

	/**
	 * Checkbox answer is returned as an unique string with all selected options divided by spaces. We need 'contains'
	 * XForms method to know if the answer is selected or not. But a 'contains' can fail if an element is a substring of
	 * other element, then we add space to delimiter substrings.
	 * 
	 * @param token
	 * @return
	 */
	private void getMultiCheckBoxVisibility(StringBuilder visibility, TokenComparationAnswer token) {
		visibility.append("contains(concat($")
				.append(getXFormsHelper().getXFormsObject(token.getQuestion()).getControlName())
				.append(", ' '), concat('").append(token.getAnswer().getLabel()).append("', ' '))");
	}

	/**
	 * Visibility for a token that is a component that allows to select an answer to the user. Selectable elements that
	 * are not a multicheckbox use standard $control-name='answer' string.
	 * 
	 * @param token
	 * @return
	 */
	private void getBasicSelectionVisibility(StringBuilder visibility, TokenComparationAnswer token) {
		visibility.append("$").append(
				getXFormsHelper().getXFormsObject(((TokenComparationAnswer) token).getQuestion()).getControlName());
		visibility.append(token.getType().getOrbeonRepresentation());
		visibility.append("'").append(((TokenComparationAnswer) token).getAnswer()).append("'");
	}

	/**
	 * Translate token of an input field to orbeon relevant rules.
	 * 
	 * @param token
	 * @return
	 * @throws ParseException
	 * @throws PostCodeRuleSyntaxError
	 */
	private void getInputFieldVisibility(StringBuilder visibility, TokenComparationValue token)
			throws InvalidDateException {
		if (token.getQuestion().getAnswerFormat() != null) {
			switch (token.getQuestion().getAnswerFormat()) {
			case NUMBER:
				visibility.append("number($")
						.append(getXFormsHelper().getXFormsObject(token.getQuestion()).getControlName()).append(")");
				visibility.append(" ").append(token.getType().getOrbeonRepresentation()).append(" ");
				visibility.append(token.getValue());
				break;
			case TEXT:
			case POSTAL_CODE:
				visibility.append("$").append(getXFormsHelper().getXFormsObject(token.getQuestion()).getControlName());
				visibility.append(" ").append(token.getType().getOrbeonRepresentation());
				visibility.append(" '").append(token.getValue()).append("'");
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
						visibility.append("xs:date($")
								.append(getXFormsHelper().getXFormsObject(token.getQuestion()).getControlName())
								.append(") ");
						visibility.append(token.getType().getOrbeonRepresentation());
						visibility.append(" xs:date('").append(formatter.format(date)).append("')");
					} catch (ParseException e) {
						throw new InvalidDateException(e.getMessage());
					}
					break;
				case DATE_PERIOD:
					translateDatePeriod(visibility, token);
					break;
				default:
					break;
				}
				break;
			}
		}
	}

	private void translateDatePeriod(StringBuilder visibility, TokenComparationValue token) {
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
			visibility.append("xs:date($")
					.append(getXFormsHelper().getXFormsObject(token.getQuestion()).getControlName()).append(") ");
			visibility.append(getOrbeonDatesOpposite(token.getType()).getOrbeonRepresentation());
			visibility.append(" adjust-date-to-timezone(current-date(), ()) - xs:").append(xPathOperation)
					.append("('P").append(token.getValue()).append(token.getDatePeriodUnit().getAbbreviature())
					.append("')");
		} else {
			visibility.append("xs:date($")
					.append(getXFormsHelper().getXFormsObject(token.getQuestion()).getControlName()).append(") ");
			visibility.append(token.getType().getOrbeonRepresentation()).append(
					" adjust-date-to-timezone(current-date(), ()) + xs:");
			visibility.append(xPathOperation).append("('P").append(token.getValue())
					.append(token.getDatePeriodUnit().getAbbreviature()).append("')");
		}
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
		StringBuilder visibility = new StringBuilder();

		// One flow with condition, is same visibility as previous element.
		if (flows.size() == 1) {
			if (((Flow) flows.iterator().next()).getCondition().isEmpty()) {
				String previousElementVisibility = getXFormsHelper().getVisibilityOfElement(getSource());
				if (previousElementVisibility != null) {
					return previousElementVisibility;
				}
			}
		}

		// More than one flow.
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
		StringBuilder flowvisibility = new StringBuilder();
		// returns the condition or the 'others' rule.
		for (Token token : simplifiedVisibility) {
			convertTokenToXForms(flowvisibility, token);
			flowvisibility.append(" ");
		}

		if (flowvisibility.length() > 0) {
			visibility.append("(").append(flowvisibility).append(")");
		}

		return visibility.toString();
	}

	/**
	 * Obtain all flows of an element as tokens.
	 * 
	 * @param flows
	 * @return
	 * @throws InvalidDateException
	 */
	protected List<Token> getRelevantByFlowsAsTokens(Set<Flow> flows) throws InvalidDateException {
		List<Token> visibility = new ArrayList<>();

		for (Flow flow : flows) {
			List<Token> flowvisibility = flow.getConditionSimpleTokens();

			// Others must assure that the question is answered.
			if (flow.isOthers()) {
				if (existPreviousCondition(flowvisibility)) {
					flowvisibility.add(Token.and());
				}
				flowvisibility.add(new TokenAnswerNeeded((Question) flow.getOrigin(), ((Question) flow.getOrigin())
						.getAnswerFormat() != null
						&& ((Question) flow.getOrigin()).getAnswerFormat().equals(AnswerFormat.DATE)));
				// If condition is empty, inherit the relevance of the previous element. Others also has empty
				// condition.
			} else if (flow.getCondition().isEmpty()) {
				List<Token> previousVisibility = getXFormsHelper().getPreviousVisibilityTokens(flow);
				if (!previousVisibility.isEmpty()) {
					// Add 'AND'.
					if (existPreviousCondition(flowvisibility)) {
						flowvisibility.add(Token.and());
					}

					flowvisibility.addAll(previousVisibility);
				}
			} else {
				// Some rules must pass through a specific question despite condition is from previous question. We can
				// detect if origin of the flow is not in the condition.
				boolean originUsedInCondition = false;
				for (Token token : flowvisibility) {
					if (token instanceof TokenComparationValue) {
						if (((TokenComparationValue) token).getQuestion().equals(flow.getOrigin())) {
							originUsedInCondition = true;
						}
					} else if (token instanceof TokenComparationAnswer) {
						if (((TokenComparationAnswer) token).getQuestion().equals(flow.getOrigin())) {
							originUsedInCondition = true;
						}
					}
				}
				// If is not used in condition, add as needed.
				if (!originUsedInCondition) {
					if (existPreviousCondition(flowvisibility)) {
						flowvisibility.add(Token.and());
					}
					flowvisibility.add(new TokenAnswerNeeded((Question) flow.getOrigin(), ((Question) flow.getOrigin())
							.getAnswerFormat() != null
							&& ((Question) flow.getOrigin()).getAnswerFormat().equals(AnswerFormat.DATE)));
				}
			}

			// Concat rule to relevant rules.
			if (existPreviousCondition(flowvisibility)) {
				// Connector with previous rule if exists.
				if (!visibility.isEmpty()) {
					visibility.add(Token.or());
				}

				// Add visibility of previous element.
				visibility.add(Token.leftPar());
				visibility.addAll(flowvisibility);
				visibility.add(Token.rigthPar());
			}
		}
		return visibility;
	}

	/**
	 * Detects if exist a previous condition in a flow chain.
	 * 
	 * @param flowChain
	 * @return
	 */
	private boolean existPreviousCondition(List<Token> flowChain) {
		for (Token token : flowChain) {
			if (token instanceof TokenComparationValue || token instanceof TokenComparationAnswer) {
				return true;
			}
		}
		return false;
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

	/**
	 * Get all flows that points to this element.
	 * 
	 * @return
	 */
	public abstract Set<Flow> getFlowsTo();

}
