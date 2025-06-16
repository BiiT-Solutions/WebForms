package com.biit.webforms.exporters.xforms;

import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.exporters.xforms.exceptions.InvalidDateException;
import com.biit.webforms.exporters.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.exporters.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.exporters.xforms.exceptions.StringRuleSyntaxError;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Question;

import java.util.HashSet;
import java.util.Set;

public class XFormsAnswer extends XFormsObject<Answer> {
	private static final String PARENT_ANSWER_CSS_CLASS = "subanswer-parent";
	private static final String SUBANSWER_CSS_CLASS = "subanswer-child";
	private static final String CSS_CLASS_ANSWER = "webforms-answer";

	public XFormsAnswer(XFormsHelper xFormsHelper, Answer answer) throws NotValidTreeObjectException, NotValidChildException {
		super(xFormsHelper, answer);
	}

	@Override
	public void getBinding(StringBuilder binding) throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		// Do nothing.
	}

	/**
	 * Calculates the section body for answers and also for subanswers.
	 */
	@Override
	public void getSectionBody(StringBuilder stringBuilder) {
		// An itemset for standard answers.
		if (getSource().getChildren().isEmpty()) {
			stringBuilder.append("<xf:itemset ref=\"instance('fr-form-resources')/resource/");
			stringBuilder.append(getParent().getPath() + "/" + getItemGroupName() + "\" class=\"" + getCssClass() + "\" >");
			stringBuilder.append("<xf:label ref=\"label\" " + isHtmlText() + " />");
			stringBuilder.append("<xf:value ref=\"value\" />");
			stringBuilder.append("<xf:hint ref=\"hint\" />");
			stringBuilder.append("</xf:itemset>");
		} else {
			// Parent answer.
			stringBuilder.append("<xf:itemset ref=\"instance('fr-form-resources')/resource/");
			stringBuilder.append(getParent().getPath() + "/" + getItemGroupName() + "\" class=\"" + getCssClass() + " " + PARENT_ANSWER_CSS_CLASS + "\" >");
			stringBuilder.append("<xf:label ref=\"label\" " + isHtmlText() + " />");
			stringBuilder.append("<xf:value ref=\"value\" />");
			stringBuilder.append("<xf:hint ref=\"hint\" />");
			stringBuilder.append("</xf:itemset>");

			// Subanswer
			stringBuilder.append("<xf:itemset ref=\"instance('fr-form-resources')/resource/");
			stringBuilder.append(getParent().getPath() + "/" + getItemGroupName() + "/sub" + getItemGroupName() + "\" class=\"" + getCssClass() + " "
					+ SUBANSWER_CSS_CLASS + "\" >");
			stringBuilder.append("<xf:label ref=\"label\" " + isHtmlText() + " />");
			stringBuilder.append("<xf:value ref=\"value\" />");
			stringBuilder.append("<xf:hint ref=\"hint\" />");
			stringBuilder.append("</xf:itemset>");
		}
	}

	/**
	 * Answers are divided in groups split by answers with subanswers.
	 * 
	 * @return
	 */
	public String getItemGroupName() {
		return "item" + ((XFormsQuestion) getParent()).getItemGroupName(this);
	}

	/**
	 * Answer has no definitions in the model.
	 */
	@Override
	public String getDefinition() {
		return "";
	}

	protected String getValue() {
		return "<value><![CDATA[" + getSource().getName() + "]]></value>";
	}

	@Override
	protected String getHint(OrbeonLanguage language) {
		if (((Answer) getSource()).getDescription() != null && ((Answer) getSource()).getDescription().length() > 0) {
			return "<hint><![CDATA[" + ((Answer) getSource()).getDescription() + "]]></hint>";
		}
		return "<hint/>";
	}

	/**
	 * Answers has a special definition inside a question.
	 */
	@Override
	protected String getResources(OrbeonLanguage language) throws NotExistingDynamicFieldException {
		return getResource(getItemGroupName(), language);
	}

	/**
	 * Set answers resource and subanswers.
	 * 
	 * @param prefix
	 *            the name of the xml element
	 * @param index
	 *            if not null, a numeric sufix is added to the label.
	 * @return
	 * @throws NotExistingDynamicFieldException
	 */
	private String getResource(String prefix, OrbeonLanguage language) throws NotExistingDynamicFieldException {
		String resource = "<" + prefix + ">";

		resource += getLabel(language);
		resource += getHint(language);
		resource += getValue();

		// Add subanswers also.
		for (XFormsObject<?> child : getChildren()) {
			resource += ((XFormsAnswer) child).getResource("sub" + getItemGroupName(), language);
		}

		resource += "</" + prefix + ">";
		return resource;
	}

	@Override
	protected String getAllFlowsVisibility() throws InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		return null;
	}

	@Override
	protected String getDefaultVisibility() throws InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		return null;
	}

	@Override
	public Set<Flow> getFlowsTo() {
		return new HashSet<>();
	}

	@Override
	protected String getCalculateStructure(String flow) {
		return "";
	}

	/**
	 * Defines the structure of the element in the body part of the XForms. For
	 * answers with subanswers, add the needed CSS class.
	 *
	 */
	@Override
	protected String getBodyStructure(String structure, boolean html) {
		String text = "<xf:" + structure + " ref=\"instance('fr-form-resources')/resource/" + getPath() + "/" + structure + "\"";
		if (html) {
			text += " mediatype=\"text/html\"";
		}
		text += " />";
		return text;
	}

	/**
	 * Some elements needs to insert HTML text. Adds the tags to allow html code
	 * in the element.
	 *
	 * @return
	 */
	protected String isHtmlText() {
		switch (((Question) getSource().getParent()).getAnswerType()) {
		case MULTIPLE_SELECTION:
			return "mediatype=\"text/html\"";
		default:
			return "";
		}
	}

	@Override
	protected String getCssClass() {
		return super.getCssClass() + " " + CSS_CLASS_ANSWER;
	}

	@Override
	protected String getVisibilityStructure() {
		return "";
	}

}
