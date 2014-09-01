package com.biit.webforms.authentication.exception;

public class SameOriginAndDestinationException extends Exception{
	private static final long serialVersionUID = -8672734219700947649L;

	public SameOriginAndDestinationException(String message) {
		super(message);
	}
}
