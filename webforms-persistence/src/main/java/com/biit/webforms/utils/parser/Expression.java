package com.biit.webforms.utils.parser;

public abstract class Expression {

	public abstract void getString(StringBuilder builder);

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		getString(builder);
		return builder.toString();
	}
}
