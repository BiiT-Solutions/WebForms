package com.biit.webforms.gui.common.language;

public enum CommonLanguageCodes implements ILanguageCode {
	LOGIN_CAPTION_EMAIL("login.caption.email"),
	LOGIN_ERROR_EMAIL("login.error.email"),
	LOGIN_CAPTION_PASSWORD("login.caption.password"),
	LOGIN_ERROR_PASSWORD("login.error.password"),
	LOGIN_CAPTION_SIGN_IN("login.caption.signIn"),
	LOGIN_ERROR_MESSAGE_MESSAGE_USER("login.errorMessage.user"),
	LOGIN_ERROR_MESSAGE_MESSAGE_BADUSERPSWD("login.errorMessage.badUserPassword"),
	LOGIN_ERROR_MESSAGE_TRYAGAIN("login.errorMessage.tryAgain"),
	LOGIN_ERROR_MESSAGE_USER_SERVICE("login.errorMessage.userService"),
	LOGIN_ERROR_MESSAGE_CONTACT("login.errorMessage.contact"),
	LOGIN_ERROR_MESSAGE_ENCRYPTINGPASSWORD("login.errorMessage.password"),
	ERROR_UNEXPECTED_ERROR("securedWebPageComponent.errorMessage.unexpectedError")
	;
	
	private String value;
	
	CommonLanguageCodes(String value){
		this.value = value;
	}

	@Override
	public String getCode() {
		return value;
	}
	
}
