package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.condition.TokenInValue;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.lang.reflect.Type;

public class TokenInValueDeserializer extends StorableObjectDeserializer<TokenInValue> {

    private final Form form;

    public TokenInValueDeserializer(Form element) {
        this.form = element;
    }

    @Override
    public void deserialize(TokenInValue element, JsonNode jsonObject, DeserializationContext context) throws IOException {
         super.deserialize(element, jsonObject, context);

        element.setAnswerValue((Answer) FormElementDeserializer.parseTreeObjectPath("answer_id", form, jsonObject, context));
    }

    @Override
    public TokenInValue deserialize(JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {
        TokenInValue instance = new TokenInValue();
        deserialize(json, context, instance);
        return instance;
    }

}