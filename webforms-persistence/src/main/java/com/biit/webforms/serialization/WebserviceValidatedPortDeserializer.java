package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.CustomDeserializer;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.webservices.WebserviceValidatedPort;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Arrays;

public class WebserviceValidatedPortDeserializer extends CustomDeserializer<WebserviceValidatedPort> {

    @Override
    public void deserialize(WebserviceValidatedPort element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setName(parseString("name", jsonObject));
        element.setXpath(parseString("xpath", jsonObject));
        element.setValidationXpath(parseString("validationXpath", jsonObject));
        element.setType(AnswerType.from(parseString("type", jsonObject)));
        element.setFormat(AnswerFormat.from(parseString("format", jsonObject)));
        element.setSubformat(AnswerSubformat.from(parseString("subformat", jsonObject)));

        if (jsonObject.get("errorCodes") != null) {
            element.setErrorCodes(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("errorCodes").toString(), String[].class)));
        }
    }
}
