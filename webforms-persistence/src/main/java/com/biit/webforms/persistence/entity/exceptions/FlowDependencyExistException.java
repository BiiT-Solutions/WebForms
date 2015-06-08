package com.biit.webforms.persistence.entity.exceptions;

import com.biit.form.exceptions.DependencyExistException;

public class FlowDependencyExistException extends DependencyExistException {
	private static final long serialVersionUID = 6319874852643449215L;

	public FlowDependencyExistException(String message) {
		super(message);
	}

}
