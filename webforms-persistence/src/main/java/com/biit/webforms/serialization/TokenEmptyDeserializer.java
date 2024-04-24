package com.biit.webforms.serialization;

import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.persistence.entity.condition.TokenEmpty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class TokenEmptyDeserializer extends TokenWithQuestionDeserializer<TokenEmpty> {

    @Override
    public void deserialize(TokenEmpty element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setSubformat(AnswerSubformat.from(parseString("subformat", jsonObject)));
        element.setValue(parseString("value", jsonObject));
    }

}
