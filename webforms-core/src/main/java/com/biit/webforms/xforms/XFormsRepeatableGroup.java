package com.biit.webforms.xforms;

import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

/**
 * Repeated groups are represented as a section + section iteration in Orbeon.
 * 
 */
public class XFormsRepeatableGroup extends XFormsObject {
	protected final static int MIN_REPEATS = 1;
	protected final static int MAX_REPEATS = 100;

	public XFormsRepeatableGroup(Group group) throws NotValidTreeObjectException, NotValidChildException {
		super(group);
	}

	/**
	 * Add section and section-iteration
	 */
	@Override
	protected String getDefinition() {
		String section = "<" + getControlName() + ">";
		section += "<" + getIteratorControlName() + ">";

		for (XFormsObject child : getChildren()) {
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
	protected String getBinding() throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		String elementBinding = "<xf:bind id=\"" + getBindingName() + "\" name=\"" + getControlName() + "\""
				+ getRelevantStructure() + " ref=\"" + getControlName() + "\" >";
		elementBinding += "<xf:bind id=\"" + getIteratorBindingName() + "\" name=\"" + getIteratorControlName() + "\""
				+ getRelevantStructure() + " ref=\"" + getIteratorControlName() + "\" >";
		// Add also children.
		for (XFormsObject child : getChildren()) {
			elementBinding += child.getBinding();
		}
		elementBinding += "</xf:bind>";
		elementBinding += "</xf:bind>";
		return elementBinding;
	}

	/**
	 * We add a grid in a section to allow loops.
	 */
	@Override
	protected String getSectionBody() {
		String section = "";
		// Dummy section
		section += "<fr:section id=\"" + getSectionControlName() + "\" bind=\"" + getBindingName()
				+ "\" repeat=\"true\" min=\"" + MIN_REPEATS + "\" max=\"" + MAX_REPEATS + "\" template=\"instance('"
				+ getTemplateName() + "')\" >";
		section += getBodyLabel();
		section += getBodyHint();
		section += getBodyAlert();
		section += getBodyHelp();
		for (XFormsObject child : getChildren()) {
			section += child.getSectionBody();
		}
		section += "</fr:section>";
		return section;
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
		for (XFormsObject child : getChildren()) {
			template += child.getDefinition();
		}
		template += "</" + getIteratorControlName() + ">";

		template += "</xf:instance>";

		// Add templates of nested loops.
		for (XFormsObject child : getChildren()) {
			template += child.getTemplates();
		}
		return template;
	}

}
