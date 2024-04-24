package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.condition.TokenBetween;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class TokenBetweenSerializer extends TokenWithQuestionSerializer<TokenBetween> {

    @Override
    public void serialize(TokenBetween src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        if (src != null && src.getSubformat() != null) {
            jgen.writeStringField("subformat", src.getSubformat().name());
        }
        if (src != null && src.getDatePeriodUnit() != null) {
            jgen.writeStringField("datePeriodUnit", src.getDatePeriodUnit().name());
        }
        if (src != null) {
            jgen.writeStringField("valueStart", src.getValueStart());
        }
        if (src != null) {
            jgen.writeStringField("valueEnd", src.getValueEnd());
        }
    }

}
