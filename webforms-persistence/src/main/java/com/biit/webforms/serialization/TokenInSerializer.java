package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.webforms.persistence.entity.condition.TokenIn;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class TokenInSerializer extends TokenSerializer<TokenIn>{

	@Override
	public JsonElement serialize(TokenIn src, Type typeOfSrc,
			JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);
		
		jsonObject.add("question_id", context.serialize(src.getQuestion().getPath()));
		jsonObject.add("values", context.serialize(src.getValues()));

		return jsonObject;
	}

}