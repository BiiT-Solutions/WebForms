package com.biit.webforms.validators.reports;

import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class TableSameColumnsNumber extends Report {

	public TableSameColumnsNumber(TreeObject group1, TreeObject group2) {
		super(ReportLevel.ERROR, "Group '" + group1.getPathName() + "' must have the same questions as '" + group2.getPathName() + "'.");
	}

}