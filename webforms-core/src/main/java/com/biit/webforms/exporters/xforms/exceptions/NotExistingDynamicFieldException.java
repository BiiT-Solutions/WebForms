package com.biit.webforms.exporters.xforms.exceptions;

public class NotExistingDynamicFieldException extends Exception {
	private static final long serialVersionUID = 200325249326305317L;

	public NotExistingDynamicFieldException() {
		super();
	}

	public NotExistingDynamicFieldException(String message) {
		super(message);
	}
}
