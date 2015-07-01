package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLinkErrors;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class WebserviceCallInputErrorsDeserializer implements JsonDeserializer<WebserviceCallInputLinkErrors> {

	@Override
	public WebserviceCallInputLinkErrors deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		
		JsonObject jobject = json.getAsJsonObject();

		return new WebserviceCallInputLinkErrors(parseString("errorCode", jobject, context), parseString("errorMessage", jobject, context));		
		
	}
	
	public String parseString(String name,JsonObject jobject,JsonDeserializationContext context){
		if(jobject.get(name)!=null){
			return (String) context.deserialize(jobject.get(name), String.class);
		}
		return "";
	}

}
