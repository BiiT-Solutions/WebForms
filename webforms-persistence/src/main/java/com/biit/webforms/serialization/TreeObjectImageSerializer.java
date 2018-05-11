package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.form.json.serialization.StorableObjectSerializer;
import com.biit.webforms.persistence.entity.TreeObjectImage;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class TreeObjectImageSerializer extends StorableObjectSerializer<TreeObjectImage> {

	@Override
	public JsonElement serialize(TreeObjectImage src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);

		jsonObject.add("fileName", context.serialize(src.getFileName()));
		jsonObject.add("width", context.serialize(src.getWidth()));
		jsonObject.add("height", context.serialize(src.getHeight()));
		jsonObject.add("data", context.serialize(src.toBase64()));
		jsonObject.add("imageUrl", context.serialize(src.getUrl()));

		return jsonObject;

	}
}
