package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.biit.webforms.persistence.entity.TreeObjectImage;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class TreeObjectImageDeserializer extends StorableObjectDeserializer<TreeObjectImage> {

    @Override
    public void deserialize(TreeObjectImage element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setFileName(parseString("fileName", jsonObject));
        element.setWidth(parseInteger("width", jsonObject));
        element.setHeight(parseInteger("height", jsonObject));
        element.fromBase64(parseString("data", jsonObject));
    }
}
