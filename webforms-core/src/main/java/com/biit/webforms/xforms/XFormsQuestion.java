package com.biit.webforms.xforms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import com.biit.webforms.persistence.entity.DynamicAnswer;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOriginException;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSourceException;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputErrors;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallOutputLink;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

public class XFormsQuestion extends XFormsObject<BaseQuestion> {
	private final static int MAX_YEARS_BIRTHDAY = 120;
	private final static String CSS_CLASS_RADIO_BUTTON_HORIZONTAL = "fr-radio-horizontal";
	private static final String CSS_CLASS_QUESTION = "webforms-question";
	private static final String CSS_CLASS_QUESTION_HELP = "webforms-help";

	public XFormsQuestion(XFormsHelper xFormsHelper, BaseQuestion question) throws NotValidTreeObjectException, NotValidChildException {
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
	public void getBinding(StringBuilder binding) throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		binding.append("<xf:bind id=\"").append(getBindingId()).append("\"  name=\"").append(getBindingName()).append("\" ");
		// Reference must be always to a name and not to a complete xpath, if
		// the xpath is used, in a loop all repeated
		// questions would always have the same answers selected.
		binding.append("ref=\"").append(getName()).append("\" ");
		getXFormsType(binding);
		isMandatory(binding);
		isReadOnly(binding);
		getRelevantStructure(binding);
		if (!hasConstrainsts()) {
			binding.append(" />");
		} else {
			binding.append(" >");
			getConstraints(binding);
			binding.append("</xf:bind>");
		}
	}

	private boolean hasConstrainsts() {
		return hasSubtypeConstraint() || hasWebserviceValidation();
	}

