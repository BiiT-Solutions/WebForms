package com.biit.webforms.validators.reports;

import com.biit.form.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class MultipleFlowsWithSameOriginAndDestiny extends Report {

	private TreeObject origin;
	private TreeObject destination;

	public MultipleFlowsWithSameOriginAndDestiny(TreeObject treeObject, TreeObject treeObject2) {
		super(ReportLevel.ERROR, generateReport(treeObject, treeObject2));
		this.origin = treeObject;
		this.destination = treeObject2;
	}

	private static String generateReport(TreeObject treeObject, TreeObject treeObject2) {
		return "There are multiple flows between element '" + treeObject.getPathName() + "' and '"
				+ treeObject2.getPathName() + "'";
	}

	public TreeObject getOrigin() {
		return origin;
	}

	public TreeObject getDestination() {
		return destination;
	}

}
