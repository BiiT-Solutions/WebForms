package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.webforms.persistence.entity.CompleteFormView;
import com.biit.webforms.persistence.entity.Form;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class CompleteFormViewDeserializer extends FormElementDeserializer<CompleteFormView> {

    @Override
    public void deserialize(CompleteFormView element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if ((jsonObject.get("form") != null)) {
            element.setForm(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("form").toString(), Form.class));
        }

    }
}
