package com.biit.webforms.utils.lexer;

public class WebformsLexer extends Lexer{
	
	public WebformsLexer() {
		super();
		for(TokenTypes tokenType: TokenTypes.values()){
			addTokenDefinition(tokenType);
		}
	}

}
