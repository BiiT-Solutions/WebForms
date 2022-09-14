package com.biit.webforms.serialization;

import com.biit.form.json.serialization.BaseFormSerializer;
import com.biit.webforms.persistence.entity.Block;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;

public class BlockSerializer extends BaseFormSerializer<Block> {

	@Override
	public JsonElement serialize(Block src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);

		jsonObject.add("description", context.serialize(src.getDescription()));
		jsonObject.add("flows", context.serialize(src.getFlows()));
		jsonObject.add("webserviceCalls", context.serialize(src.getWebserviceCalls()));
		if (src.getImage() != null) {
			jsonObject.add("image", context.serialize(src.getImage()));
		} else {
			jsonObject.add("image", context.serialize(null));
		}

		return jsonObject;
	}

}
