package com.biit.webforms.serialization;

import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class TokenComparationValueDeserializer extends TokenWithQuestionDeserializer<TokenComparationValue> {

    @Override
    public void deserialize(TokenComparationValue element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setSubformat(AnswerSubformat.from(parseString("subformat", jsonObject)));
        element.setDatePeriodUnit(DatePeriodUnit.from(parseString("datePeriodUnit", jsonObject)));
        element.setValue(parseString("value", jsonObject));
    }

}