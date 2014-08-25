package com.biit.webforms.gui.common.language;

public enum CommonComponentsLanguageCodes implements ILanguageCode {
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
	ERROR_UNEXPECTED_ERROR("securedWebPageComponent.errorMessage.unexpectedError"),
	ERROR_CONTACT("commonComponent.errorMessage.errorContact"),
	ACCEPT_CANCEL_WINDOW_CAPTION_ACCEPT("accept.cancel.windo.caption.accept"),
	ACCEPT_CANCEL_WINDOW_CAPTION_CANCEL("accept.cancel.windo.caption.cancel"),
	ACCEPT_CANCEL_WINDOW_TOOLTIP_ACCEPT("accept.cancel.windo.tooltip.accept"),
	ACCEPT_CANCEL_WINDOW_TOOLTIP_CANCEL("accept.cancel.windo.tooltip.cancel"),
	FORM_TREE_PROPERTY_NAME("form.tree.property.name"), 
	
	TREE_OBJECT_PROPERTIES_CREATED_BY("storable.object.properties.caption.createdBy"),
	TREE_OBJECT_PROPERTIES_CREATION_TIME("storable.object.properties.caption.createDate"), 
	TREE_OBJECT_PROPERTIES_UPDATED_BY("storable.object.properties.caption.updatedBy"), 
	TREE_OBJECT_PROPERTIES_UPDATE_TIME("storable.object.properties.caption.updateDate"), 
	TREE_OBJECT_PROPERTIES_COMMON_FORM_CAPTION("storable.object.properties.caption.treeObjectProperties"), 
	
	;
	
	private String value;
	
	CommonComponentsLanguageCodes(String value){
		this.value = value;
	}

	@Override
	public String getCode() {
		return value;
	}

	@Override
	public String translation() {
		return ServerTranslate.translate(value);
	}
	
}
