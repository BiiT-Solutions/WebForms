package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.webservices.WebserviceCallOutputLink;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class WebserviceCallOutputLinkDeserializer extends WebserviceCallLinkDeserializer<WebserviceCallOutputLink> {

    @Override
    public void deserialize(WebserviceCallOutputLink element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setEditable(parseBoolean("isEditable", jsonObject));
    }

}