package com.biit.webforms.serialization;

import java.io.IOException;
import java.lang.reflect.Type;

import com.biit.webforms.persistence.entity.BlockReference;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class TokenComparationValueSerializer extends TokenSerializer<TokenComparationValue> {

	@Override
	public void serialize(TokenComparationValue src, JsonGenerator jgen) throws IOException {
		super.serialize(src, jgen);

		if (src != null && src.getQuestion() != null) {
			jgen.writeFieldName("question_id");
			jgen.writeStartArray("question_id");
			for (String reference : src.getQuestion().getPath()) {
				jgen.writeString(reference);
			}
			jgen.writeEndArray();
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