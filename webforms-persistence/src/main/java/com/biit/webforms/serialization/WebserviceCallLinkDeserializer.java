package com.biit.webforms.serialization;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallLink;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class WebserviceCallLinkDeserializer<T extends WebserviceCallLink> extends StorableObjectDeserializer<T> {

    @Override
    public void deserialize(T element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setWebservicePort(parseString("webservicePort", jsonObject));
        element.setFormElement((BaseQuestion) FormElementDeserializer.parseTreeObjectPath("formElement_id", form, jsonObject, context));
        if ((jsonObject.get("webserviceCall") != null)) {
            element.setWebserviceCall(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("webserviceCall").toString(), WebserviceCall.class));
        }
    }

}