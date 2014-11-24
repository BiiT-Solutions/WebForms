package com.biit.webforms.persistence.xforms.exceptions;

public class DuplicatedXFormException extends DuplicatedKeyException {
	private static final long serialVersionUID = -2500992570402055002L;

	public DuplicatedXFormException(String info) {
		super(info);
	}
}
