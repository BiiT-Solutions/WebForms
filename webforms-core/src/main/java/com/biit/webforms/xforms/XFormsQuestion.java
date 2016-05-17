package com.biit.webforms.xforms;

import java.text.SimpleDateFormat;
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
import com.biit.webforms.persistence.entity.ElementWithImage;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLinkErrors;
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

	private List<List<XFormsAnswer>> answersGroups;

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
	protected String getDefinition() {
		StringBuilder section = new StringBuilder();

		// Add element's image
		if (getXFormsHelper().isImagesEnabled() && getSource() instanceof ElementWithImage && ((ElementWithImage) getSource()).getImage() != null) {
			section.append(XFormsImage.getDefinition(((ElementWithImage) getSource()).getImage(), getXFormsHelper(),
					(Form) getSource().getAncestor(Form.class), getXFormsHelper().getOrganization(), getXFormsHelper().isPreviewMode()));
		}

		section.append("<" + getName() + ">");
		if (getSource() instanceof Question) {
			if (((Question) getSource()).getDefaultValueTime() != null) {
				SimpleDateFormat formatter = new SimpleDateFormat(XFormsObject.XPATH_DATE_FORMAT);
				section.append(formatter.format(((Question) getSource()).getDefaultValueTime()));
			} else {
				section.append(((Question) getSource()).getDefaultValue());
			}
		}
		section.append("</" + getName() + ">");
		return section.toString();
	}

	@Override
	public String getHelp(OrbeonLanguage language) {
		// Avoid empty help windows.
		if (((Question) getSource()).getDescription().length() > 0) {
			return "<help><![CDATA[" + ((Question) getSource()).getDescription() + "]]></help>";
		}
		return "";
	}

	@Override
	public void getBinding(StringBuilder binding) throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		// Add form image binding
		if (getXFormsHelper().isImagesEnabled() && ((ElementWithImage) getSource()).getImage() != null) {
			XFormsImage.getBinding(this.getParent(), ((ElementWithImage) getSource()).getImage(), binding, getRelevantStructure(), getXFormsHelper(),
					getSource());
		}

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
		if (!(getSource() instanceof Question) || ((Question) getSource()).getAnswerSubformat() == null) {
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
		case POSITIVE_NUMBER:
		case NEGATIVE_NUMBER:
			return true;
		case DATE_PERIOD:
			// This type will never arrive here -no constraint-
			return false;
		case POSITIVE_FLOAT:
		case NEGATIVE_FLOAT:
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
		if ((link != null && !link.isEditable())) {
			binding.append("readonly=\"true()\" ");
		} else {
			// Orbeon $fr-mode indicates form modes "new", "edit", and "view".
			// Disable the edition of the question if it is not a new form.
			if (((Form) getSource().getAncestor(Form.class)).isEditionDisabled()
					|| (getSource() instanceof Question && ((Question) getSource()).isEditionDisabled())) {
				binding.append("readonly=\"$fr-mode != 'new'\" ");
			} else {
				binding.append("readonly=\"false()\" ");
			}
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

	private List<WebserviceCallInputLinkErrors> getWebserviceCallInputErrors() {
		List<WebserviceCallInputLinkErrors> callInputErrors = new ArrayList<>();
		Set<WebserviceCallInputLink> inputLinks = getXFormsHelper().getWebserviceCallInputLinks(getSource());

		for (WebserviceCallInputLink inputLink : inputLinks) {
			callInputErrors.addAll(inputLink.getValidErrors());
		}
		Collections.sort(callInputErrors);

		return callInputErrors;
	}

	private void getWebserviceConstraints(StringBuilder constraints) {
		List<WebserviceCallInputLinkErrors> webserviceValidations = getWebserviceCallInputErrors();

		for (int i = 0; i < webserviceValidations.size(); i++) {
			constraints.append("<xf:constraint id=\"webservice-constraint-" + getXFormsHelper().getUniqueName(getSource()) + "-" + i + "-validation\" ");
			constraints.append("value=\"$" + getXFormsHelper().getWebserviceValidationField(getSource()).getUniqueName() + " != '");
			constraints.append(webserviceValidations.get(i).getErrorCode());
			constraints.append("'\"/>");
		}
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
		// "If $timezone is the empty sequence, returns an xs:date without a
		// timezone."
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
		case POSITIVE_FLOAT:
			constraints.append(". &gt;= 0");
			break;
		case NEGATIVE_FLOAT:
			constraints.append(". &lt;= 0");
			break;
		case TEXT:
		case DATE:
		case EMAIL:
		case NUMBER:
		case POSITIVE_NUMBER:
		case NEGATIVE_NUMBER:
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
	protected String getAlert(OrbeonLanguage language) {
		StringBuilder sb = new StringBuilder();
		if (getSource() instanceof Question) {
			if (((Question) getSource()).getAnswerSubformat() != null) {
				String alert = "";
				switch (((Question) getSource()).getAnswerSubformat()) {
				case DATE_FUTURE:
					alert = OrbeonLanguageManager.getInstance().getAlertDateFuture(language);
					break;
				case DATE_PAST:
				case DATE_BIRTHDAY:
					alert = OrbeonLanguageManager.getInstance().getAlertDatePast(language);
					break;
				case BSN:
					alert = OrbeonLanguageManager.getInstance().getAlertBsn(language);
					break;
				case IBAN:
					alert = OrbeonLanguageManager.getInstance().getAlertIban(language);
					break;
				case PHONE:
					alert = OrbeonLanguageManager.getInstance().getAlertPhone(language);
					break;
				case POSTAL_CODE:
					alert = OrbeonLanguageManager.getInstance().getAlertPostalCode(language);
					break;
				case POSITIVE_NUMBER:
				case NEGATIVE_NUMBER:
				case POSITIVE_FLOAT:
				case NEGATIVE_FLOAT:
					alert = OrbeonLanguageManager.getInstance().getAlertInvalidNumber(language);
					break;
				case TEXT:
				case DATE:
				case EMAIL:
				case NUMBER:
				case FLOAT:
				case AMOUNT:
				case DATE_PERIOD:
					alert = OrbeonLanguageManager.getInstance().getAlertDefault(language);
					break;
				}
				sb.append("<alert><![CDATA[" + alert + "]]></alert>");
			} else {
				sb.append("<alert><![CDATA[" + OrbeonLanguageManager.getInstance().getAlertDefault(language) + "]]></alert>");
			}
		}

		if (hasWebserviceValidation()) {
			List<WebserviceCallInputLinkErrors> webserviceValidations = getWebserviceCallInputErrors();
			for (WebserviceCallInputLinkErrors inputError : webserviceValidations) {
				sb.append("<alert><![CDATA[");
				sb.append(inputError.getErrorMessage());
				sb.append("]]></alert>");
			}
		}

		String alert = sb.toString();
		if (!alert.isEmpty()) {
			return alert;
		} else {
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
				case POSITIVE_NUMBER:
					type.append(" type=\"xf:positiveInteger\" ");
					break;
				case NEGATIVE_NUMBER:
					type.append(" type=\"xf:negativeInteger\" ");
					break;
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

		// Add element's image.
		if (getXFormsHelper().isImagesEnabled() && getSource() instanceof ElementWithImage && ((ElementWithImage) getSource()).getImage() != null) {
			XFormsImage.getBody(this.getParent(), ((ElementWithImage) getSource()).getImage(), section, getXFormsHelper());
		}

		section.append("<xf:" + getElementFormDefinition() + " " + getApparence() + " id=\"" + getSectionControlName() + "\" class=\"" + getCssClass()
				+ "\" bind=\"" + getBindingId() + "\">");
		section.append(getBodyLabel());
		section.append(getBodyHint());

		section.append(getAlert(null, null));
		int alertsAdded = 1;
		// Add subtype constraint
		if (hasSubtypeConstraint()) {
			section.append(getAlert(alertsAdded, "subtype-constraint-" + getXFormsHelper().getUniqueName(getSource()) + "-validation"));
			alertsAdded++;
		}
		// Add webservice alerts
		if (hasWebserviceValidation()) {
			List<WebserviceCallInputLinkErrors> webserviceValidations = getWebserviceCallInputErrors();
			for (int i = 0; i < webserviceValidations.size(); i++) {
				section.append(getAlert(i + alertsAdded, "webservice-constraint-" + getXFormsHelper().getUniqueName(getSource()) + "-" + i + "-validation"));
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

			// For each group of answer, create a itemset
			for (List<XFormsAnswer> group : getAnswersGroups()) {
				if (!group.isEmpty()) {
					group.get(0).getSectionBody(row);
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
	protected String getHint(OrbeonLanguage language) {
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
		if (getSource() instanceof Question && ((Question) getSource()).getDescription() != null && ((Question) getSource()).getDescription().length() > 0) {
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

	/**
	 * To maintain the order of the answers if subanswers are used, we need to
	 * create different sets of "items" or Orbeon will order first the answers
	 * and later the answers with subanswers.
	 * 
	 * @return
	 */
	private List<List<XFormsAnswer>> getItemGroups() {
		List<List<XFormsAnswer>> itemGroups = new ArrayList<>();
		List<XFormsAnswer> group = new ArrayList<>();
		for (XFormsObject<? extends TreeObject> answer : getChildren()) {
			// Answers with subanswers always ina separate group.
			if (!((TreeObject) answer.getSource()).getChildren().isEmpty()) {
				if (!group.isEmpty()) {
					// Store previous group and start a new one.
					itemGroups.add(group);
					group = new ArrayList<>();
				}
				// Store current group also.
				group.add((XFormsAnswer) answer);
				itemGroups.add(group);
				group = new ArrayList<>();
			} else {
				// Standard answer without subanswers. Include in current group.
				group.add((XFormsAnswer) answer);
			}
		}
		// Store last group if needed.
		if (!group.isEmpty()) {
			itemGroups.add(group);
		}
		return itemGroups;
	}

	/**
	 * An answer group is a group of answers that are represented by a itemset
	 * in orbeon. A standard question only has an itemset, but this itemset can
	 * be split in two or more by answers with subanswers that has its own
	 * itemsets
	 * 
	 * @return
	 */
	private List<List<XFormsAnswer>> getAnswersGroups() {
		if (answersGroups == null) {
			// Initialize groups.
			answersGroups = getItemGroups();
		}
		return answersGroups;
	}

	private Integer getItemGroupIndex(XFormsAnswer answer) {
		if (answersGroups == null) {
			// Initialize groups.
			answersGroups = getItemGroups();
		}
		for (List<XFormsAnswer> group : getAnswersGroups()) {
			if (group.contains(answer)) {
				return getAnswersGroups().indexOf(group);
			}
		}
		return null;
	}

	public String getItemGroupName(XFormsAnswer answer) {
		Integer index = getItemGroupIndex(answer);
		if (index == null || index == 0) {
			return "";
		}
		return index + "";
	}
}
