package com.biit.webforms.utils.exporters.json;

import java.lang.reflect.Type;

import com.biit.form.entity.BaseFormMetadata;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class BaseFormMetadataSerializer implements JsonSerializer<BaseFormMetadata> {

	@Override
	public JsonElement serialize(BaseFormMetadata src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		
		if (src.getFormLabel() != null) {
			jsonObject.add("label", context.serialize(src.getFormLabel()));
		}
		if (src.getFormOrganizationId() != null) {
			jsonObject.add("organizationId", context.serialize(src.getFormOrganizationId()));
		}
		if (src.getFormVersion() != null) {
			jsonObject.add("version", context.serialize(src.getFormVersion()));
		}
		
		if (src.getLinkedLabel() != null) {
			jsonObject.add("linkedLabel", context.serialize(src.getLinkedLabel()));
		}
		if (src.getLinkedOrganizationId() != null) {
			jsonObject.add("linkedOrganizationId", context.serialize(src.getLinkedOrganizationId()));
		}
		if (src.getLinkedVersions() != null) {
			jsonObject.add("linkedVersions", context.serialize(src.getLinkedVersions()));
		}
		
		return jsonObject;
	}
}
