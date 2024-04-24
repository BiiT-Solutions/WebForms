package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Arrays;

public class TokenComparationAnswerDeserializer extends TokenWithQuestionDeserializer<TokenComparationAnswer> {

    @Override
    public void deserialize(TokenComparationAnswer element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        if (jsonObject.get("answer_id") != null) {
            element.setAnswerReferencePath(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("answer_id").toString(), String[].class)));
        }
    }

}