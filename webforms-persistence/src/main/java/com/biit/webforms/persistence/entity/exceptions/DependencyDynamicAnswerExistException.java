package com.biit.webforms.persistence.entity.exceptions;

import com.biit.form.exceptions.DependencyExistException;

public class DependencyDynamicAnswerExistException extends DependencyExistException {
	private static final long serialVersionUID = -5148630374607727024L;
	
	public DependencyDynamicAnswerExistException(String message) {
		super(message);
	}

}
