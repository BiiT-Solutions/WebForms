package com.biit.webforms.xforms;

import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

public class XFormsGroup extends XFormsObject {

	public XFormsGroup(Group group) throws NotValidTreeObjectException, NotValidChildException {
		super(group);
	}

	/**
	 * Groups has not definition section. Only add its children.
	 */
	@Override
	protected String getDefinition() {
		String section = "";
		for (XFormsObject child : getChildren()) {
			section += child.getDefinition();
		}
		return section;
	}

	/**
	 * Groups has not binding section. Only add its children.
	 */
	@Override
	protected String getBinding() throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		String elementBinding = "";
		// Add also children.
		for (XFormsObject child : getChildren()) {
			elementBinding += child.getBinding();
		}
		return elementBinding;
	}

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
	 * Groups has no resources section. Only add its children.
	 */
	@Override
	protected String getResources() throws NotExistingDynamicFieldException {
		String resource = "";

		for (XFormsObject child : getChildren()) {
			resource += child.getResources();
		}

		return resource;
	}

}
