package com.biit.webforms.validators.reports;

import com.biit.form.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class IncompleteLogicReport extends Report {

	private TreeObject element;

	public IncompleteLogicReport(TreeObject element) {
		super(ReportLevel.ERROR, generateReport(element));
		this.element = element;
	}

	private static String generateReport(TreeObject element) {
		return "Condition in Flows from element '" + element.getPathName() + "' are incomplete.";
	}

	public TreeObject getElement() {
		return element;
	}

}
