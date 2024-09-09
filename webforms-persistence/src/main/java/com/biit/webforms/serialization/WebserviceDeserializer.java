package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.CustomDeserializer;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.webforms.webservices.Webservice;
import com.biit.webforms.webservices.WebservicePort;
import com.biit.webforms.webservices.WebserviceValidatedPort;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class WebserviceDeserializer extends CustomDeserializer<Webservice> {

    @Override
    public void deserialize(Webservice element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setName(parseString("name", jsonObject));
        element.setDescription(parseString("description", jsonObject));
        element.setProtocol(parseString("protocol", jsonObject));
        element.setHost(parseString("host", jsonObject));
        element.setPort(parseString("port", jsonObject));
        element.setPath(parseString("path", jsonObject));


        if (jsonObject.get("inputPorts") != null) {
            element.setInputPorts(new HashSet<>(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("inputPorts").toString(), WebserviceValidatedPort[].class))));
        }
        if (jsonObject.get("outputPorts") != null) {
            element.setOutputPorts(new HashSet<>(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("outputPorts").toString(), WebservicePort[].class))));
        }
    }
}
