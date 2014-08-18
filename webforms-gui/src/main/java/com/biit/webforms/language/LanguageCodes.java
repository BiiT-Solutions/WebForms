package com.biit.webforms.language;

public enum LanguageCodes {
	CAPTION_ANSWER_FORMAT_TEXT("caption.answerFormat.text"),
	CAPTION_ANSWER_FORMAT_NUMBER("caption.answerFormat.number"),
	CAPTION_ANSWER_FORMAT_DATE("caption.answerFormat.date"),
	CAPTION_ANSWER_FORMAT_POSTAL_CODE("caption.answerFormat.postalCode"),
	CAPTION_ANSWER_TYPE_INPUT_FIELD("caption.answerType.inputField"),
	CAPTION_ANSWER_TYPE_RADIO_BUTTON("caption.answerType.radioButton"),
	CAPTION_ANSWER_TYPE_MULTI_CHECKBOX("caption.answerType.multiCheckbox"),
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
	LOGIN_ERROR_MESSAGE_ENCRYPTINGPASSWORD("login.errorMessage.password")
	;

	private String value;

	private LanguageCodes(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
