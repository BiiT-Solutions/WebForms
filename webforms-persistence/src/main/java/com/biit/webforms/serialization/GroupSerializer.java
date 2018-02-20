package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.form.json.serialization.BaseRepeatableGroupSerializer;
import com.biit.webforms.persistence.entity.Group;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class GroupSerializer extends BaseRepeatableGroupSerializer<Group> {

	@Override
	public JsonElement serialize(Group src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);

		jsonObject.add("isTable", context.serialize(src.isShownAsTable()));

		return jsonObject;
	}
}
