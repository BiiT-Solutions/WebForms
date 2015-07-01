package com.biit.webforms.serialization;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import com.biit.form.entity.BaseQuestion;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLinkErrors;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class WebserviceCallInputDeserializer extends StorableObjectDeserializer<WebserviceCallInputLink> {

	private final Form form;

	public WebserviceCallInputDeserializer(Form form) {
		this.form = form;
	}
	
	@Override
	public WebserviceCallInputLink deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		WebserviceCallInputLink instance = new WebserviceCallInputLink();
		deserialize(json, context, instance);
		return instance;
	}

	@Override
	public void deserialize(JsonElement json, JsonDeserializationContext context, WebserviceCallInputLink element) {
		JsonObject jobject = (JsonObject) json;

		element.setWebservicePort(parseString("webservicePort", jobject, context));
		element.setFormElement((BaseQuestion) FormDeserializer.parseTreeObjectPath("formElement_id", form, jobject, context));
		element.setErrors(parseErrors("errors", form, jobject, context));
		element.setValidationXpath(parseString("validationXpath", jobject, context));

		super.deserialize(json, context, element);
	}

	private Set<WebserviceCallInputLinkErrors> parseErrors(String name, Form form, JsonObject jobject, JsonDeserializationContext context) {
		HashSet<WebserviceCallInputLinkErrors> parsedErrors = new HashSet<>();
		
		JsonElement valuesJson = jobject.get(name);
		if (valuesJson != null) {
			Type listType = new TypeToken<Set<WebserviceCallInputLinkErrors>>() {
			}.getType();
			@SuppressWarnings("unchecked")
			Set<WebserviceCallInputLinkErrors> errors = (Set<WebserviceCallInputLinkErrors>) context.deserialize(valuesJson, listType);
			if (errors != null) {
				for (WebserviceCallInputLinkErrors error : errors) {
					parsedErrors.add(error);
				}
			}
		}
		
		return parsedErrors;
	}

}