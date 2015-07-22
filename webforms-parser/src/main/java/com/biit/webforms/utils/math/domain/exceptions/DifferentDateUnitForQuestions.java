package com.biit.webforms.utils.math.domain.exceptions;

import java.util.Set;

import com.biit.webforms.persistence.entity.WebformsBaseQuestion;

public class DifferentDateUnitForQuestions extends Exception {
	private static final long serialVersionUID = 8266149757522953344L;

	private final Set<WebformsBaseQuestion> questionsAffected;

	public DifferentDateUnitForQuestions(Set<WebformsBaseQuestion> questionsAffected) {
		this.questionsAffected = questionsAffected;
	}

	public Set<WebformsBaseQuestion> getQuestionsAffected() {
		return questionsAffected;
	}
}
