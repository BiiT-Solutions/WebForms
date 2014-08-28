package com.biit.webforms.persistence.entity;

public enum AnswerType {
	SINGLE_SELECTION,

	MULTIPLE_SELECTION,

	// Uses answer format.
	INPUT;

	public boolean isInputField() {
		return this.equals(AnswerType.INPUT);
	}
}
