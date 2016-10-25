package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.condition.TokenInValue;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class TokenInValueDeserializer extends StorableObjectDeserializer<TokenInValue> {

	private final Form form;
	
	public TokenInValueDeserializer(Form element) {
		this.form = element;
	}
	
	@Override
	public void deserialize(JsonElement json,JsonDeserializationContext context, TokenInValue element){
		JsonObject jobject = (JsonObject) json;
		
		element.setAnswerValue((Answer) FormDeserializer.parseTreeObjectPath("answer_id", form, jobject, context));
		
		super.deserialize(json, context, element);
	}
	
	@Override
	public TokenInValue deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		TokenInValue instance = new TokenInValue();
		deserialize(json, context, instance);
		return instance;
	}

}