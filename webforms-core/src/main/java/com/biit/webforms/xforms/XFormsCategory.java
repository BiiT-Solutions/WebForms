package com.biit.webforms.xforms;

import com.biit.form.BaseCategory;
import com.biit.form.BaseGroup;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;

/**
 * Categories are Sections in Orbeon. The same as groups.
 * 
 */
public class XFormsCategory extends XFormsGroup {

	private static final String CSS_CLASS_CATEGORY = "webforms-category";

	public XFormsCategory(XFormsHelper xFormsHelper, BaseCategory category) throws NotValidTreeObjectException,
			NotValidChildException {
		super(xFormsHelper, category);
	}

	@Override
	protected void setSource(BaseGroup treeObject) throws NotValidTreeObjectException {
		if (treeObject instanceof BaseCategory) {
			super.setSource(treeObject);
		} else {
			throw new NotValidTreeObjectException("Invalid source!");
		}
	}

	@Override
	protected String getCassGroupClass() {
		return CSS_CLASS_CATEGORY;
	}

}
