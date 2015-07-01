package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.webforms.persistence.entity.webservices.WebserviceCall;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class WebserviceCallSerializer extends StorableObjectSerializer<WebserviceCall>  {

	@Override
	public JsonElement serialize(WebserviceCall src, Type typeOfSrc,
			JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);

		jsonObject.add("name", context.serialize(src.getName()));
		jsonObject.add("webserviceName", context.serialize(src.getWebserviceName()));
		jsonObject.add("formElementTrigger_id", context.serialize(src.getFormElementTrigger().getPath()));
		jsonObject.add("inputLinks", context.serialize(src.getInputLinks()));
		jsonObject.add("outputLinks", context.serialize(src.getOutputLinks()));

		return jsonObject;
	}
	
}
