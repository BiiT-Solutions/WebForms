package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.form.jackson.serialization.TreeObjectDeserializer;
import com.biit.webforms.persistence.entity.DynamicAnswer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Arrays;

public class DynamicAnswerDeserializer extends TreeObjectDeserializer<DynamicAnswer> {


    @Override
    public void deserialize(DynamicAnswer element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if (jsonObject.get("reference") != null) {
            element.setReferencePath(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("reference").toString(), String[].class)));
        }
    }

}
