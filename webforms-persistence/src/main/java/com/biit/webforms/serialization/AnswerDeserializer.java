package com.biit.webforms.serialization;

import com.biit.form.json.serialization.TreeObjectDeserializer;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.TreeObjectImage;
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
		element.setImage((TreeObjectImage) context.deserialize(jobject.get("image"), TreeObjectImage.class));

		super.deserialize(json, context, element);
	}
	
}
