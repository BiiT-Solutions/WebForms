package com.biit.webforms.validators.reports;

import com.biit.form.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class OthersOrphanAt extends Report {

	private final TreeObject origin;

	public OthersOrphanAt(TreeObject origin) {
		super(ReportLevel.ERROR, "Element '" + origin.getPathName() + "' has a others flow without any normal flow.");
		this.origin = origin;
	}

	public TreeObject getOrigin() {
		return origin;
	}

}