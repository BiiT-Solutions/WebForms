package com.biit.webforms.xforms;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biit.form.BaseGroup;
import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

/**
 * Groups are represented as a nested Sections in Orbeon.
 * 
 */
public class XFormsGroup extends XFormsObject<BaseGroup> {

	public XFormsGroup(XFormsHelper xFormsHelper, BaseGroup group) throws NotValidTreeObjectException,
			NotValidChildException {
		super(xFormsHelper, group);
	}

	@Override
	protected String getBinding() throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		String elementBinding = "<xf:bind id=\"" + getBindingName() + "\" name=\"" + getControlName() + "\""
				+ getRelevantStructure() + " ref=\"" + getControlName() + "\" >";
		// Add also children.
		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			elementBinding += child.getBinding();
		}
		elementBinding += "</xf:bind>";
		return elementBinding;
	}

	/**
	 * Groups are represented as sections.
	 */
	@Override
	protected String getSectionBody() {
		String section = "";
		section += "<fr:section id=\"" + getSectionControlName() + "\" bind=\"" + getBindingName() + "\">";
		section += getBodyLabel();
		section += getBodyHint();
		section += getBodyAlert();
		section += getBodyHelp();
		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			section += child.getSectionBody();
		}
		section += "</fr:section>";
		return section;
	}

	/**
	 * Is the visibility of all the questions that are inside.
	 */
	@Override
	protected String getAllFlowsVisibility() throws InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {

		// Load stored visibility if exists.
		if (getXFormsHelper().getVisibilityOfQuestion(getSource()) != null) {
			return getXFormsHelper().getVisibilityOfQuestion(getSource());
		}

		String visibility = "";
		LinkedHashSet<TreeObject> questionsInGroup = getSource().getAllChildrenInHierarchy(BaseQuestion.class);
		Set<Flow> flowsToGroup = new HashSet<>();
		// Get all flows from outside to any question of the group.
		for (TreeObject questionInGroup : questionsInGroup) {
			Set<Flow> flowsToQuestion = getXFormsHelper().getFlowsWithDestiny(questionInGroup);
			for (Flow flow : flowsToQuestion) {
				// Is a flow from outside of the group...
				if (!questionsInGroup.contains(flow.getOrigin())) {
					flowsToGroup.add(flow);
				}
			}
		}

		// obtain the visibility for each question
		if (flowsToGroup.isEmpty()) {
			visibility = getDefaultVisibility();
		} else {
			for (Flow flow : flowsToGroup) {
				// returns the expression or the 'others' rule.
				for (Token token : flow.getCondition()) {
					String conditionVisibility = convertTokenToXForms(token);
					visibility += conditionVisibility + " ";
				}

				// Others rules need that source must select an answer.
				visibility += othersSourceMustBeFilledUp(flow);
			}
		}

		// Store calculated visibility
		getXFormsHelper().addVisibilityOfQuestion(getSource(), visibility);

		return visibility;
	}

	/**
	 * In groups the default visibility is the default visibility of the first element.
	 */
	@Override
	protected String getDefaultVisibility() throws InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		// Same visibility that the first question
		TreeObject firstQuestion = null;
		LinkedHashSet<XFormsObject<? extends TreeObject>> questionsInGroup = getAllChildrenInHierarchy(XFormsQuestion.class);
		if (!questionsInGroup.isEmpty()) {
			firstQuestion = questionsInGroup.iterator().next().getSource();
		}

		if (firstQuestion != null) {
			// First element always visible.
			if (getXFormsHelper().isFirstQuestion(firstQuestion)) {
				return "";
			}
			// Other elements uses the previous element visibility.
			return getXFormsHelper().getVisibilityOfQuestion(getXFormsHelper().getPreviousQuestion(firstQuestion));
		}
		// Empty groups are hidden.
		return "false";
	}
}
