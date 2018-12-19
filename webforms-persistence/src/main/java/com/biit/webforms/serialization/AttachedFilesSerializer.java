package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.form.json.serialization.TreeObjectSerializer;
import com.biit.webforms.persistence.entity.AttachedFiles;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class AttachedFilesSerializer extends TreeObjectSerializer<AttachedFiles> {

	@Override
	public JsonElement serialize(AttachedFiles src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);

		jsonObject.add("mandatory", context.serialize(src.isMandatory()));
		jsonObject.add("editionDisabled", context.serialize(src.isEditionDisabled()));

		return jsonObject;
	}

}
