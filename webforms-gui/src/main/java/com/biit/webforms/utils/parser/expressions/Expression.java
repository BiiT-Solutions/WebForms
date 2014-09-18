package com.biit.webforms.utils.parser.expressions;

public abstract class Expression {

	public abstract void getString(StringBuilder builder);
	
	public String getString(){
		StringBuilder builder = new StringBuilder();
		getString(builder);
		return builder.toString();
	}
	
}
