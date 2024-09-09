package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.webforms.persistence.entity.condition.TokenIn;
import com.biit.webforms.persistence.entity.condition.TokenInValue;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Arrays;

public class TokenInDeserializer extends TokenWithQuestionDeserializer<TokenIn> {

    @Override
    public void deserialize(TokenIn element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setValues(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("values").toString(), TokenInValue[].class)));
    }
}