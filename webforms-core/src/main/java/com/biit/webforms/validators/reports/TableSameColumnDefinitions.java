package com.biit.webforms.validators.reports;

import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class TableSameColumnDefinitions extends Report {

	public TableSameColumnDefinitions(TreeObject question1, TreeObject question2) {
		super(ReportLevel.ERROR, "Question '" + question2.getPathName() + "' must have the same definition as '" + question1.getPathName() + "'.");
	}

}