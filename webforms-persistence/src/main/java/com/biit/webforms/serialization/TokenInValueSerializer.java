package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.webforms.persistence.entity.condition.TokenInValue;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class TokenInValueSerializer extends StorableObjectSerializer<TokenInValue> {

	@Override
	public JsonElement serialize(TokenInValue src, Type typeOfSrc,
			JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src,
				typeOfSrc, context);

		jsonObject.add("answer_id",context.serialize(src.getAnswerValue().getPath()));

		return jsonObject;
	}

}