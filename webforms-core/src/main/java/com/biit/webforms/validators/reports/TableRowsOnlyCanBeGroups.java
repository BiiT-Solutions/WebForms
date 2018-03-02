package com.biit.webforms.validators.reports;

import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class TableRowsOnlyCanBeGroups extends Report {
	private final TreeObject treeObject;

	public TableRowsOnlyCanBeGroups(TreeObject treeObject) {
		super(ReportLevel.ERROR, "Element '" + treeObject.getPathName() + "' is not a group. Tables only can have groups as columns");
		this.treeObject = treeObject;
	}

	public TreeObject getTreeObject() {
		return treeObject;
	}
}