package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.TreeObjectImage;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class CategoryDeserializer extends TreeObjectDeserializer<Category> {

	public void deserialize(JsonElement json, JsonDeserializationContext context, Category element) {
		JsonObject jobject = (JsonObject) json;

		element.setImage((TreeObjectImage) context.deserialize(jobject.get("image"), TreeObjectImage.class));

		super.deserialize(json, context, element);
	}

	public CategoryDeserializer() {
		super(Category.class);
	}

}
