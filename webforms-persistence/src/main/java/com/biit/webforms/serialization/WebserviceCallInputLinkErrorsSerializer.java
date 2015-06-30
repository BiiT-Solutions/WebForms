package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLinkErrors;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class WebserviceCallInputLinkErrorsSerializer implements JsonSerializer<WebserviceCallInputLinkErrors>  {

	@Override
	public JsonElement serialize(WebserviceCallInputLinkErrors src, Type typeOfSrc,
			JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();

		jsonObject.add("errorCode", context.serialize(src.getErrorCode()));
		jsonObject.add("errorMessage", context.serialize(src.getErrorMessage()));

		return jsonObject;
	}
	
}