package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.webforms.persistence.entity.Form;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class FormSerializer extends BaseFormSerializer<Form> {

	@Override
	public JsonElement serialize(Form src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);

		jsonObject.add("description", context.serialize(src.getDescription()));
		jsonObject.add("flows", context.serialize(src.getFlows()));
		jsonObject.add("webserviceCalls", context.serialize(src.getWebserviceCalls()));

		return jsonObject;
	}

}
