package com.biit.webforms.persistence.entity.exceptions;

public class FormIsUsedAsReferenceException extends Exception {
	private static final long serialVersionUID = -1173327363086198898L;

	public FormIsUsedAsReferenceException(String message) {
		super(message);
	}
}
