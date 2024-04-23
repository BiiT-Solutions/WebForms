package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonParseException;

import java.io.IOException;

public class TokenDeserializer<T extends Token> extends StorableObjectDeserializer<T> {

    @Override
    public void deserialize(T element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        try {
            if (jsonObject.get("type") != null) {
                element.setType(TokenTypes.from(jsonObject.get("type").textValue()));
            }
        } catch (NotValidTokenType e) {
            throw new JsonParseException(e);
        }
    }
}
