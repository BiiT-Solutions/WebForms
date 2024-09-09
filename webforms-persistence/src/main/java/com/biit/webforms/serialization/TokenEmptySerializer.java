package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.condition.TokenEmpty;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class TokenEmptySerializer extends TokenWithQuestionSerializer<TokenEmpty> {

    @Override
    public void serialize(TokenEmpty src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        if (src.getSubformat() != null) {
            jgen.writeStringField("subformat", src.getSubformat().name());
        }
        jgen.writeStringField("value", src.getValue());
    }

}
