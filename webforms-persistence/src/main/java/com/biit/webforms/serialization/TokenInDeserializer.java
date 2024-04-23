package com.biit.webforms.serialization;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.persistence.entity.condition.TokenIn;
import com.biit.webforms.persistence.entity.condition.TokenInValue;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class TokenInDeserializer extends TokenDeserializer<TokenIn> {

	@Override
	public void deserialize(TokenIn element, JsonNode jsonObject, DeserializationContext context) throws IOException {
		 super.deserialize(element, jsonObject, context);
		
		element.setQuestion((WebformsBaseQuestion) FormElementDeserializer.parseTreeObjectPath("question_id", form, jsonObject, context));
		element.setValues(parseTokenInValues("values", jsonObject, context));
		
		super.deserialize(json, context, element);
	}

	@SuppressWarnings("unchecked")
	private List<TokenInValue> parseTokenInValues(String name,JsonObject jsonObject, JsonDeserializationContext context) {
		List<TokenInValue> values = new ArrayList<TokenInValue>();
		
		JsonElement valuesJson = jsonObject.get(name);
		if(valuesJson!=null){
			Type listType = new TypeToken<List<TokenInValue>>() {}.getType();
			values.addAll((List<TokenInValue>)context.deserialize(valuesJson, listType));
		}
		
		return values;
	}

}