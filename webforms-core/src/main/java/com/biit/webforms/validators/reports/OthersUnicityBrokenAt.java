package com.biit.webforms.validators.reports;

import com.biit.form.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class OthersUnicityBrokenAt extends Report {

	private final TreeObject origin;

	public OthersUnicityBrokenAt(TreeObject origin) {
		super(ReportLevel.ERROR, "Element '" + origin + "' has more than one others flow");
		this.origin = origin;
	}

	public TreeObject getOrigin() {
		return origin;
	}

}
