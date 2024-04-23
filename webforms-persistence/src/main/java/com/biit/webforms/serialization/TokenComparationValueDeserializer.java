package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class TokenComparationValueDeserializer extends TokenDeserializer<TokenComparationValue> {

	private final Form form;
	
	public TokenComparationValueDeserializer(Form element) {
		super(TokenComparationValue.class);
		this.form = element;
	}

	@Override
	public void deserialize(TokenComparationValue element, JsonNode jsonObject, DeserializationContext context) throws IOException {
		 super.deserialize(element, jsonObject, context);
		
		element.setQuestion((WebformsBaseQuestion) FormElementDeserializer.parseTreeObjectPath("question_id", form, jsonObject, context));
		element.setSubformat(QuestionDeserializer.parseAnswerSubformat("subformat", jsonObject, context));
		element.setDatePeriodUnit(parseDatePeriodUnit("datePeriodUnit", jsonObject, context));
		element.setValue(parseString("value", jsonObject, context));
		
		super.deserialize(json, context, element);
	}

}