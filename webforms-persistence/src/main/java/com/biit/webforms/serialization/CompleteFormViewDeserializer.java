package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.Form;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class CompleteFormViewDeserializer extends FormElementDeserializer<Form> {

    private static final long serialVersionUID = -1220003569801067990L;

    @Override
    public void deserialize(Form element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
    }

}
