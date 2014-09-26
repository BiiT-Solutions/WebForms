package com.biit.webforms.utils.lexer;

public abstract class Token {

	private String originalString;
	private int position;
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

	public void setOriginalString(String originalString, int position) {
		this.originalString = originalString;
		this.position = position;
	}
	
	public int getPosition(){
		return position;
	}
	
	public String getTypeName(){
		return getClass().getSimpleName();
	}
}
