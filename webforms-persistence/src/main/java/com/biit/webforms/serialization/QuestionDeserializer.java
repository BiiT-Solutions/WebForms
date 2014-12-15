package com.biit.webforms.serialization;

import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class QuestionDeserializer extends TreeObjectDeserializer<Question> {

	public QuestionDeserializer() {
		super(Question.class);
	}

	public void deserialize(JsonElement json, JsonDeserializationContext context, Question element) {
		JsonObject jobject = (JsonObject) json;

		
		try {
			element.setAnswerType(parseAnswerType("answerType", jobject, context));
			element.setAnswerFormat(parseAnswerFormat("answerFormat", jobject, context));
			element.setAnswerSubformat(parseAnswerSubformat("answerSubformat", jobject, context));
		} catch (InvalidAnswerFormatException | InvalidAnswerSubformatException e) {
			throw new JsonParseException(e);
		}		

		element.setMandatory(parseBoolean("mandatory", jobject, context));
		element.setHorizontal(parseBoolean("horizontal", jobject, context));
		element.setDescription(parseString("description", jobject, context));

		super.deserialize(json, context, element);
	}

	public static AnswerType parseAnswerType(String name, JsonObject jobject, JsonDeserializationContext context) {
		if(jobject.get(name)!=null){
			return (AnswerType) context.deserialize(jobject.get(name), AnswerType.class);
		}
		return null;
	}
	
	public static AnswerFormat parseAnswerFormat(String name, JsonObject jobject, JsonDeserializationContext context) {
		if(jobject.get(name)!=null){
			return (AnswerFormat) context.deserialize(jobject.get(name), AnswerFormat.class);
		}
		return null;
	}
	
	public static AnswerSubformat parseAnswerSubformat(String name, JsonObject jobject, JsonDeserializationContext context) {
		if(jobject.get(name)!=null){
			return (AnswerSubformat) context.deserialize(jobject.get(name), AnswerSubformat.class);
		}
		return null;
	}

}
