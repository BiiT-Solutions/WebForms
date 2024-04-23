package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.condition.TokenIn;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class TokenInSerializer extends TokenSerializer<TokenIn> {

    @Override
    public void serialize(TokenIn src, JsonGenerator jgen) throws IOException {
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
            jgen.writeObjectField("values", src.getValues());
        }
    }

}