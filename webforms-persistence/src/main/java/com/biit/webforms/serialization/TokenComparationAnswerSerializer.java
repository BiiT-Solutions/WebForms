package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class TokenComparationAnswerSerializer extends TokenWithQuestionSerializer<TokenComparationAnswer> {

    @Override
    public void serialize(TokenComparationAnswer src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        if (src != null && src.getAnswer() != null) {
            jgen.writeFieldName("answer_id");
            jgen.writeStartArray("answer_id");
            for (String reference : src.getAnswer().getPath()) {
                jgen.writeString(reference);
            }
            jgen.writeEndArray();
        }
    }

}
