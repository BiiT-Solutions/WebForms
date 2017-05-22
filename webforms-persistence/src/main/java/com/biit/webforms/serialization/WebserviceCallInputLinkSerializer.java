package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.form.json.serialization.StorableObjectSerializer;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class WebserviceCallInputLinkSerializer extends StorableObjectSerializer<WebserviceCallInputLink>  {

	@Override
	public JsonElement serialize(WebserviceCallInputLink src, Type typeOfSrc,
			JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);

		jsonObject.add("webservicePort", context.serialize(src.getWebservicePort()));
		jsonObject.add("formElement_id", context.serialize(src.getFormElement().getPath()));
		jsonObject.add("errors", context.serialize(src.getErrors()));		
		jsonObject.add("validationXpath", context.serialize(src.getValidationXpath()));

		return jsonObject;
	}
	
}