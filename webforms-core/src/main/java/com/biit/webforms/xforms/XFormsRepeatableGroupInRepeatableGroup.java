package com.biit.webforms.xforms;

import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.persistence.entity.Group;

/**
 * Orbeon Sections are not allowed in loops. Must be represented in another way.
 */
public class XFormsRepeatableGroupInRepeatableGroup extends XFormsRepeatableGroup {

	public XFormsRepeatableGroupInRepeatableGroup(Group group) throws NotValidTreeObjectException,
			NotValidChildException {
		super(group);
	}
	
	/**
	 * Cannot be in a section. Only add the loop.
	 */
	@Override
	protected String getSectionBody() {
		String section = "";
		//Loop
		section += getLoopHeaderTags(MIN_REPEATS, MAX_REPEATS);
		for (XFormsObject child : getChildren()) {
			section += child.getSectionBody();
		}
		section += "</fr:grid>";
		return section;
	}

}
