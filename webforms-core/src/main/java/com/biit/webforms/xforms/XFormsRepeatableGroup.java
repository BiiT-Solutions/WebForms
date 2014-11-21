package com.biit.webforms.xforms;

import com.biit.form.BaseRepeatableGroup;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

/**
 * Repeated groups are represented as a section + section iteration in Orbeon.
 * 
 */
public class XFormsRepeatableGroup extends XFormsGroup {
	protected final static int MIN_REPEATS = 1;
	protected final static int MAX_REPEATS = 100;

	public XFormsRepeatableGroup(XFormsHelper xFormsHelper, BaseRepeatableGroup group)
			throws NotValidTreeObjectException, NotValidChildException {
		super(xFormsHelper, group);
	}

	/**
	 * Add section and section-iteration
	 */
	@Override
	protected String getDefinition() {
		String section = "<" + getControlName() + ">";
		section += "<" + getIteratorControlName() + ">";

		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			section += child.getDefinition();
		}

		section += "</" + getIteratorControlName() + ">";
		section += "</" + getControlName() + ">";
		return section;
	}

	private String getIteratorControlName() {
		return getControlName() + "-iterator";
	}

	private String getIteratorBindingName() {
		return getIteratorControlName() + "-bind";
	}

	/**
	 * Add also a dummy section iteration.
	 */
	@Override
	protected void getBinding(StringBuilder binding) throws NotExistingDynamicFieldException, InvalidDateException,
			StringRuleSyntaxError, PostCodeRuleSyntaxError {
		binding.append("<xf:bind id=\"").append(getBindingName()).append("\" name=\"").append(getControlName())
				.append("\"");
		getRelevantStructure(binding);
		binding.append(" ref=\"").append(getControlName()).append("\" >");
		binding.append("<xf:bind id=\"").append(getIteratorBindingName()).append("\" ");
		binding.append("name=\"").append(getIteratorControlName()).append("\" ");
		getRelevantStructure(binding);
		binding.append(" ref=\"").append(getIteratorControlName()).append("\" >");
		// Add also children.
		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			child.getBinding(binding);
		}
		binding.append("</xf:bind>");
		binding.append("</xf:bind>");
	}

	/**
	 * We add a grid in a section to allow loops.
	 */
	@Override
	protected void getSectionBody(StringBuilder body) {
		// Dummy section
		body.append("<fr:section id=\"").append(getSectionControlName()).append("\" ");
		body.append("bind=\"").append(getBindingName()).append("\" ");
		body.append("repeat=\"true\" min=\"").append(MIN_REPEATS).append("\" max=\"").append(MAX_REPEATS).append("\" ");
		body.append("template=\"instance('").append(getTemplateName()).append("')\" >");
		body.append(getBodyLabel());
		body.append(getBodyHint());
		body.append(getBodyAlert());
		body.append(getBodyHelp());
		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			child.getSectionBody(body);
		}
		body.append("</fr:section>");
	}

	protected String getLoopHeaderTags(int minRepeats, int maxRepeats) {
		String maxRepeatsTag = "";
		String minRepeatsTag = "";
		if (maxRepeats > 0) {
			maxRepeatsTag = " max=\"" + maxRepeats + "\" ";
		}
		if (minRepeats > 0) {
			minRepeatsTag = " min=\"" + minRepeats + "\" ";
		}

		String group = "<fr:grid id=\"" + getSectionControlName() + "\" repeat=\"true\" bind=\"" + getBindingName()
				+ "\" origin=\"instance('" + getTemplateName() + "')\" " + minRepeatsTag + maxRepeatsTag + ">";

		return group;
	}

	private String getTemplateName() {
		return getControlName() + "-template";
	}

	/**
	 * Only loops uses templates
	 * 
	 * @return
	 */
	@Override
	protected String getTemplates() {
		String template = "";
		template += "<xf:instance xxf:readonly=\"true\" id=\"" + getTemplateName() + "\">";

		template += "<" + getIteratorControlName() + ">";
		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			template += child.getDefinition();
		}
		template += "</" + getIteratorControlName() + ">";

		template += "</xf:instance>";

		// Add templates of nested loops.
		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			template += child.getTemplates();
		}
		return template;
	}

}
