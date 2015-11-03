package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.TreeObjectImage;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TreeObjectImageDeserializer extends StorableObjectDeserializer<TreeObjectImage> {

	@Override
	public void deserialize(JsonElement json, JsonDeserializationContext context, TreeObjectImage element) {
		JsonObject jobject = (JsonObject) json;

		element.setFileName(parseString("fileName", jobject, context));
		element.setWidth(parseInteger("width", jobject, context));
		element.setHeight(parseInteger("height", jobject, context));
		element.fromBase64(parseString("data", jobject, context));

		super.deserialize(json, context, element);
	}
}
