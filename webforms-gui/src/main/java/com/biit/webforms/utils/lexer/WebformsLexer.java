package com.biit.webforms.utils.lexer;

public class WebformsLexer extends Lexer{
	
	public WebformsLexer() {
		super();
		for(WebformsTokenTypes tokenType: WebformsTokenTypes.values()){
			addTokenDefinition(tokenType);
		}
	}

}
