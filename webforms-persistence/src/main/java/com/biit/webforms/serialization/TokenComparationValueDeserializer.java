package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TokenComparationValueDeserializer extends TokenDeserializer<TokenComparationValue> {

	private final Form form;
	
	public TokenComparationValueDeserializer(Form element) {
		super(TokenComparationValue.class);
		this.form = element;
	}
	
	public void deserialize(JsonElement json,JsonDeserializationContext context, TokenComparationValue element){
		JsonObject jobject = (JsonObject) json;
		
		element.setQuestion((Question) FormDeserializer.parseTreeObjectPath("question_id", form, jobject, context));
		element.setSubformat(QuestionDeserializer.parseAnswerSubformat("subformat", jobject, context));
		element.setDatePeriodUnit(parseDatePeriodUnit("datePeriodUnit", jobject, context));
		element.setValue(parseString("value", jobject, context));
		
		super.deserialize(json, context, element);
	}

}