package com.biit.webforms.utils.parser.expressions;

import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.utils.lexer.Token;

public class Reference extends Expression {

	private List<String> references;

	public Reference(List<Token> tokens) {
		references = new ArrayList<>();
		for (Token token : tokens) {
			references.add(token.getContent());
		}
	}

	public List<String> getReferences() {
		return references;
	}

	@Override
	public void getString(StringBuilder builder) {
		builder.append(references);
	}

}