package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.biit.webforms.persistence.entity.TreeObjectVideo;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class TreeObjectVideoDeserializer extends StorableObjectDeserializer<TreeObjectVideo> {

    @Override
    public void deserialize(TreeObjectVideo element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setUrl(parseString("videoUrl", jsonObject));
    }
}
