package com.biit.webforms.exporters.json;

import com.biit.form.entity.BaseFormMetadata;
import com.biit.webforms.persistence.entity.Form;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Exporter to json of the extra version data information of the form.
 *
 */
public class BaseFormMetadataExporter {

	public static String exportFormMetadata(Form form) {

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		gsonBuilder.registerTypeAdapter(BaseFormMetadata.class, new BaseFormMetadataSerializer());
		Gson gson = gsonBuilder.create();
		return gson.toJson(form.getFormMetadata());

	}
}
