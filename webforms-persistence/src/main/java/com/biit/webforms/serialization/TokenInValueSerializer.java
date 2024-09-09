package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.biit.webforms.persistence.entity.condition.TokenInValue;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class TokenInValueSerializer extends StorableObjectSerializer<TokenInValue> {

    @Override
    public void serialize(TokenInValue src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        if (src.getAnswerValue() != null) {
            jgen.writeFieldName("answer_id");
            jgen.writeStartArray("answer_id");
            for (String reference : src.getAnswerValue().getPath()) {
                jgen.writeString(reference);
            }
            jgen.writeEndArray();
        }
    }

}