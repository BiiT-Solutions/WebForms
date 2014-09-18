package com.biit.webforms.utils.parser;

import com.biit.webforms.utils.lexer.ITokenType;

public class ExpectedTokenNotFound extends Exception {
	private static final long serialVersionUID = 3489582832259193623L;
	private ITokenType expected;
	private ITokenType type;

	public ExpectedTokenNotFound(ITokenType expected, ITokenType type) {
		super("Expected token " + expected + " and found " + type);
		this.expected = expected;
		this.type = type;
	}

	public ITokenType getExpected() {
		return expected;
	}

	public void setExpected(ITokenType expected) {
		this.expected = expected;
	}

	public ITokenType getType() {
		return type;
	}

	public void setType(ITokenType type) {
		this.type = type;
	}
}
