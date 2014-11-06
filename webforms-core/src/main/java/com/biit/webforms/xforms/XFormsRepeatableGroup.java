package com.biit.webforms.xforms;

import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

public class XFormsRepeatableGroup extends XFormsObject {

	public XFormsRepeatableGroup(Group group) throws NotValidTreeObjectException, NotValidChildException {
		super(group);
	}

	/**
	 * Nested grids are not allowed in orbeon. Only first group is added.
	 */
	@Override
	protected String getSectionBody() {
		String section = "<fr:grid>";
		for (XFormsObject child : getChildren()) {
			section += child.getSectionBody();
		}
		section += "</fr:grid>";
		return section;
	}

	/**
	 * Groups has no labels in resources section. Only add its children.
	 */
	@Override
	protected String getResources() throws NotExistingDynamicFieldException {
		String resource = "<" + getControlName() + ">";

		for (XFormsObject child : getChildren()) {
			resource += child.getResources();
		}

		resource += "</" + getControlName() + ">";

		return resource;
	}

	@Override
	public String getBinding() throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
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

}
