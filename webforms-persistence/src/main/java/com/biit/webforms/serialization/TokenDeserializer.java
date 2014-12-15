package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class TokenDeserializer<T extends Token> extends
		StorableObjectDeserializer<T> {

	Class<T> specificClass;

	public TokenDeserializer(Class<T> specificClass) {
		this.specificClass = specificClass;
	}

	public void deserialize(JsonElement json,JsonDeserializationContext context, T element){
		JsonObject jobject = (JsonObject) json;
		
		try {
			element.setType(parseTokenType("type",jobject,context));
		} catch (NotValidTokenType e) {
			throw new JsonParseException(e);
		}
		
		super.deserialize(json, context, element);
	}
	
	public static TokenTypes parseTokenType(String name, JsonObject jobject, JsonDeserializationContext context) {
		if (jobject.get(name) != null) {
			return (TokenTypes) context.deserialize(jobject.get(name),TokenTypes.class);
		}
		return null;
	}

	public static DatePeriodUnit parseDatePeriodUnit(String name, JsonObject jobject, JsonDeserializationContext context) {
		if(jobject.get(name)!=null){
			return (DatePeriodUnit) context.deserialize(jobject.get(name), DatePeriodUnit.class);
		}
		return null;
	}

	@Override
	public T deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		T instance;
		try {
			instance = specificClass.newInstance();
			deserialize(json, context, instance);
			return instance;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new JsonParseException(e);
		}
	}

}
