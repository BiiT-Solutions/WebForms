package com.biit.webforms.utils.lexer;

public abstract class Token {

	private String originalString;
	public ITokenType type; 
	
	public ITokenType getType() {
		return type;
	}

	public void setType(ITokenType type) {
		this.type = type;
	}

	public String getContent() {
		return originalString;
	}

	public void setOriginalString(String originalString) {
		this.originalString = originalString;
	}
	
	public String getTypeName(){
		return getClass().getSimpleName();
	}
}
