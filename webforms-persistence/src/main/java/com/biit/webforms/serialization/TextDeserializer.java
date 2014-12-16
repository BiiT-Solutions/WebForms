package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.Text;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TextDeserializer extends TreeObjectDeserializer<Text> {

	public TextDeserializer() {
		super(Text.class);
	}

	public void deserialize(JsonElement json, JsonDeserializationContext context, Text element) {
		JsonObject jobject = (JsonObject) json;

		element.setDescription(parseString("description", jobject, context));

		super.deserialize(json, context, element);
	}
}
