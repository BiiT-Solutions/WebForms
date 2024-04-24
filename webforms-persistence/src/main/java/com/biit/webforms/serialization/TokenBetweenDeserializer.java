package com.biit.webforms.serialization;

import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.persistence.entity.condition.TokenBetween;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class TokenBetweenDeserializer extends TokenWithQuestionDeserializer<TokenBetween> {

    @Override
    public void deserialize(TokenBetween element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        element.setSubformat(AnswerSubformat.from(parseString("subformat", jsonObject)));
        element.setDatePeriodUnit(DatePeriodUnit.from(parseString("datePeriodUnit", jsonObject)));
        element.setValueStart(parseString("valueStart", jsonObject));
        element.setValueEnd(parseString("valueEnd", jsonObject));

        super.deserialize(element, jsonObject, context);
    }

}
