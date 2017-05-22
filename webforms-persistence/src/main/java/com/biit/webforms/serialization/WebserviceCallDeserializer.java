package com.biit.webforms.serialization;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.json.serialization.StorableObjectDeserializer;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallOutputLink;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class WebserviceCallDeserializer extends StorableObjectDeserializer<WebserviceCall> {

	private final Form form;

	public WebserviceCallDeserializer(Form form) {
		this.form = form;
	}

	@Override
	public void deserialize(JsonElement json, JsonDeserializationContext context, WebserviceCall element) {
		JsonObject jobject = (JsonObject) json;

		element.setName(parseString("name", jobject, context));
		element.setWebserviceName(parseString("webserviceName", jobject, context));

		element.setFormElementTrigger((BaseQuestion) FormDeserializer.parseTreeObjectPath("formElementTrigger_id", form, jobject, context));

		element.setInputLinks(parseInputLinks("inputLinks", jobject, context));
		element.setOutputLinks(parseOutputLinks("outputLinks", jobject, context));

		super.deserialize(json, context, element);
	}
	
	@Override
	public WebserviceCall deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		WebserviceCall instance = new WebserviceCall();
		deserialize(json, context, instance);
		return instance;
	}

	private Set<WebserviceCallOutputLink> parseOutputLinks(String name, JsonObject jobject, JsonDeserializationContext context) {
		HashSet<WebserviceCallOutputLink> outputLinks = new HashSet<>();

		JsonElement valuesJson = jobject.get(name);
		if (valuesJson != null) {
			Type listType = new TypeToken<Set<WebserviceCallOutputLink>>() {
			}.getType();
			@SuppressWarnings("unchecked")
			Set<WebserviceCallOutputLink> links = (Set<WebserviceCallOutputLink>) context.deserialize(valuesJson, listType);
			if (links != null) {
				for (WebserviceCallOutputLink link : links) {
					outputLinks.add(link);
				}
			}
		}

		return outputLinks;
	}

	private Set<WebserviceCallInputLink> parseInputLinks(String name, JsonObject jobject, JsonDeserializationContext context) {
		HashSet<WebserviceCallInputLink> inputLinks = new HashSet<>();

		JsonElement valuesJson = jobject.get(name);
		if (valuesJson != null) {
			Type listType = new TypeToken<Set<WebserviceCallInputLink>>() {
			}.getType();
			@SuppressWarnings("unchecked")
			Set<WebserviceCallInputLink> links = (Set<WebserviceCallInputLink>) context.deserialize(valuesJson, listType);
			if (links != null) {
				for (WebserviceCallInputLink link : links) {
					inputLinks.add(link);
				}
			}
		}

		return inputLinks;
	}

}
