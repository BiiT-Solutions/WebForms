package com.biit.webforms.serialization;

import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.persistence.entity.condition.TokenEmpty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class TokenEmptyDeserializer extends TokenDeserializer<TokenEmpty> {

    private final Form form;

    public TokenEmptyDeserializer(Form element) {
        super(TokenEmpty.class);
        this.form = element;
    }

    @Override
    public void deserialize(TokenEmpty element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setQuestion((WebformsBaseQuestion) FormElementDeserializer.parseTreeObjectPath("question_id", form, jsonObject, context));
        element.setSubformat(AnswerSubformat.from(parseString("subformat", jsonObject)));
        element.setValue(parseString("value", jsonObject));
    }

}
