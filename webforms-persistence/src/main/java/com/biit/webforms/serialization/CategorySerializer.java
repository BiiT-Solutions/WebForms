package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.webforms.persistence.entity.Category;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class CategorySerializer extends TreeObjectSerializer<Category> {

	@Override
	public JsonElement serialize(Category src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);

		if (src.getImage() != null) {
			jsonObject.add("image", context.serialize(src.getImage()));
		} else {
			jsonObject.add("image", context.serialize(null));
		}

		return jsonObject;
	}
}
