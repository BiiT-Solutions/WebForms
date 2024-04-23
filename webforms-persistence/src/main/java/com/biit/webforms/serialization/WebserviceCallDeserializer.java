package com.biit.webforms.serialization;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallOutputLink;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class WebserviceCallDeserializer extends StorableObjectDeserializer<WebserviceCall> {

	private final Form form;

	public WebserviceCallDeserializer(Form form) {
		this.form = form;
	}

	@Override
	public void deserialize(WebserviceCall element, JsonNode jsonObject, DeserializationContext context) throws IOException {
		 super.deserialize(element, jsonObject, context);

		element.setName(parseString("name", jsonObject, context));
		element.setWebserviceName(parseString("webserviceName", jsonObject, context));

		element.setFormElementTrigger((BaseQuestion) FormElementDeserializer.parseTreeObjectPath("formElementTrigger_id", form, jsonObject, context));

		element.setInputLinks(parseInputLinks("inputLinks", jsonObject, context));
		element.setOutputLinks(parseOutputLinks("outputLinks", jsonObject, context));

		super.deserialize(json, context, element);
	}
	
	@Override
	public WebserviceCall deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		WebserviceCall instance = new WebserviceCall();
		deserialize(json, context, instance);
		return instance;
	}

	private Set<WebserviceCallOutputLink> parseOutputLinks(String name, JsonObject jsonObject, JsonDeserializationContext context) {
		HashSet<WebserviceCallOutputLink> outputLinks = new HashSet<>();

		JsonElement valuesJson = jsonObject.get(name);
		if (valuesJson != null) {
			Type listType = new TypeToken<Set<WebserviceCallOutputLink>>() {
			}.getType();
			@SuppressWarnings("unchecked")
			Set<WebserviceCallOutputLink> links = context.deserialize(valuesJson, listType);
			if (links != null) {
				for (WebserviceCallOutputLink link : links) {
					outputLinks.add(link);
				}
			}
		}

		return outputLinks;
	}

	private Set<WebserviceCallInputLink> parseInputLinks(String name, JsonObject jsonObject, JsonDeserializationContext context) {
		HashSet<WebserviceCallInputLink> inputLinks = new HashSet<>();

		JsonElement valuesJson = jsonObject.get(name);
		if (valuesJson != null) {
			Type listType = new TypeToken<Set<WebserviceCallInputLink>>() {
			}.getType();
			@SuppressWarnings("unchecked")
			Set<WebserviceCallInputLink> links = context.deserialize(valuesJson, listType);
			if (links != null) {
				for (WebserviceCallInputLink link : links) {
					inputLinks.add(link);
				}
			}
		}

		return inputLinks;
	}

}
