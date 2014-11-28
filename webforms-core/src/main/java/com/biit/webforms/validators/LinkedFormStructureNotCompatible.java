package com.biit.webforms.validators;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Form;

public class LinkedFormStructureNotCompatible extends Report {

	public LinkedFormStructureNotCompatible(Form form, com.biit.abcd.persistence.entity.Form abcdForm) {
		super(ReportLevel.ERROR, generateReport(form, abcdForm));
	}

	private static String generateReport(Form form, com.biit.abcd.persistence.entity.Form abcdForm) {
		StringBuilder sb = new StringBuilder();
		sb.append("Form '");
		sb.append(form.getLabel());
		sb.append("' Version '");
		sb.append(abcdForm.getVersion());
		sb.append("' is not compatible with '");
		sb.append(abcdForm);
		sb.append("' version: ");
		sb.append(abcdForm.getVersion());
		return sb.toString();
	}

}
