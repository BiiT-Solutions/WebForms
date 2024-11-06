package com.biit.webforms.exporters.xforms;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.BaseRepeatableGroup;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.condition.TokenUtils;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.exporters.xforms.exceptions.InvalidDateException;
import com.biit.webforms.exporters.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.exporters.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.exporters.xforms.exceptions.StringRuleSyntaxError;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.DynamicAnswer;
import com.biit.webforms.persistence.entity.ElementWithMedia;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.persistence.entity.condition.TokenWithQuestion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public abstract class XFormsObject<T extends TreeObject> {
	protected static final String RANGE_TOKEN = "between";
	protected static final String XPATH_DATE_FORMAT = "yyyy-MM-dd";
	private static final String DATE_FORMAT = "[Y]-[M01]-[D01]";
	private static final String CSS_CLASS = "webforms-element";

	private T source;
	private XFormsHelper xFormsHelper;

	private List<XFormsObject<? extends TreeObject>> children;

	private XFormsObject<? extends TreeObject> parent;

	public XFormsObject(XFormsHelper xFormsHelper, T treeObject) throws NotValidTreeObjectException, NotValidChildException {
		setSource(treeObject);
		this.xFormsHelper = xFormsHelper;
		children = new ArrayList<>();
		for (TreeObject child : treeObject.getAllNotHiddenChildren()) {
			if (!child.isHiddenElement()) {
				addChild(child);
			}
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
				xFormsHelper.addXFormsObject(newChild);
			} else if (child instanceof Group) {
				if (((Group) child).isRepeatable()) {
					newChild = new XFormsRepeatableGroup(xFormsHelper, (Group) child);
				} else {
					newChild = new XFormsGroup(xFormsHelper, (Group) child);
				}
				xFormsHelper.addXFormsObject(newChild);
			} else if (child instanceof BaseQuestion) {
				if (child instanceof Text) {
					newChild = new XFormsText(xFormsHelper, (Text) child);
				} else if (child instanceof SystemField) {
					newChild = new XFormsSystemField(xFormsHelper, (SystemField) child);
				} else {
					// Add webservice validation field if applies.
					if (hasWebserviceValidation((BaseQuestion) child)) {
						addWebserviceValidationField((BaseQuestion) child);
					}

					newChild = new XFormsQuestion(xFormsHelper, (BaseQuestion) child);
				}
				xFormsHelper.addXFormsQuestion((XFormsQuestion) newChild);
			} else if (child instanceof Answer) {
				newChild = new XFormsAnswer(xFormsHelper, (Answer) child);
				xFormsHelper.addXFormsObject(newChild);
			} else if (child instanceof DynamicAnswer) {
				newChild = new XFormsDynamicAnswer(xFormsHelper, (DynamicAnswer) child);
				xFormsHelper.addXFormsObject(newChild);
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

	private void addWebserviceValidationField(BaseQuestion child) throws NotValidTreeObjectException, NotValidChildException {
		// Webservice validation
		XFormsWebserviceValidationField webformsValidation = xFormsHelper.getWebserviceValidationField(child);
		webformsValidation.setParent(this);
		children.add(webformsValidation);
	}

	private boolean hasWebserviceValidation(BaseQuestion child) {
		return xFormsHelper.getWebserviceCallInputLinks(child) != null && !xFormsHelper.getWebserviceCallInputLinks(child).isEmpty();
	}

	protected T getSource() {
		return source;
	}

	protected String getAlert(OrbeonLanguage language) {
		return "<alert/>";
	}

	protected String getBindingId() {
		return getUniqueName() + "-bind";
	}

	protected String getBindingName() {
		return getUniqueName();
	}

	protected String getXPath() {
		if (getParent() != null) {
			if (getParent() instanceof XFormsRepeatableGroup) {
				// Repeatable groups are composed by a section + iterator. We
				// need both elements in the XPath.
				return getParent().getXPath() + "/" + ((XFormsRepeatableGroup) getParent()).getIteratorControlName() + "/" + getName();
			} else {
				return getParent().getXPath() + "/" + getName();
			}
		}
		return "/form/" + getName();
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
	 * ¡
	 * @return¡
	 */
	protected String getBodyStructure(String structure, boolean html) {
		String text = "<xf:" + structure + " ref=\"instance('fr-form-resources')/resource/" + getPath() + "/" + structure + "\"";
		if (html) {
			text += " mediatype=\"text/html\" ";
		}
		text += " />";
		return text;
	}

	protected String getAlert(Integer position, String validation) {
		String text = "<xf:alert ref=\"instance('fr-form-resources')/resource/" + getPath() + "/alert";
		if (position != null) {
			text += "[" + position + "]";
		}
		text += "\"";

		if (validation != null) {
			text += " validation=\"" + validation + "\"";
		}
		text += "/>";

		return text;
	}

	/**
	 * Return the complete path of the element.
	 * 
	 * @return
	 */
	protected String getPath() {
		if (getParent() != null) {
			return getParent().getPath() + "/" + getName();
		}
		return getName();
	}

	protected List<XFormsObject<? extends TreeObject>> getChildren() {
		return children;
	}

	/**
	 * Returns the name of the element.
	 * 
	 * @return
	 */
	protected String getName() {
		return getSource().getName();
	}

	protected String getUniqueName() {
		return getXFormsHelper().getUniqueName(getSource());
	}

	protected String getHelp(OrbeonLanguage language) {
		// Avoid empty help windows.
		return "";
	}

	protected String getLabel(OrbeonLanguage language) {
		return "<label><![CDATA[" + getSource().getLabel() + "]]></label>";
	}

	protected String getHint(OrbeonLanguage language) {
		return "<hint/>";
	}

	protected void getRelevantStructure(StringBuilder relevant) throws InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		String flow = getAllFlowsVisibility();
		if (flow != null && flow.length() > 0) {
			getFlowRule(relevant);
			relevant.append(" relevant=\"").append(flow).append("\"");
			// Calculate now is handle by events.
			// relevant.append(getCalculateStructure(flow));
		}
	}

	protected String getRelevantStructure() throws InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		StringBuilder builder = new StringBuilder();
		getRelevantStructure(builder);
		return builder.toString();
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
	 * Calculate is used to clean up the value of the element if the previous
	 * element value is changed. Then all relvant rules of next elements are
	 * forced to recalculate.
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
	protected String getAllFlowsVisibility() throws InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
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
	 * Is the default visibility of an element. For Categories is the visibility
	 * of the first question, for questions, is the visibility of the previous
	 * question and for the first question is always true.
	 * 
	 * @return
	 * @throws InvalidDateException
	 * @throws StringRuleSyntaxError
	 * @throws PostCodeRuleSyntaxError
	 */
	protected abstract String getDefaultVisibility() throws InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError;

	protected String getResources(OrbeonLanguage language) throws NotExistingDynamicFieldException {
		StringBuilder resource = new StringBuilder();

		// Add element's image.
		if (getXFormsHelper().isImagesEnabled() && getSource() instanceof ElementWithMedia && ((ElementWithMedia) getSource()).getImage() != null) {
			resource.append(XFormsImage.getResources(((ElementWithMedia) getSource()).getImage(), language, getXFormsHelper()));
		}

		resource.append("<" + getName() + ">");
		resource.append(getLabel(language));
		resource.append(getHint(language));
		resource.append(getAlert(language));
		resource.append(getHelp(language));

		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			resource.append(child.getResources(language));
		}

		resource.append("</" + getName() + ">");
		return resource.toString();
	}

	protected String getSectionControlName() {
		return getUniqueName() + "-control";
	}

	protected abstract void getBinding(StringBuilder binding) throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError;

	protected abstract void getSectionBody(StringBuilder body);

	protected String getDefinition() {
		StringBuilder section = new StringBuilder();

		// Add element's image
		if (getXFormsHelper().isImagesEnabled() && getSource() instanceof ElementWithMedia && ((ElementWithMedia) getSource()).getImage() != null) {
			section.append(XFormsImage.getDefinition(((ElementWithMedia) getSource()).getImage(), getXFormsHelper(),
					(Form) getSource().getAncestor(Form.class), getXFormsHelper().getOrganization(), getXFormsHelper().isPreviewMode()));
		}

		section.append("<" + getName() + ">");
		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			section.append(child.getDefinition());
		}
		section.append("</" + getName() + ">");
		return section.toString();
	}

	/**
	 * Used for storing values of events related to visibility.
	 * 
	 * @return
	 */
	protected abstract String getVisibilityStructure();

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
	 * Returns a linkedHashSet with all the children in the current hierarchy
	 * that are instance of the filter type in appearance order.
	 * 
	 * @param filter
	 * @return
	 */
	public LinkedHashSet<XFormsObject<? extends TreeObject>> getAllChildrenInHierarchy(Class<? extends XFormsObject<? extends TreeObject>> filter) {
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
			// Check the event.
			visibility.append("instance('visible')/" + getXFormsHelper().getUniqueName(((TokenAnswerNeeded) token).getQuestion()) + " != 'false'");
		} else if (token instanceof TokenOthersMustBeAnswered) {
			// Others ensure that question is answered with a string-length() or
			// checks that is not visible with the
			// event.
			visibility.append("(string-length(").append(getXPath(((TokenOthersMustBeAnswered) token).getQuestion())).append("/text()) &gt; 0 ");
			if (!isInRepeatableGroup(((TokenOthersMustBeAnswered) token).getQuestion())) {
				// Checks that is not visible.
				visibility
						.append("or instance('visible')/" + getXFormsHelper().getUniqueName(((TokenOthersMustBeAnswered) token).getQuestion()) + " = 'false'");
			} else {
				// Repeatable groups cannot use events. Copy relevant rule.
				visibility.append(" or not(" + getXFormsHelper().getVisibilityOfElement(((TokenOthersMustBeAnswered) token).getQuestion()) + ")");
			}
			visibility.append(")");
		} else if (token instanceof TokenInheritRelevant) {
			// Uses same visibility that this element.
			visibility.append("instance('visible')/" + getXFormsHelper().getUniqueName(((TokenInheritRelevant) token).getInheritedQuestion()) + " != 'false'");
		} else {
			// An operator 'and', 'or', ...
			visibility.append(token.getType().getOrbeonRepresentation());
		}
	}

	/**
	 * Returns XPath expression except for repeatable groups that cause troubles
	 * in Orbeon. In this case the Orbeon variable is returned.
	 * 
	 * @param element
	 * @return
	 */
	private String getXPath(BaseQuestion element) {
		if (isInRepeatableGroup(element)) {
			return "$" + element.getName();
		} else {
			return getXFormsHelper().getXFormsObject(element).getXPath();
		}
	}

	/**
	 * Checkbox answer is returned as an unique string with all selected options
	 * divided by spaces. We need 'contains' XForms method to know if the answer
	 * is selected or not. But a 'contains' can fail if an element is a
	 * substring of other element, then we add space to delimiter substrings.
	 * 
	 * @param token
	 * @return
	 */
	private void getMultiCheckBoxVisibility(StringBuilder visibility, TokenComparationAnswer token) {
		// Not equals is translated as a not.
		if (token.getType().equals(TokenTypes.NE)) {
			// Not Equals is true if the answer is empty! Avoid it.
			if (token.getQuestion().isMandatory()) {
				visibility.append("(string-length(" + getXPath(((TokenComparationAnswer) token).getQuestion()) + "/text()) &gt; 0 and ");
			}
			visibility.append("not(");
		}
		visibility.append("contains(concat(").append(getXPath(token.getQuestion())).append(", ' '), concat('").append(token.getAnswer().getName())
				.append("', ' '))");
		if (token.getType().equals(TokenTypes.NE)) {
			visibility.append(")");
			if (token.getQuestion().isMandatory()) {
				visibility.append(")");
			}
		}
	}

	/**
	 * Visibility for a token that is a component that allows to select an
	 * answer to the user. Selectable elements that are not a multicheckbox use
	 * standard $control-name='answer' string.
	 * 
	 * @param token
	 * @return
	 */
	private void getBasicSelectionVisibility(StringBuilder visibility, TokenComparationAnswer token) {
		// Not Equals is true if the answer is empty! Avoid it.
		if (token.getQuestion().isMandatory() && token.getType().equals(TokenTypes.NE)) {
			visibility.append("(string-length(" + getXPath(((TokenComparationAnswer) token).getQuestion()) + "/text()) &gt; 0 and ");
		}
		visibility.append(getXPath(((TokenComparationAnswer) token).getQuestion()));
		visibility.append(token.getType().getOrbeonRepresentation());
		visibility.append("'").append(((TokenComparationAnswer) token).getAnswer()).append("'");
		if (token.getQuestion().isMandatory() && token.getType().equals(TokenTypes.NE)) {
			visibility.append(")");
		}
	}

	/**
	 * Translate token of an input field to orbeon relevant rules.
	 * 
	 * @param token
	 * @return
	 * @throws ParseException
	 * @throws PostCodeRuleSyntaxError
	 */
	private void getInputFieldVisibility(StringBuilder visibility, TokenComparationValue token) throws InvalidDateException {
		if (token.getQuestion().getAnswerFormat() != null) {
			switch (token.getQuestion().getAnswerFormat()) {
			case NUMBER:
				visibility.append("number(").append(getXPath(token.getQuestion())).append(")");
				visibility.append(" ").append(token.getType().getOrbeonRepresentation()).append(" ");
				visibility.append(token.getValue());
				break;
			case TEXT:
			case POSTAL_CODE:
				visibility.append(getXPath(token.getQuestion()));
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
						// Compare it as string. Less problems with null dates.
						// $dateField/text() ge '2015-05-27'
						formatter.applyPattern(XPATH_DATE_FORMAT);
						visibility.append(getXPath(token.getQuestion())).append("/text() ");
						visibility.append(token.getType().getOrbeonRepresentation());
						visibility.append(" '").append(formatter.format(date)).append("'");
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
			// "If $timezone is the empty sequence, returns an xs:date without a
			// timezone." So you can write:
			// adjust-date-to-timezone(current-date(), ())"
			visibility.append(getXPath(token.getQuestion())).append("/text() ");
			visibility.append(getOrbeonDatesOpposite(token.getType()).getOrbeonRepresentation());
			visibility.append(" format-date(adjust-date-to-timezone(current-date(), ()) - xs:").append(xPathOperation).append("('P").append(token.getValue())
					.append(token.getDatePeriodUnit().getAbbreviature()).append("'), '" + DATE_FORMAT + "')");
		} else {
			visibility.append(getXPath(token.getQuestion())).append("/text() ");
			visibility.append(token.getType().getOrbeonRepresentation()).append(" format-date(adjust-date-to-timezone(current-date(), ()) + xs:");
			visibility.append(xPathOperation).append("('P").append(token.getValue()).append(token.getDatePeriodUnit().getAbbreviature())
					.append("'), '" + DATE_FORMAT + "')");
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

		// Get all visibility rule as tokens.
		List<Token> visibilityAsToken = getRelevantByFlowsAsTokens(flows);
		if (visibilityAsToken.isEmpty()) {
			// Store for future reuse.
			getXFormsHelper().addVisibilityOfQuestionAsToken(getSource(), visibilityAsToken);
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

		visibility.append(flowvisibility);

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

			// Others must assure that the question is answered if it is
			// mandatory. Otherwise without answering the
			// question the others is always true.
			if (flow.isOthers()) {
				List<Token> othersVisibility = new ArrayList<>();
				// Others needs that all the conditions are answered if
				// mandatory.
				// Only must check one time by flow.
				Set<WebformsBaseQuestion> alreadyCheckedQuestions = new HashSet<>();
				for (Token token : flowvisibility) {
					if (token instanceof TokenWithQuestion) {
						// Condition must be answered if mandatory
						if ((((TokenWithQuestion) token).getQuestion()).isMandatory()
								&& !alreadyCheckedQuestions.contains(((TokenWithQuestion) token).getQuestion())) {
							othersVisibility.add(new TokenOthersMustBeAnswered(((TokenWithQuestion) token).getQuestion()));
							alreadyCheckedQuestions.add(((TokenWithQuestion) token).getQuestion());
						} else {
							// No token added: remove previous AND or OR.
							if (othersVisibility.size() > 1 && TokenUtils.isLogicalOperator(othersVisibility.get(othersVisibility.size() - 1))) {
								othersVisibility.remove(othersVisibility.size() - 1);
							}
						}
					} else if (TokenUtils.isLogicalOperator(token)) {
						// If are multiple conditions, we need to add also the
						// and/or conjunction
						othersVisibility.add(token.generateCopy());
					}
				}
				// Add parenthesis to new tokens created.
				if (TokenUtils.needsEnclosingParenthesis(othersVisibility)) {
					othersVisibility.add(0, Token.getLeftParenthesisToken());
					othersVisibility.add(Token.getRigthParenthesisToken());
				}

				// Add parenthesis to all predicate if needed.
				if (existPreviousCondition(flowvisibility) && !othersVisibility.isEmpty()) {
					flowvisibility.add(0, Token.getLeftParenthesisToken());
					othersVisibility.add(0, Token.getAndToken());
					othersVisibility.add(Token.getRigthParenthesisToken());
				}
				flowvisibility.addAll(othersVisibility);

				// Ensure origin is visible.
				List<Token> previousVisibility = getPreviousVisibility(flow.getOrigin());
				if (previousVisibility != null && !previousVisibility.isEmpty()) {
					flowvisibility.add(0, Token.getLeftParenthesisToken());
					flowvisibility.add(Token.getAndToken());
					flowvisibility.addAll(previousVisibility);
					flowvisibility.add(Token.getRigthParenthesisToken());
				}

				// If condition is empty, inherit the relevance of the previous
				// element. Others also has empty
				// condition.
			} else if (flow.getComputedCondition().isEmpty()) {
				// Get previous visibility.
				List<Token> previousVisibility = getPreviousVisibility(flow.getOrigin());

				if (!previousVisibility.isEmpty()) {
					// Add 'AND'.
					if (existPreviousCondition(flowvisibility)) {
						// Add parenthesis.
						if (TokenUtils.needsEnclosingParenthesis(flowvisibility)) {
							flowvisibility.add(0, Token.getLeftParenthesisToken());
							flowvisibility.add(Token.getRigthParenthesisToken());
						}

						flowvisibility.add(Token.getAndToken());
					}

					// Add parenthesis if needed. Needed only if does not starts
					// with a parenthesis or starts with one
					// and the closing one is not the last one.
					boolean addParenthesis = TokenUtils.needsEnclosingParenthesis(previousVisibility);
					if (addParenthesis) {
						flowvisibility.add(Token.getLeftParenthesisToken());
					}
					flowvisibility.addAll(previousVisibility);
					if (addParenthesis) {
						flowvisibility.add(Token.getRigthParenthesisToken());
					}
				}
			} else {
				// Some rules must pass through a specific question despite
				// condition is from previous question. We can
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
				if (flow.getOrigin() instanceof WebformsBaseQuestion) {
					if (!originUsedInCondition) {
						if (existPreviousCondition(flowvisibility)) {
							// Add parenthesis.
							if (TokenUtils.needsEnclosingParenthesis(flowvisibility)) {
								flowvisibility.add(0, Token.getLeftParenthesisToken());
								flowvisibility.add(Token.getRigthParenthesisToken());
							}
							flowvisibility.add(Token.getAndToken());
						}
						flowvisibility.add(new TokenAnswerNeeded(flow.getOrigin(), (flow.getOrigin() instanceof Question)
								&& ((Question) flow.getOrigin()).getAnswerFormat() != null
								&& ((Question) flow.getOrigin()).getAnswerFormat().equals(AnswerFormat.DATE)));
					}
				}
			}

			// Concatenate rule to relevant rules.
			if (existPreviousCondition(flowvisibility)) {
				// Connector with previous rule if exists.
				if (!visibility.isEmpty()) {
					visibility.add(Token.getOrToken());
				}

				// Add visibility of previous element.
				boolean addParenthesis = TokenUtils.needsEnclosingParenthesis(flowvisibility) && !visibility.isEmpty();
				if (addParenthesis) {
					visibility.add(Token.getLeftParenthesisToken());
				}
				visibility.addAll(flowvisibility);
				if (addParenthesis) {
					visibility.add(Token.getRigthParenthesisToken());
				}
			}
		}
		return visibility;
	}

	/**
	 * Obtains the previous element visibility. Can be an event if the previous
	 * element is a question, a copy of the relevant rule if the previous one is
	 * a system field or if the element is inside a repeatable group.
	 * 
	 * @param flow
	 * @return
	 */
	private List<Token> getPreviousVisibility(BaseQuestion element) {
		// If it is first element.
		if (getXFormsHelper().getFlowsWithDestiny(element).isEmpty()) {
			// No visibility rules defined.
			return new ArrayList<>();
		} else {
			// Not first element, inherited relevant rule. Inherit must skip all
			// hidden elements by default, as system
			// fields.
			BaseQuestion origin = element;
			while (isAlwaysHiddenElement(origin)) {
				Set<Flow> previousFlow = getXFormsHelper().getFlowsWithDestiny(origin);
				if (previousFlow.size() > 1) {
					// If the system field has a complex flow, we cannot use
					// events but inherit the relevant
					// rule.
					break;
				} else {
					// Get previous element by flow.
					try {
						origin = previousFlow.iterator().next().getOrigin();
					} catch (NoSuchElementException nse) {
						// System Field is the first element of the form. Always
						// visible next element.
						return new ArrayList<Token>();
					}
				}
			}
			if (!isAlwaysHiddenElement(origin)) {
				if (!isInRepeatableGroup(origin)) {
					// Event visibility from previous element
					List<Token> previousVisibility = new ArrayList<>();
					previousVisibility.add(new TokenInheritRelevant(origin));
					return previousVisibility;
				} else {
					return getXFormsHelper().getVisibilityOfQuestionAsToken(origin);
				}
			} else {
				// Copy relevant rule. Due to visibility of the previous element
				// is always false.
				return getXFormsHelper().getVisibilityOfQuestionAsToken(origin);
			}
		}
	}

	/**
	 * Orbeon has some limitations in elements inside a repeatable group. We
	 * need to detect them.
	 * 
	 * @param element
	 * @return
	 */
	private boolean isInRepeatableGroup(BaseQuestion element) {
		List<TreeObject> parentGroups = element.getAncestors(BaseRepeatableGroup.class, false);
		for (TreeObject group : parentGroups) {
			if (((BaseRepeatableGroup) group).isRepeatable()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Some extra fields are always hidden and must skip standard relevant
	 * rules.
	 * 
	 * @param element
	 * @return
	 */
	private boolean isAlwaysHiddenElement(BaseQuestion element) {
		if (element == null) {
			return false;
		}
		return (element instanceof SystemField);
	}

	/**
	 * Detects if exist a previous condition in a flow chain.
	 * 
	 * @param flowChain
	 * @return
	 */
	private boolean existPreviousCondition(List<Token> flowChain) {
		for (Token token : flowChain) {
			// Skip any parenthesis, look for real rules.
			if (token instanceof TokenComparationValue || token instanceof TokenComparationAnswer || token instanceof TokenInheritRelevant) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the opposite value used in Orbeon. Note: &lt;= the opposite is &gt;=.
	 * The equals are maintained.
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

	protected String getCssClass() {
		return CSS_CLASS;
	}

}
