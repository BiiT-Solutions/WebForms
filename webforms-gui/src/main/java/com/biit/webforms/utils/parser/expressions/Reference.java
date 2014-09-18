package com.biit.webforms.utils.parser.expressions;

import com.biit.webforms.utils.lexer.Token;

public class Reference extends Expression{

	private String referenceString;
	
	public Reference(Token token){
		setReferenceString(token.getContent());
	}

	public String getReferenceString() {
		return referenceString;
	}

	private void setReferenceString(String referenceString) {
		this.referenceString = referenceString;
	}

	@Override
	public void getString(StringBuilder builder) {
		builder.append(referenceString);
	}
	
}