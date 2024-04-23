package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.CustomDeserializer;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.webservices.WebservicePort;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class WebservicePortDeserializer extends CustomDeserializer<WebservicePort> {

    @Override
    public void deserialize(WebservicePort element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setName(parseString("name", jsonObject));
        element.setXpath(parseString("xpath", jsonObject));
        element.setType(AnswerType.from(parseString("type", jsonObject)));
        element.setFormat(AnswerFormat.from(parseString("format", jsonObject)));
        element.setSubformat(AnswerSubformat.from(parseString("subformat", jsonObject)));
    }
}
