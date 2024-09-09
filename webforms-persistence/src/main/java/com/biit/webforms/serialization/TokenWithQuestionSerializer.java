package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.condition.TokenWithQuestion;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class TokenWithQuestionSerializer<T extends TokenWithQuestion> extends TokenSerializer<T> {

    @Override
    public void serialize(T src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        if (src != null && src.getQuestion() != null) {
            jgen.writeFieldName("question_id");
            jgen.writeStartArray("question_id");
            for (String reference : src.getQuestion().getPath()) {
                jgen.writeString(reference);
            }
            jgen.writeEndArray();
        }
    }
}
