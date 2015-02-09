package com.biit.webforms.validators;

import com.biit.form.BaseForm;
import com.biit.form.BaseQuestion;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class QuestionCompatibilityError extends Report {

	public QuestionCompatibilityError(BaseQuestion question1, BaseForm form, BaseQuestion question2) {
		super(ReportLevel.ERROR, generateReport(question1, form, question2));
	}

	private static String generateReport(BaseQuestion question1, BaseForm form, BaseQuestion question2) {
		StringBuilder sb = new StringBuilder();
		sb.append("Form '");
		sb.append(form.getLabel());
		sb.append("' Version ");
		sb.append(form.getVersion());
		sb.append(" Question '");
		sb.append(question2.getPathName());
		sb.append("' is not compatible.");
		if (question2 instanceof com.biit.abcd.persistence.entity.Question) {
			sb.append(" '");
			sb.append(((com.biit.abcd.persistence.entity.Question) question2).getAnswerType());
			sb.append("' ");
			if (((com.biit.abcd.persistence.entity.Question) question2).getAnswerFormat() != null) {
				sb.append(" Format '");
				sb.append(((com.biit.abcd.persistence.entity.Question) question2).getAnswerFormat());
				sb.append("' ");
			}
		}

		return sb.toString();
	}

}
