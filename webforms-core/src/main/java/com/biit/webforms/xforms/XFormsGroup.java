package com.biit.webforms.xforms;

import com.biit.form.TreeObject;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

/**
 * Groups are represented as a nested Sections in Orbeon.
 * 
 */
public class XFormsGroup extends XFormsObject {

	public XFormsGroup(Group group) throws NotValidTreeObjectException, NotValidChildException {
		super(group);
	}

	@Override
	protected void setSource(TreeObject treeObject) throws NotValidTreeObjectException {
		if (treeObject instanceof Group) {
			super.setSource(treeObject);
		} else {
			throw new NotValidTreeObjectException("Invalid source!");
		}
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

	/**
	 * Groups are represented as sections.
	 */
	@Override
	public String getSectionBody() {
		String section = "";
		section += "<fr:section id=\"" + getSectionControlName() + "\" bind=\"" + getBindingName() + "\">";
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
}
