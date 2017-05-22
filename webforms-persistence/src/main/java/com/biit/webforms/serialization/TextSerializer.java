package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.form.json.serialization.TreeObjectSerializer;
import com.biit.webforms.persistence.entity.Text;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class TextSerializer extends TreeObjectSerializer<Text> {

	@Override
	public JsonElement serialize(Text src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);

		jsonObject.add("description", context.serialize(src.getDescription()));
		if (src.getImage() != null) {
			jsonObject.add("image", context.serialize(src.getImage()));
		} else {
			jsonObject.add("image", context.serialize(null));
		}

		return jsonObject;
	}

}
