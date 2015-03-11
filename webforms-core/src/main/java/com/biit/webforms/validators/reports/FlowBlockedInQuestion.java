package com.biit.webforms.validators.reports;

import com.biit.form.BaseQuestion;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class FlowBlockedInQuestion extends Report {
	
	private final BaseQuestion element;

	public FlowBlockedInQuestion(BaseQuestion element) {
		super(ReportLevel.ERROR, generateMessage(element));
		this.element = element;
	}

	private static String generateMessage(BaseQuestion element) {
		return "The flow in question '"+element.getPathName()+"' will be blocked as one or more exit flow conditions use questions that can be bypassed.";
	}

	public BaseQuestion getElement() {
		return element;
	}

}
