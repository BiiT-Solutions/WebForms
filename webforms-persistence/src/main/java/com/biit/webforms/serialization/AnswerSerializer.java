package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.webforms.persistence.entity.Answer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class AnswerSerializer extends TreeObjectSerializer<Answer>{
	
	@Override
	public JsonElement serialize(Answer src, Type typeOfSrc,
			JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);
		
		jsonObject.add("description", context.serialize(src.getDescription()));

		return jsonObject;
	}

}
