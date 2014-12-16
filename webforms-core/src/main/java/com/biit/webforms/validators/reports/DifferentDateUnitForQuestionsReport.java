package com.biit.webforms.validators.reports;

import java.util.Set;

import com.biit.form.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Question;

public class DifferentDateUnitForQuestionsReport extends Report {

	public DifferentDateUnitForQuestionsReport(TreeObject element, Set<Question> questions) {
		super(ReportLevel.ERROR, generateReport(element,questions));
	}

	private static String generateReport(TreeObject element, Set<Question> questions) {
		return "Flows that start from '"+element.getPathName()+"' have different time units for questions "+questions;
	}

}
