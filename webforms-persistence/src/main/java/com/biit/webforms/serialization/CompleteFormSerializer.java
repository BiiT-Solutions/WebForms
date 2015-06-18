package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.webforms.persistence.entity.CompleteFormView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class CompleteFormSerializer extends BaseFormSerializer<CompleteFormView> {

	@Override
	public JsonElement serialize(CompleteFormView src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);

		jsonObject.add("description", context.serialize(src.getDescription()));
		jsonObject.add("flows", context.serialize(src.getFlows()));

		return jsonObject;
	}

}
