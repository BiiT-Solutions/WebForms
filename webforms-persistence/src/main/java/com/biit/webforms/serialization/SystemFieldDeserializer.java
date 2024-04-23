package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.TreeObjectDeserializer;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.persistence.entity.SystemField;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonParseException;

import java.io.IOException;

public class SystemFieldDeserializer extends TreeObjectDeserializer<SystemField> {

    @Override
    public void deserialize(SystemField element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        try {
            element.setFieldName(parseString("fieldName", jsonObject));
        } catch (FieldTooLongException e) {
            throw new JsonParseException(e);
        }
    }
}
