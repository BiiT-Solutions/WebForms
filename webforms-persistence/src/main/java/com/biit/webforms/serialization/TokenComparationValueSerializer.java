package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class TokenComparationValueSerializer extends TokenWithQuestionSerializer<TokenComparationValue> {

    @Override
    public void serialize(TokenComparationValue src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        if (src != null && src.getSubformat() != null) {
            jgen.writeStringField("subformat", src.getSubformat().name());
        }
        if (src != null && src.getDatePeriodUnit() != null) {
            jgen.writeStringField("datePeriodUnit", src.getDatePeriodUnit().name());
        }
        if (src != null) {
            jgen.writeStringField("value", src.getValue());
        }
    }

}