package com.biit.webforms.utils.lexer;

public class WebformsLexer extends Lexer{
	
	public WebformsLexer() {
		super();
		for(WebformTokenTypes tokenType: WebformTokenTypes.values()){
			addTokenDefinition(tokenType);
		}
	}

}
