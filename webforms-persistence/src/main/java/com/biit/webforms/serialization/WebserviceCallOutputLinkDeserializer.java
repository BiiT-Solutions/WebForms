package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.form.entity.BaseQuestion;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallOutputLink;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class WebserviceCallOutputLinkDeserializer extends StorableObjectDeserializer<WebserviceCallOutputLink> {

	private final Form form;

	public WebserviceCallOutputLinkDeserializer(Form form) {
		this.form = form;
	}
	
	@Override
	public WebserviceCallOutputLink deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		WebserviceCallOutputLink instance = new WebserviceCallOutputLink();
		deserialize(json, context, instance);
		return instance;
	}

	@Override
	public void deserialize(JsonElement json, JsonDeserializationContext context, WebserviceCallOutputLink element) {
		JsonObject jobject = (JsonObject) json;

		element.setWebservicePort(parseString("webservicePort", jobject, context));
		element.setFormElement((BaseQuestion) FormDeserializer.parseTreeObjectPath("formElement_id", form, jobject, context));
		element.setEditable(parseBoolean("isEditable", jobject, context));

		super.deserialize(json, context, element);
	}

}