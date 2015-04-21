package com.biit.webforms.xforms;

import java.util.HashSet;
import java.util.Set;

import com.biit.form.entity.BaseAnswer;
import com.biit.form.entity.BaseQuestion;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

public class XFormsAnswer extends XFormsObject<Answer> {
	private final static String PARENT_ANSWER_CSS_CLASS = "subanswer-parent";
	private final static String SUBANSWER_CSS_CLASS = "subanswer-child";
	private static final String CSS_CLASS_ANSWER = "webforms-answer";

	public XFormsAnswer(XFormsHelper xFormsHelper, Answer answer) throws NotValidTreeObjectException,
			NotValidChildException {
		super(xFormsHelper, answer);
	}

	@Override
	public void getBinding(StringBuilder binding) throws NotExistingDynamicFieldException, InvalidDateException,
			StringRuleSyntaxError, PostCodeRuleSyntaxError {
		// Do nothing.
	}

	/**
	 * Calculates the section body for answers and also for subanswers.
	 */
	@Override
	public void getSectionBody(StringBuilder stringBuilder) {
		// An itemset for standard answers.
		if (getSource().getChildren().isEmpty()) {
			stringBuilder.append("<xf:itemset ref=\"$form-resources/");
			stringBuilder.append(getParent().getPath() + "/item\" class=\"" + getCssClass() + "\" >");
			stringBuilder.append("<xf:label ref=\"label\" " + isHtmlText() + " />");
			stringBuilder.append("<xf:value ref=\"value\" />");
			stringBuilder.append("<xf:hint ref=\"hint\" />");
			stringBuilder.append("</xf:itemset>");
		} else {
			int subanswerParentsCounter = ((Question) getSource().getParent())
					.getAnswerWithSubanswersIndex((BaseAnswer) getSource());
			// Parent answer.
			stringBuilder.append("<xf:itemset ref=\"$form-resources/");
			stringBuilder.append(getParent().getPath() + "/item" + subanswerParentsCounter + "\" class=\""
					+ getCssClass() + " " + PARENT_ANSWER_CSS_CLASS + "\" >");
			stringBuilder.append("<xf:label ref=\"label\" " + isHtmlText() + " />");
			stringBuilder.append("<xf:value ref=\"value\" />");
			stringBuilder.append("<xf:hint ref=\"hint\" />");
			stringBuilder.append("</xf:itemset>");

			// Subanswer
			stringBuilder.append("<xf:itemset ref=\"$form-resources/");
			stringBuilder.append(getParent().getPath() + "/item" + subanswerParentsCounter + "/subitem"
					+ subanswerParentsCounter + "\" class=\"" + getCssClass() + " " + SUBANSWER_CSS_CLASS + "\" >");
			stringBuilder.append("<xf:label ref=\"label\" " + isHtmlText() + " />");
			stringBuilder.append("<xf:value ref=\"value\" />");
			stringBuilder.append("<xf:hint ref=\"hint\" />");
			stringBuilder.append("</xf:itemset>");
		}
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
	protected String getHint() {
		if (((Answer) getSource()).getDescription() != null && ((Answer) getSource()).getDescription().length() > 0) {
			return "<hint><![CDATA[" + ((Answer) getSource()).getDescription() + "]]></hint>";
		}
		return "<hint/>";
	}

	/**
	 * Answers has a special definition inside a question.
	 */
	@Override
	protected String getResources() throws NotExistingDynamicFieldException {
		return getResource("item", getSource().getChildren().isEmpty() ? null
				: ((BaseQuestion) getSource().getParent()).getAnswerWithSubanswersIndex(getSource()));
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
	private String getResource(String prefix, Integer index) throws NotExistingDynamicFieldException {
		String resource = "<" + prefix + (index != null ? index.toString() : "") + ">";

		resource += getLabel();
		resource += getHint();
		resource += getValue();

		// Add subanswers also.
		for (XFormsObject<?> child : getChildren()) {
			resource += ((XFormsAnswer) child).getResource("subitem", index);
		}

		resource += "</" + prefix + (index != null ? index.toString() : "") + ">";
		return resource;
	}

	@Override
	protected String getAllFlowsVisibility() throws InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
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
	 * Defines the structure of the element in the body part of the XForms. For answers with subanswers, add the needed
	 * CSS class.
	 * 
	 * @param treeObject
	 * @return
	 * @throws InvalidFlowInForm
	 */
	@Override
	protected String getBodyStructure(String structure, boolean html) {
		String text = "<xf:" + structure + " ref=\"$form-resources/" + getPath() + "/" + structure + "\"";
		if (html) {
			text += " mediatype=\"text/html\"";
		}
		text += " />";
		return text;
	}

	/**
	 * Some elements needs to insert HTML text. Adds the tags to allow html code in the element.
	 * 
	 * @param element
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

}
