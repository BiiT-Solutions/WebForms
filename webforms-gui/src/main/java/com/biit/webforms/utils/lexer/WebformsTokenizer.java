package com.biit.webforms.utils.lexer;

public class WebformsTokenizer extends Tokenizer{
	
	public WebformsTokenizer() {
		super();
		for(WebformTokenTypes tokenType: WebformTokenTypes.values()){
			addTokenDefinition(tokenType);
		}
	}

}
