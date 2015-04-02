package com.biit.webforms.utils.exporters.json;

import com.biit.form.BaseFormMetadata;
import com.biit.webforms.persistence.entity.Form;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MetadataExporter {

	public static String exportFormMetadata(Form form) {
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		gsonBuilder.registerTypeAdapter(BaseFormMetadata.class, new MetadataSerializer());
		Gson gson = gsonBuilder.create();
		return gson.toJson(form.getFormMetadata());
	

	}
}
