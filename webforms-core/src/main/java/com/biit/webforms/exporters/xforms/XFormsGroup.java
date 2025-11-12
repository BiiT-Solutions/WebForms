package com.biit.webforms.exporters.xforms;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biit.form.entity.BaseGroup;
import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.exporters.xforms.exceptions.InvalidDateException;
import com.biit.webforms.exporters.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.exporters.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.exporters.xforms.exceptions.StringRuleSyntaxError;
import com.biit.webforms.persistence.entity.Flow;

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
	 * In groups the default visibility is the default visibility of the first
	 * element.
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
		LinkedHashSet<BaseQuestion> questionsInGroup = getSource().getAllChildrenInHierarchy(BaseQuestion.class);
		Set<Flow> flowsToGroup = new HashSet<>();
		// Get all flows from outside to any question of the group.
		for (BaseQuestion questionInGroup : questionsInGroup) {
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

	@Override
	protected String getVisibilityStructure() {
		String section = "<" + getUniqueName() + ">";
		section += "0";
		section += "</" + getUniqueName() + ">";
		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			section += child.getVisibilityStructure();
		}
		return section;
	}
}
