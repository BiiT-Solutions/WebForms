package com.biit.webforms.validators.reports;

import com.biit.form.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class MultipleEndLoopsFromSameElement extends Report {

	public MultipleEndLoopsFromSameElement(TreeObject origin) {
		super(ReportLevel.ERROR, generateReport(origin));
	}

	private static String generateReport(TreeObject origin) {
		return "There are multiple END_LOOP flows from element '" + origin.getPathName() + "'";
	}

}
