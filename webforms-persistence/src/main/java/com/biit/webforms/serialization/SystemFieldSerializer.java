package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.webforms.persistence.entity.SystemField;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class SystemFieldSerializer extends TreeObjectSerializer<SystemField>{

	@Override
	public JsonElement serialize(SystemField src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);
		
		jsonObject.add("fieldName", context.serialize(src.getFieldName()));

		return jsonObject;
	}
	
}