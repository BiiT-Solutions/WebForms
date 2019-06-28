package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.TokenEmpty;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TokenEmptyDeserializer extends TokenDeserializer<TokenEmpty> {

	private final Form form;

	public TokenEmptyDeserializer(Form element) {
		super(TokenEmpty.class);
		this.form = element;
	}

	@Override
	public void deserialize(JsonElement json, JsonDeserializationContext context, TokenEmpty element) {
		JsonObject jobject = (JsonObject) json;

		element.setQuestion((Question) FormDeserializer.parseTreeObjectPath("question_id", form, jobject, context));
		element.setSubformat(QuestionDeserializer.parseAnswerSubformat("subformat", jobject, context));
		element.setDatePeriodUnit(parseDatePeriodUnit("datePeriodUnit", jobject, context));
		element.setValue(parseString("value", jobject, context));

		super.deserialize(json, context, element);
	}

}
