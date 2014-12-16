package com.biit.webforms.serialization;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.TokenIn;
import com.biit.webforms.persistence.entity.condition.TokenInValue;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class TokenInDeserializer extends TokenDeserializer<TokenIn> {

	private final Form form;
	
	public TokenInDeserializer(Form element) {
		super(TokenIn.class);
		this.form = element;
	}
	
	public void deserialize(JsonElement json,JsonDeserializationContext context, TokenIn element){
		JsonObject jobject = (JsonObject) json;
		
		element.setQuestion((Question) FormDeserializer.parseTreeObjectPath("question_id", form, jobject, context));
		element.setValues(parseTokenInValues("values", jobject, context));
		
		super.deserialize(json, context, element);
	}

	@SuppressWarnings("unchecked")
	private List<TokenInValue> parseTokenInValues(String name,JsonObject jobject, JsonDeserializationContext context) {
		List<TokenInValue> values = new ArrayList<TokenInValue>();
		
		JsonElement valuesJson = jobject.get(name);
		if(valuesJson!=null){
			Type listType = new TypeToken<List<TokenInValue>>() {}.getType();
			values.addAll((List<TokenInValue>)context.deserialize(valuesJson, listType));
		}
		
		return values;
	}

}