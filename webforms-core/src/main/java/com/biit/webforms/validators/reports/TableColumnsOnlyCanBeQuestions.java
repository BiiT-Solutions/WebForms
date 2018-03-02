package com.biit.webforms.validators.reports;

import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class TableColumnsOnlyCanBeQuestions extends Report {
	private final TreeObject treeObject;

	public TableColumnsOnlyCanBeQuestions(TreeObject treeObject) {
		super(ReportLevel.ERROR, "Element '" + treeObject.getPathName() + "' is not a question. Tables only can have questions as rows");
		this.treeObject = treeObject;
	}

	public TreeObject getTreeObject() {
		return treeObject;
	}
}