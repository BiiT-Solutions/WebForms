package com.biit.webforms.validators;

import com.biit.abcd.persistence.entity.Form;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.Question;

public class QuestionCompatibilityError extends Report {

	public QuestionCompatibilityError(Question question, Form abcdForm, com.biit.abcd.persistence.entity.Question abcdQuestion) {
		super(ReportLevel.ERROR, generateReport(question,abcdForm,abcdQuestion));
	}

	private static String generateReport(Question question, Form abcdForm, com.biit.abcd.persistence.entity.Question abcdQuestion) {
		StringBuilder sb = new StringBuilder();
		sb.append("Form '");
		sb.append(abcdForm);
		sb.append("' Version: ");
		sb.append(abcdForm.getVersion());
		sb.append(" Question '");
		sb.append(abcdQuestion.getPathName());
		sb.append("' is not compatible. Type '");
		sb.append(abcdQuestion.getAnswerType());
		sb.append("' ");
		if(abcdQuestion.getAnswerFormat()!=null){
			sb.append(" Format '");
			sb.append(abcdQuestion.getAnswerFormat());
			sb.append("' ");
		}
		
		return sb.toString();
	}

}
