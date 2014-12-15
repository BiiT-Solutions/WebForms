package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.Answer;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AnswerDeserializer  extends TreeObjectDeserializer<Answer> {

	public AnswerDeserializer() {
		super(Answer.class);
	}

	public void deserialize(JsonElement json, JsonDeserializationContext context, Answer element) {
		JsonObject jobject = (JsonObject) json;

		element.setDescription(parseString("description", jobject, context));

		super.deserialize(json, context, element);
	}
	
}
