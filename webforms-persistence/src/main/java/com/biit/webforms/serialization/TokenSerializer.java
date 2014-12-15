package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.webforms.persistence.entity.condition.Token;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class TokenSerializer<T extends Token> extends StorableObjectSerializer<T>{

	@Override
	public JsonElement serialize(T src, Type typeOfSrc,
			JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);
		
		jsonObject.add("type", context.serialize(src.getType()));

		return jsonObject;
	}
	
}
