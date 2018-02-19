package com.biit.webforms.exporters.xforms;

import java.util.HashSet;
import java.util.Set;

import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.exporters.xforms.exceptions.InvalidDateException;
import com.biit.webforms.exporters.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.exporters.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.exporters.xforms.exceptions.StringRuleSyntaxError;
import com.biit.webforms.persistence.entity.DynamicAnswer;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Question;

public class XFormsDynamicAnswer extends XFormsObject<DynamicAnswer> {
	private static final String CSS_CLASS_ANSWER = "webforms-answer";

	public XFormsDynamicAnswer(XFormsHelper xFormsHelper, DynamicAnswer treeObject)
			throws NotValidTreeObjectException, NotValidChildException {
		super(xFormsHelper, treeObject);
	}

	@Override
	protected String getCalculateStructure(String flow) {
		return "";
	}

	@Override
	protected String getDefaultVisibility()
			throws InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		return null;
	}

	@Override
	protected void getBinding(StringBuilder binding) throws NotExistingDynamicFieldException, InvalidDateException,
			StringRuleSyntaxError, PostCodeRuleSyntaxError {
		// Do nothing
	}

	@Override
	protected void getSectionBody(StringBuilder stringBuilder) {
		if (getSource().getReference().getAnswerType() == AnswerType.INPUT) {
			getSectionBodyInputField(stringBuilder);
		} else {
			getSectionBodySelection(stringBuilder);
		}
	}

	private void getSectionBodySelection(StringBuilder stringBuilder) {
		// An itemset for standard selections.
		stringBuilder.append("<xf:itemset ref=\"$form-resources/");
		stringBuilder.append(getReferencePath());
		stringBuilder.append("/item[value = xxf:split(instance('fr-form-instance')/");
		stringBuilder.append(getReferenceFormXPath());
		stringBuilder.append(")]\"");
		stringBuilder.append(" class=\"" + getCssClass() + "\" >");
		stringBuilder.append("<xf:label ref=\"label\" " + isHtmlText() + " />");
		stringBuilder.append("<xf:value ref=\"value\" />");
		stringBuilder.append("<xf:hint ref=\"hint\" />");
		stringBuilder.append("</xf:itemset>");
	}

	private void getSectionBodyInputField(StringBuilder stringBuilder) {
		// An itemset for input.
		stringBuilder.append("<xf:itemset ref=\"instance('fr-form-instance')");
		stringBuilder.append(getReferenceFormXPathParent());
		stringBuilder.append("\"");
		stringBuilder.append(" class=\"" + getCssClass() + "\" >");
		stringBuilder.append("<xf:label ref=\"" + getSource().getReference().getName() + "\" " + isHtmlText() + " />");
		stringBuilder.append("<xf:value ref=\"" + getSource().getReference().getName() + "\" />");
		stringBuilder.append("<xf:hint ref=\"hint\" />");
		stringBuilder.append("</xf:itemset>");
	}

	private String getReferenceFormXPathParent() {
		String path = getReferenceFormXPath();
		int lastIndex = path.lastIndexOf('/');
		return path.substring(0, lastIndex);
	}

	private String getReferenceFormXPath() {
		String currentPath = getXFormsHelper().getXFormsObject(getSource().getReference()).getXPath();
		return currentPath.replace("form/", "");
	}

	private String getReferencePath() {
		return getXFormsHelper().getXFormsObject(getSource().getReference()).getPath();
	}

	@Override
	public Set<Flow> getFlowsTo() {
		return new HashSet<>();
	}

	protected String isHtmlText() {
		switch (((Question) getSource().getParent()).getAnswerType()) {
		case MULTIPLE_SELECTION:
			return "mediatype=\"text/html\"";
		default:
			return "";
		}
	}

	/**
	 * Dynamic Answer has no definitions in the model.
	 */
	@Override
	public String getDefinition() {
		return "";
	}

	/**
	 * Dynamic Answers don't have resources.
	 */
	@Override
	protected String getResources(OrbeonLanguage language) throws NotExistingDynamicFieldException {
		return "";
	}

	@Override
	protected String getCssClass() {
		return super.getCssClass() + " " + CSS_CLASS_ANSWER;
	}

	/**
	 * Defines the structure of the element in the body part of the XForms. For
	 * answers with subanswers, add the needed CSS class.
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

	@Override
	protected String getVisibilityStructure() {
		return "";
	}
}
