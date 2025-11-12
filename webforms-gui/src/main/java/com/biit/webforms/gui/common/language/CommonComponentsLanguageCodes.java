package com.biit.webforms.gui.common.language;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
	ERROR_NOT_AUTHORIZED("error.user.not.authorized"),
	ACCEPT_CANCEL_WINDOW_CAPTION_ACCEPT("accept.cancel.window.caption.accept"),
	ACCEPT_CANCEL_WINDOW_CAPTION_CANCEL("accept.cancel.window.caption.cancel"),
	ACCEPT_CANCEL_WINDOW_TOOLTIP_ACCEPT("accept.cancel.window.tooltip.accept"),
	ACCEPT_CANCEL_WINDOW_TOOLTIP_CANCEL("accept.cancel.window.tooltip.cancel"),
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
