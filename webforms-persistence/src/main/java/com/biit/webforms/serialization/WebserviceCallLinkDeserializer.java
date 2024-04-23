package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallLink;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Arrays;

public class WebserviceCallLinkDeserializer<T extends WebserviceCallLink> extends StorableObjectDeserializer<T> {

    @Override
    public void deserialize(T element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setWebservicePort(parseString("webservicePort", jsonObject));

        if (jsonObject.get("formElement_id") != null) {
            element.setFormElementPath(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("formElement_id").toString(), String[].class)));
        }
    }

}