package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallOutputLink;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class WebserviceCallDeserializer extends StorableObjectDeserializer<WebserviceCall> {

    @Override
    public void deserialize(WebserviceCall element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setName(parseString("name", jsonObject));
        element.setWebserviceName(parseString("webserviceName", jsonObject));


        if (jsonObject.get("formElementTrigger_id") != null) {
            element.setFormElementTriggerPath(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("formElementTrigger_id").toString(), String[].class)));
        }

        element.setInputLinks(new HashSet<>(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("inputLinks").toString(), WebserviceCallInputLink[].class))));
        element.setOutputLinks(new HashSet<>(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("outputLinks").toString(), WebserviceCallOutputLink[].class))));
    }
}
