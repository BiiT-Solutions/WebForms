package com.biit.webforms.serialization;

import com.biit.form.json.serialization.TreeObjectDeserializer;
import com.biit.webforms.persistence.entity.AttachedFiles;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AttachedFilesDeserializer extends TreeObjectDeserializer<AttachedFiles> {

	public AttachedFilesDeserializer() {
		super(AttachedFiles.class);
	}

	@Override
	public void deserialize(JsonElement json, JsonDeserializationContext context, AttachedFiles element) {
		JsonObject jobject = (JsonObject) json;

		element.setMandatory(parseBoolean("mandatory", jobject, context));
		element.setEditionDisabled(parseBoolean("editionDisabled", jobject, context));

		super.deserialize(json, context, element);
	}
}