	private boolean hasWebserviceValidation() {
		Set<WebserviceCallInputLink> inputLinks = getXFormsHelper().getWebserviceCallInputLinks(getSource());
		if (inputLinks != null) {
			for (WebserviceCallInputLink inputLink : inputLinks) {
				if (!inputLink.getValidErrors().isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns constraint
	 * 
	 * @return
	 */
	private boolean hasSubtypeConstraint() {
		if(!(getSource() instanceof Question) || ((Question) getSource()).getAnswerSubformat() == null){
			return false;
		}

		switch (((Question) getSource()).getAnswerSubformat()) {
		case PHONE:
		case POSTAL_CODE:
		case BSN:
		case IBAN:
		case DATE_FUTURE:
		case DATE_PAST:
		case DATE_BIRTHDAY:
			return true;
		case DATE_PERIOD:
			// This type will never arrive here -no constraint-
			return false;
		case TEXT:
		case DATE:
		case EMAIL:
		case NUMBER:
		case FLOAT:
			// No constraint, type handles restriction.
			return false;
		case AMOUNT:
			// Not used type
			return false;
		}
		return false;
	}

	private void isReadOnly(StringBuilder binding) {
		WebserviceCallOutputLink link = getXFormsHelper().getWebserviceCallOutputLink(getSource());
		if (link != null && !link.isEditable()) {
			binding.append("readonly=\"true()\" ");
		} else {
			binding.append("readonly=\"false()\" ");
		}
	}

	protected void isMandatory(StringBuilder binding) {
		if (((Question) getSource()).isMandatory()) {
			binding.append(" required=\"true()\" ");
		}
	}

	/**
	 * Get all the constraints.
	 * 
	 * @return
	 */
	protected void getConstraints(StringBuilder constraints) {
		if (hasSubtypeConstraint()) {
			getTypeConstraints(constraints);
		}
		if (hasWebserviceValidation()) {
			getWebserviceConstraints(constraints);
		}
	}

	private List<WebserviceCallInputErrors> getWebserviceCallInputErrors() {
		List<WebserviceCallInputErrors> callInputErrors = new ArrayList<>();
		Set<WebserviceCallInputLink> inputLinks = getXFormsHelper().getWebserviceCallInputLinks(getSource());

		for (WebserviceCallInputLink inputLink : inputLinks) {
			callInputErrors.addAll(inputLink.getValidErrors());
		}
		Collections.sort(callInputErrors);
		
		return callInputErrors;
	}

	private void getWebserviceConstraints(StringBuilder constraints) {
		List<WebserviceCallInputErrors> webserviceValidations = getWebserviceCallInputErrors();

		for (int i = 0; i < webserviceValidations.size(); i++) {
			String webserviceValidatorInstance = getWebserviceValidatorInstance(webserviceValidations.get(i).getWebserviceCallInput());
			constraints.append("<xf:constraint id=\"webservice-constraint-"+ getXFormsHelper().getUniqueName(getSource()) +"-" + i + "-validation\" ");
			constraints.append("value=\"instance('" + webserviceValidatorInstance + "')"+webserviceValidations.get(i).getWebserviceCallInput().getValidationXpath()+" != '");
			constraints.append(webserviceValidations.get(i).getErrorCode());
			constraints.append("'\"/>");
		}
	}

	private String getWebserviceValidatorInstance(WebserviceCallInputLink webserviceCallInputLink) {
		return webserviceCallInputLink.getWebserviceCall().getName() + "-" + webserviceCallInputLink.getWebservicePort() + "-instance";
	}

	private void getTypeConstraints(StringBuilder constraints) {
		if (((Question) getSource()).getAnswerSubformat() == null) {
			// Avoid corrupt data.
			return;
		}

		// We define constraint as subtype-constraint-[unique name]
		constraints.append("<xf:constraint id=\"subtype-constraint-" + getXFormsHelper().getUniqueName(getSource()) + "-validation\" ");
		constraints.append("value=\"");
		// Add condition depending on answer subformat.

		// Aditional notes:
		// adjust-date-to-timezone is used to remove timestamp
		// "If $timezone is the empty sequence, returns an xs:date without a timezone."
		// So you can write:
		// adjust-date-to-timezone(current-date(), ())"

		String sourceXpath = getXFormsHelper().getXFormsObject(getSource()).getXPath();

		switch (((Question) getSource()).getAnswerSubformat()) {
		case PHONE:
			constraints.append(". = '' or matches(., '^").append(WebformsConfigurationReader.getInstance().getRegexPhone()).append("$')");
			break;
		case POSTAL_CODE:
			constraints.append(". = '' or matches(., '^").append(WebformsConfigurationReader.getInstance().getRegexPostalCode()).append("$')");
			break;
		case BSN:
			constraints.append(". = '' or matches(., '^").append(WebformsConfigurationReader.getInstance().getRegexBsn()).append("$')");
			break;
		case IBAN:
			constraints.append(". = '' or matches(., '^").append(WebformsConfigurationReader.getInstance().getRegexIban()).append("$')");
			break;
		case DATE_FUTURE:
			constraints.append("string-length(" + sourceXpath + "/text())=0 or . &gt;= adjust-date-to-timezone(current-date(), ())");
			break;
		case DATE_PAST:
			constraints.append("string-length(" + sourceXpath + "/text())=0 or . &lt;= adjust-date-to-timezone(current-date(), ())");
			break;
		case DATE_BIRTHDAY:
			constraints.append("string-length(" + sourceXpath + "/text())=0 or . &lt;= adjust-date-to-timezone(current-date(), ()) ");
			constraints.append("and (year-from-date(current-date()) - year-from-date(.) &lt;= ");
			constraints.append(MAX_YEARS_BIRTHDAY).append(")");
			break;
		case DATE_PERIOD:
			// Do nothing this type will never arrive here
			break;
		case TEXT:
		case DATE:
		case EMAIL:
		case NUMBER:
		case FLOAT:
			// No constraint, type handles restriction.
			break;
		case AMOUNT:
			// Not used type
			break;
		}
		constraints.append("\"/>");

	}

	@Override
	protected String getAlert() {
		StringBuilder sb = new StringBuilder();
		if (getSource() instanceof Question && ((Question) getSource()).getAnswerSubformat()!=null) {
			switch (((Question) getSource()).getAnswerSubformat()) {
			case DATE_FUTURE:
				sb.append("<alert><![CDATA[Date must be at the future!]]></alert>");
				break;
			case DATE_PAST:
			case DATE_BIRTHDAY:
				sb.append("<alert><![CDATA[Date must be at the past!]]></alert>");
				break;
			case BSN:
				sb.append("<alert><![CDATA[BSN format is not correct!]]></alert>");
				break;
			case IBAN:
				sb.append("<alert><![CDATA[IBAN format is not correct!]]></alert>");
				break;
			case PHONE:
				sb.append("<alert><![CDATA[Phone number format is not correct!]]></alert>");
				break;
			case POSTAL_CODE:
				sb.append("<alert><![CDATA[Postal code format is not correct!]]></alert>");
				break;
			case TEXT:
			case DATE:
			case EMAIL:
			case NUMBER:
			case FLOAT:
			case AMOUNT:
			case DATE_PERIOD:
				sb.append("<alert><![CDATA[Missing or incorrect value]]></alert>");
				break;
			}
		}
		
		if(hasWebserviceValidation()){
			List<WebserviceCallInputErrors> webserviceValidations = getWebserviceCallInputErrors();
			for(WebserviceCallInputErrors inputError: webserviceValidations){
				sb.append("<alert><![CDATA[");
				sb.append(inputError.getErrorMessage());
				sb.append("]]></alert>");
			}
		}		
		
		String alert = sb.toString();
		if(!alert.isEmpty()){
			return alert;
		}else{
			return "<alert/>";
		}
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
				if (((Question) getSource()).getAnswerSubformat() == AnswerSubformat.EMAIL) {
					type.append(" type=\"xf:email\" ");
				}
				break;
			case DATE:
				type.append(" type=\"xf:date\" ");
				break;
			case NUMBER:
				switch (((Question) getSource()).getAnswerSubformat()) {
				case NUMBER:
					type.append(" type=\"xf:integer\" ");
					break;
				case FLOAT:
					type.append(" type=\"xf:decimal\" ");
					break;
				default:
					break;
				}
				break;
			default:
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
	 * Creates a static element that does not depends on the value of other
	 * element.
	 * 
	 * @return
	 * @throws InvalidFlowInForm
	 */
	private String createElementBody() {
		StringBuilder section = new StringBuilder();
		section.append("<xf:" + getElementFormDefinition() + " " + getApparence() + " id=\"" + getSectionControlName() + "\" class=\""
				+ getCssClass() + "\" bind=\"" + getBindingId() + "\">");
		section.append(getBodyLabel());
		section.append(getBodyHint());
		
		//Add subtype constraint
		if(hasSubtypeConstraint()){
			section.append(getAlert(1,"subtype-constraint-" + getXFormsHelper().getUniqueName(getSource()) + "-validation"));
		}else{
			section.append(getAlert(1,null));
		}
		//Add webservice alerts
		if(hasWebserviceValidation()){
			List<WebserviceCallInputErrors> webserviceValidations = getWebserviceCallInputErrors();
			for (int i = 0; i < webserviceValidations.size(); i++) {
				section.append(getAlert(i+2,"webservice-constraint-"+ getXFormsHelper().getUniqueName(getSource()) +"-" + i + "-validation"));
			}
		}
		
		section.append(getBodyHelp());
		createElementAnswersItems(section);
		section.append("</xf:" + getElementFormDefinition() + " >");
		return section.toString();
	}

	/**
	 * XForms has input, output and selectable elements. This method translates
	 * an element to the correct one.
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
	 * XForms has input, output and selectable elements. This method translates
	 * an element to the correct one.
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
				if (answer.getSource() instanceof DynamicAnswer) {
					answer.getSectionBody(row);
				} else {
					if (!answer.getChildren().isEmpty() || !simpleElementsAdded) {
						answer.getSectionBody(row);
						if (answer.getChildren().isEmpty()) {
							simpleElementsAdded = true;
						}
					}
				}
			}
		}
	}

	/**
	 * Some elements needs to insert HTML text. Adds the tags to allow html code
	 * in the element.
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
	 * Creates a new computed go to next element Flow. Type normal, condition =
	 * '' -> true
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
		return " calculate=\"if(" + parsedFlow + ") then . else ''\"";
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

	@Override
	protected String getVisibilityStructure() {
		String section = "<" + getUniqueName() + ">";
		section += "false";
		section += "</" + getUniqueName() + ">";
		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			section += child.getVisibilityStructure();
		}
		return section;
	}
}
