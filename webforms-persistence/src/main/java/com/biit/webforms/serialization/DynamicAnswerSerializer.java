package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.webforms.persistence.entity.DynamicAnswer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class DynamicAnswerSerializer extends TreeObjectSerializer<DynamicAnswer>{

	@Override
	public JsonElement serialize(DynamicAnswer src, Type typeOfSrc,	JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);
		
		jsonObject.add("reference", context.serialize(src.getReference().getPath()));

		return jsonObject;
	}
	
}
