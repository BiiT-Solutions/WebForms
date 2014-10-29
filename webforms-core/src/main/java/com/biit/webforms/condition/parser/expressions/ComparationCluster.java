package com.biit.webforms.condition.parser.expressions;

import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.utils.parser.Expression;

public class ComparationCluster extends Expression{

	private Token token;
	
	public ComparationCluster(Token token) {
		this.token = token;
	}
	
	@Override
	public void getString(StringBuilder builder) {
		builder.append(token.toString());
	}

}
