package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class TokenComparationValueSerializer extends TokenSerializer<TokenComparationValue> {

	@Override
	public JsonElement serialize(TokenComparationValue src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);

		if (src != null && src.getQuestion() != null) {
			jsonObject.add("question_id", context.serialize(src.getQuestion().getPath()));
		}
		if (src != null) {
			jsonObject.add("subformat", context.serialize(src.getSubformat()));
		}
		if (src != null) {
			jsonObject.add("datePeriodUnit", context.serialize(src.getDatePeriodUnit()));
		}
		if (src != null) {
			jsonObject.add("value", context.serialize(src.getValue()));
		}

		return jsonObject;
	}

}