package com.biit.webforms.serialization;

import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.persistence.entity.SystemField;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class SystemFieldDeserializer extends TreeObjectDeserializer<SystemField> {

	public SystemFieldDeserializer() {
		super(SystemField.class);
	}

	@Override
	public void deserialize(JsonElement json, JsonDeserializationContext context, SystemField element) {
		JsonObject jobject = (JsonObject) json;

		try {
			element.setFieldName(parseString("fieldName", jobject, context));
		} catch (FieldTooLongException e) {
			throw new JsonParseException(e);
		}

		super.deserialize(json, context, element);
	}
}
