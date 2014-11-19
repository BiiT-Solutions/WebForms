package com.biit.webforms.persistence.entity.condition;

import java.util.List;

public abstract class TokenComplex extends Token{

	public abstract List<Token> getSimpleTokens();
}
