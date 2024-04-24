package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLinkErrors;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class WebserviceCallInputLinkDeserializer extends WebserviceCallLinkDeserializer<WebserviceCallInputLink> {


    @Override
    public void deserialize(WebserviceCallInputLink element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if ((jsonObject.get("errors") != null)) {
            element.setErrors(new HashSet<>(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("errors").toString(), WebserviceCallInputLinkErrors[].class))));
        }
        element.setValidationXpath(parseString("validationXpath", jsonObject));
    }
}