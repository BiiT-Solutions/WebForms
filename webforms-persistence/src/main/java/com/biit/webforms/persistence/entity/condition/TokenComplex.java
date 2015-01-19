package com.biit.webforms.persistence.entity.condition;

import java.util.List;

public abstract class TokenComplex extends Token {
	private static final long serialVersionUID = 7516931985658776753L;

	public abstract List<Token> getSimpleTokens();
}
