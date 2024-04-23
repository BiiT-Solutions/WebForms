package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.condition.TokenIn;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class TokenInSerializer extends TokenWithQuestionSerializer<TokenIn> {

    @Override
    public void serialize(TokenIn src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        if (src != null) {
            jgen.writeObjectField("values", src.getValues());
        }
    }

}