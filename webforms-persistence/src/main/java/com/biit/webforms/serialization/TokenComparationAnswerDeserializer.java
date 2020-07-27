package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TokenComparationAnswerDeserializer extends TokenDeserializer<TokenComparationAnswer> {

	private final Form form;
	
	public TokenComparationAnswerDeserializer(Form element) {
		super(TokenComparationAnswer.class);
		this.form = element;
	}
	
	@Override
	public void deserialize(JsonElement json,JsonDeserializationContext context, TokenComparationAnswer element){
		JsonObject jobject = (JsonObject) json;
		
		element.setQuestion((WebformsBaseQuestion) FormDeserializer.parseTreeObjectPath("question_id", form, jobject, context));
		element.setAnswer((Answer) FormDeserializer.parseTreeObjectPath("answer_id", form, jobject, context));
		
		super.deserialize(json, context, element);
	}

}