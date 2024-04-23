package com.biit.webforms.serialization;

import java.io.IOException;
import java.lang.reflect.Type;

import com.biit.webforms.persistence.entity.BlockReference;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class TokenComparationAnswerSerializer extends TokenSerializer<TokenComparationAnswer> {

	@Override
	public void serialize(TokenComparationAnswer src, JsonGenerator jgen) throws IOException {
		super.serialize(src, jgen);

		if (src != null && src.getQuestion() != null) {
			jgen.writeFieldName("question_id");
			jgen.writeStartArray("question_id");
			for (String reference : src.getQuestion().getPath()) {
				jgen.writeString(reference);
			}
			jgen.writeEndArray();
		}

		if (src != null && src.getAnswer() != null) {
			jsonObject.add("answer_id", context.serialize(src.getAnswer().getPath()));
		}

		return jsonObject;
	}

}
