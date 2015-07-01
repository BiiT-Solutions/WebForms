package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.webforms.persistence.entity.webservices.WebserviceCallOutputLink;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class WebserviceCallOutputLinkSerializer extends StorableObjectSerializer<WebserviceCallOutputLink>  {

	@Override
	public JsonElement serialize(WebserviceCallOutputLink src, Type typeOfSrc,
			JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);
		
		jsonObject.add("webservicePort", context.serialize(src.getWebservicePort()));
		jsonObject.add("formElement_id", context.serialize(src.getFormElement().getPath()));
		jsonObject.add("isEditable", context.serialize(src.isEditable()));

		return jsonObject;
	}
	
}