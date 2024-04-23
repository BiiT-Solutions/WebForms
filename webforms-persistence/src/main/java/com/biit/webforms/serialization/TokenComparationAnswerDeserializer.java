package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class TokenComparationAnswerDeserializer extends TokenDeserializer<TokenComparationAnswer> {

	private final Form form;
	
	public TokenComparationAnswerDeserializer(Form element) {
		this.form = element;
	}

	@Override
	public void deserialize(TokenComparationAnswer element, JsonNode jsonObject, DeserializationContext context) throws IOException {
		 super.deserialize(element, jsonObject, context);
		
		element.setQuestion((WebformsBaseQuestion) FormElementDeserializer.parseTreeObjectPath("question_id", form, jsonObject, context));
		element.setAnswer((Answer) FormElementDeserializer.parseTreeObjectPath("answer_id", form, jsonObject, context));
	}

}