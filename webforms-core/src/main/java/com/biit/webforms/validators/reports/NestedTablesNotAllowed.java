package com.biit.webforms.validators.reports;

import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class NestedTablesNotAllowed extends Report {
	private final TreeObject treeObject;

	public NestedTablesNotAllowed(TreeObject treeObject) {
		super(ReportLevel.ERROR, "Group '" + treeObject.getPathName() + "' cannot be defined as a table. Nested tables are not allowed.");
		this.treeObject = treeObject;
	}

	public TreeObject getTreeObject() {
		return treeObject;
	}
}