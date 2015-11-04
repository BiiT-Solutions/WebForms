package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.webforms.persistence.entity.TreeObjectImage;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

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

	@Override
	public TreeObjectImage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		TreeObjectImage instance;
		try {
			instance = TreeObjectImage.class.newInstance();
			deserialize(json, context, instance);
			return instance;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new JsonParseException(e);
		}
	}
}
