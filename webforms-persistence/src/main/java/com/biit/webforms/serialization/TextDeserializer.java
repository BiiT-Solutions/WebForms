package com.biit.webforms.serialization;

import com.biit.form.json.serialization.TreeObjectDeserializer;
import com.biit.webforms.persistence.entity.Text;
import com.biit.webforms.persistence.entity.TreeObjectImage;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TextDeserializer extends TreeObjectDeserializer<Text> {

	public TextDeserializer() {
		super(Text.class);
	}

	@Override
	public void deserialize(JsonElement json, JsonDeserializationContext context, Text element) {
		JsonObject jobject = (JsonObject) json;

		element.setDescription(parseString("description", jobject, context));
		element.setImage((TreeObjectImage) context.deserialize(jobject.get("image"), TreeObjectImage.class));

		super.deserialize(json, context, element);
	}
}
