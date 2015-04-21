package com.biit.webforms.xforms;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biit.form.entity.BaseGroup;
import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

/**
 * Groups are represented as a nested Sections in Orbeon.
 * 
 */
public class XFormsGroup extends XFormsObject<BaseGroup> {
	private static final String CSS_CLASS_GROUP = "webforms-group";

	public XFormsGroup(XFormsHelper xFormsHelper, BaseGroup group) throws NotValidTreeObjectException,
			NotValidChildException {
		super(xFormsHelper, group);
	}

	@Override
	protected void getBinding(StringBuilder binding) throws NotExistingDynamicFieldException, InvalidDateException,
			StringRuleSyntaxError, PostCodeRuleSyntaxError {
		binding.append("<xf:bind id=\"").append(getBindingId()).append("\" name=\"").append(getBindingName())
				.append("\"");
		getRelevantStructure(binding);
		binding.append(" ref=\"").append(getXPath()).append("\" >");
		// Add also children.
		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			child.getBinding(binding);
		}
		binding.append("</xf:bind>");
	}

	/**
	 * Groups are represented as sections.
	 */
	@Override
	protected void getSectionBody(StringBuilder body) {
		body.append("<fr:section id=\"").append(getSectionControlName()).append("\" class=\"").append(getCssClass())
				.append("\" bind=\"").append(getBindingId()).append("\">");
		body.append(getBodyLabel());
		body.append(getBodyHint());
		body.append(getBodyAlert());
		body.append(getBodyHelp());
		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			child.getSectionBody(body);
		}
		body.append("</fr:section>");
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
			return getXFormsHelper().getVisibilityOfElement(
					getXFormsHelper().getPreviousBaseQuestion((BaseQuestion) firstQuestion));
		}
		// Empty groups are hidden.
		return "false";
	}

	@Override
	public Set<Flow> getFlowsTo() {
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
		return flowsToGroup;
	}

	@Override
	protected String getCalculateStructure(String flow) {
		return "";
	}

	@Override
	protected String getCssClass() {
		return super.getCssClass() + " " + getCassGroupClass();
	}

	protected String getCassGroupClass() {
		return CSS_CLASS_GROUP;
	}
}
