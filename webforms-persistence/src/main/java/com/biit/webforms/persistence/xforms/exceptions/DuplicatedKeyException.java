package com.biit.webforms.persistence.xforms.exceptions;

public class DuplicatedKeyException extends Exception {
	private static final long serialVersionUID = 938673235792427341L;

	public DuplicatedKeyException(String info) {
		super(info);
	}

}
