package com.biit.webforms.validators.reports;

import com.biit.form.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class MultipleEndLoopsFromSameElement extends Report {

	private TreeObject origin;

	public MultipleEndLoopsFromSameElement(TreeObject origin) {
		super(ReportLevel.ERROR, generateReport(origin));
		this.origin = origin;
	}

	private static String generateReport(TreeObject origin) {
		return "There are multiple END_LOOP flows from element '" + origin.getPathName() + "'";
	}

	public TreeObject getOrigin() {
		return origin;
	}

}
