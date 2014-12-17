package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.TokenBetween;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TokenBetweenDeserializer extends TokenDeserializer<TokenBetween> {

	private final Form form;
	
	public TokenBetweenDeserializer(Form element) {
		super(TokenBetween.class);
		this.form = element;
	}
	
	public void deserialize(JsonElement json,JsonDeserializationContext context, TokenBetween element){
		JsonObject jobject = (JsonObject) json;
		
		element.setQuestion((Question) FormDeserializer.parseTreeObjectPath("question_id", form, jobject, context));
		element.setSubformat(QuestionDeserializer.parseAnswerSubformat("subformat", jobject, context));
		element.setDatePeriodUnit(parseDatePeriodUnit("datePeriodUnit", jobject, context));
		element.setValueStart(parseString("valueStart", jobject, context));
		element.setValueEnd(parseString("valueEnd", jobject, context));

		super.deserialize(json, context, element);
	}

}
