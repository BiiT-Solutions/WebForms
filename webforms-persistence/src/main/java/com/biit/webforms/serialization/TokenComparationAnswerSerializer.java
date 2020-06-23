package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class TokenComparationAnswerSerializer extends TokenSerializer<TokenComparationAnswer> {

	@Override
	public JsonElement serialize(TokenComparationAnswer src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);

		if (src != null && src.getQuestion() != null) {
			jsonObject.add("question_id", context.serialize(src.getQuestion().getPath()));
		}
		if (src != null && src.getAnswer() != null) {
			jsonObject.add("answer_id", context.serialize(src.getAnswer().getPath()));
		}

		return jsonObject;
	}

}
