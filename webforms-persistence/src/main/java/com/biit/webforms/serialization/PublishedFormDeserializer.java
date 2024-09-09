package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.BaseStorableObjectDeserializer;
import com.biit.webforms.persistence.entity.PublishedForm;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class PublishedFormDeserializer extends BaseStorableObjectDeserializer<PublishedForm> {

    @Override
    public void deserialize(PublishedForm element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setLabel(parseString("label", jsonObject));
        element.setVersion(parseInteger("version", jsonObject));
        element.setOrganizationId(parseLong("organizationId", jsonObject));
        element.setJsonCode(parseString("jsonCode", jsonObject));
    }
}
