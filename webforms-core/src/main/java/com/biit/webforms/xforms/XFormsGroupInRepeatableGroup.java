package com.biit.webforms.xforms;

import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.persistence.entity.Group;

/**
 * Orbeon Sections are not allowed in loops. Group representation is ignored.
 */
public class XFormsGroupInRepeatableGroup extends XFormsGroup {

	public XFormsGroupInRepeatableGroup(Group group) throws NotValidTreeObjectException, NotValidChildException {
		super(group);
	}

	/**
	 * Skip graphical representation of the group.
	 */
	@Override
	public String getSectionBody() {
		String section = "";
		for (XFormsObject child : getChildren()) {
			section += child.getSectionBody();
		}
		return section;
	}

}
