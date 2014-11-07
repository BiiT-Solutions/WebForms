package com.biit.webforms.xforms;

import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

/**
 * Repeated groups are represented as a section (called dummy section) + repeatable grid in Orbeon.
 * 
 */
public class XFormsRepeatableGroup extends XFormsObject {
	protected final static int MIN_REPEATS = 1;
	protected final static int MAX_REPEATS = 100;

	public XFormsRepeatableGroup(Group group) throws NotValidTreeObjectException, NotValidChildException {
		super(group);
	}

	/**
	 * Add also a dummy section definition.
	 */
	@Override
	protected String getBinding() throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		String elementBinding = "<xf:bind id=\"" + getBindingName() + "\" name=\"" + getControlName() + "\""
				+ getRelevantStructure() + " ref=\"" + getControlName() + "\" >";
		// Add also children.
		for (XFormsObject child : getChildren()) {
			elementBinding += child.getBinding();
		}
		elementBinding += "</xf:bind>";
		return elementBinding;
	}

	private String getSectionControlNameOfDummySection() {
		return getControlName() + "-section-control";
	}

	/**
	 * We add a grid in a section to allow loops.
	 */
	@Override
	protected String getSectionBody() {
		String section = "";
		// Dummy section
		section += "<fr:section id=\"" + getSectionControlNameOfDummySection() + "\" bind=\"" + getBindingName()
				+ "\">";
		section += getBodyLabel();
		section += getBodyHint();
		section += getBodyAlert();
		section += getBodyHelp();
		// Loop
		section += getLoopHeaderTags(MIN_REPEATS, MAX_REPEATS);
		for (XFormsObject child : getChildren()) {
			section += child.getSectionBody();
		}
		section += "</fr:grid>";
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

		template += "<" + getControlName() + ">";
		for (XFormsObject child : getChildren()) {
			template += child.getDefinition();
		}
		template += "</" + getControlName() + ">";

		template += "</xf:instance>";
		return template;
	}

}
