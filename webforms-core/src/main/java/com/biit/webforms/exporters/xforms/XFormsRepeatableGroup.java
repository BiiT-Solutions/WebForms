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

import com.biit.form.entity.BaseRepeatableGroup;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.exporters.xforms.exceptions.InvalidDateException;
import com.biit.webforms.exporters.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.exporters.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.exporters.xforms.exceptions.StringRuleSyntaxError;

/**
 * Repeated groups are represented as a section + section iteration in Orbeon.
 *
 */
public class XFormsRepeatableGroup extends XFormsGroup {
	protected static final int MIN_REPEATS = 1;
	protected static final int MAX_REPEATS = 100;

	private static final String CSS_CLASS_REPEATABLE_GROUP = "webforms-repeatablegroup";

	public XFormsRepeatableGroup(XFormsHelper xFormsHelper, BaseRepeatableGroup group)
			throws NotValidTreeObjectException, NotValidChildException {
		super(xFormsHelper, group);
	}

	/**
	 * Add section and section-iteration
	 */
	@Override
	protected String getDefinition() {
		String section = "<" + getName() + ">";
		section += "<" + getIteratorControlName() + ">";

		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			section += child.getDefinition();
		}

		section += "</" + getIteratorControlName() + ">";
		section += "</" + getName() + ">";
		return section;
	}

	protected String getIteratorControlName() {
		return getUniqueName() + "-iterator";
	}

	private String getIteratorBindingId() {
		return getUniqueName() + "-iterator-bind";
	}

	private String getIteratorBindingName() {
		return getUniqueName() + "-iterator";
	}

	/**
	 * Add also a dummy section iteration.
	 */
	@Override
	protected void getBinding(StringBuilder binding) throws NotExistingDynamicFieldException, InvalidDateException,
			StringRuleSyntaxError, PostCodeRuleSyntaxError {
		binding.append("<xf:bind id=\"").append(getBindingId()).append("\" name=\"").append(getBindingName())
				.append("\"");
		getRelevantStructure(binding);
		binding.append(" ref=\"").append(getXPath()).append("\" >");
		binding.append("<xf:bind id=\"").append(getIteratorBindingId()).append("\" ");
		binding.append("name=\"").append(getIteratorBindingName()).append("\" ");
		getRelevantStructure(binding);
		binding.append(" ref=\"").append(getXPath() + "/" + getIteratorControlName()).append("\" >");
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
		body.append("class=\"").append(getCssClass()).append("\" ");
		body.append("bind=\"").append(getBindingId()).append("\" ");
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

		String group = "<fr:grid id=\"" + getSectionControlName() + "\" repeat=\"true\" bind=\"" + getBindingId()
				+ "\" origin=\"instance('" + getTemplateName() + "')\" " + minRepeatsTag + maxRepeatsTag + ">";

		return group;
	}

	private String getTemplateName() {
		return getUniqueName() + "-template";
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

	@Override
	protected String getCassGroupClass() {
		return CSS_CLASS_REPEATABLE_GROUP;
	}

}
