package com.biit.webforms.utils.math.domain.exceptions;

import java.util.Set;

import com.biit.webforms.persistence.entity.Question;

public class DifferentDateUnitForQuestions extends Exception {
	private static final long serialVersionUID = 8266149757522953344L;
	
	private final Set<Question> questionsAffected;
	
	public DifferentDateUnitForQuestions(Set<Question> questionsAffected) {
		this.questionsAffected = questionsAffected;
	}

	public Set<Question> getQuestionsAffected() {
		return questionsAffected;
	}
}
