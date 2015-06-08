package com.biit.webforms.validators.reports;

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.DynamicAnswer;
import com.biit.webforms.persistence.entity.Question;

/**
 * Report when one or more dynamic answers reference the same element as a
 * previous one.
 *
 */
public class MultipleDynamicAnswersReferenceTheSameQuestion extends Report {

	private Question question;
	private DynamicAnswer answer;

	public MultipleDynamicAnswersReferenceTheSameQuestion(Question question, DynamicAnswer answer) {
		super(ReportLevel.ERROR, generateReport(question, answer));
		this.question = question;
		this.answer = answer;
	}

	private static String generateReport(Question question, DynamicAnswer answer) {
		StringBuilder sb = new StringBuilder();
		sb.append("Question '");
		sb.append(question.getPathName());

		sb.append("' has one or more dynamic answers that refere '" + answer.getReference() + "'");
		return sb.toString();
	}

	public Question getQuestion() {
		return question;
	}

	public DynamicAnswer getAnswer() {
		return answer;
	}
}
