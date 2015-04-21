package com.biit.webforms.validators.reports;

import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class FormElementWithoutFlowIn extends Report {

	private final TreeObject origin;

	public FormElementWithoutFlowIn(TreeObject origin) {
		super(ReportLevel.ERROR, "Element '" + origin.getPathName() + "' doesn't have flow in.");
		this.origin = origin;
	}

	public TreeObject getOrigin() {
		return origin;
	}

}