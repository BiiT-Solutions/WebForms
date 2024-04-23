package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.condition.TokenEmpty;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class TokenEmptySerializer extends TokenSerializer<TokenEmpty> {

    @Override
    public void serialize(TokenEmpty src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        if (src != null && src.getQuestion() != null) {
            jgen.writeFieldName("question_id");
            jgen.writeStartArray("question_id");
            for (String reference : src.getQuestion().getPath()) {
                jgen.writeString(reference);
            }
            jgen.writeEndArray();
        }

        if (src.getSubformat() != null) {
            jgen.writeStringField("subformat", src.getSubformat().name());
        }
        jgen.writeStringField("value", src.getValue());
    }

}
